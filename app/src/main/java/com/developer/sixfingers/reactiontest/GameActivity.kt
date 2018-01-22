package com.developer.sixfingers.reactiontest

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.developer.sixfingers.reactiontest.helpers.GameState
import java.util.concurrent.TimeUnit

class GameActivity : AppCompatActivity() {

    private var countTV: TextView? = null
    private var count: Int = 3
    private var isStart: Boolean? = null

    private var gameState: GameState = GameState.instance
    private val runStart = Runnable{
        run{
            if(count>0) {
                countTV?.text = count.toString()
            }
            else{
                countTV?.text = getString(R.string.start_string)
                isStart = true
            }
        }
    }

    private val runGame = Runnable{
        run{
            countTV?.visibility = GONE
            gameState.run()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gameState.setContext(this)

        countTV = findViewById(R.id.countTV)
        isStart = false

        val t = Thread(Runnable {
             run{
                 countTV?.visibility = VISIBLE
                 for(item: Int in 1..4) {
                     count = 3 - item + 1
                     runOnUiThread(runStart)
                     TimeUnit.SECONDS.sleep(1)
                 }


                 runOnUiThread(runGame)
            }

        })
        t.start()




        //gameState.run()
    }


}
