package com.gregkluska.minesweepercanvasui


sealed class Event {

    object Intro : Event()

    /**
     * Start the winning animation. Reveal last (x, y) field
     */
    data class Win(
        val x: Int,
        val y: Int
    ) : Event()

    /**
     * Start the game over animation starting with (x,y) field
     */
    data class GameOver(
        val x: Int,
        val y: Int
    ) : Event()

    data class OptionsUpdate(
        val options: Options
    ) : Event()

    data class Click(
        val x: Int,
        val y: Int
    ) : Event()

    data class LongClick(
        val x: Int,
        val y: Int
    ) : Event()
}