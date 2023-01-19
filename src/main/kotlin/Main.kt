import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

fun main() {
    // 检查Config是否有效
    if(!Config.checkConfigFolder()) {
        println("Config folder is not exist")
        return
    }

    val fileHelper = FileHelper()
    val folderBooks = fileHelper.ScanFile(Config.folderPath)
    val doubanWeb = DoubanWeb()
    val chromeOp = ChromeOptions()
    val driver = ChromeDriver(chromeOp)

    folderBooks.forEach() {
        doubanWeb.fillBook(it, driver)

        Thread.sleep(500)
    }

    driver.quit()

    for (book in Book.BookHash.values){
        try {
            book.save()
            println(book.toString())
        } catch (e:Exception) {
            println("Catch Exception ${book.toString()}")
        }

    }

}