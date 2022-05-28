package io.gripxtech.odoojsonrpcclient

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.gripxtech.odoojsonrpcclient.core.authenticator.SplashActivity
import io.gripxtech.odoojsonrpcclient.core.utils.android.ktx.subscribeEx
import io.gripxtech.odoojsonrpcclient.databinding.ActivityNewPrincipalBinding
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewActivityPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityNewPrincipalBinding
    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navView = findViewById(R.id.bottom_navigation)
        navigation()

        binding.exit.setOnClickListener {
            binding.exit.setColorFilter(ContextCompat.getColor(this, R.color.background_text))
            cerrarSesion()
        }
    }



    @SuppressLint("ResourceType")
    fun navigation() {

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_cartelera,
                R.id.nav_miembros,
                R.id.nav_ministerios,
                R.id.nav_perfil
            )
        )


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_cartelera -> {
                    toolbar("Living Hope")
                    enableButtonMenu()
                }
                R.id.nav_miembros -> {
                    toolbar("Living Hope")
                    enableButtonMenu()
                }
                R.id.nav_ministerios -> {
                    toolbar("Living Hope")
                    enableButtonMenu()
                }
                R.id.nav_perfil -> {
                    toolbar("Living Hope")
                    enableButtonMenu()

                }
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun cerrarSesion() {

        val bottomSheetDialog = BottomSheetDialog(
            this, R.style.BottomSheetDialogTheme
        )


        var bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_bottom_sheet,
            findViewById(R.id.bottomSheet) as LinearLayout?
        )


        /*bottomSheetView.findViewById<View>(R.id.idSeSionName).idSeSionName.text = sesionName!!.toUpperCase()
        bottomSheetView.findViewById<View>(R.id.idSeSionEmail).idSeSionEmail.text = sesionEmail*/

        bottomSheetView.findViewById<View>(R.id.idCancelar).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<View>(R.id.idAceptar).setOnClickListener {

            val activity = application
            Single.fromCallable {
                for (odooUser in activity.getOdooUsers()) {
                    activity.deleteOdooUser(odooUser)
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeEx {
                    onSuccess {
                        //editProvider.pref_cerrarSesion()

                        this@NewActivityPrincipal.finish()
                        TaskStackBuilder.create(activity)
                            .addNextIntent(Intent(activity, SplashActivity::class.java))
                            .startActivities()

                        bottomSheetDialog.dismiss()
                    }

                    onError { error ->
                        error.printStackTrace()
                    }
                }

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun toolbar(text: String?){
        binding.ttoolbar.visibility = View.VISIBLE
        binding.ttoolbar.text = text
    }

    fun enableButtonMenu() {
        navView.menu.getItem(0).isEnabled = true
        navView.menu.getItem(1).isEnabled = true
        navView.menu.getItem(2).isEnabled = true
        navView.menu.getItem(3).isEnabled = true
    }
}