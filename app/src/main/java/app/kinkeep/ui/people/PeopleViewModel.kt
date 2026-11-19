package app.kinkeep.ui.people

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kinkeep.domain.model.Person
import app.kinkeep.domain.repository.PersonRepository
import app.kinkeep.ui.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PeopleViewModel @Inject constructor(
    people: PersonRepository,
) : ViewModel() {
    val people: StateFlow<List<Person>> = people.observePeople()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    people: PersonRepository,
) : ViewModel() {
    private val personId: Long = savedStateHandle.get<Long>(Route.PersonDetail.ARG_ID) ?: 0L

    val person: StateFlow<Person?> = people.observePerson(personId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
