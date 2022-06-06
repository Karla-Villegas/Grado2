package io.gripxtech.odoojsonrpcclient.fragments.ministerios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.gripxtech.odoojsonrpcclient.NewActivityPrincipal
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMinisteriosBinding
import io.gripxtech.odoojsonrpcclient.fragments.miembros.AdapterMiembros
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.gripxtech.odoojsonrpcclient.gson
import io.gripxtech.odoojsonrpcclient.toJsonObject
import kotlinx.android.synthetic.main.activity_new_principal.*
import timber.log.Timber


class Fragment_Ministerios: Fragment() {

    private var _binding: FragmentMinisteriosBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: NewActivityPrincipal private set

   /* val adapter: AdapterMinisterio by lazy {
        AdapterMinisterio(this, arrayListOf())
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMinisteriosBinding.inflate(layoutInflater, container, false)
        return binding.root
        activity = getActivity() as NewActivityPrincipal
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as NewActivityPrincipal

        fetchMinisterios()


    }

    private fun fetchMinisterios() {
        Odoo.route("/departments", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        val result = call.result.asString.toJsonObject()
                        Timber.w("MINISTERIOS ${result}")

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