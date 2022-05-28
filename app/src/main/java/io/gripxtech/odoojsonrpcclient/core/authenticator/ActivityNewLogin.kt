package io.gripxtech.odoojsonrpcclient.core.authenticator

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModelProvider
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.core.entities.session.authenticate.AuthenticateResult
import io.gripxtech.odoojsonrpcclient.core.entities.webclient.versionInfo.VersionInfo
import io.gripxtech.odoojsonrpcclient.core.utils.android.ktx.subscribeEx
import io.gripxtech.odoojsonrpcclient.core.utils.createProgress
import io.gripxtech.odoojsonrpcclient.databinding.ActivityNewLoginBinding
import io.gripxtech.odoojsonrpcclient.databinding.ProgressDialogBinding
import io.gripxtech.odoojsonrpcclient.viewModel.viewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ActivityNewLogin : AppCompatActivity(){

    private var _binding: ActivityNewLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: App
    private lateinit var viewModel: viewModel
    private var emailError = ""
    private var passwordError = ""
    private lateinit var progress : ProgressDialogBinding
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewLoginBinding.inflate(layoutInflater)
        app = application as App
        setContentView(binding.root)
        progress = ProgressDialogBinding.inflate(LayoutInflater.from(this))
        progressBar = ProgressDialog(this)
        viewModel = ViewModelProvider(this).get(io.gripxtech.odoojsonrpcclient.viewModel.viewModel::class.java)

        binding.Button.setOnClickListener {
            validate()
        }
    }

    private fun validate() {
        passwordError = ""
        emailError = ""
        if (!emailValidate()){
            createProgress(this,emailError,passwordError)
        }else{
            binding.textLogin.text = "Autenticando..."
            binding.progressLogin.visibility = View.VISIBLE
            vmVersionInfo()
        }
    }

    private fun emailValidate(): Boolean {
        return if (binding.emailEditext.text.toString().isEmpty()&& binding.passwordEditext.text.toString().isEmpty()) {
            emailError = "Campo correo está vacío"
            passwordError = "Campo contraseña está vacío"
            false
        }else if (binding.emailEditext.text.toString().isNotEmpty()&& binding.passwordEditext.text.toString().isEmpty()) {
            emailError = ""
            passwordError = "Campo contraseña está vacío"
            false
        } else if (binding.emailEditext.text.toString().isEmpty()&& binding.passwordEditext.text.toString().isNotEmpty()) {
            emailError = "Campo correo está vacío"
            passwordError = ""
            false
        }
        else  {
            true
        }
    }

    private fun vmVersionInfo() {
        viewModel.vmVersionInfo(
            onSuccess = { versionInfo ->
                Timber.e( "vmVersionInfo: $versionInfo" )
                vmDblist(versionInfo)
            }, onFailure = {
                binding.progressLogin.visibility = View.GONE
                binding.textLogin.text = "Iniciar Sesión"
                createProgress(this,"Falló la consulta en version","")
                Timber.e("vmVersionInfo: Falló la consulta", )
                Timber.e("vmVersionInfo: Falló la consulta", )
            })
    }
    private fun vmDblist(versionInfo: VersionInfo) {
        viewModel.vmDblist(
            versionInfo = versionInfo,
            onSuccess = {
                Timber.e("vmDblist: $it" )
                vmAuthenticate(it)
            }, onFailure = {
                binding.progressLogin.visibility = View.GONE
                binding.textLogin.text = "Iniciar Sesión"
                createProgress(this,"Falló la consulta en Autenticar version","")
                Timber.e("vmDblist: Falló la consulta", )
            })
    }

    private fun vmAuthenticate(database: String) {
        viewModel.vmAuthenticate(
            login = binding.emailEditext.text.toString(),
            password = binding.passwordEditext.text.toString(),
            database = database,
            onSuccess = { authenticateResult ->
                Log.e("TAG", "vmAuthenticate: $authenticateResult", )
                createAccount(authenticateResult)
            },
            onFailure =  {
                binding.progressLogin.visibility = View.GONE
                binding.textLogin.text = "Iniciar Sesión"
                createProgress(this,"Falló la consulta autenticación de login","")
                Log.e("TAG", "vmAuthenticate: Falló la consulta" )
            },
            onFailureOdoo = {
                binding.progressLogin.visibility = View.GONE
                binding.textLogin.text = "Iniciar Sesión"
                createProgress(this,"Credenciales invalidas","")
                Timber.e( "vmAuthenticate onFailure $it:" )
            })
    }

    private fun createAccount(authenticateResult: AuthenticateResult) {
        Observable.fromCallable {
            if (createOdooUser(authenticateResult)) {
                val odooUser = odooUserByAndroidName(authenticateResult.androidName)
                if (odooUser != null) {
                    loginOdooUser(odooUser)
                    Odoo.user = odooUser
                    app.cookiePrefs.setCookies(Odoo.pendingAuthenticateCookies)
                }
                Odoo.pendingAuthenticateCookies.clear()
                true
            } else {
                false
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeEx {
                onSubscribe {

                }

                onNext { t ->
                    resultCallback(t)
                }

                onError { error ->
                    error.printStackTrace()
                    Log.e("TAG", "createAccount:${error} " )
                }
            }
    }
    private fun resultCallback(result: Boolean) {
        if (result) {
            intent?.let {
                when {
                    it.hasExtra(LoginActivity.FROM_ANDROID_ACCOUNTS) -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    it.hasExtra(LoginActivity.FROM_APP_SETTINGS) -> {
                        restartApp()
                    }
                    else -> {
                        restartApp()
                    }
                }
                Unit
            }
        } else {
        }
    }


}