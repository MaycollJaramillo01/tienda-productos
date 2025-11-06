package cr.ac.utn.census.ui.personlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cr.ac.utn.census.data.PersonRepository
import cr.ac.utn.census.data.local.PersonEntity
import cr.ac.utn.census.ui.common.Event
import kotlinx.coroutines.launch

class PersonListViewModel(private val repository: PersonRepository) : ViewModel() {

    val persons: LiveData<List<PersonEntity>> = repository.getPersons().asLiveData()

    private val _message = MutableLiveData<Event<PersonListMessage>>()
    val message: LiveData<Event<PersonListMessage>> = _message

    fun deletePerson(person: PersonEntity) {
        viewModelScope.launch {
            val result = repository.deletePerson(person)
            if (result.isFailure) {
                _message.value = Event(
                    PersonListMessage.Error(result.exceptionOrNull()?.localizedMessage ?: "Unknown error")
                )
            } else {
                _message.value = Event(PersonListMessage.Deleted)
            }
        }
    }

    sealed class PersonListMessage {
        data class Error(val message: String) : PersonListMessage()
        object Deleted : PersonListMessage()
    }

    class Factory(private val repository: PersonRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PersonListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PersonListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
