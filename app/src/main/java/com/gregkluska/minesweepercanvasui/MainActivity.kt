package com.gregkluska.minesweepercanvasui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val fields = remember {
                mutableStateOf(
                    List<List<Game.Field>>(10) { y ->
                        List<Game.Field>(10) { x ->
                            Game.Field(
                                x = x,
                                y = y,
                                state = Game.FieldState.Close,
                                mine = false,
                                adjacentMines = 0
                            )
                        }
                    }
                )

            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Board(
                    rows = 10,
                    columns = 10,
                    fields = fields.value,
                    onClick = { x, y ->
                        val newFields = fields.value.toMutableList()
                        val newColumn = newFields[y].toMutableList()
                        newColumn[x] = newColumn[x].copy(state = Game.FieldState.Open)
                        newFields[y] = newColumn
                        fields.value = newFields
                        println("PRESSED X: $x     Y: $y")
                    }
                )
            }
        }
    }
}
