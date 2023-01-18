import java.io.File

class FileHelper {

    fun ScanFile(folderName:String):MutableList<BookFileInfo>  {
        val fileNames: MutableList<BookFileInfo> = mutableListOf()
        val fileTree: FileTreeWalk = File(folderName).walk()

        fileTree.maxDepth(3)
            .filter { it.isFile }
            .filter { it.extension in listOf("mobi", "epub", "pdf", "azw3", ".txt") }
            .forEach { fileNames.add(BookFileInfo(it.name, it.path)) }

        return fileNames
    }
}

class BookFileInfo(var name:String, var path:String) {
    init {
        name = Book.findName(name)
    }
}