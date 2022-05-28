package io.gripxtech.odoojsonrpcclient.fragments.cartelera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.NewActivityPrincipal
import io.gripxtech.odoojsonrpcclient.RECORD_LIMIT
import io.gripxtech.odoojsonrpcclient.databinding.FragmentCarteleraBinding
import io.gripxtech.odoojsonrpcclient.fragments.cartelera.entities.Cartelera
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_new_principal.*

class Fragment_Cartelera: Fragment() {

    private var _binding: FragmentCarteleraBinding? = null
    private val binding get() = _binding!!
    private val ListTypeMiembros = object : TypeToken<ArrayList<Cartelera>>() {}.type
    private val limit = RECORD_LIMIT
    private var compositeDisposable: CompositeDisposable? = null

    val adapter: AdapterCartelera by lazy {
            AdapterCartelera(this, arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCarteleraBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  binding.rvNoticias.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvNoticias.layoutManager = layoutManager
        binding.rvNoticias.adapter = adapter

        adapter.setupScrollListener(binding.rvNoticias)*/

        setupRecyclerView()
    }

    private fun setupRecyclerView() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

