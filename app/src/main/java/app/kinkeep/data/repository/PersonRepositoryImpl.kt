package app.kinkeep.data.repository

import app.kinkeep.data.db.dao.PersonDao
import app.kinkeep.data.db.entity.PersonEntity
import app.kinkeep.domain.model.Circle
import app.kinkeep.domain.model.Person
import app.kinkeep.domain.repository.PersonRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class PersonRepositoryImpl @Inject constructor(
    private val dao: PersonDao,
) : PersonRepository {

    override fun observePeople(): Flow<List<Person>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observePerson(id: Long): Flow<Person?> =
        dao.observe(id).map { it?.toDomain() }

    override fun observePersonCount(): Flow<Int> = dao.observeCount()

    override suspend fun add(displayName: String, circle: Circle): Long =
        dao.insert(
            PersonEntity(
                displayName = displayName,
                circle = circle,
                createdAt = System.currentTimeMillis(),
            ),
        )
}

private fun PersonEntity.toDomain() = Person(
    id = id,
    displayName = displayName,
    circle = circle,
    customCadenceDays = customCadenceDays,
    preferredMethod = preferredMethod,
    muted = muted,
    pinned = pinned,
)
