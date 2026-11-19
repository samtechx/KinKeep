package app.kinkeep.di

import app.kinkeep.data.repository.PersonRepositoryImpl
import app.kinkeep.domain.repository.PersonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPersonRepository(impl: PersonRepositoryImpl): PersonRepository
}
