package pl.wsei.pam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab01.databinding.ActivityMainBinding

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var switchButton: Switch
    private val sharedPrefFile = "pl.wsei.pam.PREFERENCES"
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        switchButton = findViewById(R.id.switchButton)


        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val isChecked = sharedPreferences.getBoolean("SWITCH_BUTTON_STATE", false)
        switchButton.isChecked = isChecked

        switchButton.setOnCheckedChangeListener { _, isChecked ->

            with(sharedPreferences.edit()) {
                putBoolean("SWITCH_BUTTON_STATE", isChecked)
                apply()
            }
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_settings -> {

            }
            R.id.nav_about -> {



            }
            R.id.nav_authors -> {
                val intent = Intent(this, AuthorsActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
