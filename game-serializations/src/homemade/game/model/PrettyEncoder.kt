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
                builder.append(when (cellState.value()) {
                    CellState.UNDEFINED_VALUE -> "."
                    else -> cellState.value().toString()
                })
            }
        }
        return builder.toString()
    }

    fun fromPrettyPrint(pretty: String): GameState {
        TODO()
    }
}
