package app.kinkeep.domain.model

data class Person(
    val id: Long,
    val displayName: String,
    val circle: Circle,
    val customCadenceDays: Int?,
    val preferredMethod: ContactMethod,
    val muted: Boolean,
    val pinned: Boolean,
) {
    /** Target gap in days between contacts: custom value wins, else the circle default (null = not scored). */
    val cadenceDays: Int? get() = customCadenceDays ?: circle.defaultCadenceDays
}
