package com.mobdeve.leyesam.mp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mobdeve.leyesam.mp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGame.setOnClickListener{view: View? ->
            var goToCard = Intent(this, GameActivity::class.java)
            goToCard.putExtra("type", "new")
            startActivity(goToCard)
        }

        binding.continueGame.setOnClickListener{view: View? ->
            var goToCard = Intent(this, GameActivity::class.java)
            goToCard.putExtra("type", "continue")
            startActivity(goToCard)
        }

        binding.viewStatistics.setOnClickListener{view: View? ->
            var goToCard = Intent(this, StatisticsActivity::class.java)
            startActivity(goToCard)
        }

        var goToCard = Intent(this, GameActivity::class.java)
        startActivity(goToCard)
    }
}