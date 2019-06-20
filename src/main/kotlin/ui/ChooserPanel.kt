package ui

import com.intellij.util.EventDispatcher
import model.DeprecatedResource
import java.awt.BorderLayout
import java.awt.event.ActionListener
import java.util.*
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

class ChooserPanel : JPanel(BorderLayout()) {
    private lateinit var mPanel: JPanel
    private lateinit var mResourceName: JTextField
    private lateinit var mResourceType: JTextField
    private lateinit var mSaveButton: JButton

    private val myDispatcher = EventDispatcher.create(ResourceUpdateListener::class.java)

    init {
        add(mPanel)

        val actionListener = ActionListener {
            myDispatcher.multicaster.onUpdate()
        }

        mSaveButton.addActionListener(actionListener)
    }

    fun getPanel(): JPanel = this

    fun reset() {
        mResourceName.text = null
        mResourceType.text = null
    }

    fun reset(deprecatedResource: DeprecatedResource) {
        mResourceName.text = deprecatedResource.name
        mResourceType.text = deprecatedResource.type
    }

    fun apply(deprecatedResource: DeprecatedResource) {
        deprecatedResource.name = mResourceName.text
        deprecatedResource.type = mResourceType.text
    }

    fun addListener(listener: ResourceUpdateListener) {
        myDispatcher.addListener(listener)
    }

    interface ResourceUpdateListener : EventListener {
        fun onUpdate()
    }
}
