package com.example.app.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.app.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

data class GoogleUser(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?
)

class GoogleAuthManager(
    private val context: Context
) {

    private val credentialManager =
        CredentialManager.create(context)

    suspend fun signIn(): GoogleUser {

        val googleIdOption =
            GetGoogleIdOption.Builder()
                .setServerClientId(
                    context.getString(
                        R.string.default_web_client_id
                    )
                )
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .build()

        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

        val result =
            credentialManager.getCredential(
                context,
                request
            )

        val credential =
            GoogleIdTokenCredential.createFrom(
                result.credential.data
            )

        return GoogleUser(
            id = credential.id,
            name = credential.displayName ?: "",
            email = credential.id,
            photoUrl =
                credential.profilePictureUri?.toString()
        )
    }
}
