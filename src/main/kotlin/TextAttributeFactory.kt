
import com.intellij.openapi.project.Project
import model.DeprecatedResource
import model.TextAttributeType

object TextAttributeFactory {
    private lateinit var deprecatedResourcesMap: MutableMap<String, MutableSet<DeprecatedResource>>

    fun update(project: Project) {
        deprecatedResourcesMap = DeprecatedResourceConfig.getInstance(project).deprecatedResourcesMap
    }

    fun get(project: Project, type: String, name: String): TextAttributeType? {
        if (!::deprecatedResourcesMap.isInitialized) {
            update(project)
        }
        return (deprecatedResourcesMap.getOrElse(type) { null })?.let { resourceNames ->
            if (resourceNames.any { it.name == name }) {
                TextAttributeType.DEPRECATED
            } else {
                TextAttributeType.NORMAL
            }
        }
    }
}