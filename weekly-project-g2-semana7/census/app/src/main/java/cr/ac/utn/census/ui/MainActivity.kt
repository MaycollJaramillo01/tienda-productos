package cr.ac.utn.census.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import cr.ac.utn.census.CensusApplication
import cr.ac.utn.census.R
import cr.ac.utn.census.data.local.PersonEntity
import cr.ac.utn.census.ui.about.AboutActivity
import cr.ac.utn.census.ui.personform.AddEditPersonActivity
import cr.ac.utn.census.ui.personlist.PersonListAdapter
import cr.ac.utn.census.ui.personlist.PersonListViewModel
import cr.ac.utn.census.ui.personlist.PersonListViewModel.PersonListMessage

class MainActivity : AppCompatActivity() {

    private lateinit var personAdapter: PersonListAdapter
    private lateinit var viewModel: PersonListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerPersons)
        val emptyStateView: View = findViewById(R.id.emptyState)
        val fab: FloatingActionButton = findViewById(R.id.fabAddPerson)

        val repository = (application as CensusApplication).repository
        viewModel = ViewModelProvider(
            this,
            PersonListViewModel.Factory(repository)
        )[PersonListViewModel::class.java]

        personAdapter = PersonListAdapter { person ->
            showPersonOptions(person)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = personAdapter
        }

        viewModel.persons.observe(this) { persons ->
            personAdapter.submitList(persons)
            emptyStateView.visibility = if (persons.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { event ->
            when (val message = event.getContentIfNotHandled() ?: return@observe) {
                is PersonListMessage.Error ->
                    Snackbar.make(recyclerView, message.message, Snackbar.LENGTH_LONG).show()
                PersonListMessage.Deleted ->
                    Snackbar.make(recyclerView, getString(R.string.message_deleted_success), Snackbar.LENGTH_LONG).show()
            }
        }

        fab.setOnClickListener {
            navigateToAddPerson()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_person -> {
                navigateToAddPerson()
                true
            }
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPersonOptions(person: PersonEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.dialog_person_title, person.fullName))
            .setItems(R.array.person_actions) { _, which ->
                when (which) {
                    0 -> navigateToEditPerson(person.id)
                    1 -> confirmDelete(person)
                }
            }
            .show()
    }

    private fun confirmDelete(person: PersonEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(getString(R.string.dialog_delete_message, person.fullName))
            .setPositiveButton(R.string.action_delete) { _, _ ->
                viewModel.deletePerson(person)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun navigateToAddPerson() {
        startActivity(Intent(this, AddEditPersonActivity::class.java))
    }

    private fun navigateToEditPerson(personId: Long) {
        val intent = Intent(this, AddEditPersonActivity::class.java)
        intent.putExtra(AddEditPersonActivity.EXTRA_PERSON_ID, personId)
        startActivity(intent)
    }
}
