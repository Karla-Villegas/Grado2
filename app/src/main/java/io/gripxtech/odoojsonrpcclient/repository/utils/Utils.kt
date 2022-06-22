package io.gripxtech.odoojsonrpcclient.repository.utils

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.core.authenticator.SplashActivity
/*import io.gripxtech.odoojsonrpcclient.core.entities.organization.Organization*/
import io.gripxtech.odoojsonrpcclient.core.utils.android.ktx.subscribeEx
import io.gripxtech.odoojsonrpcclient.deleteOdooUser
import io.gripxtech.odoojsonrpcclient.getOdooUsers
import io.gripxtech.odoojsonrpcclient.gson
/*import io.gripxtech.odoojsonrpcclient.ui.fragments.cartelera.entities.DetallesProyecto
import io.gripxtech.odoojsonrpcclient.ui.fragments.cartelera.entities.Messages
import io.gripxtech.odoojsonrpcclient.ui.fragments.cartelera.entities.PorConfirmar
import io.gripxtech.odoojsonrpcclient.ui.fragments.cartelera.entities.Publicaciones*/
import io.gripxtech.odoojsonrpcclient.viewModel.viewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
/*import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.bottomsheetproyecto.view.**/
import kotlinx.android.synthetic.main.layout_bottom_sheet.view.*
/*import kotlinx.android.synthetic.main.sheet_search_cartelera.view.**/
import org.json.JSONObject
import timber.log.Timber


private lateinit var viewModel: viewModel
private var serverId = 0
/*private val organizationListType = object : TypeToken<ArrayList<Organization>>() {}.type*/

/*fun FragmentActivity.onClickSearch(callback: () -> Unit) = findViewById<ImageView>(R.id.search).setOnClickListener { callback() }

fun FragmentActivity.onSearchClear(boolean: Boolean) {
    when(boolean) {
        false -> { findViewById<ImageView>(R.id.search).setBackgroundResource(R.drawable.ic_buscar) }
        true -> { findViewById<ImageView>(R.id.search).setBackgroundResource(R.drawable.ic_close) }
    }
}*/

fun onNavDestinationSelected(
    itemId: Int,
    navController: NavController
): Boolean {
    val builder = NavOptions.Builder()
        .setLaunchSingleTop(true)
    if (navController.currentDestination!!.parent!!.findNode(itemId) is ActivityNavigator.Destination) {
        builder.setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
    } else {
        builder.setEnterAnim(R.animator.nav_default_enter_anim)
            .setExitAnim(R.animator.nav_default_exit_anim)
            .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
            .setPopExitAnim(R.animator.nav_default_pop_exit_anim)
    }
    /* if (itemId == getChildAt(0).id) {
     builder.setPopUpTo(findStartDestination(navController.graph)!!.id, true)
      }*/
    builder.setPopUpTo(itemId, true)
    val options = builder.build()
    return try {
        navController.navigate(itemId, null, options)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun stripHtml(html: String?): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(html).toString()
    }
}

