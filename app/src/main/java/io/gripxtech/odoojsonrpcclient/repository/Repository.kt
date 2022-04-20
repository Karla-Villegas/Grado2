package io.gripxtech.odoojsonrpcclient.repository

import android.util.Log
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.core.entities.database.listdb.ListDb
import io.gripxtech.odoojsonrpcclient.core.entities.dataset.callkw.CallKw
import io.gripxtech.odoojsonrpcclient.core.entities.dataset.searchread.SearchRead
import io.gripxtech.odoojsonrpcclient.core.entities.method.create.Create
import io.gripxtech.odoojsonrpcclient.core.entities.session.authenticate.Authenticate
import io.gripxtech.odoojsonrpcclient.core.entities.session.authenticate.AuthenticateResult
import io.gripxtech.odoojsonrpcclient.core.entities.webclient.versionInfo.VersionInfo
import io.reactivex.disposables.CompositeDisposable

object Repository {
    private var compositeDisposable: CompositeDisposable? = null

    /** CONEXIONES CON LOS METODOS O WEB SERVICES DEL SERVIDOR DE ODOO*/

    /** Metodo para busqueda y lectura*/
  fun searchRead(
      model: String,
      fields: ArrayList<String>,
      domain: List<Any>,
      rowItemCount: Int,
      limit: Int,
      orden: String,
      onSuccess: (SearchRead) -> Unit,
      onFailure: (String) -> Unit){

      Odoo.searchRead(
          model = model,
          fields = fields,
          domain = domain,
          offset = rowItemCount,
          limit = limit,
          sort = orden
      )
      {
          onSubscribe { disposable ->
              compositeDisposable?.add(disposable)
          }
          onNext { response ->
              if (response.isSuccessful) {
                  val response = response.body()!!
                  onSuccess(response)
              } else {
              }
          }

          onError { error ->
              error.printStackTrace()
              onFailure(error.printStackTrace().toString())
          }

          onComplete { }
      }
  }

    /** Metodo para filtrar por modelo y nombre del metodo personalizado*/
    fun callKw(
        model: String,
        method: String,
        domain: List<Any>,
        onSuccess: (CallKw) -> Unit,
        onFailure: (String) -> Unit){

        Odoo.callKw(
            model = model,
            method = method,
            args = domain
        )
        {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }
            onNext { response ->
                if (response.isSuccessful) {
                    val response = response.body()!!
                    onSuccess(response)
                } else {

                }
            }

            onError { error ->
                error.printStackTrace()
                onFailure(error.printStackTrace().toString())
            }

            onComplete { }
        }
    }

    /** Método para crear registro en algún modelo */
    fun create(model: String,
               values: Any,
               kwArgs: Map<String, Any> = mapOf(),
               onSuccess: (Create) -> Unit,
               onFailure: (String) -> Unit) {

        Odoo.create(
            model = model,
            values = values,
            kwArgs = kwArgs
        ) {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }
            onNext { response ->
                if (response.isSuccessful) {
                    val create = response.body()!!
                    if (create.isSuccessful) {
                        val result = create.result
                        val response = response.body()!!
                        onSuccess(response)
                    } else {
                        // Odoo specific error
                        /*Timber.w("create() failed with ${create.errorMessage}")*/
                    }
                } else {
                    /*Timber.w("request failed with ${response.code()}:${response.message()}")*/
                }
            }
            onError { error ->
                error.printStackTrace()
                onFailure(error.printStackTrace().toString())
            }
            onComplete { }
        }

    }

    /** Método para obtener la información del servidor*/
    fun versionInfo(onSuccess: (VersionInfo) -> Unit,
                    onFailure: (String) -> Unit) {

        Odoo.versionInfo {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }
            onNext { response ->
                if (response.isSuccessful) {
                    val versionInfo = response.body()!!
                    if (versionInfo.isSuccessful) {
                        val result = versionInfo.result
                        val response = response.body()!!
                        onSuccess(response)
                    } else {
                        // Odoo specific error
                        /*Timber.w("create() failed with ${create.errorMessage}")*/
                    }

                } else {
                    /*Timber.w("request failed with ${response.code()}:${response.message()}")*/
                }
            }
            onError { error ->
                error.printStackTrace()
                onFailure(error.printStackTrace().toString())
            }
            onComplete { }
        }
    }

    /** Método para obtener la información del servidor*/
    fun dbList(versionInfo: VersionInfo,
               onSuccess: (String) -> Unit,
               onFailure: (String) -> Unit) {

        Odoo.listDb(versionInfo.result.serverVersion) {
            onSubscribe { disposable ->
                compositeDisposable?.add(disposable)
            }
            onNext { response ->
                if (response.isSuccessful) {
                    val listDb = response.body()!!
                    if (listDb.isSuccessful) {
                        onSuccess(listDb.result.first())
                    } else {
                        // Odoo specific error
                    }
                } else {

                }
            }
            onError { error ->
                error.printStackTrace()
                onFailure(error.printStackTrace().toString())
            }
            onComplete { }
        }
    }



    /** Método para autenticar usuario en login */
    fun authenticate(
        login: String,
        password: String,
        database: String,
        onSuccess: (AuthenticateResult) -> Unit,
        onFailure: (String) -> Unit,
        onFailureOdoo :  (String) -> Unit
    ) {
        Odoo.authenticate(
            login = login,
            password = password,
            database = database) {

            Odoo.authenticate(login, password, database) {
                onSubscribe { disposable ->
                    compositeDisposable?.add(disposable)
                }
                onNext { response ->
                    if (response.isSuccessful) {
                        val authenticate = response.body()!!
                        if (authenticate.isSuccessful) {
                            val authenticateResult = authenticate.result
                            authenticateResult.password = password
                            onSuccess(authenticateResult)
                        } else {
                           onFailureOdoo(authenticate.errorMessage)
                        }
                    } else {
                        onFailureOdoo("onFailureOdoo")
                    }
                }
                onError { error ->
                    error.printStackTrace()
                    onFailure(error.printStackTrace().toString())
                }
                onComplete { }
            }
        }
    }





    /** CONEXIONES CON LOS METODOS DE LA DB DE ROOM*/

  }