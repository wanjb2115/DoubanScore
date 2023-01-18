import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import java.time.Duration

import kotlin.math.max
import kotlin.math.min

class DoubanWeb {

    val url = "https://search.douban.com/book/subject_search?search_text="

    companion object {
        var loginFlag = false
    }

    fun getLevenshteinDistance(X: String, Y: String): Int {
        val m = X.length
        val n = Y.length
        val T = Array(m + 1) { IntArray(n + 1) }
        for (i in 1..m) {
            T[i][0] = i
        }
        for (j in 1..n) {
            T[0][j] = j
        }
        var cost: Int
        for (i in 1..m) {
            for (j in 1..n) {
                cost = if (X[i - 1] == Y[j - 1]) 0 else 1
                T[i][j] = min(min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                    T[i - 1][j - 1] + cost)
            }
        }
        return T[m][n]
    }

    fun findSimilarity(x: String?, y: String?): Double {
        require(!(x == null || y == null)) { "Strings should not be null" }

        val maxLength = max(x.length, y.length)
        return if (maxLength > 0) {
            (maxLength * 1.0 - getLevenshteinDistance(x, y)) / maxLength * 1.0
        } else 1.0
    }

    fun fillBook(bookFileInfo:BookFileInfo, driver:ChromeDriver) {

        if (Book.NameHash[bookFileInfo.name] != null && Book.BookHash[Book.NameHash[bookFileInfo.name]]!=null)
        {
            Book.BookHash[Book.NameHash[bookFileInfo.name]]!!.bookPath.add(bookFileInfo.path)
            return
        }

        driver.get("$url${bookFileInfo.name}")
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))

        var bookInfoList = driver.findElements(By.className("title-text"))

        if (!loginFlag && bookInfoList.size == 0) {
            loginFlag = true
            Thread.sleep(30000)

            driver.get("$url${bookFileInfo.name}")
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))
            bookInfoList = driver.findElements(By.className("title-text"))
        }

        for (it in bookInfoList) {
            val douban_url = it.getDomAttribute("href")
            val regex_url = Regex("""https://book.douban.com/subject/""")

            if (regex_url.containsMatchIn(douban_url)) {

                if (findSimilarity(it.text, bookFileInfo.name) < 0.01) {
                    println(it.text + "," + bookFileInfo.name)
                    break
                }

                val book:Book = if (Book.BookHash[it.text] == null) Book(bookFileInfo.name)  else Book.BookHash[it.text]!!

                book.douban_url = douban_url
                book.douban_name = it.text
                book.bookPath.add(bookFileInfo.path)

                Book.NameHash[bookFileInfo.name] =book.douban_name
                Book.BookHash[it.text] = book

                driver.get(book.douban_url)
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))

                try {
                    book.douban_score = driver.findElement(By.className("rating_num")).text

                    try {
                        book.douban_score.toFloat()
                    } catch (e:Exception) {
                        println("error:${e.message}")
                    }

                } catch (e:NoSuchElementException) {
                    println("error:${e.message}")
                }

                val bookHtml = driver.executeScript("return document.documentElement.outerHTML")
                val bookRegexRes = Regex("""criteria = '.+'""").find(bookHtml.toString())?.value

                if (bookRegexRes != null) {
                    val bookCategory = bookRegexRes.split("'")[1].split("|")

                    for (c in bookCategory) {
                        if (c.contains(Regex("""7:"""))) {
                            book.douban_category = Book.getCategory(c.split(":")[1])
                            if (book.douban_category != "未分类") return
                        }
                    }

                    println(bookCategory)

                }

                return
            }
        }

        if (Book.BookHash[bookFileInfo.name] == null) Book.BookHash[bookFileInfo.name] = Book(bookFileInfo.name)

        Book.BookHash[bookFileInfo.name]!!.bookPath.add(bookFileInfo.path)


    }
}