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
        fun findName(originName:String):String {

            var reName = originName.replace("""[^(a-zA-Z0-9\\u4e00-\\u9fa5)]""", "")

            reName = reName.replace(Regex("""\[\d?\.?\d?]"""), "")

            reName = reName.split(".")[0].split(" ")[0].split("_")[0]

            reName = reName.split("（")[0].split("【")[0].split("(")[0]

            return reName
        }

        var BookHash = HashMap<String, Book>()
        var NameHash = HashMap<String, String>()

        fun getCategory(cCategory:String):String {
            val availableCategory = listOf<String>("科普", "计算机", "心理", "传记", "成长", "文学", "管理", "育儿", "女性", "历史", "经济", "社会", "小说", "漫画", "互联网", "学习", "职场", "艺术", "工具", "哲学", "政治", "生活")

            for (it:String in availableCategory) {
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