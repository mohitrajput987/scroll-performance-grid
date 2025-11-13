package com.otp.scrollperformancegride.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otp.scrollperformancegride.data.Row
import com.otp.scrollperformancegride.data.Square
import kotlin.random.Random

class MainViewModel : ViewModel() {

    companion object {
        private const val TOTAL_SQUARES = 10000
        private const val MIN_SQUARES_PER_ROW = 1
        private const val MAX_SQUARES_PER_ROW = 100
        private const val COLOR_COMPONENT_RANGE = 256
    }

    private val _rows = MutableLiveData<MutableList<Row>>()
    val rows: LiveData<MutableList<Row>> = _rows

    init {
        if (_rows.value == null) {
            generateRows()
        }
    }

    private fun generateRows() {
        val newRows = mutableListOf<Row>()
        var squareCount = 0
        var rowId = 0
        while (squareCount < TOTAL_SQUARES) {
            val squaresInRow = Random.nextInt(MIN_SQUARES_PER_ROW, MAX_SQUARES_PER_ROW + 1)
            val squares = mutableListOf<Square>()
            repeat(squaresInRow) {
                if (squareCount < TOTAL_SQUARES) {
                    squares.add(Square(id = squareCount++, color = getRandomColor()))
                }
            }
            newRows.add(Row(id = rowId++, squares = squares))
        }
        _rows.value = newRows
    }

    fun removeSquare(row: Row, square: Square) {
        val currentRows = _rows.value ?: return
        val newRows = currentRows.toMutableList()
        val rowIndex = newRows.indexOfFirst { it.id == row.id }
        if (rowIndex == -1) return

        val oldRow = newRows[rowIndex]
        val newSquares = oldRow.squares.toMutableList()
        newSquares.remove(square)

        if (newSquares.isEmpty()) {
            newRows.removeAt(rowIndex)
        } else {
            newRows[rowIndex] = oldRow.copy(squares = newSquares)
        }
        _rows.value = newRows
    }

    private fun getRandomColor(): Int {
        return Color.rgb(Random.nextInt(COLOR_COMPONENT_RANGE), Random.nextInt(COLOR_COMPONENT_RANGE), Random.nextInt(COLOR_COMPONENT_RANGE))
    }
}