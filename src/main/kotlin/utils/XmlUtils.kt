package utils

object XmlUtils {
    fun extractAndroidResource(value: String): Array<String>? {
        return if (value.startsWith("@")) {
            val valueTokens = value.split("/")
            val type = valueTokens.getOrNull(0)?.removePrefix("@") ?: ""
            val name = valueTokens.getOrNull(1) ?: ""
            arrayOf(type, name)
        } else {
            null
        }
    }
}