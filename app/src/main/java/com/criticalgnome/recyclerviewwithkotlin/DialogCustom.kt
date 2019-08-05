package com.criticalgnome.recyclerviewwithkotlin

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MyFragment : DialogFragment() {

    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var _view: View = getActivity()!!.getLayoutInflater().inflate(R.layout.fragment_dialog, null)

        this.button = _view.findViewById(R.id.retryButton)
        var alert = AlertDialog.Builder(activity)
        alert.setView(_view)

        this.button!!.setOnClickListener({
            dismiss()
            (activity as MainActivity).retrofitGetDataFromUrl()
        })

        alert.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                (activity as MainActivity).finish()
                true
            } else {
                false
            }

        }
        return alert.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog().setCanceledOnTouchOutside(false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}