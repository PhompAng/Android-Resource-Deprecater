
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttributeValue
import model.DeprecatedResource
import model.TextAttributeType

object TextAttributeFactory {
    private lateinit var deprecatedResourcesMap: MutableMap<String, MutableSet<DeprecatedResource>>

    fun update(project: Project) {
        deprecatedResourcesMap = DeprecatedResourceConfig.getInstance(project).deprecatedResourcesMap
    }

    fun get(project: Project, element: XmlAttributeValue): TextAttributeType {
        if (!::deprecatedResourcesMap.isInitialized) {
            update(project)
        }
        val value = element.value
        return if (value.startsWith("@")) {
            val valueTokens = value.split("/")
            val type = valueTokens[0].removePrefix("@")
            val name = valueTokens[1]
            (deprecatedResourcesMap.getOrElse(type) { null })?.let { resourceNames ->
                if (resourceNames.any { it.name == name }) {
                    TextAttributeType.DEPRECATED
                } else {
                    TextAttributeType.NORMAL
                }
            } ?: TextAttributeType.NORMAL
        } else {
            TextAttributeType.NORMAL
        }
    }
}