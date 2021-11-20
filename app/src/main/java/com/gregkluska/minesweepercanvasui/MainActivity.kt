package com.gregkluska.minesweepercanvasui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val gameState = viewModel.gameState.collectAsState()

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                println("recalled")
                Board(
                    rows = gameState.value.options.rows,
                    columns = gameState.value.options.columns,
                    fields = gameState.value.fields,
                    onClick = { x, y ->
                        viewModel.dispatch(Event.Click(x, y))
                    }
                )
            }
        }
    }
}
