package com.mobdeve.leyesam.mp

import android.content.Intent
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mobdeve.leyesam.mp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val TAG = "GameActivity"
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val helper = MyDBHelper(applicationContext)
        val db = helper.readableDatabase
        db.execSQL("DELETE FROM statistics")
        db.execSQL("INSERT INTO statistics VALUES (12, 2, 4, 6)")

        binding.startGame.setOnClickListener{view: View? ->
            val goToCard = Intent(this, GameActivity::class.java)
            goToCard.putExtra("type", "new")
            startActivity(goToCard)
        }

        binding.continueGame.setOnClickListener{view: View? ->
            val goToCard = Intent(this, GameActivity::class.java)
            goToCard.putExtra("type", "continue")
            startActivity(goToCard)
        }

        binding.viewStatistics.setOnClickListener{view: View? ->
            val result = db.rawQuery("SELECT * FROM statistics", null)
            var gamesPlayed = "0"
            var whiteWins = "0"
            var blackWins = "0"
            var draws = "0"
            if (result.moveToLast()) {
                gamesPlayed = result.getString(0)
                whiteWins = result.getString(1)
                blackWins = result.getString(2)
                draws = result.getString(3)
            }

            Log.d(TAG, "$gamesPlayed $whiteWins $blackWins $draws")

            val goToCard = Intent(this, StatisticsActivity::class.java)
            goToCard.putExtra("gamesPlayed", gamesPlayed)
            goToCard.putExtra("whiteWins", whiteWins)
            goToCard.putExtra("blackWins", blackWins)
            goToCard.putExtra("draws", draws)
            startActivity(goToCard)
        }

        //var goToCard = Intent(this, GameActivity::class.java)
        //startActivity(goToCard)
    }
}