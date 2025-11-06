package cr.ac.utn.census.ui.personform

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import cr.ac.utn.census.CensusApplication
import cr.ac.utn.census.R
import cr.ac.utn.census.data.local.PersonEntity

class AddEditPersonActivity : AppCompatActivity() {

    private lateinit var viewModel: PersonFormViewModel
    private var personId: Long = 0

    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var lastNameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout

    private lateinit var firstNameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit_person)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        personId = intent.getLongExtra(EXTRA_PERSON_ID, 0)

        val repository = (application as CensusApplication).repository
        viewModel = ViewModelProvider(
            this,
            PersonFormViewModel.Factory(repository)
        )[PersonFormViewModel::class.java]

        bindViews()
        observeViewModel()

        if (personId != 0L) {
            title = getString(R.string.title_edit_person)
            viewModel.observePerson(personId).observe(this) { person ->
                person?.let { populateForm(it) }
            }
        } else {
            title = getString(R.string.title_add_person)
        }

        findViewById<android.view.View>(R.id.buttonSavePerson).setOnClickListener {
            if (validateForm()) {
                savePerson()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun bindViews() {
        firstNameLayout = findViewById(R.id.layoutFirstName)
        lastNameLayout = findViewById(R.id.layoutLastName)
        emailLayout = findViewById(R.id.layoutEmail)
        phoneLayout = findViewById(R.id.layoutPhone)

        firstNameInput = findViewById(R.id.inputFirstName)
        lastNameInput = findViewById(R.id.inputLastName)
        emailInput = findViewById(R.id.inputEmail)
        phoneInput = findViewById(R.id.inputPhone)
    }

    private fun observeViewModel() {
        viewModel.saveResult.observe(this) { event ->
            val result = event.getContentIfNotHandled() ?: return@observe
            if (result) {
                Toast.makeText(this, R.string.message_saved_success, Toast.LENGTH_LONG).show()
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { event ->
            val message = event.getContentIfNotHandled() ?: return@observe
            Snackbar.make(firstNameLayout, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun populateForm(person: PersonEntity) {
        firstNameInput.setText(person.firstName)
        lastNameInput.setText(person.lastName)
        emailInput.setText(person.email)
        phoneInput.setText(person.phone)
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val firstName = firstNameInput.text?.toString()?.trim().orEmpty()
        val lastName = lastNameInput.text?.toString()?.trim().orEmpty()
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val phone = phoneInput.text?.toString()?.trim().orEmpty()

        if (firstName.isEmpty()) {
            firstNameLayout.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            firstNameLayout.error = null
        }

        if (lastName.isEmpty()) {
            lastNameLayout.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            lastNameLayout.error = null
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = getString(R.string.error_invalid_email)
            isValid = false
        } else {
            emailLayout.error = null
        }

        if (phone.isEmpty()) {
            phoneLayout.error = getString(R.string.error_required_field)
            isValid = false
        } else {
            phoneLayout.error = null
        }

        return isValid
    }

    private fun savePerson() {
        val person = PersonEntity(
            id = personId,
            firstName = firstNameInput.text?.toString()?.trim().orEmpty(),
            lastName = lastNameInput.text?.toString()?.trim().orEmpty(),
            email = emailInput.text?.toString()?.trim().orEmpty(),
            phone = phoneInput.text?.toString()?.trim().orEmpty()
        )
        viewModel.savePerson(person)
    }

    companion object {
        const val EXTRA_PERSON_ID = "extra_person_id"
    }
}
