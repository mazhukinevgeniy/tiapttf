package homemade.game.state.impl

import homemade.game.fieldstructure.FieldStructure
import homemade.game.model.GameSettings
import homemade.game.state.ConfigState
import homemade.game.state.FieldState
import homemade.game.state.getNumberOfBlocks
import homemade.util.PiecewiseConstantFunction
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Let's say field is empty if you can't make a combo,
 * field is saturated if it's not empty and there's plenty of blocks and some free space,
 * field is oversaturated if block percentage is above a certain threshold
 * and field is full if there's no free space.
 *
 * The desirable period is like that:
 * 1) It's little when field is empty;
 * 2) It's manageable when field is saturated, ie you can set up huge combos in time;
 * 3) It will fill the field in the desirable time after the point of oversaturation.
 *
 * Plus, it goes down a bit every time you deny a spawn.
 */
class SpawnPeriod private constructor(private val basePeriod: PiecewiseConstantFunction<Int, Int>) {
    fun getSpawnPeriod(fieldState: FieldState, configState: ConfigState): Int {
        val base = basePeriod.getValueAt(fieldState.getNumberOfBlocks())
        val decrementFromDenies = 15 * configState.spawnsDenied
        return max(MIN_PERIOD, base - decrementFromDenies)
    }

    companion object {
        private const val MIN_PERIOD = 100
        fun newFastStart(fieldStructure: FieldStructure, settings: GameSettings): SpawnPeriod {
            val minCombo = settings.minCombo
            val saturationPoint = minCombo * 3
            val oversaturationPoint = fieldStructure.fieldSize - minCombo * 2
            check(saturationPoint < oversaturationPoint) {
                "minCombo is too big and fieldSize is too small"
            }
            val separators = listOf(
                    saturationPoint / 2,
                    saturationPoint,
                    (oversaturationPoint + saturationPoint) / 2,
                    oversaturationPoint
            )
            val periods = listOf(0.2f, 0.3f, 0.5f, 0.8f, 1.0f).map {
                (it * settings.period).roundToInt()
            }.toList()
            return SpawnPeriod(PiecewiseConstantFunction(separators, periods))
        }
    }
}
