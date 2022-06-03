package com.mobdeve.leyesam.mp

import android.util.Log

class CheckersModel {
    var board = arrayOf<IntArray>()

    init {
        val row1 = intArrayOf(0, 2, 0, 2, 0, 2, 0, 2)
        val row2 = intArrayOf(2, 0, 2, 0, 2, 0, 2, 0)
        val row3 = intArrayOf(0, 2, 0, 2, 0, 2, 0, 2)
        val row4 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row5 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val row6 = intArrayOf(1, 0, 1, 0, 1, 0, 1, 0)
        val row7 = intArrayOf(0, 1, 0, 1, 0, 1, 0, 1)
        val row8 = intArrayOf(1, 0, 1, 0, 1, 0, 1, 0)
        board = arrayOf(row1, row2, row3, row4, row5, row6, row7, row8)
    }

    override fun toString(): String {
        var output = " \n"
        val chars = arrayOf(".", "O", "X")
        for (i in 0..7) {
            for (j in 0..7) {
                output = "$output${chars[board[i][j]]}  "
            }
            output = "$output\n"
        }

        return "\n$output"
    }

    fun movePiece(xsrc: Int, ysrc: Int, xdest: Int, ydest: Int) {

    }
}