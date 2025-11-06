package cr.ac.utn.census.data

import cr.ac.utn.census.data.local.PersonDao
import cr.ac.utn.census.data.local.PersonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersonRepository(private val personDao: PersonDao) {

    fun getPersons(): Flow<List<PersonEntity>> = personDao.getPersons()

    fun getPersonById(id: Long): Flow<PersonEntity?> = personDao.getPersonById(id)

    suspend fun insertPerson(person: PersonEntity): Result<Long> = runDbOperation {
        personDao.insert(person)
    }

    suspend fun updatePerson(person: PersonEntity): Result<Unit> = runDbOperation {
        personDao.update(person)
        Unit
    }

    suspend fun deletePerson(person: PersonEntity): Result<Unit> = runDbOperation {
        personDao.delete(person)
        Unit
    }

    private suspend fun <T> runDbOperation(block: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            runCatching { block() }
        }
    }
}
