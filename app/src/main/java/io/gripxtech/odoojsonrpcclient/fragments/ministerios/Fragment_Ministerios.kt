package io.gripxtech.odoojsonrpcclient.fragments.ministerios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.gripxtech.odoojsonrpcclient.NewActivityPrincipal
import io.gripxtech.odoojsonrpcclient.databinding.FragmentMinisteriosBinding
import kotlinx.android.synthetic.main.activity_new_principal.*


class Fragment_Ministerios: Fragment() {

    private var _binding: FragmentMinisteriosBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: NewActivityPrincipal private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMinisteriosBinding.inflate(layoutInflater, container, false)
        return binding.root
        activity = getActivity() as NewActivityPrincipal
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}