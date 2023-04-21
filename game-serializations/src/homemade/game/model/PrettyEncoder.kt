package homemade.game.model

import homemade.game.state.GameState

class PrettyEncoder {
    fun prettyPrint(state: GameState): String {
        check(state.configState.settings.maxBlockValue < 10)

        val builder = StringBuilder()
        builder.append(state.selectionState.selection?.let { "${it.x},${it.y}" } ?: "none")
        for (row in 0 until state.fieldState.structure.height) {
            builder.append('\n')
            for (col in 0 until state.fieldState.structure.width) {
                val cellCode = state.fieldState.structure.getCellCode(col, row)
                val cellState = state.fieldState.getCellState(cellCode)
                builder.append(when (cellState.type()) {
                    Cell.EMPTY -> "."
                    Cell.OCCUPIED -> cellState.value().toString()
                    Cell.DEAD_BLOCK -> "x"
                    Cell.MARKED_FOR_SPAWN -> "o"
                })
            }
        }
        return builder.toString()
    }

    /*
    *
        val width: Int,
        val height: Int,
        val spawnPeriod: Int,
        val settings: GameSettings,
        val cellStates: List<CellState>,
        val selection: Coordinates?,
        val denies: Int,
        val score: Int,
        val multiplier: Int*/
    /*
    * val gameMode: GameMode = GameMode.TURN_BASED,
        val minCombo: Int = 5,
        val spawn: Int = 4,
        val period: Int = 500,
        val maxBlockValue: Int = 9*/
    /**
     * format:
     * [0]selection.x,selection.y or none
     * [1]spawnPeriod,denies,score,multiplier
     * [2]mode,minCombo,spawnCount,maxBlockValue // mode = 1 if turn based, 0 if real time
     * [3+] field in ascii. 1-9 - blocks, . - empty, x - dead
     */
    fun fromPrettyPrint(pretty: String): GameState {
        val lineByLine = pretty.split('\n')
        check(lineByLine.size >= 4)
        val selection = run {
            if (lineByLine[0] == "none") {
                return@run null
            }
            val (x, y) = lineByLine[0].split(',').map { it.toInt() }
            return@run Coordinates(x, y)
        }
        val (spawnPeriod, denies, score, multiplier) = lineByLine[1].split(',').map { it.toInt() }
        val (mode, minCombo, spawnCount, maxBlockValue) = lineByLine[2].split(',').map { it.toInt() }

    }
}
