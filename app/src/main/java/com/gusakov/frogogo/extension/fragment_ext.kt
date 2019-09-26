package com.gusakov.frogogo.extension

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

const val DIALOG_TAG = "dialog_tag"
fun <T : DialogFragment> Fragment.showDialog(dialog: T, block: T.() -> Unit = {}): T {
    childFragmentManager.beginTransaction().apply {
        dialog.apply(block)
        childFragmentManager.findFragmentByTag(DIALOG_TAG)?.let { remove(it) }
        dialog.show(this, DIALOG_TAG)
    }
    return dialog
}