package io.gripxtech.odoojsonrpcclient.fragments.micuenta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.gripxtech.odoojsonrpcclient.NewActivityPrincipal
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMiCuentaBinding
import kotlinx.android.synthetic.main.activity_new_principal.*

class Fragment_MiCuenta: Fragment() {

    private var _binding: FragmentMiCuentaBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: NewActivityPrincipal private set

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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}