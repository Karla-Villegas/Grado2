package io.gripxtech.odoojsonrpcclient.core

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.authenticator.ActivityNewLogin
import io.gripxtech.odoojsonrpcclient.fragments.miembros.Fragment_ListaMiembros
import io.gripxtech.odoojsonrpcclient.toJsonObject
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_registro.*

import timber.log.Timber

class RegistroActivity : AppCompatActivity() {

    /*private var _binding: ActivityRegistroBinding? = null
    private val binding get() = _binding!!*/
    private var compositeDisposable: CompositeDisposable? = null
    private var name: String = ""
    private var email: String = ""
    private var pass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

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
       /* createPartner(name, email, pass)*/
        createBeliever(name, email, pass)
    }

    private fun startLoginActivity() {
            val intent = Intent(this, Fragment_ListaMiembros::class.java).apply {}
        startActivity(intent)
        finish()
    }

    private fun createBeliever(name:String, email:String, pass:String) {
        val values = mapOf<String, Any>("name" to name, "email" to email, "password" to pass)
        Odoo.route("/register/believer", "", args = values) {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        val respuesta = call.result.asString.toJsonObject()
                        /*val item = respuesta.getAsJsonArray("url")*/
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




    /*private fun createPartner(name:String, email:String, pass:String) {
        Odoo.create(
            model = "res.partner",
            values = mapOf(
                "name" to name
            )
        ) {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onNext { response ->
                if (response.isSuccessful) {
                    val create = response.body()!!
                    if (create.isSuccessful) {
                        val result = create.result
                        Log.e("CREATE_PARTNER", "ID PARTNER--> ${result}")
                        createUsers(result, email, pass)
                    } else {
                        // Odoo specific error
                        Timber.w("create() failed with ${create.errorMessage}")
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

    private fun createUsers(result: Long, email: String, pass: String) {
        Odoo.create(
            model = "res.users",
            values = mapOf(
                "partner_id" to result,
                "login" to email,
                "password" to pass
            )
        ) {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }

            onNext { response ->
                if (response.isSuccessful) {
                    val create = response.body()!!
                    if (create.isSuccessful) {
                        val user = create.result
                        Log.e("CREATE_USERS", "USERS--> ${user}")
                        Toast.makeText(this@RegistroActivity, "¡Registro exitoso!", Toast.LENGTH_LONG).show()
                        startLoginActivity()
                        //createUsers(result, email, pass)
                    } else {
                        // Odoo specific error
                        Timber.w("create() failed with ${create.errorMessage}")
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
    }*/





}