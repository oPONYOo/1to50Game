package com.example.a1to50game

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
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
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private var mnextNum = 1
    private var mCurrentNum = 1
    private var mNum = 0
    private var count = 0
    private var timerTask: Timer? = null
    private var time = 0

    private var secTime = ""
    private var milTime = ""
    var finish = false
    private var sizevalueFloat = 0f
    private var sizevalueInt = 0

    private var density = 0f
    private var changeSize: Float? = null
    private var size_X = 0
    private var size_Y = 0

    private var difficulty = true

    private  var arraylist = ArrayList<Int>()
    private  var arraylist2 = ArrayList<Int>()
    private lateinit var mrandList: ArrayList<Int>
    private lateinit var mrandList2: ArrayList<Int>

    private var numquestLayout = ArrayList<numberLayout>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.getSize()
        changeSize()

        if (savedInstanceState == null){
            initGame(intent.getStringExtra("level").toString(), intent.getStringExtra("difficulty").toString())
        }

    }

    fun initGame(GmLevel: String, Gmdifficulty: String) {
        when (GmLevel) {

            "1to50"-> {
                sizevalueFloat = 5f
                sizevalueInt = 5
                arraylist = arrayListOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25)
                arraylist2 = arrayListOf(26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50)
                arraylist2.shuffle()
            }
            "1to8" -> {
                sizevalueFloat = 2f
                sizevalueInt = 2
                arraylist = arrayListOf(
                    1, 2, 3, 4)
                arraylist2 = arrayListOf(5, 6, 7, 8)
                arraylist2.shuffle()
            }
            "1to18" -> {
                sizevalueFloat = 3f
                sizevalueInt = 3
                arraylist = arrayListOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9)
                arraylist2 = arrayListOf(10, 11, 12, 13, 14, 15, 16, 17, 18)
                arraylist2.shuffle()
            }
            "1to32" -> {
                sizevalueFloat = 4f
                sizevalueInt = 4
                arraylist = arrayListOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
                arraylist2 = arrayListOf(17, 18, 19, 20, 21, 22, 23, 24, 25,26, 27, 28, 29, 30, 31, 32)
                arraylist2.shuffle()
            }
            "1to72" -> {
                sizevalueFloat = 6f
                sizevalueInt = 6
                arraylist = arrayListOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
                arraylist2 = arrayListOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72)
                arraylist2.shuffle()
            }

        }
        difficulty = when(Gmdifficulty){
            "easy" -> true
            else -> false
        }

        count = 0
        CoroutineScope(mainDispatcher).launch {
            delay(1000L)
            mrandList = genRandom()
            count = 1
            mrandList2 = genRandom()
            genQuest()
            start()
        }
    }

    fun getSize(): Point {
        val display = windowManager.defaultDisplay // in case of Activity
        /* val display = activity!!.windowManaver.defaultDisplay */ // in case of Fragment
        val size = Point()
        display.getSize(size) // or getSize(size)
        Log.e("size", "${size}")
        return size
    }

    fun changeSize() {
        val displaySize: Point = this@MainActivity.getSize()
        density = resources.displayMetrics.density
        /* px, dp간의 비율 */
        Log.e("density", "${density}")
        size_X = (displaySize.x/density).toInt()
        size_Y = (displaySize.y/density).toInt()
        Log.e("size_X", "${size_X}")
        Log.e("size_Y", "${size_Y}")

    }

    private fun genRandom(): ArrayList<Int> {
        arraylist.shuffle()

        val idx = ArrayList<Int>()
        when (count) {
            0 -> {
                for (i in 0 until arraylist.size) {
                    idx.add(arraylist[i])

                }
            }
            else -> {
                for (i in 0 until arraylist2.size) {
                    idx.add(arraylist2[i])
                }
            }
        }
        return idx
    }



    private fun genQuest() {
        changeSize = binding.questAreaLayout.width / sizevalueFloat

        Log.e("mrandList", "${mrandList}")

        for (count in 0 until mrandList.size) {
            val questLayout = numberLayout(this)
            questLayout.setQuestNum(mrandList[count])
                .setXY(changeSize!! * (count % sizevalueInt), changeSize!! * (count / sizevalueInt))
                .setVisible(true)
            questLayout.setFinally()
            binding.questAreaLayout.addView(questLayout)
            questLayout.setTxtSize(changeSize!! / 2.8f)
            questLayout.layoutParams.width = changeSize!!.toInt()
            questLayout.layoutParams.height = changeSize!!.toInt()
        }

        for (j in 0 until mrandList.size) {
            val questLayout = binding.questAreaLayout.getChildAt(j) as numberLayout
            questLayout.setVisible(true)
            val questTxtView: AppCompatTextView = questLayout.findViewById(R.id.numberTxtView)

            numquestLayout.add(questLayout)
            questLayout.currentNumber(mCurrentNum, difficulty)
            questLayout.setOnClickListener {
                Log.e(TAG, "mCurrentNum =" + mCurrentNum + " | " + questTxtView.text.toString())

                if (mCurrentNum.toString() == questTxtView.text.toString()) {
                    questLayout.setVisible(false)
                    mnextNum = mCurrentNum+1
                    binding.showNumberTxtView.text = mnextNum.toString()
                    val prevX = questLayout.x
                    val prevY = questLayout.y
                    Log.e("marandListSize", "${mrandList.size}")
                    if (mCurrentNum <= mrandList.size) {
                        mrandList[j] = mrandList2[mNum]
                            binding.questAreaLayout.removeView(questLayout)
                        if (mrandList.size>mNum){
                            questLayout.setQuestNum(mrandList[j]).setXY(prevX, prevY)
                                .setVisible(true)
                            Log.e("mrandList", "${mrandList}")
                            mNum++
                            questLayout.setFinally()
                            binding.questAreaLayout.addView(questLayout)
                        }
                    }else{
                        mrandList[j] = 0
                        questLayout.setQuestNum(0).setXY(prevX, prevY)
                            .setVisible(true)
                        questLayout.setFinally()
                    }
                    if (mnextNum>mrandList.size*2){
                        finish = true
                    }
                    mCurrentNum++
                    for (j in 0 until mrandList.size) {
                        numquestLayout[j].currentNumber(mCurrentNum, difficulty)
                        Log.e("text2", "${questTxtView.text}")
                    }
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    for (j in 0 until mrandList.size) {
                        shakeAnim(numquestLayout[j])
                    }
                    CoroutineScope(mainDispatcher).launch {
                        delay(500L)
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }

                }
                if (finish) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    binding.showNumberTxtView.text = "끝"
                    pause()
                }

            }
        }

    }


    private fun start() {
        timerTask = timer(period = 10) {
            time++
            val sec = time / 100
            val milli = time % 100

            runOnUiThread {
                secTime = "$sec."
                milTime = "$milli"

                binding.secTxtView.text = secTime
                binding.milliTxtView.text = milTime

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("mrandList", mrandList)
        outState.putIntegerArrayList("mrandList2", mrandList2)
        outState.putIntegerArrayList("arraylist", arraylist)
        outState.putIntegerArrayList("arraylist2", arraylist2)
        outState.putInt("mCurrentNum", mCurrentNum)
        outState.putInt("time", time)
        outState.putInt("mNum", mNum)
        outState.putBoolean("finish", finish)

        outState.putInt("sizevalueInt", sizevalueInt)
        outState.putFloat("sizevalueFloat", sizevalueFloat)

        outState.putString("secTxt", binding.secTxtView.text.toString())
        outState.putString("millTxt", binding.milliTxtView.text.toString())
        outState.putString("showNum", binding.showNumberTxtView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mrandList = savedInstanceState.getIntegerArrayList("mrandList")!!
        mrandList2 = savedInstanceState.getIntegerArrayList("mrandList2")!!
        arraylist = savedInstanceState.getIntegerArrayList("arraylist")!!
        arraylist2 = savedInstanceState.getIntegerArrayList("arraylist2")!!
        sizevalueInt = savedInstanceState.getInt("sizevalueInt")
        sizevalueFloat = savedInstanceState.getFloat("sizevalueFloat")

        finish = savedInstanceState.getBoolean("finish")
        mNum = savedInstanceState.getInt("mNum")
        mCurrentNum = savedInstanceState.getInt("mCurrentNum")
        time = savedInstanceState.getInt("time")
        binding.secTxtView.text = savedInstanceState.getString("secTxt")
        binding.milliTxtView.text = savedInstanceState.getString("millTxt")
        binding.showNumberTxtView.text = savedInstanceState.getString("showNum")


        CoroutineScope(mainDispatcher).launch {
            delay(1000L)
            if (!finish){
                genQuest()
                start()
            }

        }

    }
}