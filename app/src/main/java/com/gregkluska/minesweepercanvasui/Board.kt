package com.gregkluska.minesweepercanvasui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.gregkluska.minesweepercanvasui.ui.theme.Green
import com.gregkluska.minesweepercanvasui.ui.theme.GreenLight

@Composable
fun Board(
    rows: Int,
    columns: Int,
    onClick: (x: Int, y: Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {

        val maxWidthDp = with(LocalDensity.current) { maxWidth.toPx() }

        val fieldSize: Float = (maxWidthDp / columns).toFloat()

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Green)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            println(offset)
                            println(fieldSize)
                            val x = offset.x.div(fieldSize)
                            val y = offset.y.div(fieldSize)
                            onClick(x.toInt(), y.toInt())
                        }
                    )
                }
        ) {
            var counter = 0

            for(row in 0 until rows) {
                for(x in 0 until columns) {

                    val column = if(row.rem(2) == 0) x else 9-x
                    drawRect(
                        color = if(counter.rem(2)==0) Green else GreenLight,
                        topLeft = Offset(x = column*fieldSize, y = row*fieldSize),
                        size = Size(width = fieldSize, height = fieldSize)
                    )

                    counter +=1
                }
            }

        }
    }
}

@Composable
@Preview
private fun BoardPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Board(
            rows = 15, columns = 10, onClick = {x, y -> println("PRESSED X: $x     Y: $y")})
    }

}