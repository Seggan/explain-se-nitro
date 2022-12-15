private val mdToHtml = mapOf(
    ">(.*?)" to "<blockquote>$1</blockquote>",
    "```(.*?)```" to "<pre><code>$1</code></pre>",
    "`(.*?)`" to "<code>$1</code>",
    "# (.*?)" to "<h1>$1</h1>",
    "## (.*?)" to "<h2>$1</h2>",
    "### (.*?)" to "<h3>$1</h3>",
    "#### (.*?)" to "<h4>$1</h4>",
    "##### (.*?)" to "<h5>$1</h5>",
    "###### (.*?)" to "<h6>$1</h6>",
    "\\[(.*?)\\]\\((.*?)\\)" to "<a href=\"$2\">$1</a>",
    "\\*(.*?)\\*" to "<em>$1</em>",
    "\\*\\*(.*?)\\*\\*" to "<strong>$1</strong>",
    "\\n\\n" to "<br>",
).mapKeys { it.key.toRegex() }

fun String.parseMd(): String {
    var result = this
    mdToHtml.forEach { (regex, replacement) ->
        result = result.replace(regex, replacement)
    }
    return result
}