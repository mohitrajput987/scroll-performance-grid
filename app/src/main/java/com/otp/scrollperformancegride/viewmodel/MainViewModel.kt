package com.otp.scrollperformancegride.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otp.scrollperformancegride.data.Row
import com.otp.scrollperformancegride.data.Square
import com.otp.scrollperformancegride.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    companion object {
        private const val TOTAL_SQUARES = 10000
        private const val MIN_SQUARES_PER_ROW = 1
        private const val MAX_SQUARES_PER_ROW = 100
        private const val COLOR_COMPONENT_RANGE = 256
    }

    val rows = mutableListOf<Row>()

    private val _onSquareRemoved = MutableLiveData<Event<Pair<Int, Int>>>()
    val onSquareRemoved: LiveData<Event<Pair<Int, Int>>> = _onSquareRemoved

    private val _onRowRemoved = MutableLiveData<Event<Int>>()
    val onRowRemoved: LiveData<Event<Int>> = _onRowRemoved

    private val _onDataGenerated = MutableLiveData<Event<Unit>>()
    val onDataGenerated: LiveData<Event<Unit>> = _onDataGenerated

    init {
        if (rows.isEmpty()) {
            generateRows()
        }
    }

    private fun generateRows() {
        viewModelScope.launch(Dispatchers.IO) {
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
                rows.add(Row(rowId++, squares))
            }
            _onDataGenerated.postValue(Event(Unit))
        }
    }

    fun removeSquare(rowIndex: Int, squareIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (rowIndex < 0 || rowIndex >= rows.size) return@launch
            val row = rows[rowIndex]
            if (squareIndex < 0 || squareIndex >= row.squares.size) return@launch

            row.squares.removeAt(squareIndex)
            _onSquareRemoved.postValue(Event(Pair(rowIndex, squareIndex)))

            if (row.squares.isEmpty()) {
                rows.removeAt(rowIndex)
                _onRowRemoved.postValue(Event(rowIndex))
            }
        }
    }

    private fun getRandomColor(): Int {
        return Color.rgb(Random.nextInt(COLOR_COMPONENT_RANGE), Random.nextInt(COLOR_COMPONENT_RANGE), Random.nextInt(COLOR_COMPONENT_RANGE))
    }
}