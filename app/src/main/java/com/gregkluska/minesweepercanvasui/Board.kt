package com.gregkluska.minesweepercanvasui

import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.helper.widget.Layer
import com.gregkluska.minesweepercanvasui.ui.theme.*

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

            // Brown Fields
            for (row in 0 until rows) {
                for (x in 0 until columns) {
                    val column = if (row.rem(2) == 0) x else (columns - x - 1)
                    val field = fields[row][column]
                    val colorScheme = if (counter.rem(2) == 0) darkFieldColor else lightFieldColor

                    val bounds = RectF(
                        column * fieldSize,
                        row * fieldSize,
                        (column * fieldSize) + fieldSize,
                        (row * fieldSize) + fieldSize
                    )

                    // Field
                    drawRect(
                        color = colorScheme.revealed,
                        topLeft = Offset(x = bounds.left, y = bounds.top),
                        size = Size(width = fieldSize, height = fieldSize)
                    )

                    // Number
                    if (field.adjacentMines != 0 || (field.adjacentMines != 0 && !field.mine)) {
                        drawIntoCanvas { canvas ->

                            val textPaint = TextPaint()
                            textPaint.color = Orange.toArgb()
                            textPaint.textSize = fieldSize * 0.6F
                            textPaint.textAlign = Paint.Align.CENTER

                            val textHeight = textPaint.descent() - textPaint.ascent()
                            val textOffset = textHeight / 2 - textPaint.descent()

                            canvas.nativeCanvas.drawText(
                                field.adjacentMines.toString(),
                                bounds.centerX(),
                                bounds.centerY() + textOffset,
                                textPaint
                            )
                        }
                    }
                    counter += 1
                }
            }

            // Outline Fields, each field is a bit larger (2F)
            for (row in 0 until rows) {
                for (x in 0 until columns) {
                    val column = if (row.rem(2) == 0) x else (columns - x - 1)
                    val field = fields[row][column]

                    if(field.state != Game.FieldState.Open) {
                        val bounds = RectF(
                            column * fieldSize -5,
                            row * fieldSize -5,
                            (column * fieldSize) + fieldSize +5,
                            (row * fieldSize) + fieldSize +5
                        )
                        // Field
                        drawRect(
                            color = GreenBorder,
                            topLeft = Offset(x = bounds.left, y = bounds.top),
                            size = Size(width = fieldSize+10, height = fieldSize+10)
                        )
                    }
                }
            }
            // Green Fields
            counter = 0
            for (row in 0 until rows) {
                for (x in 0 until columns) {

                    val column = if (row.rem(2) == 0) x else (columns - x - 1)
                    val field = fields[row][column]
                    val colorScheme = if (counter.rem(2) == 0) darkFieldColor else lightFieldColor

                    if(field.state != Game.FieldState.Open) {
                        val bounds = RectF(
                            column * fieldSize,
                            row * fieldSize,
                            (column * fieldSize) + fieldSize,
                            (row * fieldSize) + fieldSize
                        )

                        // Just a field
                        drawRect(
                            color = colorScheme.default,
                            topLeft = Offset(x = bounds.left, y = bounds.top),
                            size = Size(width = fieldSize, height = fieldSize)
                        )
                    }

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
            rows = 10,
            columns = 10,
            listOf(),
            onClick = { x, y -> println("PRESSED X: $x     Y: $y") })
    }

}