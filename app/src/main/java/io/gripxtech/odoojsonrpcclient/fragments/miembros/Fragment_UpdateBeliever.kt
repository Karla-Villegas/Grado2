package io.gripxtech.odoojsonrpcclient.fragments.miembros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentUpdateBelieverBinding
import io.gripxtech.odoojsonrpcclient.toJsonObject
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class Fragment_UpdateBeliever: Fragment() {

    private var _binding: FragmentUpdateBelieverBinding? = null
    private val binding get() = _binding!!
    private var compositeDisposable: CompositeDisposable? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBelieverBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun UpdateBeliever() {
        Odoo.route("/update/believer/<int:believer_id>", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        val respuesta = call.result.asString.toJsonObject()

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
}