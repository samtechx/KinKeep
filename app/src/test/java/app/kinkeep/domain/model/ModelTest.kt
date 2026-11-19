package app.kinkeep.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ModelTest {

    @Test
    fun `null score means NEW`() {
        assertEquals(HealthCategory.NEW, HealthCategory.fromScore(null))
    }

    @Test
    fun `category boundaries`() {
        assertEquals(HealthCategory.THRIVING, HealthCategory.fromScore(100))
        assertEquals(HealthCategory.THRIVING, HealthCategory.fromScore(85))
        assertEquals(HealthCategory.HEALTHY, HealthCategory.fromScore(84))
        assertEquals(HealthCategory.HEALTHY, HealthCategory.fromScore(65))
        assertEquals(HealthCategory.NEEDS_ATTENTION, HealthCategory.fromScore(64))
        assertEquals(HealthCategory.NEEDS_ATTENTION, HealthCategory.fromScore(45))
        assertEquals(HealthCategory.FADING, HealthCategory.fromScore(44))
        assertEquals(HealthCategory.FADING, HealthCategory.fromScore(25))
        assertEquals(HealthCategory.DORMANT, HealthCategory.fromScore(24))
        assertEquals(HealthCategory.DORMANT, HealthCategory.fromScore(0))
    }

    @Test
    fun `circle default cadences`() {
        assertEquals(7, Circle.INNER.defaultCadenceDays)
        assertEquals(14, Circle.CLOSE.defaultCadenceDays)
        assertEquals(30, Circle.FRIENDS.defaultCadenceDays)
        assertEquals(90, Circle.ACQUAINTANCES.defaultCadenceDays)
        assertNull(Circle.IGNORE.defaultCadenceDays)
    }

    @Test
    fun `custom cadence overrides circle default`() {
        val base = Person(1, "A", Circle.FRIENDS, null, ContactMethod.CALL, muted = false, pinned = false)
        assertEquals(30, base.cadenceDays)
        assertEquals(10, base.copy(customCadenceDays = 10).cadenceDays)
        assertNull(base.copy(circle = Circle.IGNORE).cadenceDays)
    }
}
