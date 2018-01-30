package com.developer.sixfingers.reactiontest.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.developer.sixfingers.reactiontest.R
import com.developer.sixfingers.reactiontest.ResultActivity
import java.util.*
import java.util.concurrent.TimeUnit

enum class StateArea{
    GREEN,
    RED
}

class GameState private constructor() {
    private var stateArea: StateArea
    private var currentTime: Long = 0
    private var currentWrongTime: Long = 0

    private var currentIterationTime: Long = 0
    private var iterationTime: Int = 0

    private val minIterationTime = 50
    private val maxIterationTime = 600

    private var isPress: Boolean = false

    private var handler: Handler = Handler()
    private var handlerWrong: Handler = Handler()

    private var runnable: Runnable = Runnable {
        run {
            upData()
        }
    }

    private var startRunnable: Runnable = Runnable {
        run {
            val countTV: TextView = (context as Activity).findViewById(R.id.countTV)
            countTV.visibility = View.VISIBLE
            for(item: Int in 1..3){
                countTV.text = item.toString()
                TimeUnit.SECONDS.sleep(1)
            }

            countTV.setText("START")
            TimeUnit.SECONDS.sleep(1)
            countTV.visibility = View.GONE
        }
    }

    private var wrongTimwRunnable: Runnable = Runnable{
        run{
            wrongSituationUpdate()
        }
    }

    private var context: Context? = null
    private var mainFrame: FrameLayout? = null
    private var currentTimeTV: TextView? = null
    private var currentWrongTimeTV: TextView? = null

    private val rand: Random = Random()
    private var isExit: Boolean = false

    private var MillisecondTime: Long = 0
    private var StartTime: Long = 0
    private var TimeBuff: Long = 0
    private var UpdateTime = 0L

    private var Seconds: Int = 0
    private var Minutes: Int = 0
    private var MilliSeconds: Int = 0

    init {
        val rand = Random()
        val tempState = rand.nextBoolean()
        stateArea = when  (tempState){
            true -> StateArea.GREEN
            false -> StateArea.RED
        }

        currentTime = 0
        currentWrongTime = 0
        currentIterationTime = 0
        iterationTime = rand.nextInt(maxIterationTime - minIterationTime) + minIterationTime
    }

    private object Holder {
        val INSTANCE = GameState()
    }


    companion object {
        val instance: GameState by lazy { Holder.INSTANCE }
    }

    fun run(){
        StartTime = SystemClock.uptimeMillis()

        currentWrongTime = 0
        currentIterationTime = 0

        runnable.run()
        wrongTimwRunnable.run()
    }
    fun runStart(){
        startRunnable.run()
    }

    fun setContext(con: Context){
        context = con

        mainFrame = (context as Activity).findViewById(R.id.mainFiled)
        mainFrame?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    isPress = true
                    true
                }
                MotionEvent.ACTION_UP -> {
                    isPress = false
                    false
                }
                else -> {
                    isPress = false
                    false
                }
            }
        }

        currentTimeTV = (context as Activity).findViewById(R.id.currentTimeTV)
        currentWrongTimeTV = (context as Activity).findViewById(R.id.currentWrongTimeTV)

        fillColor()
    }

   /* private var isStartWrong: Boolean = false
    private var oldWrongTime: Long = 0*/
    private fun upData(){
        MillisecondTime = SystemClock.uptimeMillis() - StartTime
        UpdateTime = TimeBuff + MillisecondTime
        Seconds = (UpdateTime / 1000).toInt()
        Minutes = Seconds / 60
        Seconds %= 60
        MilliSeconds = (UpdateTime % 1000).toInt()

        currentIterationTime+=1


        if(currentIterationTime>=iterationTime){
            currentIterationTime = 0
            iterationTime = rand.nextInt(maxIterationTime - minIterationTime) + minIterationTime

            stateArea = when(stateArea){
                StateArea.GREEN->StateArea.RED
                StateArea.RED->StateArea.GREEN
            }
            fillColor()
        }

        /*if((isPress && stateArea == StateArea.RED) || (!isPress && stateArea == StateArea.GREEN)){
            currentWrongTime+=10
        }*/


        fillColor()
        if(isExit){
            handler.removeCallbacks(runnable)
            handlerWrong.removeCallbacks(wrongTimwRunnable)
        }
        else{
            currentWrongTimeTV?.text = (Math.round((currentWrongTime/1000).toDouble())).toString() + ":" +  (currentWrongTime % 1000).toString()
            currentTimeTV?.text = Minutes.toString() + ":" + Seconds.toString() + ":" +  MilliSeconds.toString() //convertNumberToString(currentTime)

            handler.postDelayed(runnable, 0)
        }

        //Проигрыш
        if(currentWrongTime>=3000){
            handler?.removeCallbacks(runnable)
            isExit = true
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra("result", UpdateTime)
            context?.startActivity(intent)
        }

    }

    private fun wrongSituationUpdate(){
        if((isPress && stateArea == StateArea.RED) || (!isPress && stateArea == StateArea.GREEN)){
            currentWrongTime+=20
        }
        handlerWrong.postDelayed(wrongTimwRunnable, 10)
    }

    private fun fillColor() {
        when (stateArea) {
            StateArea.GREEN -> {
                mainFrame?.setBackgroundResource(R.color.greenColor)
            }
            StateArea.RED -> {
                mainFrame?.setBackgroundResource(R.color.redColor)
            }
        }
    }

}