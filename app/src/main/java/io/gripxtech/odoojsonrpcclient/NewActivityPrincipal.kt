package io.gripxtech.odoojsonrpcclient

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.gripxtech.odoojsonrpcclient.databinding.ActivityNewPrincipalBinding

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