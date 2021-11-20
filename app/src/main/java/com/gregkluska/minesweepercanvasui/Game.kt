package com.gregkluska.minesweepercanvasui

class Game {

    data class Field(
        val x: Int,
        val y: Int,
        val state: FieldState,
        val mine: Boolean,
        val adjacentMines: Int
    )

    enum class FieldState { Open, Close, Flag }
}