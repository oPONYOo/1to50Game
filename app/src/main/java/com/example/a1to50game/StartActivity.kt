package com.example.a1to50game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.example.a1to50game.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    private var level = ""
    private var levelSelect = true
    private var difficulty = ""
    private var difficultySelect = true
    val btnArray = ArrayList<AppCompatButton>()
    val btnArray2 = ArrayList<AppCompatButton>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnArray.add(binding.firstBtn)
        btnArray.add(binding.secondBtn)
        btnArray.add(binding.thirdBtn)
        btnArray.add(binding.fourthBtn)
        btnArray.add(binding.fifthBtn)

        btnArray2.add(binding.easyBtn)
        btnArray2.add(binding.normalBtn)

        binding.firstBtn.tag = "1to8"
        binding.secondBtn.tag= "1to18"
        binding.thirdBtn.tag = "1to32"
        binding.fourthBtn.tag = "1to50"
        binding.fifthBtn.tag = "1to72"
        binding.easyBtn.tag = "easy"
        binding.normalBtn.tag = "normal"


        for (i in 0 until btnArray.size){
            btnArray[i].setOnClickListener(onClickListener)
        }

        for (i in 0 until btnArray2.size){
            btnArray2[i].setOnClickListener(difficultyClickListener)
        }

        binding.startBtn.setOnClickListener{
            if (levelSelect && difficultySelect){
                val nextIntent = Intent(this, MainActivity::class.java)
                nextIntent.putExtra("level", level)
                nextIntent.putExtra("difficulty", difficulty)
                startActivity(nextIntent)

            }
        }

    }

    private var onClickListener = View.OnClickListener { view ->
        level = view.tag.toString()
        Log.e("tag", level)
        levelSelect = true
    }

    private var difficultyClickListener = View.OnClickListener { view ->
        difficulty = view.tag.toString()
        Log.e("tag2", difficulty)
        difficultySelect = true
    }


}