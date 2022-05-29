package io.gripxtech.odoojsonrpcclient.fragments.miembros

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import timber.log.Timber

class Fragment_ListaMiembros: Fragment() {

    private var _binding: FragmentMiembrosBinding? = null
    private val binding get() = _binding!!
    private val MiembrosListType = object : TypeToken<ArrayList<Miembros>>() {}.type
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
                        Timber.e("callkw()--->  ${result}")
                        Timber.e("callkw()--->  ${items}")
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
                    /*detalleBeliever(believer_id.toInt(), v)*/
                }
            }
        }
    }

    private fun detalleBeliever(believerId: Int, v: View) {
        Log.e("DETALLE B--->", "${believerId}")
        Odoo.load(
            id = believerId,
            model = "res.partner"
        ) {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onNext { response ->
                if (response.isSuccessful) {
                    val load = response.body()!!
                    if (load.isSuccessful) {
                        val result = load.result
                        // ...
                    } else {
                        // Odoo specific error
                        Timber.w("load() failed with ${load.errorMessage}")
                    }
                } else {
                    Timber.w("request failed with ${response.code()}:${response.message()}")
                }
            }

            onError { error ->
                error.printStackTrace()
            }

            onComplete { }
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