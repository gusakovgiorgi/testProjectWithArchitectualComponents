package com.gusakov.frogogo.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.gusakov.frogogo.R

class ProgressDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val customView = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
        val dialog = AlertDialog.Builder(context)
            .setView(customView)
            .create().apply {
                setCanceledOnTouchOutside(false)
                window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        return dialog
    }

}