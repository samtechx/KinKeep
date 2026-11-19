package app.kinkeep.di

import android.content.Context
import androidx.room.Room
import app.kinkeep.data.db.AppDatabase
import app.kinkeep.data.db.DatabaseKeyProvider
import app.kinkeep.data.db.Migrations
import app.kinkeep.data.db.dao.InteractionDao
import app.kinkeep.data.db.dao.NoteDao
import app.kinkeep.data.db.dao.PersonDao
import app.kinkeep.data.db.dao.PersonPhoneDao
import app.kinkeep.data.db.dao.ReminderDao
import app.kinkeep.data.db.dao.ScoreSnapshotDao
import app.kinkeep.data.db.dao.SyncStateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        keys: DatabaseKeyProvider,
    ): AppDatabase {
        System.loadLibrary("sqlcipher")
        val factory = SupportOpenHelperFactory(keys.passphrase())
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
            .openHelperFactory(factory)
            .addMigrations(*Migrations.ALL)
            .build()
    }

    @Provides fun providePersonDao(db: AppDatabase): PersonDao = db.personDao()

    @Provides fun providePersonPhoneDao(db: AppDatabase): PersonPhoneDao = db.personPhoneDao()

    @Provides fun provideInteractionDao(db: AppDatabase): InteractionDao = db.interactionDao()

    @Provides fun provideScoreSnapshotDao(db: AppDatabase): ScoreSnapshotDao = db.scoreSnapshotDao()

    @Provides fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()

    @Provides fun provideReminderDao(db: AppDatabase): ReminderDao = db.reminderDao()

    @Provides fun provideSyncStateDao(db: AppDatabase): SyncStateDao = db.syncStateDao()
}
