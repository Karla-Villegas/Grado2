package io.gripxtech.odoojsonrpcclient.core.authenticator

import io.gripxtech.odoojsonrpcclient.App
import io.gripxtech.odoojsonrpcclient.databinding.ActivityLoginBinding
import io.gripxtech.odoojsonrpcclient.viewModel.viewModel

class ActivityNewLogin {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: App
    private lateinit var viewModel: viewModel
    private var emailError = ""
    private var passwordError = ""

}