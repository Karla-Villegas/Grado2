package io.gripxtech.odoojsonrpcclient.fragments.ministerios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.utils.recycler.RecyclerBaseAdapter
import io.gripxtech.odoojsonrpcclient.fragments.ministerios.entities.Ministerio
import kotlinx.android.synthetic.main.fragment_ministerios.*
import kotlinx.android.synthetic.main.item_new_ministry.view.*

class AdapterMinisterio(
    private val fragment: Fragment_Ministerios,
    items: ArrayList<Any>
) : RecyclerBaseAdapter(items, fragment.rv_ministerios) {

    var starClick = true

    companion object {
        const val TAG: String = "MinisterioAdapter"
        private const val VIEW_TYPE_ITEM = 0
    }

    private val rowItems: ArrayList<Ministerio> = ArrayList(
        items.filterIsInstance<Ministerio>()
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
           VIEW_TYPE_ITEM -> {
                val view = inflater.inflate(
                    R.layout.item_new_ministry,
                    parent,
                    false
                )
                return ViewHolderMinisterio(view)
            }
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(baseHolder: RecyclerView.ViewHolder, basePosition: Int) {
        super.onBindViewHolder(baseHolder, basePosition)
        val position = baseHolder.adapterPosition
        when (getItemViewType(basePosition)) {
           VIEW_TYPE_ITEM -> {
                val holder = baseHolder as ViewHolderMinisterio
                val item = items[position] as Ministerio

                holder.itemView.ministerio_name.text = item.name


            }
        }
    }

    val rowItemCount: Int get() = rowItems.size

    override fun getItemViewType(position: Int): Int {
        val o = items[position]
        if (o is Ministerio) {
            return VIEW_TYPE_ITEM
        }
        return super.getItemViewType(position)
    }

    private fun updateRowItems() {
        updateSearchItems()
        rowItems.clear()
        rowItems.addAll(
            ArrayList(
                items.filterIsInstance<Ministerio>()
            )
        )
    }

    fun addRowItems(rowItems: ArrayList<Ministerio>) {
        this.rowItems.addAll(rowItems)
        addAll(rowItems.toMutableList<Any>() as ArrayList<Any>)
    }

    override fun clear() {
        rowItems.clear()
        super.clear()
    }

    fun setCanStart(can: Boolean) {
        starClick = can
    }

}