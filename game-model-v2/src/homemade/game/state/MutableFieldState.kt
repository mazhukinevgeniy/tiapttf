package homemade.game.state

import homemade.game.fieldstructure.FieldStructure
import homemade.game.state.impl.BlockValuePool
import homemade.game.state.impl.CellMap

class MutableFieldState(
        override val structure: FieldStructure,
        val blockValuePool: BlockValuePool
) : CellMap(structure, blockValuePool) {

}
