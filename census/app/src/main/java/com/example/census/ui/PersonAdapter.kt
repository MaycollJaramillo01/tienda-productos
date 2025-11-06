package com.example.census.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.census.Entity.Person
import com.example.census.R

/**
 * Adapter that binds [Person] data into the RecyclerView.
 */
class PersonAdapter(private val listener: PersonInteractionListener) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    private val people = mutableListOf<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(people[position])
    }

    override fun getItemCount(): Int = people.size

    /** Replaces the current dataset with [newPeople]. */
    fun updateData(newPeople: List<Person>) {
        people.clear()
        people.addAll(newPeople)
        notifyDataSetChanged()
    }

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.personNameTextView)
        private val ageTextView: TextView = itemView.findViewById(R.id.personAgeTextView)
        private val genderTextView: TextView = itemView.findViewById(R.id.personGenderTextView)
        private val editButton: ImageButton = itemView.findViewById(R.id.editPersonButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deletePersonButton)

        fun bind(person: Person) {
            nameTextView.text = person.name
            ageTextView.text = itemView.context.getString(R.string.item_age_format, person.age)
            genderTextView.text = itemView.context.getString(R.string.item_gender_format, person.gender)

            editButton.setOnClickListener { listener.onEdit(person) }
            deleteButton.setOnClickListener { listener.onDelete(person) }
        }
    }

    /** Listener used to propagate interactions from the UI. */
    interface PersonInteractionListener {
        fun onEdit(person: Person)
        fun onDelete(person: Person)
    }
}
