package com.example.census.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.census.controller.PersonController
import com.example.census.entity.Person
import com.example.census.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Screen used to create or edit a [Person].
 */
class PersonFormActivity : AppCompatActivity() {

    private lateinit var controller: PersonController
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var ageInputLayout: TextInputLayout
    private lateinit var nameEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var genderSpinner: Spinner
    private lateinit var saveButton: Button

    private var editingPersonId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_form)

        controller = PersonController(this)

        nameInputLayout = findViewById(R.id.nameInputLayout)
        ageInputLayout = findViewById(R.id.ageInputLayout)
        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        genderSpinner = findViewById(R.id.genderSpinner)
        saveButton = findViewById(R.id.saveButton)

        setupGenderSpinner()
        setupPersonData()
        setupSaveButton()
    }

    private fun setupGenderSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.gender_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }
    }

    private fun setupPersonData() {
        val personId = intent.getIntExtra(EXTRA_PERSON_ID, INVALID_PERSON_ID)
        if (personId != INVALID_PERSON_ID) {
            try {
                val person = controller.getById(personId)
                editingPersonId = person.id
                nameEditText.setText(person.name)
                ageEditText.setText(person.age.toString())
                val genderPosition = resources.getStringArray(R.array.gender_options)
                    .indexOfFirst { it.equals(person.gender, ignoreCase = true) }
                genderSpinner.setSelection(if (genderPosition >= 0) genderPosition else 0)
                title = getString(R.string.title_person_form_edit)
                saveButton.setText(R.string.action_update)
            } catch (exception: Exception) {
                showInfoDialog(exception.message ?: getString(R.string.error_person_not_found))
                finish()
            }
        } else {
            title = getString(R.string.title_person_form_create)
            saveButton.setText(R.string.action_save)
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val person = validateInputs() ?: return@setOnClickListener
            val isUpdate = editingPersonId != null
            showConfirmationDialog(person, isUpdate)
        }
    }

    /** Validates the form fields and returns a [Person] when successful. */
    private fun validateInputs(): Person? {
        val name = nameEditText.text?.toString()?.trim().orEmpty()
        val ageText = ageEditText.text?.toString()?.trim().orEmpty()
        val selectedGenderIndex = genderSpinner.selectedItemPosition

        nameInputLayout.error = null
        ageInputLayout.error = null

        if (name.isEmpty()) {
            nameInputLayout.error = getString(R.string.error_empty_name)
            showInfoDialog(getString(R.string.error_empty_name))
            return null
        }

        if (ageText.isEmpty()) {
            ageInputLayout.error = getString(R.string.error_empty_age)
            showInfoDialog(getString(R.string.error_empty_age))
            return null
        }

        val age = ageText.toIntOrNull()
        if (age == null || age <= 0) {
            ageInputLayout.error = getString(R.string.error_invalid_age)
            showInfoDialog(getString(R.string.error_invalid_age))
            return null
        }

        if (selectedGenderIndex <= 0) {
            showInfoDialog(getString(R.string.error_invalid_gender))
            return null
        }

        val gender = genderSpinner.getItemAtPosition(selectedGenderIndex).toString()
        val id = editingPersonId ?: 0
        return Person(id = id, name = name, age = age, gender = gender)
    }

    /** Requests user confirmation before saving or updating data. */
    private fun showConfirmationDialog(person: Person, isUpdate: Boolean) {
        val titleRes = if (isUpdate) R.string.dialog_confirm_update_title else R.string.dialog_confirm_save_title
        val messageRes = if (isUpdate) R.string.dialog_confirm_update_message else R.string.dialog_confirm_save_message

        AlertDialog.Builder(this)
            .setTitle(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(R.string.dialog_confirm_positive) { _, _ ->
                performPersistence(person, isUpdate)
            }
            .setNegativeButton(R.string.dialog_confirm_negative, null)
            .show()
    }

    /** Persists the current form data and shows feedback to the user. */
    private fun performPersistence(person: Person, isUpdate: Boolean) {
        try {
            if (isUpdate) {
                controller.update(person)
                Toast.makeText(this, R.string.message_person_updated, Toast.LENGTH_SHORT).show()
            } else {
                controller.add(person)
                Toast.makeText(this, R.string.message_person_saved, Toast.LENGTH_SHORT).show()
            }
            finish()
        } catch (exception: Exception) {
            showInfoDialog(exception.message ?: getString(R.string.error_general_operation))
        }
    }

    /** Shows an informational dialog with the provided [message]. */
    private fun showInfoDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_info_title)
            .setMessage(message)
            .setPositiveButton(R.string.dialog_confirm_positive, null)
            .show()
    }

    companion object {
        const val EXTRA_PERSON_ID = "extra_person_id"
        private const val INVALID_PERSON_ID = -1
    }
}
