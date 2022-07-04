package com.mobdeve.leyesam.mp

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import kotlin.math.abs


private const val TAG = "GameActivity"

class CheckersView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var board = arrayOf<IntArray>()
    private var title: TextView? = null
    private var db: SQLiteDatabase? = null

    private var playerUp = MediaPlayer.create(context, R.raw.up)
    private var playerDown = MediaPlayer.create(context, R.raw.down)

    private var canvas: Canvas? = null
    private var margin = 12
    private var squareHeight: Float = 0f
    private var squareWidth: Float = 0f

    private var srcMode = true
    private var srcRow = 0
    private var srcCol = 0
    private var desRow = 0
    private var desCol = 0
    private var turn = 1
    private var notTurn = 2

    private var gameover = false

    private val whitePieceBitmap = BitmapFactory.decodeResource(resources, R.drawable.white_piece)
    private val blackPieceBitMap = BitmapFactory.decodeResource(resources, R.drawable.black_piece)
    private val whiteKingPieceBitMap = BitmapFactory.decodeResource(resources, R.drawable.white_king_piece)
    private val blackKingPieceBitMap = BitmapFactory.decodeResource(resources, R.drawable.black_king_piece)
    private val trophyBitMap = BitmapFactory.decodeResource(resources, R.drawable.trophy)
    private val highlightBitMap = BitmapFactory.decodeResource(resources, R.drawable.highlight)
    private val highlightCaptureBitMap = BitmapFactory.decodeResource(resources, R.drawable.highlight_capture)

    fun setBoard(board: Array<IntArray>) {
        this.board = board
    }
    fun setTitle(title: TextView) {
        this.title = title
    }
    fun setDB(db: SQLiteDatabase) {
        this.db = db
    }

    private fun logBoard() {
        var output = " \n"
        val chars = arrayOf("__", "W1", "B2", "WK", "BK")
        output = "$output    00 01 02 03 04 05 06 07\n"
        for (i in 0..7) {
            output = "${output}0$i  "
            for (j in 0..7) {
                output = "$output${chars[board[i][j]]} "
            }
            output = "$output\n"
        }

        Log.d(TAG, "\n$output")
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        squareHeight = height.div(8).toFloat()
        squareWidth = width.div(8).toFloat()
        this.canvas = canvas

        drawBoard()
        drawPieces()
        if (!srcMode) {
            drawHighlights()
        }
        if (gameover) {
            drawTrophy()
        }
    }
    private fun drawBoard() {
        val paint = Paint()
        for (i in 0..7) {
            for (j in 0..7) {
                paint.color = if ((i + j) % 2 == 1) Color.DKGRAY else Color.LTGRAY
                val left = i * squareWidth
                val top = j * squareHeight
                val right = (i + 1) * squareWidth
                val bottom = (j + 1) * squareWidth
                canvas?.drawRect(left, top, right, bottom, paint)
            }
        }
    }
    private fun drawPieces() {
        val paint = Paint()

        for (i in 0..7) {
            for (j in 0..7) {
                if ((i + j) % 2 == 1) {
                    val left = j * squareWidth + margin
                    val top = i * squareHeight + margin
                    val right = (j + 1) * squareWidth - margin
                    val bottom = (i + 1) * squareWidth - margin
                    val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

                    if (board[i][j] == 1) {
                        canvas?.drawBitmap(whitePieceBitmap, null, rect, paint)
                    } else if (board[i][j] == 2) {
                        canvas?.drawBitmap(blackPieceBitMap, null, rect, paint)
                    } else if (board[i][j] == 3) {
                        canvas?.drawBitmap(whiteKingPieceBitMap, null, rect, paint)
                    } else if (board[i][j] == 4) {
                        canvas?.drawBitmap(blackKingPieceBitMap, null, rect, paint)
                    }
                }
            }
        }
    }
    private fun drawTrophy() {
        val paint = Paint()
        val left = 150
        val top = 150
        val right = width - 150
        val bottom = height - 150
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        canvas?.drawBitmap(trophyBitMap, null, rect, paint)
    }
    private fun drawHighlight(row: Int, col: Int) {
        if (board[row][col] == 0) {
            val paint = Paint()
            val left = col * squareWidth
            val top = row * squareHeight
            val right = (col + 1) * squareWidth
            val bottom = (row + 1) * squareWidth
            val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            canvas?.drawBitmap(highlightBitMap, null, rect, paint)
        }
    }
    private fun drawHighlightCapture(row: Int, col: Int) {
        val paint = Paint()
        val left = col * squareWidth
        val top = row * squareHeight
        val right = (col + 1) * squareWidth
        val bottom = (row + 1) * squareWidth
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        canvas?.drawBitmap(highlightCaptureBitMap, null, rect, paint)
    }
    private fun outOfBounds(row: Int, col: Int): Boolean {
        return row < 0 || row > 7 || col < 0 || col > 7
    }
    private fun safeDrawHighlight(row: Int, col: Int) {
        if (!outOfBounds(row, col)) {
            drawHighlight(row, col)
        }
    }
    private fun safeDrawHighlightAndCapture(row: Int, col: Int, rowCapture: Int, colCapture: Int) {
        if (!outOfBounds(rowCapture, colCapture) && !outOfBounds(row, col)) {
            if (board[rowCapture][colCapture] == notTurn || board[rowCapture][colCapture] == (notTurn + 2)) {
                if (board[row][col] == 0) {
                    drawHighlight(row, col)
                    drawHighlightCapture(rowCapture, colCapture)
                }
            }
        }
    }
    private fun drawHighlights() {
        var row = 0
        var col = 0
        var rowCapture = 0
        var colCapture = 0
        if (board[srcRow][srcCol] == 3 || board[srcRow][srcCol] == 4) {
            if (board[srcRow][srcCol] == turn + 2) {
                row = srcRow - 1
                col = srcCol - 1
                safeDrawHighlight(row, col)

                row = srcRow - 1
                col = srcCol + 1
                safeDrawHighlight(row, col)

                row = srcRow + 1
                col = srcCol - 1
                safeDrawHighlight(row, col)

                row = srcRow + 1
                col = srcCol + 1
                safeDrawHighlight(row, col)
            }
        } else if (turn == 1) {
            row = srcRow - 1
            col = srcCol - 1
            safeDrawHighlight(row, col)

            row = srcRow - 1
            col = srcCol + 1
            safeDrawHighlight(row, col)
        } else {
            row = srcRow + 1
            col = srcCol - 1
            safeDrawHighlight(row, col)

            row = srcRow + 1
            col = srcCol + 1
            safeDrawHighlight(row, col)
        }

        rowCapture = srcRow - 1
        colCapture = srcCol - 1
        row = srcRow - 2
        col = srcCol - 2
        safeDrawHighlightAndCapture(row, col, rowCapture, colCapture)

        rowCapture = srcRow - 1
        colCapture = srcCol + 1
        row = srcRow - 2
        col = srcCol + 2
        safeDrawHighlightAndCapture(row, col, rowCapture, colCapture)

        rowCapture = srcRow + 1
        colCapture = srcCol - 1
        row = srcRow + 2
        col = srcCol - 2
        safeDrawHighlightAndCapture(row, col, rowCapture, colCapture)

        rowCapture = srcRow + 1
        colCapture = srcCol + 1
        row = srcRow + 2
        col = srcCol + 2
        safeDrawHighlightAndCapture(row, col, rowCapture, colCapture)
    }

    private fun movePiece() {
        val piece = board[srcRow][srcCol]
        board[srcRow][srcCol] = 0
        board[desRow][desCol] = piece
    }
    private fun removePiece(row: Int, col: Int) {
        board[row][col] = 0
    }
    private fun betweenRow(): Int {
        return ((srcRow + desRow) / 2)
    }
    private fun betweenCol(): Int {
        return (srcCol + desCol) / 2
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?: return false
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!gameover) {
                    val row = (event.y / squareHeight).toInt()
                    val col = (event.x / squareWidth).toInt()

                    if (srcMode) {
                        playerUp.start()

                        srcRow = row
                        srcCol = col
                        srcMode = false

                        invalidate()
                    } else {
                        playerDown.start()

                        desRow = row
                        desCol = col
                        srcMode = true

                        var moveDone = false
                        if (legalMove()) {
                            movePiece()
                            switchTurn()
                            moveDone = true

                            Log.d(TAG, "move [$srcRow $srcCol] -> [$desRow $desCol]")
                            logBoard()
                        } else if (legalCapture()) {
                            movePiece()
                            removePiece(betweenRow(), betweenCol())
                            switchTurn()
                            moveDone = true

                            Log.d(TAG, "capture [$srcRow $srcCol] -> [$desRow $desCol]")
                            logBoard()
                        } else {
                            Log.d(TAG, "illegal move")
                        }

                        if (moveDone) {
                            // check for promotions
                            for (i in 1..7 step 2) {
                                if (board[0][i] == 1) {
                                    board[0][i] = 3
                                }
                            }

                            for (i in 0..6 step 2) {
                                if (board[7][i] == 2) {
                                    board[7][i] = 4
                                }
                            }

                            // check for winner
                            var whitePieceCount = 0
                            var blackPieceCount = 0
                            for (i in 0..7) {
                                for (j in 0..7) {
                                    if ((i + j) % 2 == 1) {
                                        if (board[i][j] == 1 || board[i][j] == 3) {
                                            whitePieceCount++
                                        } else if (board[i][j] == 2 || board[i][j] == 4) {
                                            blackPieceCount++
                                        }
                                    }
                                }
                            }

                            if (whitePieceCount == 0 || blackPieceCount == 0) {
                                gameover = true
                                val winner = if (whitePieceCount == 0) 2 else 1
                                val message = if (winner == 1) "White Player Wins!" else "Black Player Wins!"
                                title!!.text = message

                                val result = db?.rawQuery("SELECT * FROM statistics", null)
                                var gamesPlayed: String = "0"
                                var whiteWins: String = "0"
                                var blackWins: String = "0"
                                var draws: String = "0"
                                if (result!!.moveToLast()) {
                                    gamesPlayed = result.getString(0)
                                    whiteWins = result.getString(1)
                                    blackWins = result.getString(2)
                                    draws = result.getString(3)
                                }

                                gamesPlayed = (Integer.parseInt(gamesPlayed) + 1).toString()
                                if (winner == 1) {
                                    whiteWins = (Integer.parseInt(whiteWins) + 1).toString()
                                } else if (winner == 2) {
                                    blackWins = (Integer.parseInt(blackWins) + 1).toString()
                                }

                                db?.execSQL("UPDATE statistics SET games_played = $gamesPlayed, white_wins=$whiteWins, black_wins=$blackWins, draws=$draws")
                                Log.d(TAG, "$gamesPlayed $whiteWins $blackWins $draws")
                            }
                        }

                        invalidate()
                    }
                }
            }
        }

        return true
    }

    private fun switchTurn() {
        if (turn == 1) {
            turn = 2
            notTurn = 1
            title!!.text = "Black Player's Turn"
        } else if (turn == 2) {
            turn = 1
            notTurn = 2
            title!!.text = "White Player's Turn"
        }
    }

    private fun srcPieceCorrect(): Boolean {
        return (board[srcRow][srcCol] == turn) || (board[srcRow][srcCol] == turn + 2)
    }
    private fun desTileEmpty(): Boolean {
        return board[desRow][desCol] == 0
    }
    private fun legalMove(): Boolean {
        var movementCorrect = false
        val rowDifference = if (turn == 1) 1 else -1
        if (board[srcRow][srcCol] == turn) {
            movementCorrect = (srcRow - desRow == rowDifference) && (abs(srcCol - desCol) == 1)
        } else {
            movementCorrect = (abs(srcRow - desRow) == 1) && (abs(srcCol - desCol) == 1)
        }

        return srcPieceCorrect() && desTileEmpty() && movementCorrect
    }
    private fun legalCapture(): Boolean {
        var movementCorrect = false
        var capturedPieceCorrect = false

        if (turn == 1) {
            movementCorrect = (abs(srcRow - desRow) == 2) && (abs(srcCol - desCol) == 2)
        } else if (turn == 2) {
            movementCorrect = (abs(srcRow - desRow) == 2) && (abs(srcCol - desCol) == 2)
        }

        if (movementCorrect) {
            capturedPieceCorrect = (board[betweenRow()][betweenCol()] == notTurn) || (board[betweenRow()][betweenCol()] == notTurn + 2)
        }

        return srcPieceCorrect() && desTileEmpty() && movementCorrect && capturedPieceCorrect
    }
}