import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.extension

class Book(name: String) {
    var name:String = ""
    var douban_url = ""
    var douban_name = ""
    var douban_score = ""
    var douban_publisher = ""
    var douban_author = ""
    var douban_date = ""
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

            reName = reName.split("【")[0]

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

    fun makeSaveFileName():String {
        var saveName = ""
        if (douban_name != "") saveName = "[$douban_score]$douban_name"
        else saveName = "[$douban_score]$name"

        if (douban_date != "") saveName += "_$douban_date"

        return saveName
    }

    fun makeSaveFolderName():String {

        return makeSaveFileName().replace(" ",".")
    }

    fun save() {
        douban_name = douban_name.replace(":","：").replace("(","（").replace(")","）").replace("?","？").replace("\\",".").replace("/"," ")
        name = name.replace(":","：").replace("(","（").replace(")","）").replace("?","？").replace("\\",".").replace("/"," ")

        if (douban_url == "") {
            for (i in bookPath.indices) {

                if (!Files.isDirectory(Path(Config.unDoubanPath))) {
                    Files.createDirectory(Path(Config.unDoubanPath))
                }

                val it = bookPath[i]
                val bookPath = "${Config.unDoubanPath}${makeSaveFolderName()}\\"

                if (!Files.isDirectory(Path(bookPath))) {
                    Files.createDirectory(Path(bookPath))
                }
                val extension = Path(it).extension

                val dst = "${bookPath}$name" + "_"  + "${i}.$extension"
                Files.move(Path(it), Path(dst), StandardCopyOption.REPLACE_EXISTING)
            }

        } else if (douban_score == "") {

            if (!Files.isDirectory(Path(Config.unScorePath))) {
                Files.createDirectory(Path(Config.unScorePath))
            }

            for (i in bookPath.indices) {
                val it = bookPath[i]
                val bookPath = "${Config.unScorePath}${makeSaveFolderName()}\\"

                if (!Files.isDirectory(Path(bookPath))) {
                    Files.createDirectory(Path(bookPath))
                }
                val extension = Path(it).extension
                val dst = "${bookPath}$name" + "_"  + "${i}.$extension"
//                println(dst)
                Files.move(Path(it), Path(dst), StandardCopyOption.REPLACE_EXISTING)
            }
        } else if (Config.ifCategory) {
            val targetPath = Config.targetPath + "$douban_category\\"

            if (!Files.isDirectory(Path(targetPath))) {
                Files.createDirectory(Path(targetPath))
            }

            for (i in bookPath.indices) {

                val it = bookPath[i]

                val bookPath = "$targetPath${makeSaveFolderName()}\\"

                if (!Files.isDirectory(Path(bookPath))) {
                    Files.createDirectory(Path(bookPath))
                }

                val extension = Path(it).extension

                val dst = "${bookPath}${makeSaveFileName()}" + "_"  + "$i.$extension"

//                println(dst)
                Files.move(Path(it), Path(dst), StandardCopyOption.REPLACE_EXISTING)
            }
        } else {
            val targetPath = Config.targetPath

            for (i in bookPath.indices) {

                val it = bookPath[i]

                val bookPath = "$targetPath${makeSaveFolderName()}\\"

                if (!Files.isDirectory(Path(bookPath))) {
                    Files.createDirectory(Path(bookPath))
                }

                val extension = Path(it).extension

                val dst = "${bookPath}${makeSaveFileName()}" + "_" + "$i.$extension"

                Files.move(Path(it), Path(dst), StandardCopyOption.REPLACE_EXISTING)
            }
        }

    }

    override fun toString():String {
        return "filename:$name, name:$douban_name, category:$douban_category, score:$douban_score, publisher:$douban_publisher, author:$douban_author, date:$douban_date"
    }


}