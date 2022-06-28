package io.gripxtech.odoojsonrpcclient.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.gripxtech.odoojsonrpcclient.App
import io.gripxtech.odoojsonrpcclient.NetworkError
import io.gripxtech.odoojsonrpcclient.core.entities.dataset.searchread.SearchRead
import io.gripxtech.odoojsonrpcclient.core.entities.session.authenticate.AuthenticateResult
import io.gripxtech.odoojsonrpcclient.core.entities.webclient.versionInfo.VersionInfo
import io.gripxtech.odoojsonrpcclient.core.userInfo.UserInfo
import io.gripxtech.odoojsonrpcclient.repository.Repository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

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

    /** Metodo SearchRead del FrameWord*/
    fun vmSearchRead(
        model: String,
        fields: ArrayList<String>,
        domain: List<Any>,
        rowItemCount: Int,
        limit: Int,
        orden: String,
        onSuccess: (SearchRead) -> Unit,
        onFailure: (String) -> Unit,
        onFailureOdoo: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Repository.searchRead(
                    model = model,
                    fields = fields,
                    domain = domain,
                    rowItemCount = rowItemCount,
                    limit = limit,
                    orden = orden,
                    onSuccess = { response ->
                        try {
                            onSuccess(response)
                        } catch (e: Exception){
                            if (e is CancellationException){ throw e }
                            onFailure(NetworkError.get(e.message))
                        }
                    },
                    onFailure = { Error ->
                        onFailure("onFailure: $Error")
                    },
                    onFailureOdoo = { ErrorOdoo ->
                        onFailureOdoo("onFailureOdoo: $ErrorOdoo")
                    }
                )
            }
            catch (e: Exception){
                if (e is CancellationException){ throw e }
                onFailure(NetworkError.get(e.message))
            }
        }
    }

    /** ROOM */

    /** Insertar información de usuario sesionado */
    fun vmInsertInfoUser(
        userInfo: UserInfo,
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                App.database.userInfoDao().deleteUserInfo()
                App.database.userInfoDao().inserUserInfo(userInfo)
                Timber.e("USUARIO GUARDADO: ${App.database.userInfoDao().getUser()}")
                onSuccess(true)
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Timber.e("catch ---> ${e.message}")
                onFailure(e.message.toString())
            }
        }
    }

    /** Obtener información de usuario sesionado */
    fun vmGetInfoUser(): LiveData<UserInfo?>? {
        return App.database.userInfoDao().getUserInfo()
    }

    /** Eliminar DB */
    fun vmDeleteDB() { viewModelScope.launch(Dispatchers.IO) { App.database.clearAllTables() } }

    fun vmgetInfoUser(
        onSuccess: (UserInfo?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            this.launch {
                Repository.dbGetUserInfo(
                    onSuccess = { response ->
                        onSuccess(response)
                    })
            }

        }
    }

}