package pl.wsei.pam

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "quiz_result")
class QuizResult {
    @PrimaryKey(autoGenerate = true)
    var quizId = 0
    var score = 0
    var maxScore = 0
}