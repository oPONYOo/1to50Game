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
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private var mStimulate = 0
    private var mnextNum = 1
    private var mCurrentNum = 1
    private var mNum = 0
    private var count = 0
    private var timerTask: Timer? = null
    private var time = 0

    private var secTime = ""
    private var milTime = ""

    private var sizevalueFloat = 0f
    private var sizevalueInt = 0
    private  var arraylist = ArrayList<Int>()
    private  var arraylist2 = ArrayList<Int>()
    private lateinit var mrandList: ArrayList<Int>
    private lateinit var mrandList2: ArrayList<Int>
    private lateinit var mrandList3: ArrayList<Int>

    private var numquestLayout = ArrayList<numberLayout>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null){
            initGame(intent.getStringExtra("1to50").toString())
            Log.e("1to", intent.getStringExtra("1to8").toString())
        }
    }

    fun initGame(GmLevel: String) {
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

        count = 0
        CoroutineScope(mainDispatcher).launch {
            delay(1000L)
            mrandList = genRandom()
            genQuest()
            start()
        }
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
        val areaWidth = binding.questAreaLayout.width / sizevalueFloat

        Log.e("mrandList", "${mrandList}")

        for (count in 0 until mrandList.size) {
            val questLayout = numberLayout(this)
            questLayout.setQuestNum(mrandList[count])
                .setXY(areaWidth * (count % sizevalueInt), areaWidth * (count / sizevalueInt))
                .setVisible(true)
            questLayout.setFinally()
            binding.questAreaLayout.addView(questLayout)
        }

        for (j in 0 until mrandList.size) {
            val questLayout = binding.questAreaLayout.getChildAt(j) as numberLayout
            questLayout.setVisible(true)
            val questTxtView: AppCompatTextView = questLayout.findViewById(R.id.numberTxtView)
            numquestLayout.add(questLayout)
            questLayout.currentNumber(mCurrentNum)
            questLayout.setOnClickListener {
                Log.e(TAG, "mCurrentNum =" + mCurrentNum + " | " + questTxtView.text.toString())
                var finish = false
                if (mCurrentNum.toString() == questTxtView.text.toString()) {
                    questLayout.setVisible(false)
                    mnextNum = mCurrentNum+1
                    binding.showNumberTxtView.text = mnextNum.toString()

                    if (mnextNum>mrandList.size*2){
                        finish = true
                    }
                    if (mrandList[j] == mCurrentNum) {
                            val prevX = questLayout.x
                            val prevY = questLayout.y
                            binding.questAreaLayout.removeView(questLayout)
                            count = 1
                        mrandList2 = genRandom()
                        if (mrandList2.size>mNum){
                            questLayout.setQuestNum(mrandList2[mNum]).setXY(prevX, prevY)
                                .setVisible(true)
                            Log.e("mrandList", "${mrandList}")
                            Log.e("mrandList2", "${mrandList2}")



//                            mrandList3 = mrandList
//                            mrandList3.remove(mrandList[j])
//                            Log.e("j", "${j}")
//                            Log.e("j", "${mrandList.get(mrandList[j])}")
//                            Log.e("j", "${j}")
//                            mrandList3.add(mrandList.indexOf(mrandList.get(mrandList[j])), mrandList2[mNum])
//                            Log.e("mrandList3", "${mrandList3}")

                            mNum++
                            questLayout.setFinally()
                            binding.questAreaLayout.addView(questLayout)


                        }


                    }
                    mCurrentNum++
                    for (j in 0 until mrandList.size) {
                        numquestLayout[j].currentNumber(mCurrentNum)
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

    private fun initSSL(cnts: ArrayList<Int>) {
        mStimulate = cnts[0]
        Log.e("mStimulate", "${mStimulate}")
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

        mCurrentNum = savedInstanceState.getInt("mCurrentNum")
        time = savedInstanceState.getInt("time")
        binding.secTxtView.text = savedInstanceState.getString("secTxt")
        binding.milliTxtView.text = savedInstanceState.getString("millTxt")
        binding.showNumberTxtView.text = savedInstanceState.getString("showNum")


        CoroutineScope(mainDispatcher).launch {
            delay(1000L)
            genQuest()
            start()
        }

    }
}