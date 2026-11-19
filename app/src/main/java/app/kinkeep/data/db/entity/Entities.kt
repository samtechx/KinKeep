package app.kinkeep.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.kinkeep.domain.model.Circle
import app.kinkeep.domain.model.ContactMethod
import app.kinkeep.domain.model.HealthCategory
import app.kinkeep.domain.model.InteractionSource
import app.kinkeep.domain.model.InteractionType
import app.kinkeep.domain.model.ReminderKind
import app.kinkeep.domain.model.ReminderStatus

@Entity(
    tableName = "person",
    indices = [Index("circle"), Index("systemContactId")],
)
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val displayName: String,
    val photoUri: String? = null,
    val circle: Circle = Circle.FRIENDS,
    val customCadenceDays: Int? = null,
    val preferredMethod: ContactMethod = ContactMethod.CALL,
    /** ISO date "yyyy-MM-dd", or "--MM-dd" when the year is unknown. */
    val birthday: String? = null,
    val anniversary: String? = null,
    val muted: Boolean = false,
    val pinned: Boolean = false,
    val createdAt: Long,
    val systemContactId: Long? = null,
)

@Entity(
    tableName = "person_phone",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("personId"), Index(value = ["normalized"], unique = true)],
)
data class PersonPhoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    /** E.164-style normalized number; one number belongs to exactly one person. */
    val normalized: String,
    val rawNumber: String,
    val label: String? = null,
)

@Entity(
    tableName = "interaction",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["personId", "timestamp"]),
        Index(value = ["callLogId"], unique = true),
        Index(value = ["timestamp"]),
    ],
)
data class InteractionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /** Null for call-log numbers that are not yet attached to a person ("unknown numbers"). */
    val personId: Long? = null,
    val rawNumber: String? = null,
    val type: InteractionType,
    val timestamp: Long,
    val durationSec: Int = 0,
    val initiatedByMe: Boolean? = null,
    val note: String? = null,
    val source: InteractionSource,
    val callLogId: Long? = null,
)

@Entity(
    tableName = "score_snapshot",
    primaryKeys = ["personId", "epochDay"],
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class ScoreSnapshotEntity(
    val personId: Long,
    val epochDay: Long,
    val score: Int,
    val category: HealthCategory,
    val recency: Int,
    val frequency: Int,
    val balance: Int,
    val missed: Int,
)

@Entity(
    tableName = "note",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("personId")],
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val createdAt: Long,
    val body: String,
    val isTalkAbout: Boolean = false,
    val done: Boolean = false,
)

@Entity(
    tableName = "reminder",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("personId"), Index("fireAt")],
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val kind: ReminderKind,
    val fireAt: Long,
    val snoozedUntil: Long? = null,
    val status: ReminderStatus = ReminderStatus.PENDING,
)

@Entity(tableName = "sync_state")
data class SyncStateEntity(
    @PrimaryKey val key: String,
    val value: String,
)
