import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event
import org.w3c.dom.url.URLSearchParams
import org.w3c.fetch.Response
import kotlin.math.max
import kotlin.math.min

fun main() {
    window.onload = {
        try {
            onLoad()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

private val comicRegex = "\\(\\( (\\d+) \\)\\)".toRegex()

private val chaptersPerPart = mapOf(
    1 to 6,
    2 to 6
)

fun onLoad() {
    val (part, chapter) = getPartAndChapter()
    window.fetch("https://raw.githubusercontent.com/Seggan/explain-se-nitro/master/explanations/p$part/c$chapter.md")
        .then(Response::text)
        .then(String::parseMd)
        .then {
            val html = it.replace(comicRegex) { match ->
                val comic = match.groupValues[1]
                """<a href="https://se-nitro.surge.sh/#$part-$chapter-$comic"><img src="https://se-nitro.surge.sh/comics/p$part/c$chapter/$comic.png"></a>"""
            }
            document.getElementById("content")!!.innerHTML = html
        }
    "prev-s".addEventListener("click") {
        window.location.search = "?p=${max(1, part - 1)}&c=$chapter"
    }
    "next-s".addEventListener("click") {
        window.location.search = "?p=${min(part + 1, 2)}&c=$chapter"
    }
    "prev-c".addEventListener("click") {
        window.location.search = "?p=$part&c=${max(1, chapter - 1)}"
    }
    "next-c".addEventListener("click") {
        window.location.search = "?p=$part&c=${min(chapter + 1, chaptersPerPart[part]!!)}"
    }
    document.getElementById("section")!!.textContent = "Part $part"
    document.getElementById("chapter")!!.textContent = "Chapter $chapter"
}

private fun getPartAndChapter(): Pair<Int, Int> {
    val params = URLSearchParams(window.location.search)
    val part = params.get("p")?.toIntOrNull() ?: 1
    val chapter = params.get("c")?.toIntOrNull() ?: 1
    return part to chapter
}

private fun String.addEventListener(event: String, callback: (Event) -> Unit) {
    document.getElementById(this)!!.addEventListener(event, {
        try {
            callback(it)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    })
}