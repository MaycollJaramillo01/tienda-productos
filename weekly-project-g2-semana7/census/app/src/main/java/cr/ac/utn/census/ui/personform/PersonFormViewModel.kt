package cr.ac.utn.census.ui.personform

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

class PersonFormViewModel(private val repository: PersonRepository) : ViewModel() {

    private val _saveResult = MutableLiveData<Event<Boolean>>()
    val saveResult: LiveData<Event<Boolean>> = _saveResult

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun observePerson(id: Long): LiveData<PersonEntity?> = repository.getPersonById(id).asLiveData()

    fun savePerson(person: PersonEntity) {
        viewModelScope.launch {
            val result = if (person.id == 0L) {
                repository.insertPerson(person)
            } else {
                repository.updatePerson(person).map { person.id }
            }

            if (result.isSuccess) {
                _saveResult.value = Event(true)
            } else {
                _errorMessage.value = Event(result.exceptionOrNull()?.localizedMessage ?: "Unknown error")
            }
        }
    }

    class Factory(private val repository: PersonRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PersonFormViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PersonFormViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
