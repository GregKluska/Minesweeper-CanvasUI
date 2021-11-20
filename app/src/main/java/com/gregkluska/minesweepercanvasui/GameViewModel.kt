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
        dispatch(Event.Welcome)
    }

    fun dispatch(event: Event) {
        when (event) {
            Event.Welcome -> onWelcome()
            Event.Start -> onStart()
            Event.Reset -> onReset()
            Event.GameOver -> onGameOver()
            is Event.OptionsUpdate -> onOptionsUpdate(event.options)
            is Event.Click -> onClick(event.x, event.y)
            is Event.LongClick -> onLongClick(event.x, event.y)
        }
    }

    private fun onClick(x: Int, y: Int) {
        Log.d(TAG, "onClick: called with x = $x, y = $y")

        if(game.state != Game.State.Running) {
//            return
            dispatch(Event.Start)
        }

        val field = game.fields[y][x] // IndexOutOfBoundsException

        if(field.state == Game.FieldState.Close) {
            Log.d(TAG, "Field: $field")


            val fields = game.openField(x, y)
            Log.d(TAG, "Fields: $fields")
            dispatchState(
                game.copy(
                    fields = fields,
                    state = if(field.mine) Game.State.GameOver else game.state,
                )
            )
        }
    }

    private fun onLongClick(x: Int, y: Int) {
        Log.d(TAG, "onLongClick: called with x = $x, y = $y")

        if(game.state != Game.State.Running) return
        val currState = game.fields[y][x].state
        if(currState == Game.FieldState.Open) return

        val nextState = if(currState == Game.FieldState.Close) Game.FieldState.Flag else Game.FieldState.Close

        val fields = game._fields
        fields[y][x] = fields[y][x].copy(state = nextState)

        dispatchState(
            game.copy(
                fields = fields
            )
        )

    }

    private fun onWelcome() {
        Log.d(TAG, "onWelcome: called")
        dispatchState(gameState = Game())
    }

    private fun onStart() {
        Log.d(TAG, "onStart: called")
        if (game.state == Game.State.Welcome) {
            dispatchState(gameState = game.copy(state = Game.State.Running))
        }
    }

    private fun onReset() {
        Log.d(TAG, "onReset: called")
        dispatchState(Game(options = game.options))
    }

    private fun onGameOver() {
        Log.d(TAG, "onGameOver: called")
        // Todo: state check. onGameOver can be called on running game only
        if(game.state == Game.State.Running) {
            dispatchState(game.copy(state = Game.State.GameOver))
        }
    }

    private fun onOptionsUpdate(options: Options) {
        Log.d(TAG, "onOptionsUpdate: called with options = $options")
        // It's basically reset with custom options
        dispatchState(gameState = game.copy(state = Game.State.Welcome, options = options))
    }

    private fun dispatchState(gameState: Game) {
        _gameState.value = gameState
    }

}