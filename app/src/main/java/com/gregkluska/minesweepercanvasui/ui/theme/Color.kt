package com.gregkluska.minesweepercanvasui.ui.theme

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

/** Minesweeper colors */
val GreenLight = Color(0xFFaad751)
val GreenDark = Color(0xFF4a752c)
val Green = Color(0xFFa2d149)
val Brown = Color(0xFFd7b899)
val BrownLight = Color(0xFFe5c29f)

/** Mines */
val Orange = Color(0xFFf4840d)
val OrangeLight = Color(0xFFf4c20d)
val Blue = Color(0xFF4885ed)
val BlueLight = Color(0xFF48e6f1)
val Red = Color(0xFFdb3236)
val Purple = Color(0xFFb648f2)
val Pink = Color(0xFFed44b5)
val GreenAlt = Color(0xFF008744)
val MineColor = Color(0x59000000)

data class FieldColorScheme(
    val default: Color,
    val revealed: Color,
)

val lightFieldColor = FieldColorScheme(
    default = GreenLight,
    revealed = BrownLight
)

val darkFieldColor = FieldColorScheme(
    default = Green,
    revealed = Brown
)