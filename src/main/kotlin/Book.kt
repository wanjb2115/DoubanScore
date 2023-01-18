import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.extension

class Book(name: String) {
    var name:String = ""
    var douban_url = ""
    var douban_name = ""
    var douban_score = ""
    var douban_category = "未分类"
    var bookPath = mutableListOf<String>()

    init {
        this.name = findName(name)
    }

    companion object {
        // BookHash 用于存放经过处理后的书籍信息，包含book类
        var BookHash = HashMap<String, Book>()
        // NameHash 用于存放书籍原始文件的名称
        var NameHash = HashMap<String, String>()

        // 根据书籍文件名过滤出真实的书名
        fun findName(originName:String):String {

            var reName = originName.replace("""[^(a-zA-Z0-9\\u4e00-\\u9fa5)]""", "")

            reName = reName.replace(Regex("""\[\d?\.?\d?]"""), "")

            reName = reName.split(".")[0].split("_")[0]

            reName = reName.split("（")[0].split("【")[0].split("(")[0]

            return reName
        }

        // 由豆瓣分类关键字转化为自定义的书籍分类
        fun getCategory(cCategory:String):String {
            for (it:String in Config.availableCategory) {
                if (cCategory.contains(it)) {
                    return it
                }
            }
            return "未分类"
        }
    }

    fun save() {

        douban_name = douban_name.replace(" ","").replace(":","：").replace("(","（").replace(")","）").replace("?","？")
        name = name.replace(" ","").replace(":","：").replace("(","（").replace(")","）").replace("?","？")

        if (douban_url == "") {
            for (i in bookPath.indices) {
                val it = bookPath[i]
                val bookPath = "${Config.unDoubanPath}【$douban_score】$name\\"

                if (!Files.isDirectory(Path(bookPath))) {
                    Files.createDirectory(Path(bookPath))
                }
                val extension = Path(it).extension

                val dst = "${bookPath}$name" + "_"  + "${i}.$extension"
                Files.move(Path(it), Path(dst), StandardCopyOption.REPLACE_EXISTING)
            }

        } else if (douban_score == "") {
            for (i in bookPath.indices) {
                val it = bookPath[i]
                val bookPath = "${Config.unScorePath}【$douban_score】$name\\"

                if (!Files.isDirectory(Path(bookPath))) {
                    Files.createDirectory(Path(bookPath))
                }
                val extension = Path(it).extension
                val dst = "${bookPath}$name" + "_"  + "${i}.$extension"
//                println(dst)
                Files.move(Path(it), Path(dst), StandardCopyOption.REPLACE_EXISTING)
            }
        } else {
            val targetPath = Config.targetPath + "$douban_category\\"

            if (!Files.isDirectory(Path(targetPath))) {
                Files.createDirectory(Path(targetPath))
            }

            for (i in bookPath.indices) {

                val it = bookPath[i]

                val bookPath = "$targetPath【$douban_score】$douban_name\\"

                if (!Files.isDirectory(Path(bookPath))) {
                    Files.createDirectory(Path(bookPath))
                }

                val extension = Path(it).extension

                val dst = "${bookPath}[$douban_score]$douban_name" + "_"  + "$i.$extension"

//                println(dst)
                Files.move(Path(it), Path(dst), StandardCopyOption.REPLACE_EXISTING)
            }
        }

    }

    override fun toString():String {
        return "name:$douban_name, category:$douban_category, score:$douban_score"
    }




}