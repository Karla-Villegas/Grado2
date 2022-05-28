package io.gripxtech.odoojsonrpcclient.core.utils

import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import io.gripxtech.odoojsonrpcclient.R
import io.gripxtech.odoojsonrpcclient.databinding.ProgressDialogBinding

private lateinit var progressBar: ProgressDialog
fun createProgress(context: Context, texto: String, Subtexto: String) {
    progressBar = ProgressDialog(context)
    progressBar.setCanceledOnTouchOutside(true)
    progressBar.setCancelable(true)
    progressBar.show()
    progressBar.setContentView(R.layout.progress_dialog)
    progressBar.findViewById<TextView>(R.id.passwordDialog).text = Subtexto
    progressBar.findViewById<TextView>(R.id.emailDialog).text = texto
    progressBar.findViewById<Button>(R.id.ButtonDialog).setOnClickListener {
        progressBar.dismiss()
    }

    progressBar.window!!.setBackgroundDrawableResource(android.R.color.transparent)


}
fun finishD() {
        progressBar.dismiss()
}