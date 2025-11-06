package cr.ac.utn.census

import android.app.Application
import cr.ac.utn.census.data.PersonRepository
import cr.ac.utn.census.data.local.CensusDatabase

class CensusApplication : Application() {
    val database: CensusDatabase by lazy { CensusDatabase.getInstance(this) }
    val repository: PersonRepository by lazy { PersonRepository(database.personDao()) }
}
