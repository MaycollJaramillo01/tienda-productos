package com.example.census

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.census.Util.ActivityUtils
import com.example.census.ui.PersonFormActivity
import com.example.census.ui.PersonListActivity

/**
 * Entry point of the application. Displays the main menu.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_create_person -> {
                ActivityUtils.openActivity(this, PersonFormActivity::class.java)
                true
            }

            R.id.menu_list_persons -> {
                ActivityUtils.openActivity(this, PersonListActivity::class.java)
                true
            }

            R.id.menu_exit -> {
                showExitConfirmation()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showExitConfirmation() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_exit_title)
            .setMessage(R.string.dialog_exit_message)
            .setPositiveButton(R.string.dialog_confirm_positive) { _, _ -> finishAffinity() }
            .setNegativeButton(R.string.dialog_confirm_negative, null)
            .show()
    }
}
