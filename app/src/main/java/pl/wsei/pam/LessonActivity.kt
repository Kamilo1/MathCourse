package pl.wsei.pam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pl.wsei.pam.lab01.R
class LessonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        setSupportActionBar(findViewById(R.id.toolbar))

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val pagerAdapter = LessonPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    class LessonPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val slideTitles = arrayOf("Wprowadzenie", "Kwadrat sumy", "Kwadrat różnicy", "Różnica kwadratów", "Quiz")

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
                    "Liczba punktów z ostatniego quizu:",
                    isLastSlide
                )
                else -> throw IllegalStateException("Unexpected position: $position")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? = slideTitles[position]
    }

    fun onPageSelected(position: Int) {
        if (position == 4) { // Last slide
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }
}
