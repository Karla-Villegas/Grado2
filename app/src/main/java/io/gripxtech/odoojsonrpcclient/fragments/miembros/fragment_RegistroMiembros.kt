package io.gripxtech.odoojsonrpcclient.fragments.miembros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentRegistroMiembrosBinding
import io.gripxtech.odoojsonrpcclient.toJsonObject
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.fragment_registro_miembros.*
import timber.log.Timber

class fragment_RegistroMiembros: Fragment() {

    private var _binding: FragmentRegistroMiembrosBinding? = null
    private val binding get() = _binding!!
    private var compositeDisposable: CompositeDisposable? = null
    private var name: String = ""
    private var email: String = ""
    private var pass: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistroMiembrosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btGuardar.setOnClickListener { guardar() }

    }

    private fun guardar() {
        name = etName.text.toString()
        if (name.isBlank()) {
            tlRegistroName.error = getString(R.string.login_username_error)
        }
        email = etEmail.text.toString()
        if (email.isBlank()) {
            tlRegistroEmail.error = getString(R.string.login_password_error)
        }
        pass = etPass.text.toString()
        if (pass.isBlank()) {
            tlRegistroPassword.error = getString(R.string.login_password_error)
        }

        createBeliever(name, email, pass)
    }

    private fun createBeliever(name:String, email:String, pass:String) {
        val values = mapOf<String, Any>("name" to name, "email" to email, "password" to pass)
        Odoo.route("/register/believer", "", args = values) {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        val respuesta = call.result.asString.toJsonObject()
                        Timber.e("callkw()--->  ${respuesta}")

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