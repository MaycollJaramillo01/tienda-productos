package com.example.census.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.census.Controller.PersonController
import com.example.census.Entity.Person
import com.example.census.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Displays the list of registered people.
 */
class PersonListActivity : AppCompatActivity(), PersonAdapter.PersonInteractionListener {

    private lateinit var controller: PersonController
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonAdapter
    private lateinit var emptyStateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_list)

        controller = PersonController(this)

        recyclerView = findViewById(R.id.personRecyclerView)
        emptyStateTextView = findViewById(R.id.emptyStateTextView)
        val addPersonFab: FloatingActionButton = findViewById(R.id.addPersonFab)

        adapter = PersonAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addPersonFab.setOnClickListener { openPersonForm() }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.title_person_list)
    }

    override fun onResume() {
        super.onResume()
        loadPersons()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onEdit(person: Person) {
        openPersonForm(person.id)
    }

    override fun onDelete(person: Person) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_confirm_delete_title)
            .setMessage(getString(R.string.dialog_confirm_delete_message, person.name))
            .setPositiveButton(R.string.dialog_confirm_positive) { _, _ -> deletePerson(person.id) }
            .setNegativeButton(R.string.dialog_confirm_negative, null)
            .show()
    }

    /** Removes a person after the user confirms the action. */
    private fun deletePerson(personId: Int) {
        try {
            controller.delete(personId)
            Toast.makeText(this, R.string.message_person_deleted, Toast.LENGTH_SHORT).show()
            loadPersons()
        } catch (exception: Exception) {
            showErrorDialog(exception.message ?: getString(R.string.error_general_operation))
        }
    }

    /** Loads the current people list and toggles the empty state view. */
    private fun loadPersons() {
        try {
            val people = controller.getAll()
            adapter.updateData(people)
            emptyStateTextView.visibility = if (people.isEmpty()) View.VISIBLE else View.GONE
        } catch (exception: Exception) {
            showErrorDialog(exception.message ?: getString(R.string.error_get_all_persons))
        }
    }

    /** Opens the form screen to create or edit a person. */
    private fun openPersonForm(personId: Int? = null) {
        val intent = Intent(this, PersonFormActivity::class.java)
        personId?.let { intent.putExtra(PersonFormActivity.EXTRA_PERSON_ID, it) }
        startActivity(intent)
    }

    /** Displays an informational dialog describing an error situation. */
    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_info_title)
            .setMessage(message)
            .setPositiveButton(R.string.dialog_confirm_positive, null)
            .show()
    }
}
