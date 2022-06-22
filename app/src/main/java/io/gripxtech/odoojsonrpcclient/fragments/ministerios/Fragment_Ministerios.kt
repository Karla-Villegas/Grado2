package io.gripxtech.odoojsonrpcclient.fragments.ministerios

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMinisteriosBinding
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.gripxtech.odoojsonrpcclient.fragments.ministerios.entities.Ministerio
import kotlinx.android.synthetic.main.detalles_miembros.view.*
import kotlinx.android.synthetic.main.detalles_ministerio.view.*
import org.json.JSONArray
import timber.log.Timber


class Fragment_Ministerios : Fragment() {

    private var _binding: FragmentMinisteriosBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: NewActivityPrincipal private set
    private val MinisterioListType = object : TypeToken<ArrayList<Ministerio>>() {}.type
    private val MinisterioType = object : TypeToken<Ministerio>() {}.type
    private var items = ArrayList<Ministerio>()
    private lateinit var IcMInisterio: Any
    private var departament_id: Long = 0
    private var list_name_believer: ArrayList<String> = arrayListOf()

    val adapter: AdapterMinisterio by lazy {
        AdapterMinisterio(this, arrayListOf())
    }

    val adapterDetalleDepartment: Adapter_Believer_Department by lazy {
        Adapter_Believer_Department(this, arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMinisteriosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as NewActivityPrincipal

        binding.rvMinisterios.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvMinisterios.layoutManager = layoutManager
        binding.rvMinisterios.adapter = adapter

        adapter.setupScrollListener(binding.rvMinisterios)


        fetchMinisterios()


    }

    private fun fetchMinisterios() {
        binding.shimmerLayoutMinisterio.startShimmer()
        adapterDetalleDepartment.clear()
        Odoo.route("/departments", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        val result = call.result.asString.toJsonObject()
                        val icMinisterio = result.get("records")
                        items = gson.fromJson(icMinisterio, MinisterioListType)
                        if (binding.rvMinisterios != null) {
                            OnCLick()
                        }
                        showRecyclerView()
                        adapter.addRowItems(items)


                        Timber.w("MINISTERIOS RESULT ${result}")
                        Timber.w("MINISTERIOS ITEMS ${items}")
                    } else {
                        showRecyclerView()
                        Timber.w("callkw() failed with ${it.errorBody()}")

                    }
                } else {
                    showRecyclerView()
                    Timber.w("request failed with ${it.code()}:${it.message()}")
                }
            }
            this.onError { error ->
                showRecyclerView()
                error.printStackTrace()
            }
            this.onComplete { }
        }
    }

    private fun OnCLick() {
        binding.rvMinisterios.onItemClick { recyclerView, position, v ->
            if (!items.isEmpty()) {
                if (adapter.starClick) {
                    adapter.starClick = false
                    IcMInisterio = items.get(position)
                    departament_id = (IcMInisterio as Ministerio).serverId
                    Toast.makeText(
                        requireContext(),
                        "ID DEPARTAMENT: ${departament_id}",
                        Toast.LENGTH_SHORT
                    ).show()
                    detalleDepartament(departament_id.toInt(), v)
                }
            }
        }
    }

    private fun detalleDepartament(departament_id: Int, v: View) {
        Log.e("DETALLE DEPARTAMENT--->", "${departament_id}")
        Odoo.route("/department/$departament_id", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        adapterDetalleDepartment.clear()
                        val result = call.result.asString.toJsonObject().get("record")
                        val item = gson.fromJson<Ministerio>(result, MinisterioType)

                        val AlertDialog = AlertDialog.Builder(requireContext()).create()
                        val view = layoutInflater.inflate(R.layout.detalles_ministerio, null)
                        view.rv_detalleMinisterio.layoutManager = LinearLayoutManager(requireContext())
                        view.rv_detalleMinisterio.adapter = adapterDetalleDepartment
                        view.rv_detalleMinisterio.setHasFixedSize(true)

                        /** logica para los detalles de cada ministerio y los miembros que pertencen a el*/
                        val name = item.name
                        val believer_ids = JSONArray(item.believer_ids.toString())
                        for (i in 0 until believer_ids.length()) {
                            val nameBeliever = believer_ids.getJSONObject(i).optString("name")
                            list_name_believer.add(nameBeliever)
                        }

                        if(name != null) view.DET_nombreMinisterio.text = name else view.DET_nombreMinisterio.text = ""
                        adapterDetalleDepartment.addRowItems(list_name_believer as List<JSONArray>)
                        /**..............................................................................*/

                        AlertDialog.setView(view)
                        AlertDialog.setCancelable(true)
                        AlertDialog.setOnDismissListener { adapter.starClick = true }
                        val idIconReturn = view.findViewById<ImageView>(R.id.idIconReturn)
                        idIconReturn.setOnClickListener {
                            AlertDialog.dismiss()
                            adapter.setCanStart(true)
                            adapterDetalleDepartment.clear()
                            list_name_believer.clear()
                        }
                        AlertDialog.show()
                        AlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        AlertDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

                        Timber.w("MINISTERIO RESULT ${result}")
                        Timber.w("MINISTERIO DETALLE ${item}")
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerView() {
        binding.shimmerLayoutMinisterio.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rvMinisterios.visibility = View.VISIBLE
    }
}