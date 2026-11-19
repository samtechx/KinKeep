package app.kinkeep.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import app.kinkeep.data.db.dao.InteractionDao
import app.kinkeep.data.db.dao.NoteDao
import app.kinkeep.data.db.dao.PersonDao
import app.kinkeep.data.db.dao.PersonPhoneDao
import app.kinkeep.data.db.dao.ReminderDao
import app.kinkeep.data.db.dao.ScoreSnapshotDao
import app.kinkeep.data.db.dao.SyncStateDao
import app.kinkeep.data.db.entity.InteractionEntity
import app.kinkeep.data.db.entity.NoteEntity
import app.kinkeep.data.db.entity.PersonEntity
import app.kinkeep.data.db.entity.PersonPhoneEntity
import app.kinkeep.data.db.entity.ReminderEntity
import app.kinkeep.data.db.entity.ScoreSnapshotEntity
import app.kinkeep.data.db.entity.SyncStateEntity

@Database(
    entities = [
        PersonEntity::class,
        PersonPhoneEntity::class,
        InteractionEntity::class,
        ScoreSnapshotEntity::class,
        NoteEntity::class,
        ReminderEntity::class,
        SyncStateEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao

    abstract fun personPhoneDao(): PersonPhoneDao

    abstract fun interactionDao(): InteractionDao

    abstract fun scoreSnapshotDao(): ScoreSnapshotDao

    abstract fun noteDao(): NoteDao

    abstract fun reminderDao(): ReminderDao

    abstract fun syncStateDao(): SyncStateDao

    companion object {
        const val NAME = "kinkeep.db"
    }
}

/** Add one [Migration] per schema bump here. Never use destructive migration: it would wipe the user's data. */
object Migrations {
    val ALL: Array<Migration> = emptyArray()
}
