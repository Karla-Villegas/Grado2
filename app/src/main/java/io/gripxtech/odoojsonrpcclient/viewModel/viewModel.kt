package io.gripxtech.odoojsonrpcclient.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.gripxtech.odoojsonrpcclient.NetworkError
import io.gripxtech.odoojsonrpcclient.core.entities.webclient.versionInfo.VersionInfo
import io.gripxtech.odoojsonrpcclient.repository.Repository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class viewModel: ViewModel() {

    /** Método versionInfo del Framework*/
    fun vmVersionInfo(onSuccess: (VersionInfo) -> Unit,
                      onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Repository.versionInfo(onSuccess = { response ->
                    try {
                        onSuccess(response)
                    } catch (e: Exception){
                        if (e is CancellationException){ throw e }
                        onFailure(NetworkError.get(e.message))
                    }
                }, onFailure = { Error ->
                    onFailure("onFailure: $Error")
                })
            } catch (e: Exception) {
                if (e is CancellationException){ throw e }
                onFailure(NetworkError.get(e.message))
            }
        }
    }
}