fun AppCompatActivity.bottomSheetDialog() {
    viewModel =
        ViewModelProvider(this).get(io.gripxtech.odoojsonrpcclient.viewModel.viewModel::class.java)
    val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
    val bottomSheetView = LayoutInflater.from(this)
        .inflate(R.layout.layout_bottom_sheet, findViewById<LinearLayout>(R.id.bottomSheet))
   /* val adapterOrg: AdapterBottomSheetOrganizaciones by lazy {
        AdapterBottomSheetOrganizaciones(
            this,
            arrayListOf()
        )
    }*/

    bottomSheetView.findViewById<View>(R.id.idAceptar).setOnClickListener {
        val activity = application
        Single.fromCallable {
            for (odooUser in activity.getOdooUsers()) {
                activity.deleteOdooUser(odooUser)
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeEx {
                onSuccess {
                    viewModel.vmDeleteDB()
                    finish()
                    TaskStackBuilder.create(activity)
                        .addNextIntent(Intent(activity, SplashActivity::class.java))
                        .startActivities()
                    bottomSheetDialog.dismiss()
                }
                onError { error ->
                    error.printStackTrace()
                }
            }
        bottomSheetDialog.dismiss()
    }
}

    /** Información del usuario sesionado */
    /*viewModel.vmGetInfoUser()?.observe(this, Observer { userInfo ->
        if (userInfo != null) {
            bottomSheetView.findViewById<TextView>(R.id.tvNombre).text = userInfo.name
            bottomSheetView.findViewById<TextView>(R.id.tvCorreo).text = userInfo.email
        }
    })*/

    /** Información de la organización seleccionada */
   /* viewModel.vmGetOrganization()?.observe(this, Observer { organization ->
        if (organization != null) {
            serverId = organization.serverId
            bottomSheetView.findViewById<TextView>(R.id.tvNameOrganization).text = organization.name
        }
    })*/

    /** Organizaciones a las que pertenezco */
   /* bottomSheetView.shimmer_layout_sheet.startShimmer()
    viewModel.vmOrganizations(
        onSuccess = { route ->
            val result = route.result.asString
            val item = gson.fromJson<ArrayList<Organization>>(result, organizationListType)
            if (item != null && item.size > 0) {
                bottomSheetView.shimmer_layout_sheet.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
                bottomSheetView.rvOrganizaciones.visibility = View.VISIBLE

                item?.forEach { organization ->
                    adapterOrg.setOrganizaciones(item, serverId)
                    bottomSheetView.findViewById<RecyclerView>(R.id.rvOrganizaciones).apply {
                        layoutManager = LinearLayoutManager(this@bottomSheetDialog)
                        adapter = adapterOrg
                    }
                }

                bottomSheetView.findViewById<RecyclerView>(R.id.rvOrganizaciones)
                    .addOnItemTouchListener(
                        RecyclerItemClickListener(
                            this,
                            object : RecyclerItemClickListener.OnItemClickListener {
                                override fun onItemClick(view: View?, position: Int) {
                                    val item = item[position]
                                    viewModel.vmMySessionOrganization(
                                        slug = item.slug,
                                        onSuccess = { route ->
                                            viewModel.vmInsertOrganization(item)
                                            startActivity(
                                                Intent(
                                                    this@bottomSheetDialog,
                                                    SplashActivity::class.java
                                                )
                                            )
                                            finish()
                                        },
                                        onFailure = { message ->
                                            Timber.e(message)
                                            codeResponseHttp(
                                                context = this@bottomSheetDialog,
                                                code = 200,
                                                mensaje = null,
                                                codeHttp = { mensaje, descripcion ->
                                                    menssage(
                                                        context = this@bottomSheetDialog,
                                                        mensaje = mensaje,
                                                        descripcion = descripcion,
                                                        refresh = {
                                                            if (it) {

                                                            } else {

                                                            }
                                                        }
                                                    )
                                                }
                                            )
                                        },
                                        onFailureOdoo = { error, code ->
                                            Timber.e(error)
                                            codeResponseHttp(
                                                context = this@bottomSheetDialog,
                                                code = code,
                                                mensaje = error,
                                                codeHttp = { mensaje, descripcion ->
                                                    menssage(
                                                        context = this@bottomSheetDialog,
                                                        mensaje = mensaje,
                                                        descripcion = descripcion,
                                                        refresh = {
                                                            if (it) {

                                                            } else {

                                                            }
                                                        }
                                                    )
                                                }
                                            )
                                        }
                                    )
                                }
                            })
                    )
            }
        },

        onFailure = { message ->
            bottomSheetView.shimmer_layout_sheet.apply {
                stopShimmer()
                visibility = View.GONE
            }
            bottomSheetView.rvOrganizaciones.visibility = View.VISIBLE
            Timber.e(message)
            codeResponseHttp(
                context = this@bottomSheetDialog,
                code = 200,
                mensaje = null,
                codeHttp = { mensaje, descripcion ->
                    menssage(
                        context = this@bottomSheetDialog,
                        mensaje = mensaje,
                        descripcion = descripcion,
                        refresh = {
                            if (it) {

                            } else {

                            }
                        }
                    )
                }
            )
        },
        onFailureOdoo = { error, code ->
            bottomSheetView.shimmer_layout_sheet.apply {
                stopShimmer()
                visibility = View.GONE
            }
            bottomSheetView.rvOrganizaciones.visibility = View.VISIBLE
            Timber.e(error)
            codeResponseHttp(
                context = this@bottomSheetDialog,
                code = code,
                mensaje = error,
                codeHttp = { mensaje, descripcion ->
                    menssage(
                        context = this@bottomSheetDialog,
                        mensaje = mensaje,
                        descripcion = descripcion,
                        refresh = {
                            if (it) {

                            } else {

                            }
                        }
                    )
                }
            )
        }

    )
    bottomSheetDialog.setContentView(bottomSheetView)
    bottomSheetDialog.show()
}

fun Fragment.bottomSheetDialogDetalle(
    item: DetallesProyecto,
    nameOrganization: String,
    subcategory_name: String,
    conter_members: String,
    conter_followers: String
) {

    val bottomSheetDialog = BottomSheetDialog(
        requireContext(), R.style.BottomSheetDialogTheme
    )
    val vista = layoutInflater.inflate(R.layout.bottomsheetproyecto, null)

    vista.textSeguid.text = conter_followers
    vista.textMiemb.text = conter_members
    vista.nombreOrgn.text = nameOrganization
    vista.Consejo.text = subcategory_name
    vista.descripcion.text = item.name
    vista.codigo.text = item.code
    vista.status.text = item.status


    bottomSheetDialog.setCancelable(true)
    bottomSheetDialog.setContentView(vista)
    bottomSheetDialog.show()

}

fun Fragment.bottomSheetSearch(context: Context, onclick: (String?) -> Unit){

    val bottomSheetDialog  = BottomSheetDialog(
        context, R.style.BottomSheetDialogTheme
    )
    val vista = layoutInflater.inflate(R.layout.sheet_search_cartelera, null)
    bottomSheetDialog.setCancelable(true)
    bottomSheetDialog.setContentView(vista)
    bottomSheetDialog.show()
    vista.searchSheet.setOnClickListener {
        onclick(vista.searchText.text.toString())
        if(vista.searchText.text.toString().isNotEmpty()) bottomSheetDialog.dismiss()

    }

}

fun setRecord(item:String):Messages{
    val obj = JSONObject(item)

    val id = obj.optInt("id").toString()
    obj.optString("code").toString()
    obj.optString("p_transmiter_id").toString()
    obj.optString("p_receiver_id").toString()
    val conten = obj.optString("content").toString()
    val comunicacionId= obj.optInt("comunication_id")
    val comunicacionCode= obj.optString("code").toString()
    obj.optInt("attachment_count").toString()
    obj.optString("create_date").toString()
    val token = obj.optString("token").toString()
    obj.optString("image_url").toString()
    obj.optBoolean("count").toString()
    val like = obj.optInt("like_ids").toString()
    val dislike = obj.optInt("dislike_ids").toString()
    obj.optString("class").toString()
    obj.optString("like").toString()
    obj.optString("dislike").toString()
    val mensaje = Messages(
        id =id,
        comunicationId = comunicacionId,
       content = stripHtml(conten),
       contensearch = null,
        code = comunicacionCode,
       token = token,
        like_ids = like.toInt(),
        dislike_ids = dislike.toInt()
    )
    return mensaje
}

fun setPublicacion(item:String):Publicaciones{
    val obj = JSONObject(item)

    val id = obj.optInt("id")
    val title = obj.optString("title").toString()
    val content = obj.optString("content").toString()
    val name= obj.optString("partnerName").toString()
    val token = obj.optString("token").toString()
    val like = obj.optInt("like_ids").toString()
    val dislike = obj.optInt("dislike_ids").toString()
    val model = obj.optString("model").toString()
    val publi = Publicaciones(
        id =id,
        partner_id= null,
        title = title,
        content_text = stripHtml(content),
        name = name,
        token = token,
        billtype = model,
        like_ids = like.toInt(),
        dislike_ids = dislike.toInt()
    )
    return publi
}
fun setPorConfirmar(item:String):PorConfirmar{
    val obj = JSONObject(item)

    val id = obj.optInt("id")
    val title = obj.optString("title").toString()
    val type = obj.optString("typep").toString()
    val content= obj.optString("content").toString()
    val token = obj.optString("token").toString()
    val like = obj.optInt("like_ids").toString()
    val dislike = obj.optInt("dislike_ids").toString()
    val model = obj.optString("model").toString()
    val porConfirmar = PorConfirmar(
        id =id,
        type=type ,
        title = title,
        model= model,
        token = token,
        content= stripHtml(content)
    )
    return porConfirmar
}*/