package com.example.census.Data

import com.example.census.Entity.Person
import kotlin.math.max

/**
 * In-memory implementation of [IPersonDataManager].
 * It stores data temporarily during the app session.
 */
object MemoryDataManager : IPersonDataManager {
    private val personList = mutableListOf<Person>()
    private var nextId = 1

    @Synchronized
    override fun add(person: Person): Person {
        val assignedId = if (person.id <= 0) nextId else person.id
        if (personList.any { it.id == assignedId }) {
            throw IllegalArgumentException("Duplicated person id")
        }
        val persistedPerson = person.copy(id = assignedId)
        personList.add(persistedPerson)
        nextId = max(nextId, assignedId + 1)
        return persistedPerson.copy()
    }

    @Synchronized
    override fun update(person: Person) {
        val index = personList.indexOfFirst { it.id == person.id }
        if (index == -1) {
            throw NoSuchElementException("Person not found")
        }
        personList[index] = person.copy()
    }

    @Synchronized
    override fun delete(id: Int) {
        val removed = personList.removeIf { it.id == id }
        if (!removed) {
            throw NoSuchElementException("Person not found")
        }
    }

    @Synchronized
    override fun getAll(): List<Person> = personList.map { it.copy() }

    @Synchronized
    override fun getById(id: Int): Person? = personList.firstOrNull { it.id == id }?.copy()
}
