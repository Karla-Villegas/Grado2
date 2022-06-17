package io.gripxtech.odoojsonrpcclient.fragments.cartelera

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import io.gripxtech.odoojsonrpcclient.*
import io.gripxtech.odoojsonrpcclient.core.Odoo
import io.gripxtech.odoojsonrpcclient.databinding.FragmentCarteleraBinding
import io.gripxtech.odoojsonrpcclient.fragments.cartelera.entities.Cartelera
import io.gripxtech.odoojsonrpcclient.fragments.miembros.entities.Miembros
import io.gripxtech.odoojsonrpcclient.repository.Repository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_new_principal.*
import kotlinx.android.synthetic.main.item_new_cartelera_detalle.view.*
import timber.log.Timber

class Fragment_Cartelera: Fragment() {

    private var _binding: FragmentCarteleraBinding? = null
    private val binding get() = _binding!!
    private val ListTypeCartelera = object : TypeToken<ArrayList<Cartelera>>() {}.type
    private val NoticiaType = object : TypeToken<Cartelera>() {}.type
    private val limit = RECORD_LIMIT
    private var compositeDisposable: CompositeDisposable? = null
    private var items = ArrayList<Cartelera>()
    private lateinit var activity: NewActivityPrincipal private set
    private var notice_id: Long = 0
    private lateinit var IcNotice: Any

    val adapter: AdapterCartelera by lazy {
            AdapterCartelera(this, arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCarteleraBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as NewActivityPrincipal
        binding.rvNoticias.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvNoticias.layoutManager = layoutManager
        binding.rvNoticias.adapter = adapter

        adapter.setupScrollListener(binding.rvNoticias)

        noticias()
    }

    private fun noticias(){
        adapter.clear()
        Odoo.route("news", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        val respuesta = call.result.asString.toJsonObject()
                        val ic = respuesta.get("records")
                        if (ic != null){

                            items = gson.fromJson<ArrayList<Cartelera>>(ic, ListTypeCartelera )

                            if(items != null && items.size > 0){
                                if (binding.rvNoticias != null){
                                    OnClick()
                                }
                                adapter.addRowItems(items)

                            }else{

                                binding.rvNoticias.visibility = View.GONE
                                binding.textNoticias.visibility = View.VISIBLE
                            }
                        }else{
                            binding.rvNoticias.visibility = View.GONE
                            binding.textNoticias.visibility = View.VISIBLE
                        }

                        Timber.e("callkw()--->  ${respuesta.get("records")}")
                        Timber.e("callkw()--->  ${items}")
                    } else {
                        Timber.w("callkw() failed with ${it.errorBody()}")

                    }
                } else {
                    Timber.w("request failed with ${it.code()}:${it.message()}")
                }
            }
            this.onError { error ->
                error.printStackTrace()
            }
            this.onComplete { }
        }
    }

    private fun OnClick() {
        binding.rvNoticias.onItemClick { recyclerView, position, v ->
            if (!items.isEmpty()){
                if(adapter.starClick){
                    adapter.starClick = false
                    IcNotice = items.get(position)
                    notice_id = (IcNotice as Cartelera).serverId
                    Toast.makeText(requireContext(), "ID NOTICE: ${notice_id}", Toast.LENGTH_SHORT).show()
                    detalleNoticia(notice_id.toInt(), v)
                }
            }
        }
    }

    private fun detalleNoticia(noticeId: Int, v: View) {
        Odoo.route("/new/$noticeId", "", args = "") {
            this.onNext {
                if (it.isSuccessful) {
                    val call = it.body()!!
                    if (it.isSuccessful) {
                        adapter.hideEmpty()
                        adapter.hideError()
                        adapter.hideMore()
                        val respuesta = call.result.asString.toJsonObject().get("record")
                        val item = gson.fromJson<Cartelera>(respuesta, NoticiaType)

                        val AlertDialog = AlertDialog.Builder(requireContext()).create()
                        val view = layoutInflater.inflate(R.layout.item_new_cartelera_detalle, null)

                        val title = item.title
                        val description = item.description
                        val content = stripHtml(item.content)
                        val date = item.date
                        val expiry_date = item.expiry_date
                        val state = item.state

                        if (title != null) view.title.text = title else view.title.text = ""
                        if (description != null) view.description.text = description else view.description.text = ""
                        if (content != null) view.content.text = content else view.content.text = ""
                        if (date != null) view.date.text = date else view.date.text = ""
                        if (expiry_date != null) view.expiry_date.text = expiry_date else view.expiry_date.text = ""


                        AlertDialog.setView(view)
                        AlertDialog.setCancelable(true)
                        AlertDialog.setOnDismissListener { adapter.starClick = true }
                        val idIconReturn = view.findViewById<ImageView>(R.id.idIconReturn)
                        idIconReturn.setOnClickListener {
                            AlertDialog.dismiss()
                            adapter.setCanStart(true)
                        }
                        AlertDialog.show()
                        AlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        AlertDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)


                        Timber.w("respuesta route ${respuesta}")
                        Timber.w("respuesta content ${content}")
                    } else {
                        Timber.w("callkw() failed with ${it.errorBody()}")

                    }
                } else {
                    Timber.w("request failed with ${it.code()}:${it.message()}")
                }
            }
            this.onError { error ->
                error.printStackTrace()
            }
            this.onComplete { }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

