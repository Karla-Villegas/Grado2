package io.gripxtech.odoojsonrpcclient.fragments.miembros

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMiembrosBinding
import io.gripxtech.odoojsonrpcclient.fragments.cartelera.entities.Cartelera
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.detalles_miembros.view.*
import timber.log.Timber

class Fragment_ListaMiembros: Fragment() {

    private var _binding: FragmentMiembrosBinding? = null
    private val binding get() = _binding!!
    private val MiembrosListType = object : TypeToken<ArrayList<Miembros>>() {}.type
    private val MiembroType = object : TypeToken<Miembros>() {}.type
    private val limit = RECORD_LIMIT
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var activity: NewActivityPrincipal private set
    private var believer_id: Long = 0
    private lateinit var Ic: Any
    private var items = ArrayList<Miembros>()


    val adapter: AdapterMiembros by lazy {
        AdapterMiembros(this, arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMiembrosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as NewActivityPrincipal

        binding.rvMiembros.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvMiembros.layoutManager = layoutManager
        binding.rvMiembros.adapter = adapter

        adapter.setupScrollListener(binding.rvMiembros)
        /*if (!adapter.hasRetryListener()) {
            adapter.retryListener {
                fetchMiembros()
            }
        }

        if (adapter.rowItemCount == 0) {
            adapter.showMore()
            fetchMiembros()
        }*/

        fetchMiembros()
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_nav_miembros_to_registro)
        }
    }

    private fun fetchMiembros() {
        binding.shimmerLayout.startShimmer()
        Odoo.route("/believers", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        showRecyclerView()
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        val result = call.result.asString.toJsonObject()
                        val icMiembros = result.get("records")
                        items = gson.fromJson<ArrayList<Miembros>>(icMiembros, MiembrosListType )
                        Timber.e("RESULT believers--->  ${result}")
                        Timber.e("ITEMS believers--->  ${items}")
                        if (binding.rvMiembros != null) {
                            OnClick()
                        }
                        adapter.addRowItems(items)

                    } else {
                        Timber.w("callkw() failed with ${it.errorBody()}")

                    }
                } else {
                    Timber.w("request failed with ${it.code()}:${it.message()}")
                }
            }
            this.onError { error ->
                error.printStackTrace()
            }
            this.onComplete { }
        }
    }

    private fun OnClick(){
        binding.rvMiembros.onItemClick{recyclerView, position, v ->
            if(!items.isEmpty()){
                if(adapter.starClick){
                    adapter.starClick = false
                    Ic = items.get(position)
                    believer_id = (Ic as Miembros).serverId
                    Toast.makeText(requireContext(), "ID: ${believer_id}", Toast.LENGTH_SHORT).show()
                    detalleBeliever(believer_id.toInt(), v)
                }
            }
        }
    }

    private fun detalleBeliever(believerId: Int, v: View) {
        Log.e("DETALLE B--->", "${believerId}")
        Odoo.route("believer/$believerId", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        val result = call.result.asString.toJsonObject()
                        val icDetallesMiembros = result.get("record")
                        val item = gson.fromJson<Miembros>(icDetallesMiembros, MiembroType)
                        Timber.e("item --->  ${item}")

                        val AlertDialog = AlertDialog.Builder(requireContext()).create()
                        val view = layoutInflater.inflate(R.layout.detalles_miembros, null)
                        /** lógica para adaptar los campos de los detalles del believer*/

                        val name = item.name
                        val identity = item.identity
                        val state = item.state
                        val municipality = item.municipality
                        val parish = item.parish
                        val sector = item.sector
                        val street = item.street
                        val building = item.building
                        val house = item.house
                        val localphone_number = item.localphone_number


                        Timber.e("DATOS --->  ${name}, ${identity}, ${state}, ${municipality}, ${parish}, ${sector}, ${street}, ${building}, ${house}, ${localphone_number}")

                      //if(item.name != null) view.DET_nombreMiembro.text = item.name else view.DET_nombreMiembro.text = ""

                        /****************************************************************/

                        AlertDialog.setView(view)
                        AlertDialog.setCancelable(true)
                        AlertDialog.setOnDismissListener { adapter.starClick = true }
                        val idIconReturn = view.findViewById<ImageView>(R.id.idIconReturn)
                        idIconReturn.setOnClickListener {
                            AlertDialog.dismiss()
                            adapter.setCanStart(true)
                        }
                        AlertDialog.show()
                        AlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        AlertDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

                    } else {
                        Timber.w("callkw() failed with ${it.errorBody()}")

                    }
                } else {
                    Timber.w("request failed with ${it.code()}:${it.message()}")
                }
            }
            this.onError { error ->
                error.printStackTrace()
            }
            this.onComplete { }
        }
    }

    private fun showRecyclerView() {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvMiembros.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}