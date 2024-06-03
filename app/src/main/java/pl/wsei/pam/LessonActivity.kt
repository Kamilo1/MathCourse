package pl.wsei.pam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab01.databinding.ActivityLessonBinding

class LessonActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityLessonBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        drawerLayout = binding.drawerLayout

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val pagerAdapter = LessonPagerAdapter(supportFragmentManager, this)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    class LessonPagerAdapter(
        fm: FragmentManager,
        private val context: Context
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val slideTitles = arrayOf(
            "Wprowadzenie", "Kwadrat sumy", "Kwadrat różnicy", "Różnica kwadratów", "Quiz"
        )

        private val db = AppDatabase.getDatabase(context)
        private var score = "Jeszcze nie ukończono"

        init {
            fetchResults()
        }

        private fun fetchResults() {
            GlobalScope.launch(Dispatchers.Main) {
                val results = withContext(Dispatchers.IO) {
                    db?.quizResultDao()?.getResultsByQuizId(1)
                }

                for (result in results!!) {
                    score = "${result?.score} / ${result?.maxScore}"
                }

                notifyDataSetChanged()
            }
        }

        override fun getCount(): Int = slideTitles.size

        override fun getItem(position: Int): Fragment {
            val isLastSlide = position == slideTitles.size - 1
            return when (position) {
                0 -> SlideFragment.newInstance(
                    "Wzory skróconego mnożenia",
                    "Wzory skróconego mnożenia to zestaw reguł, które umożliwiają szybkie i efektywne wykonywanie obliczeń algebraicznych.\nTe wzory pozwalają na uproszczenie równań i wyrażeń, co jest szczególnie przydatne w rozwiązywaniu problemów matematycznych.",
                    isLastSlide
                )
                1 -> SlideFragment.newInstance(
                    "(a + b)² = a² + 2ab + b²",
                    "Definicja: Wzór ten pokazuje, jak rozwinąć kwadrat sumy dwóch liczb.\nPrzykład: Rozwiń (x + 3)².\nx² + 2x3 + 3² = x² + 6x + 9\nIlustracja: Pokaż diagram ilustrujący rozkład kwadratu sumy na poszczególne części.",
                    isLastSlide
                )
                2 -> SlideFragment.newInstance(
                    "(a - b)² = a² - 2ab + b²",
                    "Definicja: Wzór ten pokazuje, jak rozwinąć kwadrat różnicy dwóch liczb.\nPrzykład: Rozwiń (x - 4)².\nx² - 2x4 + 4² = x² - 8x + 16\nIlustracja: Pokaż diagram ilustrujący rozkład kwadratu różnicy na poszczególne części.",
                    isLastSlide
                )
                3 -> SlideFragment.newInstance(
                    "a² - b² = (a + b)(a - b)",
                    "Definicja: Wzór ten pozwala rozłożyć różnicę kwadratów na iloczyn dwóch wyrażeń.\nPrzykład: Rozłóż x² - 9.\n(x + 3)(x - 3),",
                    isLastSlide
                )
                4 -> SlideFragment.newInstance(
                    "Quiz",
                    "Liczba punktów z ostatniego quizu:\n${score}\n",
                    isLastSlide
                )
                else -> throw IllegalStateException("Unexpected position: $position")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return slideTitles[position]
        }
    }

    fun onPageSelected(position: Int) {
        if (position == 4) {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }
}
