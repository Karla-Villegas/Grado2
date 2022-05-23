package io.gripxtech.odoojsonrpcclient.fragments.miembros

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.utils.recycler.RecyclerBaseAdapter
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.gripxtech.odoojsonrpcclient.trimFalse
import kotlinx.android.synthetic.main.fragment_miembros.*
import kotlinx.android.synthetic.main.item_view_lista_miembros.view.*

class AdapterMiembros(
    private val fragment: Fragment_ListaMiembros,
    items: ArrayList<Any>
) : RecyclerBaseAdapter(items, fragment.rv_miembros) {

    companion object {
        const val TAG: String = "MiembrosAdapter"
        private const val VIEW_TYPE_ITEM = 0
    }

    private val rowItems: ArrayList<Miembros> = ArrayList(
        items.filterIsInstance<Miembros>()
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
                val item = items[position] as Miembros

                holder.itemView.nameMiembro.text = item.name
                holder.itemView.emailMiembro.text = item.email.trimFalse()


            }
        }
    }

    val rowItemCount: Int get() = rowItems.size

    override fun getItemViewType(position: Int): Int {
        val o = items[position]
        if (o is Miembros) {
            return VIEW_TYPE_ITEM
        }
        return super.getItemViewType(position)
    }

    private fun updateRowItems() {
        updateSearchItems()
        rowItems.clear()
        rowItems.addAll(
            ArrayList(
                items.filterIsInstance<Miembros>()
            )
        )
    }

    fun addRowItems(rowItems: ArrayList<Miembros>) {
        this.rowItems.addAll(rowItems)
        addAll(rowItems.toMutableList<Any>() as ArrayList<Any>)
    }

    override fun clear() {
        rowItems.clear()
        super.clear()
    }
}