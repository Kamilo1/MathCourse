package pl.wsei.pam

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import pl.wsei.pam.lab01.databinding.ActivityMainBinding
import pl.wsei.pam.lab01.R
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val topicsAdapter = TopicsAdapter { topic ->
        val intent = Intent(this, LessonActivity::class.java)
        startActivity(intent)
    }
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)

        // Setup RecyclerView
        binding.topicsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.topicsRecyclerView.adapter = topicsAdapter

        // Observe topics data
        viewModel.topics.observe(this, Observer { topics ->
            topicsAdapter.submitList(topics)
        })

        // Handle spinner item selection
        binding.departmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDepartment = parent.getItemAtPosition(position).toString()
                viewModel.updateTopics(selectedDepartment)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Initialize spinner with data
        val departmentsAdapter = ArrayAdapter(this, R.layout.spinner_item, viewModel.departments.value!!)
        departmentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.departmentSpinner.adapter = departmentsAdapter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle Home action
            }
            R.id.nav_settings -> {
                // Handle Settings action
            }
            R.id.nav_about -> {
                // Handle About action
            }
            R.id.nav_authors -> {
                val intent = Intent(this, AuthorsActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
