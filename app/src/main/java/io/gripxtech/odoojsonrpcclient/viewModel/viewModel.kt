package io.gripxtech.odoojsonrpcclient.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.gripxtech.odoojsonrpcclient.NetworkError
import io.gripxtech.odoojsonrpcclient.core.entities.session.authenticate.AuthenticateResult
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

    /** Método dbList del Framework */
    fun vmDblist(
        versionInfo: VersionInfo,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Repository.dbList(versionInfo, onSuccess = { response ->
                    try {
                        onSuccess(response)
                    } catch (e: Exception) {
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

    /** Método authenticate del Framework */
    fun vmAuthenticate(
        login: String,
        password: String,
        database: String,
        onSuccess: (AuthenticateResult) -> Unit,
        onFailure: (String) -> Unit,
        onFailureOdoo: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Repository.authenticate(login, password, database,
                    onSuccess = { response ->
                        try {
                            onSuccess(response)
                        } catch (e: Exception) {
                            if (e is CancellationException){ throw e }
                            onFailure(NetworkError.get(e.message))
                        }
                    }, onFailure = { Error ->
                        onFailure("onFailure: $Error")
                    },
                    onFailureOdoo = { ErrorOdoo ->
                        onFailureOdoo("onFailureOdoo: $ErrorOdoo")
                    }
                )
            } catch (e: Exception) {
                if (e is CancellationException){ throw e }
                onFailure(NetworkError.get(e.message))
            }
        }
    }

}