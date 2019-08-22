
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText
import model.TextAttributeType
import utils.XmlUtils

class XmlAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val project = element.project
        val androidResource = when (element) {
            is XmlAttributeValue -> XmlUtils.extractAndroidResource(element.value)
            is XmlText -> XmlUtils.extractAndroidResource(element.text)
            is XmlTag -> arrayOf(element.name, element.getAttributeValue("name") ?: "")
            else -> null
        }
        val textRange = when (element) {
            is XmlAttributeValue -> element.valueTextRange
            is XmlText -> element.textRange
            is XmlTag -> element.getAttribute("name")?.textRange //TODO value text range
            else -> null
        }

        androidResource ?: return
        textRange ?: return

        when (TextAttributeFactory.get(project, androidResource[0], androidResource[1])) {
            TextAttributeType.DEPRECATED -> {
                val annotation = holder.createWarningAnnotation(textRange, "${androidResource[0]}/${androidResource[1]} is deprecated.")
                annotation.textAttributes = CodeInsightColors.DEPRECATED_ATTRIBUTES
            }
            TextAttributeType.NORMAL -> {
                val annotation = holder.createInfoAnnotation(textRange, null)
                annotation.textAttributes = XmlHighlighterColors.XML_ATTRIBUTE_VALUE
            }
            else -> {}
        }
    }
}