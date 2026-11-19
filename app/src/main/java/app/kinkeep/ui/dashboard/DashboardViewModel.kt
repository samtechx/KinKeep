package app.kinkeep.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.kinkeep.domain.model.Circle
import app.kinkeep.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(val peopleCount: Int = 0)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val people: PersonRepository,
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = people.observePersonCount()
        .map { DashboardUiState(peopleCount = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    /** Debug-build helper that proves the encrypted database reads and writes end to end. */
    fun addDemoPerson() {
        viewModelScope.launch {
            val next = uiState.value.peopleCount + 1
            people.add("Demo person $next", Circle.FRIENDS)
        }
    }
}
