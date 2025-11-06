package com.example.census.Controller

import android.content.Context
import com.example.census.Data.IPersonDataManager
import com.example.census.Data.MemoryDataManager
import com.example.census.Entity.Person
import com.example.census.R

/**
 * Coordinates UI requests with the data layer for every operation over [Person].
 */
class PersonController(
    private val context: Context,
    private val dataManager: IPersonDataManager = MemoryDataManager
) {

    /** Registers a new [Person] in the repository. */
    fun add(person: Person): Person = try {
        dataManager.add(person)
    } catch (exception: Exception) {
        throw Exception(context.getString(R.string.error_add_person), exception)
    }

    /** Updates an existing [Person]. */
    fun update(person: Person) {
        try {
            dataManager.update(person)
        } catch (exception: Exception) {
            throw Exception(context.getString(R.string.error_update_person), exception)
        }
    }

    /** Deletes the [Person] with the given [id]. */
    fun delete(id: Int) {
        try {
            dataManager.delete(id)
        } catch (exception: Exception) {
            throw Exception(context.getString(R.string.error_delete_person), exception)
        }
    }

    /** Returns the complete list of registered people. */
    fun getAll(): List<Person> = try {
        dataManager.getAll()
    } catch (exception: Exception) {
        throw Exception(context.getString(R.string.error_get_all_persons), exception)
    }

    /** Gets a [Person] by [id] or throws an exception when it does not exist. */
    fun getById(id: Int): Person = try {
        dataManager.getById(id) ?: throw Exception(context.getString(R.string.error_person_not_found))
    } catch (exception: Exception) {
        throw Exception(context.getString(R.string.error_person_not_found), exception)
    }
}
