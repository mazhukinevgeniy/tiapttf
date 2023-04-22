package homemade.game.model.combo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ComboEffectTest {

    @Test
    fun comboEffectPrices() {
        assertEquals(ComboEffect.values().size, ComboEffect.values().toSet().size) {
            "duplicate prices would break ComboEffectVendor"
        }
    }
}
