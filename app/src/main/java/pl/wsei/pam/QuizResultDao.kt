package pl.wsei.pam;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pl.wsei.pam.QuizResult

@Dao
interface QuizResultDao {
    @Insert

    fun insert(quizResult: QuizResult)
    @Query("SELECT * FROM quiz_result WHERE quizId = :quizId")
    fun getResultsByQuizId(quizId: Int): List<QuizResult?>?
}