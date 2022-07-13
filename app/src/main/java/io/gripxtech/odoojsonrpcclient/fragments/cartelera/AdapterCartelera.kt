package io.gripxtech.odoojsonrpcclient.fragments.cartelera

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.utils.recycler.RecyclerBaseAdapter
import io.gripxtech.odoojsonrpcclient.formatTo
import io.gripxtech.odoojsonrpcclient.fragments.cartelera.entities.Cartelera
import io.gripxtech.odoojsonrpcclient.toDate
import kotlinx.android.synthetic.main.fragment_cartelera_.*
import kotlinx.android.synthetic.main.item_new_cartelera.view.*

class AdapterCartelera(
    private val fragment: Fragment_Cartelera,
    items: ArrayList<Any>
): RecyclerBaseAdapter(items, fragment.rvNoticias) {

    var starClick = true

    companion object {
        const val TAG: String = "CarteleraAdapter"
        private const val VIEW_TYPE_ITEM = 0
    }

    private val rowItems: ArrayList<Cartelera> = ArrayList(
        items.filterIsInstance<Cartelera>()
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            AdapterCartelera.VIEW_TYPE_ITEM -> {
                val view = inflater.inflate(
                    R.layout.item_new_cartelera,
                    parent,
                    false
                )
                return ViewHolderCartelera(view)
            }
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(baseHolder: RecyclerView.ViewHolder, basePosition: Int) {
        super.onBindViewHolder(baseHolder, basePosition)
        val position = baseHolder.adapterPosition
        when (getItemViewType(basePosition)) {
            AdapterCartelera.VIEW_TYPE_ITEM -> {
                val holder = baseHolder as ViewHolderCartelera
                val item = items[position] as Cartelera

                holder.itemView.title.text = item.title
                holder.itemView.contentBody.text = item.description
                holder.itemView.fecha.text = formatDate(item.date)


            }
        }
    }

    val rowItemCount: Int get() = rowItems.size

    override fun getItemViewType(position: Int): Int {
        val o = items[position]
        if (o is Cartelera) {
            return AdapterCartelera.VIEW_TYPE_ITEM
        }
        return super.getItemViewType(position)
    }

    private fun updateRowItems() {
        updateSearchItems()
        rowItems.clear()
        rowItems.addAll(
            ArrayList(
                items.filterIsInstance<Cartelera>()
            )
        )
    }

    fun addRowItems(rowItems: ArrayList<Cartelera>) {
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

    private fun formatDate(fecha: String): String? {
        return fecha.toDate("yyyy-MM-d").formatTo("dd-MM-yyyy")
    }
}