package homemade.game.model

import homemade.game.model.cellstates.BlockState
import homemade.game.model.cellstates.SimpleState
import homemade.game.model.combo.ComboEffect
import homemade.game.state.GameState
import java.util.stream.IntStream.range
import kotlin.streams.asSequence

class PrettyEncoder {
    fun prettyPrint(state: GameState): String {
        check(state.configState.settings.maxBlockValue < 10)

        val builder = StringBuilder()
        builder.append(state.selectionState.selection?.let { "${it.x},${it.y}" } ?: "none")
        builder.append(state.configState.let { "\n${it.settings.period},${it.spawnsDenied},${it.gameScore},${it.globalMultiplier}" })
        builder.append(state.configState.let {
            "\n${
                if (it.settings.gameMode == GameSettings.GameMode.TURN_BASED) 1 else 0
            },${it.settings.minCombo},${it.settings.spawn},${it.settings.maxBlockValue}"
        })
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

    /**
     * format:
     * [0]selection.x,selection.y or none
     * [1]spawnPeriod,denies,score,multiplier
     * [2]mode,minCombo,spawnCount,maxBlockValue // mode = 1 if turn based, 0 if real time
     * [3+] field in ascii. 1-9 - blocks, . - empty, x - dead, o - spawning
     */
    fun fromPrettyPrint(pretty: String): GameState {
        val lineByLine = pretty.split('\n').filterNot { it == "" }

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

        val height = lineByLine.size - 3
        val width = lineByLine[3].length
        check(range(3, lineByLine.size).allMatch { lineByLine[it].length == width }) { "inconsistent field width" }
        val cellStates = range(3, lineByLine.size).asSequence().flatMap {
            lineByLine[it].map { charCode ->
                when (charCode) {
                    'x' -> SimpleState.get(Cell.DEAD_BLOCK)
                    'o' -> SimpleState.get(Cell.MARKED_FOR_SPAWN)
                    '.' -> SimpleState.get(Cell.EMPTY)

                    else -> {
                        check(charCode.isDigit()) { "not a digit: '$charCode'" }
                        check(charCode.digitToInt() in 1..maxBlockValue)
                        BlockState(charCode.digitToInt(), true, ComboEffect.UNDEFINED_COMBO_EFFECT)
                    }
                }
            }
        }.toList()
        return PlainGameState(
                width = width,
                height = height,
                spawnPeriod = spawnPeriod,
                settings = GameSettings(
                        gameMode = if (mode > 0) GameSettings.GameMode.TURN_BASED else GameSettings.GameMode.REAL_TIME,
                        minCombo = minCombo,
                        spawn = spawnCount,
                        period = spawnPeriod,
                        maxBlockValue = maxBlockValue
                ),
                cellStates = cellStates,
                selection = selection,
                denies = denies,
                score = score,
                multiplier = multiplier
        )
    }
}
