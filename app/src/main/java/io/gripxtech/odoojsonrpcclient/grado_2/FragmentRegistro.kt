package io.gripxtech.odoojsonrpcclient.grado_2

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import io.gripxtech.odoojsonrpcclient.GlideApp
import io.gripxtech.odoojsonrpcclient.GlideRequests
import io.gripxtech.odoojsonrpcclient.MainActivity
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.tb
import kotlinx.android.synthetic.main.fragment_registro.*
import timber.log.Timber

class FragmentRegistro: Fragment() {


    lateinit var activity: MainActivity private set
    lateinit var glideRequests: GlideRequests private set
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        glideRequests = GlideApp.with(this)

        activity.setSupportActionBar(tb)
        val actionBar = activity.supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        drawerToggle = ActionBarDrawerToggle(
            activity, activity.dl,
            tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        activity.dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        btGuardar.setOnClickListener { guardar() }

    }

    private fun guardar() {
        val name = etName.text.toString()
        if (name.isBlank()) {
            tlRegistroName.error = getString(R.string.login_username_error)
        }
        val email = etEmail.text.toString()
        if (email.isBlank()) {
            tlRegistroEmail.error = getString(R.string.login_password_error)
        }
        val pass = etPass.text.toString()
        if (pass.isBlank()) {
            tlRegistroPassword.error = getString(R.string.login_password_error)
        }
        createPartner(name, email, pass)
    }

    private fun createPartner(name:String, email:String, pass:String) {
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
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (::drawerToggle.isInitialized) {
            drawerToggle.onConfigurationChanged(newConfig)
        }
    }

    override fun onDestroyView() {
        compositeDisposable?.dispose()
        super.onDestroyView()
    }
}