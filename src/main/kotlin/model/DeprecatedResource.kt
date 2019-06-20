package model

import org.jdom.Element

data class DeprecatedResource(
    var type: String,
    var name: String
) {
    constructor(type: String, element: Element?) : this(
        type = type,
        name = element?.getAttributeValue("name") ?: ""
    )

    fun writeExternal(element: Element) {
        element.setAttribute("name", name)
    }

    fun isEmpty(): Boolean {
        return name.isBlank() || type.isBlank()
    }
}