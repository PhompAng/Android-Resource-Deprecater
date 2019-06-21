
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import model.DeprecatedResource
import org.jdom.Element

@State(name = "Deprecated Resource", defaultStateAsResource = true, storages = [Storage("deprecated-resources.xml")])
class DeprecatedResourceConfig : PersistentStateComponent<Element> {

    companion object {
        private const val DEPRECATED_LIST_TAG = "list"
        private const val DEPRECATED_TYPE_TAG = "type"
        private const val DEPRECATED_TYPE_TAG_ATTR = "name"
        private const val DEPRECATED_TAG = "resource"

        fun getInstance(project: Project): DeprecatedResourceConfig {
            return ServiceManager.getService(project, DeprecatedResourceConfig::class.java)
        }
    }

    var deprecatedResourcesMap = mutableMapOf<String, MutableSet<DeprecatedResource>>()

    override fun getState(): Element? {
        val root = Element("root")
        val deprecatedListTag = Element(DEPRECATED_LIST_TAG)
        for ((key, deprecatedResources) in deprecatedResourcesMap) {
            if (key.isBlank()) continue
            val deprecatedTypeTag = Element(DEPRECATED_TYPE_TAG)
            deprecatedTypeTag.setAttribute(DEPRECATED_TYPE_TAG_ATTR, key)
            for (deprecatedResource in deprecatedResources) {
                if (deprecatedResource.isEmpty()) continue
                val deprecatedTag = Element(DEPRECATED_TAG)
                deprecatedResource.writeExternal(deprecatedTag)
                deprecatedTypeTag.addContent(deprecatedTag)
            }
            deprecatedListTag.addContent(deprecatedTypeTag)
        }

        root.addContent(deprecatedListTag)
        return root
    }

    override fun loadState(state: Element) {
        val deprecatedListTag = state.getChild(DEPRECATED_LIST_TAG)
        if (deprecatedListTag != null) {
            for (deprecatedTypeTag in deprecatedListTag.getChildren(DEPRECATED_TYPE_TAG)) {
                val key = deprecatedTypeTag.getAttributeValue(DEPRECATED_TYPE_TAG_ATTR)
                if (key.isBlank()) continue
                val values = mutableSetOf<DeprecatedResource>()
                for (deprecatedTag in deprecatedTypeTag.getChildren(DEPRECATED_TAG)) {
                    values.add(DeprecatedResource(key, deprecatedTag))
                }
                deprecatedResourcesMap[key] = values
            }
        }
    }

    fun getDeprecatedResources() = deprecatedResourcesMap.flatMap { it.value }
    fun setDeprecatedResources(items: List<DeprecatedResource>) {
        items.forEach {
            deprecatedResourcesMap.getOrPut(it.type, { mutableSetOf() }).add(it)
        }
    }
}