package com.mobdeve.leyesam.mp

import android.app.ActionBar
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.leyesam.mp.databinding.ActivityGameBinding


private const val TAG = "GameActivity"

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_game)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(DisplayMetrics())
        val width = displayMetrics.widthPixels

        val cv = CheckersView(this, null)
        cv.layoutParams = ActionBar.LayoutParams(width, width)
        binding.constraintLayoutContainer.addView(cv)
         */
        val checkersView = findViewById<CheckersView>(R.id.checkers_view)
        val title = findViewById<TextView>(R.id.title);
        checkersView.setTitle(title);

        /*
        val row1 = intArrayOf(0, 2, 0, 2, 0, 2, 0, 2)
        val row2 = intArrayOf(2, 0, 2, 0, 2, 0, 2, 0)
        val row3 = intArrayOf(0, 2, 0, 2, 0, 2, 0, 2)
        val row4 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row5 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row6 = intArrayOf(1, 0, 1, 0, 1, 0, 1, 0)
        val row7 = intArrayOf(0, 1, 0, 1, 0, 1, 0, 1)
        val row8 = intArrayOf(1, 0, 1, 0, 1, 0, 1, 0)
        val board = arrayOf(row1, row2, row3, row4, row5, row6, row7, row8)

         */

        /*
        val row1 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row2 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row3 = intArrayOf(0, 2, 0, 0, 0, 0, 0, 0)
        val row4 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row5 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row6 = intArrayOf(1, 0, 1, 0, 1, 0, 1, 0)
        val row7 = intArrayOf(0, 1, 0, 1, 0, 1, 0, 1)
        val row8 = intArrayOf(1, 0, 1, 0, 1, 0, 1, 0)
        val board = arrayOf(row1, row2, row3, row4, row5, row6, row7, row8)

         */
        /*
        val row1 = intArrayOf(0, 2, 0, 2, 0, 2, 0, 2)
        val row2 = intArrayOf(2, 0, 2, 0, 2, 0, 2, 0)
        val row3 = intArrayOf(0, 2, 0, 2, 0, 2, 0, 2)
        val row4 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row5 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row6 = intArrayOf(0, 0, 1, 0, 0, 0, 0, 0)
        val row7 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row8 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val board = arrayOf(row1, row2, row3, row4, row5, row6, row7, row8)

         */

        val row1 = intArrayOf(0, 0, 0, 2, 0, 0, 0, 0)
        val row2 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row3 = intArrayOf(0, 4, 0, 0, 0, 0, 0, 0)
        val row4 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row5 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row6 = intArrayOf(0, 0, 0, 0, 3, 0, 0, 0)
        val row7 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row8 = intArrayOf(0, 0, 0, 0, 0, 0, 1, 0)
        val board = arrayOf(row1, row2, row3, row4, row5, row6, row7, row8)

        checkersView.setBoard(board)
    }
}