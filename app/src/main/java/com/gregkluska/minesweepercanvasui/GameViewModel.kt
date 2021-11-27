package com.gregkluska.minesweepercanvasui


import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    companion object {
        private const val TAG = "GameViewModel"
    }

    private val _gameState: MutableStateFlow<Game> = MutableStateFlow(Game())
    val gameState = _gameState.asStateFlow()

    private val game: Game
        get() = _gameState.value

    init {
        dispatch(Event.Intro)
    }

    fun dispatch(event: Event) {
        when (event) {
            Event.Intro -> onIntro()
            is Event.Win -> onWin(event.x, event.y)
            is Event.GameOver -> onGameOver(event.x, event.y)
            is Event.OptionsUpdate -> onOptionsUpdate(event.options)
            is Event.Click -> onClick(event.x, event.y)
            is Event.LongClick -> onLongClick(event.x, event.y)
        }
    }

    private fun onWin(x: Int, y: Int) {
        val fields = game.openField(x, y)
        dispatchState(
            gameState = game.copy(
                fields = fields,
                state = Game.State.Win
            )
        )
    }

    private fun onClick(x: Int, y: Int) {
        Log.d(TAG, "onClick: called with x = $x, y = $y")

        val field = game.fields[y][x] // IndexOutOfBoundsException

        when(game.state) {
            Game.State.Intro,
            Game.State.Running -> {
                // Only able to open field when it's closed.
                if(field.state == Game.FieldState.Close) {
                    if(field.mine) {
                        dispatch(Event.GameOver(x = x, y = y))
                        return
                    } else {
                        // Count unrevealed fields
                        val fields = game.openField(x, y)

                        val flatFields = fields.flatMap { it.asIterable() }
                        val remainingFields = flatFields.filter { it.state == Game.FieldState.Close }

                        println("Remaining fields: ${remainingFields.size}")

                        if(remainingFields.size == game.options.mines) {
                            dispatch(Event.Win(x = x, y = y))
                            return
                        }

                        dispatchState(
                            game.copy(
                                fields = fields,
                                state = Game.State.Running // Just because current state may be intro
                            )
                        )
                    }
                }
            }
            Game.State.Win,
            Game.State.GameOver -> dispatch(Event.Intro)
        }
    }

    private fun onLongClick(x: Int, y: Int) {
//        Log.d(TAG, "onLongClick: called with x = $x, y = $y")
//
//        if(game.state != Game.State.Running) return
//        val currState = game.fields[y][x].state
//        if(currState == Game.FieldState.Open) return
//
//        val nextState = if(currState == Game.FieldState.Close) Game.FieldState.Flag else Game.FieldState.Close
//
//        val fields = game._fields
//        fields[y][x] = fields[y][x].copy(state = nextState)
//
//        dispatchState(
//            game.copy(
//                fields = fields
//            )
//        )

    }

    private fun onIntro() {
        Log.d(TAG, "onIntro: called")
        dispatchState(gameState = Game(options = game.options))
    }

    private fun onGameOver(x: Int, y: Int) {
        val fields = game.openField(x, y)

        dispatchState(game.copy(
            fields = fields,
            state = Game.State.GameOver
        ))
        // Start animation with bombs
    }

    private fun onOptionsUpdate(options: Options) {
        Log.d(TAG, "onOptionsUpdate: called with options = $options")
        // It's basically reset with custom options
        dispatchState(gameState = game.copy(state = Game.State.Intro, options = options))
    }

    private fun dispatchState(gameState: Game) {
        _gameState.value = gameState
    }

}