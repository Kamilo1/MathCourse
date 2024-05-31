package pl.wsei.pam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val summaryTitleTextView: TextView = findViewById(R.id.summaryTitleTextView)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        val restartQuizButton: Button = findViewById(R.id.restartQuizButton)
        val backToLesson : Button = findViewById(R.id.backToLesson)
        val score = intent.getIntExtra("EXTRA_SCORE", 0)
        val totalQuestions = intent.getIntExtra("EXTRA_TOTAL_QUESTIONS", 0)

        summaryTitleTextView.text = "Podsumowanie Quizu"
        scoreTextView.text = "Wynik: $score / $totalQuestions"

        restartQuizButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
        backToLesson.setOnClickListener{
            val intent = Intent(this, LessonActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
