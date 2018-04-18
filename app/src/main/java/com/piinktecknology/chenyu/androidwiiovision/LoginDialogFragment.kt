package com.piinktecknology.chenyu.androidwiiovision

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login_dialog.*
import kotlinx.android.synthetic.main.fragment_login_dialog.view.*

class LoginDialogFragment : DialogFragment(){

    val userName : String = "Admin"
    val password : String = "password"

    interface Callback{
        fun OnClick(userName:String, password:String);
    }

    fun show(fragmentManager: FragmentManager){
        show(fragmentManager, "LoginDialogFragment")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        val view = inflater.inflate(R.layout.fragment_login_dialog, null)
        builder.setView(view)
                .setPositiveButton(R.string.submit, DialogInterface.OnClickListener{dialog, which ->

                    if(view.loginUserName.text.toString().equals("")){
                        view.loginUserName.setText("Admin")
                    }

                    if(view.loginPassword.text.toString().equals("")){
                        view.loginPassword.setText("password")
                    }

                    if(view.loginUserName.text.toString() == userName && view.loginPassword.text.toString() == password){
                        val intent = Intent(activity, SettingActivity::class.java)
                        activity.startActivity(intent)
                    }
                    else{
                        println("not ok")
                        Toast.makeText(activity,"Please",Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton(R.string.notsub, DialogInterface.OnClickListener{dialog, which ->
                    println("no")
                })

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}