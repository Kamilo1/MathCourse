package pl.wsei.pam

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import android.view.animation.AnimationUtils
class QuizActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var questionTextView: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var answer1RadioButton: RadioButton
    private lateinit var answer2RadioButton: RadioButton
    private lateinit var answer3RadioButton: RadioButton
    private lateinit var answer4RadioButton: RadioButton
    private lateinit var submitButton: Button

    private val questions = listOf(
        Question("Rozwiń wyrażenie (x + 5)².", listOf("x² + 10x + 25", "x² + 25", "x² + 5x + 10", "x² + 15x + 25"), 0),
        Question("Który wzór odpowiada kwadratowi różnicy?", listOf("(a - b)² = a² + 2ab + b²", "(a - b)² = a² - 2ab + b²", "(a - b)² = a² - b²", "(a - b)² = a² + b²"), 1),
        Question("Rozłóż na czynniki x² - 16.", listOf("(x - 4)(x + 4)", "(x - 4)(x - 4)", "(x + 4)(x + 4)", "(x + 2)(x - 2)"), 0),
        /*Question("Który wzór jest poprawny dla sześcianu sumy?", listOf("(a + b)³ = a³ + 3a²b + 3ab² + b³", "(a + b)³ = a³ + 3ab + b³", "(a + b)³ = a³ + b³", "(a + b)³ = a³ + 3a² + 3b² + b³"), 0),
        Question("Rozwiń wyrażenie (2x - 3)².", listOf("4x² - 12x + 9", "4x² - 9", "2x² - 6x + 9", "4x² + 6x + 9"), 0),
        Question("Który wzór jest wzorem różnicy kwadratów?", listOf("a² - b² = (a + b)(a - b)", "a² - b² = a² + 2ab + b²", "a² - b² = (a - b)(a - b)", "a² - b² = a² + b²"), 0),
        Question("Rozłóż na czynniki 9y² - 25.", listOf("(3y - 5)(3y + 5)", "(9y - 5)(y + 5)", "(3y - 5)(y + 5)", "(3y - 5)(3y - 5)"), 0),
        Question("Rozwiń wyrażenie (x - 7)².", listOf("x² - 14x + 49", "x² - 7x + 49", "x² - 49", "x² + 14x + 49"), 0),
        Question("Który wzór jest wzorem kwadratu sumy?", listOf("(a + b)² = a² + 2ab + b²", "(a + b)² = a² + 2a + b²", "(a + b)² = a² - 2ab + b²", "(a + b)² = a² + b²"), 0),
        Question("Rozłóż na czynniki x² - 49.", listOf("(x - 7)(x + 7)", "(x - 7)(x - 7)", "(x + 7)(x + 7)", "(x + 7)(x - 8)"), 0),
        Question("Rozwiń wyrażenie (3x + 2)².", listOf("9x² + 12x + 4", "9x² + 6x + 4", "3x² + 6x + 4", "9x² + 18x + 4"), 3),
        Question("Który wzór odpowiada kwadratowi różnicy?", listOf("(a - b)² = a² + 2ab + b²", "(a - b)² = a² - 2ab + b²", "(a - b)² = a² - b²", "(a - b)² = a² + b²"), 1),
        Question("Rozłóż na czynniki 4x² - 9.", listOf("(2x - 3)(2x + 3)", "(4x - 3)(x + 3)", "(2x - 3)(x + 3)", "(4x - 3)(4x - 3)"), 0),
        Question("Rozwiń wyrażenie (x + 6)².", listOf("x² + 12x + 36", "x² + 6x + 36", "x² + 18x + 36", "x² + 6x + 12"), 0),
        Question("Który wzór jest wzorem różnicy kwadratów?", listOf("a² - b² = (a + b)(a - b)", "a² - b² = a² + 2ab + b²", "a² - b² = (a - b)(a - b)", "a² - b² = a² + b²"), 0)
        */
    )

    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        toolbar = findViewById(R.id.toolbar)
        questionTextView = findViewById(R.id.questionTextView)
        answersRadioGroup = findViewById(R.id.answersRadioGroup)
        answer1RadioButton = findViewById(R.id.answer1RadioButton)
        answer2RadioButton = findViewById(R.id.answer2RadioButton)
        answer3RadioButton = findViewById(R.id.answer3RadioButton)
        answer4RadioButton = findViewById(R.id.answer4RadioButton)
        submitButton = findViewById(R.id.submitButton)

        setSupportActionBar(toolbar)

        showQuestion()

        submitButton.setOnClickListener {
            val selectedAnswerId = answersRadioGroup.checkedRadioButtonId
            if (selectedAnswerId != -1) {
                val selectedAnswer = findViewById<RadioButton>(selectedAnswerId)
                val answerIndex = answersRadioGroup.indexOfChild(selectedAnswer)
                checkAnswer(answerIndex)
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    showQuestion()
                } else {
                    val intent = Intent(this, SummaryActivity::class.java)
                    intent.putExtra("EXTRA_SCORE", score)
                    intent.putExtra("EXTRA_TOTAL_QUESTIONS", questions.size)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showQuestion() {
        // Animacja przejścia
        val slideOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
        val slideInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)

        slideOutAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val question = questions[currentQuestionIndex]
                questionTextView.text = question.text
                answersRadioGroup.clearCheck()
                answer1RadioButton.text = question.answers[0]
                answer2RadioButton.text = question.answers[1]
                answer3RadioButton.text = question.answers[2]
                answer4RadioButton.text = question.answers[3]

                questionTextView.startAnimation(slideInAnim)
                answersRadioGroup.startAnimation(slideInAnim)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        questionTextView.startAnimation(slideOutAnim)
        answersRadioGroup.startAnimation(slideOutAnim)
    }

    private fun checkAnswer(answerIndex: Int) {
        val correctAnswerIndex = questions[currentQuestionIndex].correctAnswerIndex
        if (answerIndex == correctAnswerIndex) {
            score++
        }
    }
}

data class Question(val text: String, val answers: List<String>, val correctAnswerIndex: Int)
