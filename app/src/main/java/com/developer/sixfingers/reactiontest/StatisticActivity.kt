package com.developer.sixfingers.reactiontest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TabHost
import android.widget.TextView
import android.widget.Toast
import com.developer.sixfingers.reactiontest.helpers.data.Result
import com.developer.sixfingers.reactiontest.helpers.data.getResultsFromDataBase
import com.google.firebase.database.FirebaseDatabase

class StatisticActivity : AppCompatActivity() {

    private val getStat = Runnable{
        run{
            val results = getResultsFromDataBase(FirebaseDatabase.getInstance(), { e-> val t = Toast(this); t.setText(e); t.show()})
            setContent(results)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        val tabHost = findViewById<TabHost>(android.R.id.tabhost)

        tabHost.setup()

        var tabSpec = tabHost.newTabSpec("tag1")
        tabSpec.setIndicator(getString(R.string.local_string))
        tabSpec.setContent(R.id.tab1)
        tabHost.addTab(tabSpec)


        tabSpec = tabHost.newTabSpec("tag2")
        tabSpec.setIndicator(getString(R.string.global_string))
        tabSpec.setContent(R.id.tab2)
        tabHost.addTab(tabSpec)

                //setContent()

        val t = Thread(Runnable {
            run{
                runOnUiThread(getStat)
            }

        })
        t.start()
    }

    private fun setContent(data : MutableList<Result>){

        val localLayout = findViewById<LinearLayout>(R.id.tab2)
        for(item : Result in data){
            val textView = TextView(this)
            textView.text = item.name + item.res_val.toString()
            textView.setTextColor(resources.getColor(R.color.text_color))
            textView.textSize = 14f

            if(textView.parent!=null)
                (textView.parent as ViewGroup).removeAllViews()
            localLayout.addView(textView)
        }
    }

}

