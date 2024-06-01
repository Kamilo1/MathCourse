package pl.wsei.pam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.wsei.pam.lab01.R


class SummaryActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        db = AppDatabase.getDatabase(applicationContext)!!

        val summaryTitleTextView: TextView = findViewById(R.id.summaryTitleTextView)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        val restartQuizButton: Button = findViewById(R.id.restartQuizButton)
        val backToLesson : Button = findViewById(R.id.backToLesson)
        val score = intent.getIntExtra("EXTRA_SCORE", 0)
        val totalQuestions = intent.getIntExtra("EXTRA_TOTAL_QUESTIONS", 0)

        summaryTitleTextView.text = "Podsumowanie Quizu "
        scoreTextView.text = "Wynik: ${score}/${totalQuestions}"
        // DataBase

        saveQuizResult(1, score, totalQuestions)


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

    private fun saveQuizResult(quizId: Int, score: Int, maxScore: Int) {
        lifecycleScope.launch {
            val quizResult = QuizResult().apply {
                this.quizId = quizId
                this.score = score
                this.maxScore = maxScore
            }

            withContext(Dispatchers.IO) {
                db.quizResultDao()?.insert(quizResult)
            }
        }
    }
}
