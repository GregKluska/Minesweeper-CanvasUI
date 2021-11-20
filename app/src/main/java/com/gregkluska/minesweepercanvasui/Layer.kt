package com.gregkluska.minesweepercanvasui

import androidx.compose.ui.graphics.drawscope.DrawScope

data class DrawAction(
    val draw: DrawScope.() -> Unit
)