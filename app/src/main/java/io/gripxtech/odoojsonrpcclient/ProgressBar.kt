package io.gripxtech.odoojsonrpcclient

import android.app.ProgressDialog
import android.content.Context
import android.widget.TextView

class ProgressBar {

    lateinit var progressDialog: ProgressDialog

    fun progressbar(context: Context, texto: String) {
        progressDialog =
            ProgressDialog(context)
        progressDialog.setCanceledOnTouchOutside(
            false
        )
        progressDialog.setCancelable(false)
        progressDialog.show()
        progressDialog.setContentView(R.layout.progressdialog_layout)
        val txtProgress = progressDialog.findViewById<TextView>(R.id.idtxtProgress)
        txtProgress.text = texto
        progressDialog.window!!.setBackgroundDrawableResource(
            android.R.color.transparent
        )
    }

    fun finishProgressBar() {
        progressDialog.dismiss()
    }

  /*  fun progressFalloConexion(context: Context): TextView? {
        progressDialog = ProgressDialog(context)
        progressDialog.setCanceledOnTouchOutside(true)
        progressDialog.show()
        progressDialog.setContentView(R.layout.layout_fallo_conexion)

        val cerrarDialog = progressDialog.findViewById<TextView>(R.id.idCerrarApp)
        val mensajeDialogErrorConexion =
            progressDialog.findViewById<TextView>(R.id.idMensajeFalloConexion)

        *//*mensajeDialogErrorConexion.text = error.message*//*
        cerrarDialog.setOnClickListener {

        }

        progressDialog.window!!.setBackgroundDrawableResource(
            android.R.color.transparent
        )
        return cerrarDialog
    }*/
}