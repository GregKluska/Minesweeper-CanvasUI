package com.gregkluska.minesweepercanvasui

import android.util.Log
import kotlin.random.Random
import kotlin.random.nextInt

data class Game(
    val state: State = State.Welcome,
    val options: Options = Options(),
    var fields: List<List<Field>> = listOf()
) {

    val _fields: MutableList<MutableList<Field>>
        get() = fields.map { it.toMutableList() }.toMutableList()

    init {
        if (options.mines > (options.rows * options.columns)) {
            throw IllegalArgumentException("Too many mines. Maximum value is rows*columns")
        }

        val mines = randomMines(options = options)

        if (fields.isEmpty()) {
            // Generate fields
            fields = MutableList(options.rows) { row ->
                MutableList(options.columns) { col ->
                    val index = (10 * row + col ) // TODO: Should that be -1 ?
                    Field(
                        x = col,
                        y = row,
                        state = FieldState.Close,
                        mine = index in mines,
                        adjacentMines = 0,
                    )
                }
            }

            // Get adjacent mines
            fields.forEachIndexed { y, row ->
                row.forEachIndexed { x, field ->
                    val mutableFields = _fields.toMutableList()

                    mutableFields[y][x] = field.copy(
                        adjacentMines = getAdjacentMines(x, y)
                    )
                    fields = mutableFields
                }
            }
        }

    }

    /**
     * Get fields after opening specific field
     *
     * Using flood fill algorithm
     */
    fun openField(x: Int, y: Int): List<List<Field>> {
        val fields = this._fields
        openField(x = x, y = y, fields = fields)

        return fields
    }

    private fun openField(
        x: Int,
        y: Int,
        fields: MutableList<MutableList<Field>>
    ) {
        if ((fields[y][x].state == FieldState.Close) || (fields[y][x].state == FieldState.Flag)) {
            // Only do anything if the field is closed

            fields[y][x] = fields[y][x].copy(state = FieldState.Open)
            Log.d("OPENING", "x: $x - y: $y")

            // If there's a mine, do nothing
            if (!fields[y][x].mine) {
                // Otherwise
                if (fields[y][x].adjacentMines == 0) {
                    // If the field is empty (0 fields around)
                    // Open all fields around
                    getFieldOrNull(x - 1, y - 1)?.let { openField(it.x, it.y, fields) }
                    getFieldOrNull(x, y - 1)?.let { openField(it.x, it.y, fields) }
                    getFieldOrNull(x + 1, y - 1)?.let { openField(it.x, it.y, fields) }
                    getFieldOrNull(x - 1, y)?.let { openField(it.x, it.y, fields) }
                    getFieldOrNull(x + 1, y)?.let { openField(it.x, it.y, fields) }
                    getFieldOrNull(x - 1, y + 1)?.let { openField(it.x, it.y, fields) }
                    getFieldOrNull(x, y + 1)?.let { openField(it.x, it.y, fields) }
                    getFieldOrNull(x + 1, y + 1)?.let { openField(it.x, it.y, fields) }
                }
            }
        }
    }

    /**
     * Count how many mines is around the field
     *
     * @return Adjacent mines
     */
    private fun getAdjacentMines(x: Int, y: Int): Int {
        var count = 0

        getFieldOrNull(x - 1, y - 1)?.let { if (it.mine) count += 1 }
        getFieldOrNull(x, y - 1)?.let { if (it.mine) count += 1 }
        getFieldOrNull(x + 1, y - 1)?.let { if (it.mine) count += 1 }
        getFieldOrNull(x - 1, y)?.let { if (it.mine) count += 1 }
        getFieldOrNull(x + 1, y)?.let { if (it.mine) count += 1 }
        getFieldOrNull(x - 1, y + 1)?.let { if (it.mine) count += 1 }
        getFieldOrNull(x, y + 1)?.let { if (it.mine) count += 1 }
        getFieldOrNull(x + 1, y + 1)?.let { if (it.mine) count += 1 }

        return count
    }

    private fun getFieldOrNull(x: Int, y: Int): Field? {
        return fields.getOrNull(y)?.getOrNull(x)
    }

    companion object {

        /**
         * Generate set of n mines in field of x rows and y columns
         *
         * @param rows number of rows
         * @param columns number of columns
         * @param mineCount number of mines
         * @return Set of mines
         */
        private fun randomMines(options: Options): Set<Int> {
            val mines = HashSet<Int>(options.mines)

            while (mines.size < options.mines) {
                mines += Random.nextInt(range = 0 until (options.rows * options.columns))
            }

            return mines
        }


    }

    data class Field(
        val x: Int,
        val y: Int,
        val state: FieldState,
        val mine: Boolean,
        val adjacentMines: Int
    )

    enum class FieldState { Open, Close, Flag }

    enum class State { Welcome, Running, Win, GameOver }
}

data class Options(
    val rows: Int = 10,
    val columns: Int = 10,
    val mines: Int = 10
) {
    init {
        if (rows < 1) throw IllegalArgumentException("Rows cannot be less than 1")
        if (columns < 1) throw IllegalArgumentException("Columns cannot be less than 1")
        if (mines > (rows * columns)) {
            throw IllegalArgumentException("Too many mines. Maximum value is rows*columns")
        }

    }
}