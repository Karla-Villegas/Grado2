package io.gripxtech.odoojsonrpcclient.fragments.miembros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMiembrosBinding
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class Fragment_ListaMiembros: Fragment() {

    private var _binding: FragmentMiembrosBinding? = null
    private val binding get() = _binding!!
    private val ListTypeMiembros = object : TypeToken<ArrayList<Miembros>>() {}.type
    private val limit = RECORD_LIMIT
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var activity: NewActivityPrincipal private set

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
        Odoo.searchRead(
            model = "ev.believer",
            fields = listOf("id", "name", "email"),
            offset = adapter.rowItemCount,
            limit = limit,
            sort = "name ASC") {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onNext { response ->
                if (response.isSuccessful) {
                    val searchRead = response.body()!!
                    if (searchRead.isSuccessful) {
                     /*   val result = searchRead.result*/
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        val items: ArrayList<Miembros> = gson.fromJson(searchRead.result.records, ListTypeMiembros)
                        Timber.w("searchRead() Fetch ${items}")
                        Log.e("PRUEBAAA--->", "${items}")
                        adapter.addRowItems(items)
                    } else {
                        // Odoo specific error
                        Timber.w("searchRead() failed with ${searchRead.errorMessage}")
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
}