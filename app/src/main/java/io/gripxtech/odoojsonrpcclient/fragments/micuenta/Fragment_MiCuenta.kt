package io.gripxtech.odoojsonrpcclient.fragments.micuenta

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMiCuentaBinding
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.gripxtech.odoojsonrpcclient.viewModel.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class Fragment_MiCuenta: Fragment() {

    private var _binding: FragmentMiCuentaBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: NewActivityPrincipal private set
    private lateinit var viewModel: viewModel
    private lateinit var progressBar: ProgressBar
    private val MiembroType = object : TypeToken<Miembros>() {}.type


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMiCuentaBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as NewActivityPrincipal
       /* progressBar = ProgressBar()
        setupViewModel()
        prueba()*/

    }

  /*  private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(io.gripxtech.odoojsonrpcclient.viewModel.viewModel::class.java)
    }

    @SuppressLint("LogNotTimber")
    fun prueba() {
        CoroutineScope(IO).launch {
            val db = App.database.userInfoDao().getUser()
            withContext(Main){
                Log.e("ID USER", "MI CUENTA ${db}" )
                val part_id = db?.partner_id?.asJsonArray?.get(0)?.asString?.toInt()
                Log.e("ID part_id", "MI CUENTA ${part_id}" )
                searchMiCuenta(part_id)
            }
        }

    }

    @SuppressLint("LogNotTimber", "SetTextI18n")
    fun searchMiCuenta(part_id: Int?) {
        Log.e("ID searchMiCuenta", "MI CUENTA ${part_id}" )
        progressBar.progressbar(requireContext(), "Cargando...")
        Odoo.route("believer/p/$part_id", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        progressBar.finishProgressBar()
                        val result = call.result.asString.toJsonObject().get("record")
                        if (result != null){

                        val item = gson.fromJson<Miembros>(result, MiembroType)

                        if(item.identity != "false"){
                            binding.idDocumentoIdentidad.text = item.identity
                        }else{
                            binding.idDocumentoIdentidad.text = "sin registro encontrado"
                        }

                        if(item.name != "false"){
                            binding.idNombre.text = item.name
                        }else{
                            binding.idNombre.text = "sin registro encontrado"
                        }

                        if(item.localphone_number != "false"){
                            binding.idTelefono.text = item.localphone_number
                        }else{
                            binding.idTelefono.text = "sin registro encontrado"
                        }

                        if(item.state.toString() != "false"){
                            binding.idEstado.text = JSONObject(item.state.toString()).optString("name")
                        }else{
                            binding.idEstado.text = "sin registro encontrado"
                        }

                        if(item.municipality.toString() != "false"){
                            binding.idMunicipio.text = JSONObject(item.municipality.toString()).optString("name")
                        }else{
                            binding.idMunicipio.text = "sin registro encontrado"
                        }

                        if(item.parish.toString() != "false"){
                            binding.idParroquia.text = JSONObject(item.parish.toString()).optString("name")
                        }else{
                            binding.idParroquia.text = "sin registro encontrado"
                        }

                        if(item.street != "false"){
                            binding.idCalle.text = item.street
                        }else{
                            binding.idCalle.text = "sin registro encontrado"
                        }

                        if(item.sector != "false"){
                            binding.idSector.text = item.sector
                        }else{
                            binding.idSector.text = "sin registro encontrado"
                        }

                        if(item.house != "false"){
                            binding.idCasa.text = item.house
                        }else{
                            binding.idCasa.text = "sin registro encontrado"
                        }

                        if(item.building != "false"){
                            binding.idEdificio.text = item.building
                        }else{
                            binding.idEdificio.text= "sin registro encontrado"
                        }


                        Log.e("searchMiCuenta items", "MI CUENTA ${item}" )
                        }else{
                            progressBar.finishProgressBar()
                            binding.idDocumentoIdentidad.text = "sin registro encontrado"
                            binding.idNombre.text = "sin registro encontrado"
                            binding.idTelefono.text = "sin registro encontrado"
                            binding.idEstado.text = "sin registro encontrado"
                            binding.idMunicipio.text = "sin registro encontrado"
                            binding.idParroquia.text = "sin registro encontrado"
                            binding.idCalle.text = "sin registro encontrado"
                            binding.idSector.text = "sin registro encontrado"
                            binding.idCasa.text = "sin registro encontrado"
                            binding.idEdificio.text= "sin registro encontrado"
                        }
                    } else {
                        progressBar.finishProgressBar()
                        Timber.w("callkw() failed with ${it.errorBody()}")

                    }
                } else {
                    progressBar.finishProgressBar()
                    Timber.w("request failed with ${it.code()}:${it.message()}")
                }
            }
            this.onError { error ->
                progressBar.finishProgressBar()
                error.printStackTrace()
            }
            this.onComplete { }
        }
    }

*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}