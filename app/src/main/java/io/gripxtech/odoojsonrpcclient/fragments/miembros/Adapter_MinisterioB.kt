package io.gripxtech.odoojsonrpcclient.fragments.miembros

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.utils.recycler.RecyclerBaseAdapter
import kotlinx.android.synthetic.main.detalles_miembros.*
import kotlinx.android.synthetic.main.fragment_miembros.*
import org.json.JSONArray

class Adapter_MinisterioB(
    private val fragment: Fragment_ListaMiembros,
    items: ArrayList<Any>
) : RecyclerBaseAdapter(items, fragment.rv_miembros) {

    companion object {
        const val TAG: String = "MiembrosBAdapter"
        private const val VIEW_TYPE_ITEM = 0
    }

    private val rowItems: ArrayList<Any> = ArrayList(
        items.filterIsInstance<Any>()
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = inflater.inflate(
                    R.layout.item_view_lista_miembros,
                    parent,
                    false
                )
                return ViewHolderMiembros(view)
            }
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(baseHolder: RecyclerView.ViewHolder, basePosition: Int) {
        super.onBindViewHolder(baseHolder, basePosition)
        val position = baseHolder.adapterPosition
        when (getItemViewType(basePosition)) {
            VIEW_TYPE_ITEM -> {
                val holder = baseHolder as ViewHolderMiembros
                val item = items[position]
                var objs = JSONArray(items)

                var ministerio = holder.itemView.findViewById<TextView>(R.id.nameMiembro)
                ministerio.text = objs.get(position).toString()


            }
        }
    }

    val rowItemCount: Int get() = rowItems.size

    override fun getItemViewType(position: Int): Int {
        val o = items[position]
        if (o is Any) {
            return VIEW_TYPE_ITEM
        }
        return super.getItemViewType(position)
    }

    private fun updateRowItems() {
        updateSearchItems()
        rowItems.clear()
        rowItems.addAll(
            ArrayList(
                items.filterIsInstance<Any>()
            )
        )
    }

    fun addRowItems(rowItems: List<JSONArray>) {
        this.rowItems.addAll(rowItems)
        addAll(rowItems.toMutableList<Any>() as ArrayList<Any>)
    }

    override fun clear() {
        rowItems.clear()
        super.clear()
    }

}