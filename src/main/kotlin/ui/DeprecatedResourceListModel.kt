package ui

import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import model.DeprecatedResource
import javax.swing.SortOrder

class DeprecatedResourceListModel(
    items: List<DeprecatedResource>
) : ListTableModel<DeprecatedResource>(arrayOf(NamedColumnInfo()), items, 0, SortOrder.UNSORTED) {

    private class NamedColumnInfo : ColumnInfo<DeprecatedResource, String>("name") {
        override fun valueOf(item: DeprecatedResource?): String? {
            return item?.let {
                "@${item.type}/${item.name}"
            } ?: ""
        }
    }
}