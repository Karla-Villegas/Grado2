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
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentCarteleraBinding
import io.gripxtech.odoojsonrpcclient.fragments.cartelera.entities.Cartelera
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.gripxtech.odoojsonrpcclient.gson
import io.gripxtech.odoojsonrpcclient.repository.Repository
import io.gripxtech.odoojsonrpcclient.toJsonObject
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_new_principal.*
import timber.log.Timber

class Fragment_Cartelera: Fragment() {

    private var _binding: FragmentCarteleraBinding? = null
    private val binding get() = _binding!!
    private val ListTypeCartelera = object : TypeToken<ArrayList<Cartelera>>() {}.type
    private val limit = RECORD_LIMIT
    private var compositeDisposable: CompositeDisposable? = null
    private var items = ArrayList<Cartelera>()
    private lateinit var activity: NewActivityPrincipal private set

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
        activity = getActivity() as NewActivityPrincipal
        binding.rvNoticias.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvNoticias.layoutManager = layoutManager
        binding.rvNoticias.adapter = adapter

        adapter.setupScrollListener(binding.rvNoticias)

        noticias()
    }

    private fun noticias(){
        adapter.clear()
        Odoo.route("news", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        val respuesta = call.result.asString.toJsonObject()
                        val ic = respuesta.get("records")
                        Timber.e("callkw()--->  ${respuesta.get("records")}")
                        items = gson.fromJson<ArrayList<Cartelera>>(ic, ListTypeCartelera )
                        Timber.e("callkw()--->  ${items}")

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

