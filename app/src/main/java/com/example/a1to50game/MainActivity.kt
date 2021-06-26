package com.example.a1to50game

import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.a1to50game.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private var mStimulate = 0
    private var mnextNum = 1
    private var mCurrentNum = 0
    private var mNum = 0
    private var count = 0
    private var timerTask: Timer? = null
    private var time = 0

    private var secTime = ""
    private var milTime = ""

    private lateinit var arraylist: ArrayList<Int>
    private lateinit var arraylist2: ArrayList<Int>
    private lateinit var mrandList: ArrayList<Int>
    private lateinit var mrandList2: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        arraylist = arrayListOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25)
        arraylist2 = arrayListOf(26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50)
        arraylist2.shuffle()
        initGame("easy")
    }


    fun initGame(GmLevel: String) {
//        when (GmLevel) {
//
//            "easy"-> initSSL(arraylist)
//
//            else -> initSSL(arraylist)
//        }

        count = 0
        CoroutineScope(mainDispatcher).launch {
            delay(1000L)
            genQuest()
            start()
        }
    }

    private fun genRandom(): ArrayList<Int> {
        arraylist.shuffle()

        val idx = ArrayList<Int>()
        when (count) {
            0 -> {
                for (i in 0..24) {
                    idx.add(arraylist[i])
                }
            }
            else -> {
                for (i in 0..24) {
                    idx.add(arraylist2[i])
                }
            }
        }
        return idx
    }



    private fun genQuest() {
        mCurrentNum = 1
        val areaWidth = binding.questAreaLayout.width / 5f
        mrandList = genRandom()
        Log.e("mrandList", "${mrandList}")

        for (count in 0 until mrandList.size) {
            val questLayout = numberLayout(this)
            questLayout.setQuestNum(mrandList[count])
                .setXY(areaWidth * (count % 5), areaWidth * (count / 5))
                .setVisible(true)
            questLayout.setFinally()
            binding.questAreaLayout.addView(questLayout)
        }

        for (j in 0 until mrandList.size) {
            val questLayout = binding.questAreaLayout.getChildAt(j) as numberLayout
            questLayout.setVisible(true)
            val questTxtView: AppCompatTextView = questLayout.findViewById(R.id.numberTxtView)

            questLayout.currentNumber(mCurrentNum)
            Log.e("changeNum1", "${mCurrentNum}")
            questLayout.setOnClickListener {
                Log.e(TAG, "mCurrentNum =" + mCurrentNum + " | " + questTxtView.text.toString())
                questLayout.currentNumber(mCurrentNum)
                Log.e("changeNum1", "${mCurrentNum}")

                var finish = false

                if (mCurrentNum.toString() == questTxtView.text.toString()) {
                    questLayout.setVisible(false)
                    mnextNum = mCurrentNum+1
                    binding.showNumberTxtView!!.text = mnextNum.toString()

                    if (mnextNum>50){
                        finish = true
                    }
                    if (mrandList[j] == mCurrentNum || mrandList2[j] == mCurrentNum) {
                            val prevX = questLayout.x
                            val prevY = questLayout.y
                            binding.questAreaLayout.removeView(questLayout)
                            count = 1
                        mrandList2 = genRandom()
                        if (mrandList2.size>mNum){
                            questLayout.setQuestNum(mrandList2[mNum]).setXY(prevX, prevY)
                                .setVisible(true)
                            mNum++
                            questLayout.setFinally()
                            binding.questAreaLayout.addView(questLayout)

                        }

                    }

                    mCurrentNum++
                    questLayout.currentNumber(mCurrentNum)
                    Log.e("changeNum2", "${mCurrentNum}")

                } else {
                    shakeAnim(questLayout)
                    Toast.makeText(applicationContext, "틀렸습니다.", Toast.LENGTH_SHORT)
                        .show()
                    window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    CoroutineScope(mainDispatcher).launch {
                        delay(500L)
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }

                }
                if (finish) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.showNumberTxtView!!.text = "끝"
                    pause()
                }
            }
        }
    }


    private fun initSSL(cnts: ArrayList<Int>) {
        mStimulate = cnts[0]
        Log.e("mStimulate", "${mStimulate}")
    }


    private fun start() {
        timerTask = timer(period = 10) {	// timer() 호출
            time++	// period=10, 0.01초마다 time를 1씩 증가
            val sec = time / 100	// time/100, 나눗셈의 몫 (초 부분)
            val milli = time % 100	// time%100, 나눗셈의 나머지 (밀리초 부분)

            // UI조작을 위한 메서드
            runOnUiThread {

                secTime = "$sec."
                milTime = "$milli"

                binding.secTxtView!!.text = secTime
                binding.milliTxtView!!.text = milTime

            }
        }
    }

    private fun pause() {
        timerTask!!.cancel()
    }

    private var shakeAnimation: Animation? = null

    private fun shakeAnim(view: View?) {
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake)
        view!!.startAnimation(shakeAnimation)
    }
}