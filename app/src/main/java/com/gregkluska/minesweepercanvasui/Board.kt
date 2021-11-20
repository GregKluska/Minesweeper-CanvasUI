package com.gregkluska.minesweepercanvasui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.gregkluska.minesweepercanvasui.ui.theme.darkFieldColor
import com.gregkluska.minesweepercanvasui.ui.theme.lightFieldColor

@Composable
fun Board(
    rows: Int,
    columns: Int,
    fields: List<List<Game.Field>>,
    onClick: (x: Int, y: Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {

        val maxWidthDp = with(LocalDensity.current) { maxWidth.toPx() }

        val fieldSizeDp: Dp = maxWidth / columns
        val fieldSize: Float = (maxWidthDp / columns)

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldSizeDp * rows)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val x = offset.x.div(fieldSize)
                            val y = offset.y.div(fieldSize)
                            onClick(x.toInt(), y.toInt())
                        }
                    )
                }
        ) {
            var counter = 0

            for (row in 0 until rows) {
                for (x in 0 until columns) {

                    val column = if (row.rem(2) == 0) x else 9 - x

                    val field = fields[row][column]

                    val colorScheme = if (counter.rem(2) == 0) darkFieldColor else lightFieldColor

                    drawRect(
                        color = if (field.state == Game.FieldState.Open) colorScheme.revealed else colorScheme.default,
                        topLeft = Offset(x = column * fieldSize, y = row * fieldSize),
                        size = Size(width = fieldSize, height = fieldSize)
                    )

                    counter += 1
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
            rows = 10, columns = 10, listOf(), onClick = { x, y -> println("PRESSED X: $x     Y: $y") })
    }

}