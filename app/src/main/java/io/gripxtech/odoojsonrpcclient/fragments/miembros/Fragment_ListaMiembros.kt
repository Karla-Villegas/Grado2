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
import android.widget.TextView
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
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.detalles_miembros.view.*
import org.json.JSONArray
import org.json.JSONObject
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
    private lateinit var IcMiembros: Any
    private var items = ArrayList<Miembros>()
    private var list_name_departament: ArrayList<String> = arrayListOf()


    val adapter: AdapterMiembros by lazy {
        AdapterMiembros(this, arrayListOf())
    }

    val adapterMnisterio: Adapter_MinisterioB by lazy {
        Adapter_MinisterioB(this, arrayListOf())
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

                        if (binding.rvMiembros != null) { OnClick() }
                        adapter.addRowItems(items)

                        Timber.e("ITEMS believers--->  ${items}")
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
                    IcMiembros = items.get(position)
                    believer_id = (IcMiembros as Miembros).serverId
                    Toast.makeText(requireContext(), "ID BELIEVER: ${believer_id}", Toast.LENGTH_SHORT).show()
                    detalleBeliever(believer_id.toInt(), v)
                }
            }
        }
    }

    private fun detalleBeliever(believerId: Int, v: View) {
        Odoo.route("believer/$believerId", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        val result = call.result.asString.toJsonObject().get("record")
                        val item = gson.fromJson<Miembros>(result, MiembroType)
                        Timber.e("item --->  ${item}")

                        val AlertDialog = AlertDialog.Builder(requireContext()).create()
                        val view = layoutInflater.inflate(R.layout.detalles_miembros, null)
                        view.rv_miembro_ministerio.layoutManager = LinearLayoutManager(requireContext())
                        view.rv_miembro_ministerio.adapter = adapterMnisterio
                        view.rv_miembro_ministerio.setHasFixedSize(true)


                        /** lógica para adaptar los campos de los detalles del believer*/
                        val name = item.name
                        val identity = item.identity
                        val state = JSONObject(item.state.toString()).optString("name")
                        val municipality = JSONObject(item.municipality.toString()).optString("name")
                        val parish = JSONObject(item.parish.toString()).optString("name")
                        val sector = item.sector
                        val street = item.street
                        val building = item.building
                        val house = item.house
                        val localphone_number = item.localphone_number
                        val cellphone_number = item.cellphone_number


                        if(name != null && name != "false") view.DET_nombreMiembro.text = name else view.DET_nombreMiembro.text = ""
                        if(identity != null && identity != "false") view.DET_cedulaMiembro.text = identity else view.DET_cedulaMiembro.text = ""
                        if(localphone_number != null && localphone_number != "false") view.DET_telefonoMiembro.text = localphone_number else view.DET_telefonoMiembro.text = ""
                        if(state != null && state != "false") view.DET_estadoMiembro.text = state else view.DET_estadoMiembro.text = ""
                        if(municipality != null && municipality != "false") view.DET_municipioMiembro.text = municipality else view.DET_municipioMiembro.text = ""
                        if(parish != null && parish != "false") view.DET_parroquiaMiembro.text = parish else view.DET_parroquiaMiembro.text = ""
                        if(sector != null && sector != "false") view.DET_sectorMiembro.text = sector else view.DET_sectorMiembro.text = ""
                        if(street != null && street != "false") view.DET_calleMiembro.text = street else view.DET_calleMiembro.text = ""
                        if(building != null && building != "false") view.DET_edificioMiembro.text = building else view.DET_edificioMiembro.text = ""
                        if(house != null && house != "false") view.DET_NumeroEdificioMiembro.text = house else view.DET_NumeroEdificioMiembro.text = ""


                        val department_ids = JSONArray(item.department_ids.toString())
                        for (i in 0 until department_ids.length()){
                            val nameMinisterio = department_ids.getJSONObject(i).optString("name")
                            list_name_departament.add(nameMinisterio)
                        }

                        if(list_name_departament != null && list_name_departament.size > 0){
                            adapterMnisterio.addRowItems(list_name_departament as List<JSONArray>)
                        }else{
                            val rv_miembro_ministerio = view.findViewById<RecyclerView>(R.id.rv_miembro_ministerio)
                            val textChat = view.findViewById<TextView>(R.id.textChat)
                            rv_miembro_ministerio.visibility = View.GONE
                            textChat.visibility = View.VISIBLE
                        }
                        /****************************************************************/

                        /** logica para los botones "editar" */
                        val editButton = view.findViewById<ImageView>(R.id.editButton)
                        editButton.setOnClickListener {

                        }



                        /*************************************/

                        AlertDialog.setView(view)
                        AlertDialog.setCancelable(true)
                        AlertDialog.setOnDismissListener { adapter.starClick = true }
                        val idIconReturn = view.findViewById<ImageView>(R.id.idIconReturn)
                        idIconReturn.setOnClickListener {
                            AlertDialog.dismiss()
                            adapter.setCanStart(true)
                            list_name_departament.clear()
                            adapterMnisterio.clear()
                            Log.e("DETALLE list 3--->", "${  list_name_departament}")
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