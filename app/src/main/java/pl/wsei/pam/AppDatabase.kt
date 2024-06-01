package pl.wsei.pam;

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile


@Database(entities = [QuizResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizResultDao(): QuizResultDao?

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "quiz_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}