package ui

import DeprecatedResourceConfig
import TextAttributeFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.table.TableView
import model.DeprecatedResource
import java.util.*
import javax.swing.JComponent
import javax.swing.ListSelectionModel

class ConfigPanel(private val project: Project) : Configurable, Configurable.NoScroll {
    private var mPanel: JComponent? = null
    private lateinit var deprecatedTable: TableView<DeprecatedResource>
    private lateinit var models: DeprecatedResourceListModel
    private lateinit var chooserPanel: ChooserPanel

    override fun isModified(): Boolean {
        return true
    }

    override fun getDisplayName(): String {
        return "Android Resource Deprecater"
    }

    override fun apply() {
        DeprecatedResourceConfig.getInstance(project).setDeprecatedResources(deprecatedTable.items)
        TextAttributeFactory.update(project)
    }

    override fun createComponent(): JComponent? {
        if (mPanel == null) {
            mPanel = createPanel()
        }
        return mPanel
    }

    private fun createPanel(): JComponent {
        models =
            DeprecatedResourceListModel(cloneFromStorage())
        deprecatedTable = TableView<DeprecatedResource>(models).apply {
            setShowColumns(false)
            isStriped = true
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        }
        deprecatedTable.selectionModel.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                updateChooserPanel()
            }
        }

        chooserPanel = ChooserPanel()
        val panel = panel {
            row {
                JBSplitter(false, 0.3f).apply {
                    firstComponent = ToolbarDecorator.createDecorator(deprecatedTable)
                        .setAddAction {
                            addAdd()
                        }
                        .setEditAction {
                            //TODO Edit
                        }
                        .createPanel()
                    secondComponent = chooserPanel.getPanel()
                }(CCFlags.grow, CCFlags.grow)
            }
        }

        chooserPanel.addListener(object : ChooserPanel.ResourceUpdateListener {
            override fun onUpdate() {
                deprecatedTable.selectedObject?.let {
                    val selectedRow = deprecatedTable.selectedRow
                    chooserPanel.apply(it)
                    models.fireTableRowsUpdated(selectedRow, selectedRow)
                }
            }
        })

        updateChooserPanel()

        return panel
    }

    private fun addAdd() {
        models.addRow(DeprecatedResource("", ""))
        val newRow = models.rowCount - 1
        deprecatedTable.selectionModel.setSelectionInterval(newRow, newRow)
    }

    private fun updateChooserPanel() {
        val value = deprecatedTable.selectedObject
        if (value != null) {
            chooserPanel.reset(value)
        } else {
            chooserPanel.reset()
        }
    }

    private fun cloneFromStorage(): List<DeprecatedResource> {
        val clone = ArrayList<DeprecatedResource>()
        for (deprecatedResource in DeprecatedResourceConfig.getInstance(project).getDeprecatedResources()) {
            clone.add(deprecatedResource.copy())
        }

        return clone
    }
}