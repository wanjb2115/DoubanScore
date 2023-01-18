import java.nio.file.Files
import kotlin.io.path.Path

class Config {
    companion object {
        const val folderPath = "E:\\BaiduNetdiskDownload"
        const val targetPath = "G:\\电子书\\Douban\\"
        const val unScorePath = "G:\\电子书\\未评分\\"
        const val unDoubanPath = "G:\\电子书\\未找到\\"

        fun checkConfigFolder():Boolean {
            return Files.isDirectory(Path(folderPath)) && Files.isDirectory(Path(targetPath)) && Files.isDirectory(Path(unScorePath)) && Files.isDirectory(Path(unDoubanPath))
        }
    }
}