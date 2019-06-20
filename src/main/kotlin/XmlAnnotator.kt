
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import model.TextAttributeType

class XmlAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val project = element.project
        if (element is XmlAttributeValue) {
            when (TextAttributeFactory.get(project, element)) {
                 TextAttributeType.DEPRECATED -> {
                     val annotation = holder.createWarningAnnotation(element.valueTextRange, "${element.value} is deprecated.")
                     annotation.textAttributes = CodeInsightColors.DEPRECATED_ATTRIBUTES
                 }
                TextAttributeType.NORMAL -> {
                    val annotation = holder.createInfoAnnotation(element.valueTextRange, null)
                    annotation.textAttributes = XmlHighlighterColors.XML_ATTRIBUTE_VALUE
                }
            }
        }
    }
}