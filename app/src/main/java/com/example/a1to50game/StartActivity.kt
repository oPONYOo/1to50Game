package com.example.a1to50game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a1to50game.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nextIntent = Intent(this, MainActivity::class.java)

        binding.firstBtn.setOnClickListener {
            nextIntent.putExtra("1to8", "1to8")
            startActivity(nextIntent)
        }
        binding.secondBtn.setOnClickListener {
            nextIntent.putExtra("1to32", "1to32")
            startActivity(nextIntent)
        }
        binding.thirdBtn.setOnClickListener {
            nextIntent.putExtra("1to50", "1to50")
            startActivity(nextIntent)
        }
        binding.fourthBtn.setOnClickListener {
            nextIntent.putExtra("1to72", "1to72")
            startActivity(nextIntent)
        }


    }

}