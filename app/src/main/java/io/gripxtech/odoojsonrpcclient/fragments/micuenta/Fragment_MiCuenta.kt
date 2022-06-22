package io.gripxtech.odoojsonrpcclient.fragments.micuenta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.gripxtech.odoojsonrpcclient.NewActivityPrincipal
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMiCuentaBinding
import io.gripxtech.odoojsonrpcclient.viewModel.viewModel
import kotlinx.android.synthetic.main.activity_new_principal.*

class Fragment_MiCuenta: Fragment() {

    private var _binding: FragmentMiCuentaBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: NewActivityPrincipal private set
    private lateinit var viewModel: viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMiCuentaBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as NewActivityPrincipal

        setupViewModel()
        vmOrganizacionId()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(io.gripxtech.odoojsonrpcclient.viewModel.viewModel::class.java)
    }

    fun vmOrganizacionId() {
        viewModel.vmgetInfoUser(
            onSuccess = { response ->
                binding.idDocumentoIdentidad.text = "2876542"
                binding.idNombre.text = response?.name
                binding.idTelefono.text = "04144763022"
           /*     binding.idEstado.setText(response?.state_id?.asJsonArray?.get(1)?.asString)
                binding.idMunicipio.setText(response?.municipality_id?.asJsonArray?.get(1)?.asString)
                binding.idParroquia.setText(response?.parish_id?.asJsonArray?.get(1)?.asString)*/
            }
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}