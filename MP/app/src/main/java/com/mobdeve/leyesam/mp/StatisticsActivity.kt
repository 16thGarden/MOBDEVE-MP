package com.mobdeve.leyesam.mp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mobdeve.leyesam.mp.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    private var confirmCount = 0
    private var confirmCountThreshold = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_statistics)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val helper = MyDBHelper(applicationContext)
        val db = helper.readableDatabase

        val intent = intent
        val gamesPlayed = intent.getStringExtra("gamesPlayed")
        val whiteWins = intent.getStringExtra("whiteWins")
        val blackWins = intent.getStringExtra("blackWins")
        val draws = intent.getStringExtra("draws")

        binding.gamesPlayed.text = gamesPlayed
        binding.whiteWins.text = whiteWins
        binding.blackWins.text = blackWins
        binding.draws.text = draws

        binding.resetStatistics.setOnClickListener{view: View? ->
            if (confirmCount == confirmCountThreshold) {
                db?.execSQL("UPDATE statistics SET games_played=0, white_wins=0, black_wins=0, draws=0")
                binding.gamesPlayed.text = "0"
                binding.whiteWins.text = "0"
                binding.blackWins.text = "0"
                binding.draws.text = "0"
                binding.confirm.text = "Statistics Reset"
            } else if (confirmCount < confirmCountThreshold) {
                confirmCount++
                val confirmText = "Press ${confirmCountThreshold - confirmCount + 1} more times to confirm"
                binding.confirm.text = confirmText
            }
        }
    }
}