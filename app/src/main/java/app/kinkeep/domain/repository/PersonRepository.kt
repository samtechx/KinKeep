package app.kinkeep.domain.repository

import app.kinkeep.domain.model.Circle
import app.kinkeep.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun observePeople(): Flow<List<Person>>

    fun observePerson(id: Long): Flow<Person?>

    fun observePersonCount(): Flow<Int>

    suspend fun add(displayName: String, circle: Circle): Long
}
