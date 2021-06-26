package com.example.a1to50game

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class numberLayout(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    private var mStarTxtView: AppCompatTextView
    private var mQuestNum = 0
    private var mX = 0.0f
    private var mY = 0.0f
    var mWidth = 0

    init {
        val inflaterService = Context.LAYOUT_INFLATER_SERVICE
        val inflater = context.getSystemService(inflaterService) as LayoutInflater
        val view = inflater.inflate(R.layout.item_number_sequence, this, false)
        addView(view)

        mStarTxtView = view.findViewById(R.id.numberTxtView)
        mWidth = view.layoutParams.width
    }

    fun setQuestNum(questNum: Int): numberLayout {
        this.mQuestNum = questNum
        return this
    }

    fun setXY(x: Float, y: Float): numberLayout {
        this.mX = x
        this.mY = y
        return this
    }

    fun setFinally() {
        x = this.mX
        y = this.mY
        mStarTxtView.text = this.mQuestNum.toString()
    }

    fun currentNumber(currentNum: Int) {
        Log.e("currentNum", "${currentNum}")
        Log.e("current", "${mStarTxtView.text.toString().toInt()}")
        if (mStarTxtView.text!=""&&currentNum == mStarTxtView.text.toString().toInt()){

            mStarTxtView.setTextColor(Color.parseColor("#3FB5B5"))
        }else{
            mStarTxtView.setTextColor(Color.parseColor("#B2ADDDB9"))
        }

    }

    fun setVisible(visible: Boolean) {
        mStarTxtView.visibility = if(visible) View.VISIBLE else View.GONE
    }

}