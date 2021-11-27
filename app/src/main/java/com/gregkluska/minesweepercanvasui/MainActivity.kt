package com.gregkluska.minesweepercanvasui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import com.gregkluska.minesweepercanvasui.ui.animation.Animatable
import com.gregkluska.minesweepercanvasui.ui.animation.shakeKeyframes

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val gameState = viewModel.gameState.collectAsState()
            val boardOffset = remember { Animatable(Offset(0F, 0F)) }

            LaunchedEffect(gameState.value.state) {
                when(gameState.value.state) {
                    Game.State.Running,
                    Game.State.GameOver -> {
                        boardOffset.animateTo(
                            Offset(0F,0F),
                            shakeKeyframes
                        )
                    }
                    else -> {}
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = boardOffset.value.x
                        translationY = boardOffset.value.y
                    }
            ) {
                Board(
                    rows = gameState.value.options.rows,
                    columns = gameState.value.options.columns,
                    fields = gameState.value.fields,
                    onClick = { x, y ->
                        viewModel.dispatch(Event.Click(x, y))
                    }
                )
                
                Text(text = gameState.value.state.toString())
            }
        }
    }
}
