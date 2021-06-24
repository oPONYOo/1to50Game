package com.example.a1to50game

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.a1to50game.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private var mStimulate = 0
    private var mShowTime = 0
    private var mCurrentNum = 0
    private var mNum = 0
    private var mGenCoordX = 0
    private var mGenCoordY = 0
    private lateinit var arraylist : ArrayList<Int>

    private lateinit var mExplainTxt: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        arraylist = arrayListOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25)
        initGame("easy")
    }


    fun initGame(GmLevel: String) {
        arraylist.shuffle()
        when (GmLevel) {

            "easy"-> initSSL(arraylist)

            else -> initSSL(arraylist)
        }
//        mExplainTxt =
//            "<font color='#ff9100'>숫자</font>를 기억하고\n<font color='#ff9100'>작은 순서대로</font> 누르세요."
//        questExplainDialog = SharingDialog(this)
//        questExplainDialog.setType(DialogType.NO_BTN_SINGLE_TEXT)
//            .setContent(mExplainTxt)
//            .show()

        CoroutineScope(mainDispatcher).launch {
            delay(1000L)
            genQuest()
//            questExplainDialog.dismiss()
//            timer()
//            binding.timerTxtView.visibility = View.VISIBLE
//            countSetting = true
        }
    }

    private fun genRandom(num: Int): Int {
        val areaWidth = binding.questAreaLayout.width
        return Random().nextInt(num + areaWidth/5)
    }
    private fun genQuest() {
        mCurrentNum = 1
        val areaWidth = binding.questAreaLayout.width
        val areaHeight = binding.questAreaLayout.height
        Log.e("areaWidth", "${areaWidth}")
        Log.e("areaHeight", "${areaHeight}")
        var i = 0
        while (i < mStimulate) {
            val questLayout = numberLayout(this)
            val length = questLayout.mWidth

            Log.e("i", "${i}")
            when(arraylist[i]){
                0 -> {
                    mGenCoordX = 0
                    mGenCoordY = 0
                }
                1, 2, 3, 4, 5 -> {
                    mGenCoordX += areaWidth / 5
                    mGenCoordY = 0
                    if (i > 4){
                        mGenCoordX = 0
                        mGenCoordY = areaWidth / 5
                    }
                }
                6, 7, 8, 9, 10 -> {
                    mGenCoordX += areaWidth / 5
                    mGenCoordY = areaWidth / 5
                    if (i > 9){
                        mGenCoordX = 0
                        mGenCoordY = areaWidth / 5 * 2
                    }
                }
                11, 12, 13, 14, 15 -> {
                    mGenCoordX += areaWidth / 5
                    mGenCoordY = areaWidth / 5 * 2
                    if (i > 14){
                        mGenCoordX = 0
                        mGenCoordY = areaWidth / 5 * 3
                    }
                }
                16, 17, 18, 19, 20 -> {
                    mGenCoordX += areaWidth / 5
                    mGenCoordY = areaWidth / 5 * 3
                    if (i > 19){
                        mGenCoordX = 0
                        mGenCoordY = areaWidth / 5 * 4
                    }
                }
                21, 22, 23, 24, 25 -> {
                    mGenCoordX += areaWidth / 5
                    mGenCoordY = areaWidth / 5 * 4
                }

            }
            Log.e("list", "${arraylist[i]}")
            Log.e("mGenCoordX", "${mGenCoordX}")
            Log.e("mGenCoordY", "${mGenCoordY}")

            questLayout.setQuestNum(i + 1).setXY(mGenCoordX.toFloat(), mGenCoordY.toFloat())
                .setVisible(true)
            questLayout.setFinally()
            Log.e("first", "$i")
            binding.questAreaLayout.addView(questLayout)
//            if (i != 0) {
//                for (j in 0 until i) {
//                    val prevX = binding.questAreaLayout.getChildAt(j).x.toInt()
//                    val prevY = binding.questAreaLayout.getChildAt(j).y.toInt()
//                    val currX = binding.questAreaLayout.getChildAt(i).x.toInt()
//                    val currY = binding.questAreaLayout.getChildAt(i).y.toInt()
//                    if (currX >= prevX - length &&
//                        currX <= prevX + length &&
//                        currY >= prevY - length &&
//                        currY <= prevY + length) {
//                        binding.questAreaLayout.removeViewAt(i)
//                        i--
//                        break
//                    }
//                }
//            }
            i++
        }
        for (j in 0 until mStimulate) {
            val questLayout =binding.questAreaLayout.getChildAt(j) as numberLayout
            questLayout.setVisible(true)
            val questTxtView: AppCompatTextView = questLayout.findViewById(R.id.numberTxtView)
            val background: ConstraintLayout = questLayout.findViewById(R.id.background)
            questLayout.setOnClickListener {
                Log.e(TAG, "mCurrentNum =" + mCurrentNum + " | " + questTxtView.text.toString())

//                sound = SOUND_EFFECT[Random().nextInt(SOUND_EFFECT.size)]
                var finish = false
                if (mCurrentNum.toString() == questTxtView.text.toString()) {
                    questLayout.setVisible(false)
                    if (mStimulate == mCurrentNum) {
                        questLayout.setOnClickListener(null)
                        if (mStimulate >=25){
                            questLayout.setOnClickListener(null)
                            finish = true
                        }
                    }
                    mCurrentNum++
                    mNum++
                } else {
                    Toast.makeText(applicationContext, "다시 눌러보세요.", Toast.LENGTH_SHORT).show()
                    CoroutineScope(mainDispatcher).launch {
                        delay(500L)
                        questLayout.setOnClickListener(null)
                    }
                }
                if(finish) {
                    questLayout.setOnClickListener(null)
                    Toast.makeText(applicationContext, "잘하셨습니다.", Toast.LENGTH_SHORT).show()
//                    gameLogList.add(GameLogDto(isCorrect, seconds.dot()))
//                    showToast(scene)
                }
//                playSoundPoolEffect(sound, 0)
            }
//            background.setBackgroundResource(R.drawable.img_game_memory_001)
        }

    }


    private fun initSSL(cnts: ArrayList<Int>) {
        mStimulate = cnts[0]
        Log.e("mStimulate", "${mStimulate}")
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }
}