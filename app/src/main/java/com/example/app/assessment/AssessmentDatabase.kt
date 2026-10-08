package com.example.app.assessment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AssessmentAnswer::class,
        AssessmentResult::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AssessmentDatabase : RoomDatabase() {

    abstract fun assessmentDao(): AssessmentDao

    companion object {

        @Volatile
        private var INSTANCE: AssessmentDatabase? = null

        fun getDatabase(
            context: Context
        ): AssessmentDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AssessmentDatabase::class.java,
                        "assessment_database"
                    ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}
