package com.gregkluska.minesweepercanvasui


sealed class Event {

    object Welcome : Event()
    object Start : Event()
    object Reset : Event()
    object GameOver : Event()

    data class OptionsUpdate(
        val options: Options
    ): Event()

    data class Click(
        val x: Int,
        val y: Int
    ): Event()

    data class LongClick(
        val x: Int,
        val y: Int
    ): Event()
}