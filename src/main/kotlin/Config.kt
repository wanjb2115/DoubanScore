import java.nio.file.Files
import kotlin.io.path.Path

class Config {
    companion object {
        // 存放电子书的目录
        const val folderPath = "E:\\BaiduNetdiskDownload"
        // 规格化后保存的目录
        const val targetPath = "I:\\电子书\\Douban\\"
        // 存放“找到豆瓣电子书链接但没有评分”的电子书目录
        const val unScorePath = "I:\\电子书\\未评分\\"
        // 存放“未找到豆瓣书籍链接”的电子书目录
        const val unDoubanPath = "I:\\电子书\\未找到\\"

        // 自定义的书籍分类
        val availableCategory = listOf<String>("科普", "计算机", "心理", "传记", "成长", "文学", "管理", "育儿", "女性",
                            "历史", "经济", "社会", "小说", "漫画", "互联网", "学习", "职场", "艺术", "工具", "哲学", "政治", "生活")

        // 检查config设置的目录是否有效
        fun checkConfigFolder():Boolean {
            return Files.isDirectory(Path(folderPath)) && Files.isDirectory(Path(targetPath)) && Files.isDirectory(Path(unScorePath)) && Files.isDirectory(Path(unDoubanPath))
        }
    }
}