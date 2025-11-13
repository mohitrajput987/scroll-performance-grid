package com.otp.scrollperformancegride.data

data class Square(
    val id: Int,
    val color: Int
)

data class Row(
    val id: Int,
    val squares: MutableList<Square>
)
