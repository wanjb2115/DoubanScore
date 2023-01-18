import java.io.File

class FileHelper {
    // 搜索该目录下的所有书籍文件（以"mobi", "epub", "pdf", "azw3", "txt"为结尾的文件），最多搜索3级子目录
    fun ScanFile(folderName:String):MutableList<BookFileInfo>  {
        val fileNames: MutableList<BookFileInfo> = mutableListOf()
        val fileTree: FileTreeWalk = File(folderName).walk()

        fileTree.maxDepth(3)
            .filter { it.isFile }
            .filter { it.extension in listOf("mobi", "epub", "pdf", "azw3", "txt") }
            .forEach { fileNames.add(BookFileInfo(it.name, it.path)) }

        return fileNames
    }
}

class BookFileInfo(var name:String, var path:String) {
    init {
        name = Book.findName(name)
    }
}