package com.example.census.Data

import com.example.census.Entity.Person

/**
 * Defines the contract to manage [Person] data regardless of the concrete persistence mechanism.
 */
interface IPersonDataManager {
    /** Stores a new [Person] and returns the persisted instance. */
    fun add(person: Person): Person

    /** Updates an existing [Person]. */
    fun update(person: Person)

    /** Deletes a [Person] by its identifier. */
    fun delete(id: Int)

    /** Retrieves every stored [Person]. */
    fun getAll(): List<Person>

    /** Finds a [Person] by its identifier or returns null when it does not exist. */
    fun getById(id: Int): Person?
}
