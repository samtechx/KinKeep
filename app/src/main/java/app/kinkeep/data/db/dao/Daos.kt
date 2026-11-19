package app.kinkeep.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.kinkeep.data.db.entity.InteractionEntity
import app.kinkeep.data.db.entity.NoteEntity
import app.kinkeep.data.db.entity.PersonEntity
import app.kinkeep.data.db.entity.PersonPhoneEntity
import app.kinkeep.data.db.entity.ReminderEntity
import app.kinkeep.data.db.entity.ScoreSnapshotEntity
import app.kinkeep.data.db.entity.SyncStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert
    suspend fun insert(person: PersonEntity): Long

    @Update
    suspend fun update(person: PersonEntity)

    @Query("SELECT * FROM person ORDER BY displayName COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM person WHERE id = :id")
    fun observe(id: Long): Flow<PersonEntity?>

    @Query("SELECT COUNT(*) FROM person")
    fun observeCount(): Flow<Int>

    @Query("DELETE FROM person")
    suspend fun deleteAll()
}

@Dao
interface PersonPhoneDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(phones: List<PersonPhoneEntity>): List<Long>

    @Query("SELECT * FROM person_phone WHERE personId = :personId")
    suspend fun forPerson(personId: Long): List<PersonPhoneEntity>

    @Query("SELECT * FROM person_phone WHERE normalized = :normalized LIMIT 1")
    suspend fun findByNumber(normalized: String): PersonPhoneEntity?
}

@Dao
interface InteractionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<InteractionEntity>): List<Long>

    @Query("SELECT * FROM interaction WHERE personId = :personId ORDER BY timestamp DESC")
    fun observeForPerson(personId: Long): Flow<List<InteractionEntity>>

    @Query("SELECT COUNT(*) FROM interaction")
    fun observeCount(): Flow<Int>

    @Query("SELECT MAX(timestamp) FROM interaction WHERE callLogId IS NOT NULL")
    suspend fun latestCallLogTimestamp(): Long?
}

@Dao
interface ScoreSnapshotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ScoreSnapshotEntity>)

    @Query("SELECT * FROM score_snapshot WHERE personId = :personId ORDER BY epochDay ASC")
    fun observeForPerson(personId: Long): Flow<List<ScoreSnapshotEntity>>
}

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Query("SELECT * FROM note WHERE personId = :personId ORDER BY createdAt DESC")
    fun observeForPerson(personId: Long): Flow<List<NoteEntity>>
}

@Dao
interface ReminderDao {
    @Insert
    suspend fun insert(reminder: ReminderEntity): Long

    @Update
    suspend fun update(reminder: ReminderEntity)

    @Query("SELECT * FROM reminder WHERE status IN ('PENDING', 'SNOOZED') ORDER BY fireAt ASC")
    fun observeOpen(): Flow<List<ReminderEntity>>
}

@Dao
interface SyncStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(state: SyncStateEntity)

    @Query("SELECT value FROM sync_state WHERE `key` = :key")
    suspend fun get(key: String): String?
}
