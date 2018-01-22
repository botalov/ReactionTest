package com.developer.sixfingers.reactiontest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.developer.sixfingers.reactiontest.helpers.data.Result
import com.developer.sixfingers.reactiontest.helpers.data.addResultToDataBase
import com.google.firebase.database.FirebaseDatabase

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //val res = Result("test", 0)
        //addResultToDataBase(FirebaseDatabase.getInstance(), res, {e-> val t = Toast(this); t.setText(e); t.show()})
    }

    fun onClickStart(view: View){
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    fun onClickGoToStat(view: View){
        val intent = Intent(this, StatisticActivity::class.java)
        startActivity(intent)
    }
}
