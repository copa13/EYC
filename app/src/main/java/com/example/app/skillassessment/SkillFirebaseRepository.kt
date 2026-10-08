package com.example.app.skillassessment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class SkillFirebaseRepository {

    private val auth = FirebaseAuth.getInstance()

    private val database =
        FirebaseDatabase.getInstance().reference

    suspend fun anonymousLogin() {
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
    }

    suspend fun getSkills(): List<Skill> {
        anonymousLogin()

        val snapshot =
            database
                .child("skills")
                .get()
                .await()

        return snapshot.children.mapNotNull {
            it.getValue(Skill::class.java)
        }
    }

    suspend fun getQuestions(
        skillId: String
    ): List<SkillQuestion> {

        anonymousLogin()

        val snapshot =
            database
                .child("skillQuestions")
                .child(skillId)
                .get()
                .await()

        return snapshot.children.mapNotNull {
            it.getValue(SkillQuestion::class.java)
        }.shuffled()
    }

    suspend fun saveResult(
        result: SkillResult
    ) {

        anonymousLogin()

        val uid =
            auth.currentUser?.uid ?: return

        database
            .child("skillResults")
            .child(uid)
            .child(result.skillId)
            .setValue(result)
            .await()
    }
}
