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
import io.gripxtech.odoojsonrpcclient.databinding.ActivityNewLoginBinding
import io.gripxtech.odoojsonrpcclient.databinding.ProgressDialogBinding
import io.gripxtech.odoojsonrpcclient.viewModel.viewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
        /*binding.txtRegistro.setOnClickListener {
            startRegistroActivity()
        }*/

    }

    /*private fun startRegistroActivity() {
        val intent = Intent(this, RegistroActivity::class.java).apply {}
        startActivity(intent)
        finish()
    }*/

    private fun validate() {
        passwordError = ""
        emailError = ""
        val valor = arrayOf(emailValidate(),passwordValidate())
        if (false in valor){
            progressBar = ProgressDialog(this)
            progressBar.setCanceledOnTouchOutside(false)
            progressBar.setCancelable(false)
            progressBar.show()
            progressBar.setContentView(R.layout.progress_dialog)
            progressBar.findViewById<TextView>(R.id.passwordDialog).text = passwordError
            progressBar.findViewById<TextView>(R.id.emailDialog).text = emailError
            progressBar.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            progressBar.findViewById<Button>(R.id.ButtonDialog).setOnClickListener {
                progressBar.dismiss()
            }
            return
        }else{
            binding.auth.visibility = View.VISIBLE
            vmVersionInfo()
        }
    }

    private fun passwordValidate(): Boolean {
        return if(binding.passwordEditext.text.toString().isEmpty()) {
            passwordError = "Campo contraseña está vacío"
            false
        }else {
            true
        }
    }

    private fun emailValidate(): Boolean {
        return if (binding.emailEditext.text.toString().isEmpty()) {
            emailError = "Campo correo está vacío"
            false
        }else if(binding.emailEditext.text.toString().isNotEmpty() && !PatternsCompat.EMAIL_ADDRESS.matcher(binding.emailEditext.text.toString()).matches()) {
            emailError = "Escribe un correo valido"
            false
        }
        else  {
            true
        }
    }

    private fun vmVersionInfo() {
        viewModel.vmVersionInfo(
            onSuccess = { versionInfo ->
                Log.e("TAG", "vmVersionInfo: $versionInfo" )
                vmDblist(versionInfo)
            }, onFailure = {
                binding.auth.visibility = View.GONE
                progressBar = ProgressDialog(this)
                progressBar.setCanceledOnTouchOutside(false)
                progressBar.setCancelable(false)
                progressBar.show()
                progressBar.setContentView(R.layout.progress_dialog)
                progressBar.findViewById<TextView>(R.id.passwordDialog).text = ""
                progressBar.findViewById<TextView>(R.id.emailDialog).text = "Falló la consulta en version"
                progressBar.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                progressBar.findViewById<Button>(R.id.ButtonDialog).setOnClickListener {
                    progressBar.dismiss()
                }
                Log.e("TAG", "vmVersionInfo: Falló la consulta", )
            })
    }
    private fun vmDblist(versionInfo: VersionInfo) {
        viewModel.vmDblist(
            versionInfo = versionInfo,
            onSuccess = {
                Log.e("TAG", "vmDblist: $it" )
                vmAuthenticate(it)
            }, onFailure = {
                binding.auth.visibility = View.GONE
                progressBar = ProgressDialog(this)
                progressBar.setCanceledOnTouchOutside(false)
                progressBar.setCancelable(false)
                progressBar.show()
                progressBar.setContentView(R.layout.progress_dialog)
                progressBar.findViewById<TextView>(R.id.passwordDialog).text = ""
                progressBar.findViewById<TextView>(R.id.emailDialog).text = "Falló la consulta en Autenticar version"
                progressBar.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                progressBar.findViewById<Button>(R.id.ButtonDialog).setOnClickListener {
                    progressBar.dismiss()
                }
                Log.e("TAG", "vmDblist: Falló la consulta", )
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
                binding.auth.visibility = View.GONE
                progressBar = ProgressDialog(this)
                progressBar.setCanceledOnTouchOutside(false)
                progressBar.setCancelable(false)
                progressBar.show()
                progressBar.setContentView(R.layout.progress_dialog)
                progressBar.findViewById<TextView>(R.id.passwordDialog).text = ""
                progressBar.findViewById<TextView>(R.id.emailDialog).text = "Falló la consulta autenticación de login"
                progressBar.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                progressBar.findViewById<Button>(R.id.ButtonDialog).setOnClickListener {
                    progressBar.dismiss()
                }
                Log.e("TAG", "vmAuthenticate: Falló la consulta" )
            },
            onFailureOdoo = {
                binding.auth.visibility = View.GONE
                progressBar = ProgressDialog(this)
                progressBar.setCanceledOnTouchOutside(false)
                progressBar.setCancelable(false)
                progressBar.show()
                progressBar.setContentView(R.layout.progress_dialog)
                progressBar.findViewById<TextView>(R.id.passwordDialog).text = ""
                progressBar.findViewById<TextView>(R.id.emailDialog).text = "Credenciales invalidas"
                progressBar.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                progressBar.findViewById<Button>(R.id.ButtonDialog).setOnClickListener {
                    progressBar.dismiss()
                }
                Log.e("TAG", "onFailure $it:" )
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