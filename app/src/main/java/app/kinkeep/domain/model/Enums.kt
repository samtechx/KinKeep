package app.kinkeep.domain.model

/** Relationship circles. [importance] weights the overall health ring; [defaultCadenceDays] is the target gap. */
enum class Circle(val defaultCadenceDays: Int?, val importance: Double) {
    INNER(7, 3.0),
    CLOSE(14, 2.0),
    FRIENDS(30, 1.0),
    ACQUAINTANCES(90, 0.5),
    IGNORE(null, 0.0),
    ;

    val isScored: Boolean get() = this != IGNORE
}

enum class ContactMethod { CALL, SMS, WHATSAPP }

enum class InteractionType { CALL_IN, CALL_OUT, CALL_MISSED, MANUAL, SMS_TAP, WHATSAPP_TAP }

enum class InteractionSource { CALL_LOG, MANUAL, APP_ACTION }

enum class ReminderKind { REACH_OUT, BIRTHDAY, ANNIVERSARY, MISSED_CALL }

enum class ReminderStatus { PENDING, SNOOZED, DONE, DISMISSED }

/** Health bands for a relationship score. NEW means "no history yet, still in the grace period". */
enum class HealthCategory {
    NEW,
    THRIVING,
    HEALTHY,
    NEEDS_ATTENTION,
    FADING,
    DORMANT,
    ;

    companion object {
        const val THRIVING_MIN = 85
        const val HEALTHY_MIN = 65
        const val NEEDS_ATTENTION_MIN = 45
        const val FADING_MIN = 25

        fun fromScore(score: Int?): HealthCategory = when {
            score == null -> NEW
            score >= THRIVING_MIN -> THRIVING
            score >= HEALTHY_MIN -> HEALTHY
            score >= NEEDS_ATTENTION_MIN -> NEEDS_ATTENTION
            score >= FADING_MIN -> FADING
            else -> DORMANT
        }
    }
}
