package com.developer.sixfingers.reactiontest

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.developer.sixfingers.reactiontest.helpers.convertNumberToString
import com.developer.sixfingers.reactiontest.helpers.data.Result
import com.developer.sixfingers.reactiontest.helpers.data.addResultToDataBase
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {

    private val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultValue = intent.getLongExtra("result", 0)
        val resTextView: TextView = findViewById(R.id.resultTV)

        resTextView.text = convertNumberToString(resultValue)

        if(checkNameInDataBase()){
            showDialog(getString(R.string.first_change_name_string))
        }

        val userNameTextView: TextView = findViewById(R.id.userNameTV)
        val userName = getSavedUserName()
        userNameTextView.text = userName
        val resVal = intent.getLongExtra("result", 0)
        val res = Result(userName, resVal)
        addResultToDataBase(FirebaseDatabase.getInstance(), res, { e-> val t = Toast(this); t.setText(e); t.show()})
    }

    @SuppressLint("ResourceType", "ApplySharedPref")
    fun onEditName(view: View){
        showDialog(getString(R.string.change_name_string))
    }


    private fun checkNameInDataBase() : Boolean {
        return getSavedUserName() == getString(R.string.def_user_name)
    }

    private fun getSavedUserName() : String {
        val sharedRef : SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        return sharedRef.getString(getString(R.string.user_name_key), getString(R.string.def_user_name))
    }

    override fun onBackPressed() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

    private fun showDialog(titleDialog: String){
        val dialog  = Dialog(context)
        dialog.setContentView(R.layout.set_user_name_layout)
        dialog.setTitle(titleDialog)

        var lp  = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT


        val editText : EditText = dialog.findViewById(R.id.enterUserNameET)
        val setUserNameButton : Button = dialog.findViewById(R.id.setUserNameButton)
        setUserNameButton.setOnClickListener {
            val newName: String = editText.text.toString()
            val sharedRef: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
            val editor = sharedRef.edit()
            editor.putString(getString(R.string.user_name_key), newName)
            editor.commit()

            val userNameTextView: TextView = findViewById(R.id.userNameTV)
            userNameTextView.text = getSavedUserName()

            dialog.dismiss()
        }
        dialog.show()
        dialog.window.attributes = lp
    }
}
