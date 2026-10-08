package com.example.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import coil3.compose.AsyncImage
import com.example.app.auth.GoogleAuthManager
import com.example.app.auth.GoogleUser
import com.example.app.data.AppDatabase
import com.example.app.data.StudentProfile
import kotlinx.coroutines.launch

@Composable
fun EycApp() {

    val context = LocalContext.current

    val database =
        remember {
            AppDatabase.getDatabase(context)
        }

    val authManager =
        remember {
            GoogleAuthManager(context)
        }

    val scope =
        rememberCoroutineScope()

    var googleUser by remember {
        mutableStateOf<GoogleUser?>(null)
    }

    var savedProfile by remember {
        mutableStateOf<StudentProfile?>(null)
    }

    LaunchedEffect(googleUser) {

        googleUser?.let { user ->

            savedProfile =
                database
                    .studentProfileDao()
                    .getProfile(user.id)
        }
    }

    if (googleUser == null) {

        LoginScreen(
            onLogin = {

                scope.launch {

                    try {

                        googleUser =
                            authManager.signIn()

                    } catch (_: Exception) {

                    }
                }
            }
        )

    } else {

        ProfileScreen(
            googleUser = googleUser!!,
            existingProfile = savedProfile,

            onSave = { profile ->

                scope.launch {

                    database
                        .studentProfileDao()
                        .saveProfile(profile)

                    savedProfile = profile
                }
            }
        )
    }
}


@Composable
private fun LoginScreen(
    onLogin: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "EYC",
            style = MaterialTheme
                .typography
                .displayMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Elevate Your Career",
            style = MaterialTheme
                .typography
                .headlineSmall
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Continue with Google"
            )
        }
    }
}


@Composable
private fun ProfileScreen(
    googleUser: GoogleUser,
    existingProfile: StudentProfile?,
    onSave: (StudentProfile) -> Unit
) {

    var photoUri by remember(
        existingProfile
    ) {
        mutableStateOf(
            existingProfile?.profilePhotoUri?.let {
                Uri.parse(it)
            }
        )
    }

    var age by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.age ?: ""
        )
    }

    var education by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.education ?: ""
        )
    }

    var courseTrade by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.courseTrade ?: ""
        )
    }

    var educationLevel by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.educationLevel ?: ""
        )
    }

    var interests by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.interests ?: ""
        )
    }

    var basicSkills by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.basicSkills ?: ""
        )
    }

    var preferredCareerAreas by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.preferredCareerAreas ?: ""
        )
    }

    var learningPreferences by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.learningPreferences ?: ""
        )
    }

    var currentCareerGoal by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.currentCareerGoal ?: ""
        )
    }

    var experienceProjects by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.experienceProjects ?: ""
        )
    }

    var languagesKnown by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.languagesKnown ?: ""
        )
    }

    var selfConfidenceLevel by remember(existingProfile) {
        mutableStateOf(
            existingProfile?.selfConfidenceLevel ?: ""
        )
    }

    val photoPicker =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                photoUri = uri
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp)
    ) {

        Text(
            text =
                if (existingProfile == null)
                    "Create Student Profile"
                else
                    "Update Student Profile",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        ProfilePhoto(
            photoUri = photoUri,
            onSelectPhoto = {
                photoPicker.launch("image/*")
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        ReadOnlyField(
            label = "Name",
            value = googleUser.name
        )

        ReadOnlyField(
            label = "Email",
            value = googleUser.email
        )

        ProfileField(
            label = "Age",
            value = age,
            onValueChange = {
                age = it
            }
        )

        ProfileField(
            label = "Education",
            value = education,
            onValueChange = {
                education = it
            }
        )

        ProfileField(
            label = "Course / Trade",
            value = courseTrade,
            onValueChange = {
                courseTrade = it
            }
        )

        ProfileField(
            label = "Education Level",
            value = educationLevel,
            onValueChange = {
                educationLevel = it
            }
        )

        ProfileField(
            label = "Interests",
            value = interests,
            onValueChange = {
                interests = it
            }
        )

        ProfileField(
            label = "Basic Skills",
            value = basicSkills,
            onValueChange = {
                basicSkills = it
            }
        )

        ProfileField(
            label = "Preferred Career Areas",
            value = preferredCareerAreas,
            onValueChange = {
                preferredCareerAreas = it
            }
        )

        ProfileField(
            label = "Learning Preferences",
            value = learningPreferences,
            onValueChange = {
                learningPreferences = it
            }
        )

        ProfileField(
            label = "Current Career Goal",
            value = currentCareerGoal,
            onValueChange = {
                currentCareerGoal = it
            }
        )

        ProfileField(
            label = "Experience / Projects",
            value = experienceProjects,
            onValueChange = {
                experienceProjects = it
            }
        )

        ProfileField(
            label = "Languages Known",
            value = languagesKnown,
            onValueChange = {
                languagesKnown = it
            }
        )

        ProfileField(
            label = "Self-Confidence Level",
            value = selfConfidenceLevel,
            onValueChange = {
                selfConfidenceLevel = it
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {

                val profile =
                    StudentProfile(

                        googleId =
                            googleUser.id,

                        name =
                            googleUser.name,

                        email =
                            googleUser.email,

                        profilePhotoUri =
                            photoUri?.toString(),

                        age = age,

                        education =
                            education,

                        courseTrade =
                            courseTrade,

                        educationLevel =
                            educationLevel,

                        interests =
                            interests,

                        basicSkills =
                            basicSkills,

                        preferredCareerAreas =
                            preferredCareerAreas,

                        learningPreferences =
                            learningPreferences,

                        currentCareerGoal =
                            currentCareerGoal,

                        experienceProjects =
                            experienceProjects,

                        languagesKnown =
                            languagesKnown,

                        selfConfidenceLevel =
                            selfConfidenceLevel
                    )

                onSave(profile)
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    if (existingProfile == null)
                        "Save Profile"
                    else
                        "Update Profile"
            )
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )
    }
}


@Composable
private fun ProfilePhoto(
    photoUri: Uri?,
    onSelectPhoto: () -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        if (photoUri != null) {

            AsyncImage(
                model = photoUri,
                contentDescription =
                    "Profile Photo",

                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape),

                contentScale =
                    ContentScale.Crop
            )

        } else {

            Icon(
                imageVector =
                    Icons.Default.AccountCircle,

                contentDescription =
                    "Profile Photo",

                modifier =
                    Modifier.size(120.dp)
            )
        }

        IconButton(
            onClick = onSelectPhoto
        ) {

            Icon(
                imageVector =
                    Icons.Default.PhotoCamera,

                contentDescription =
                    "Select Profile Photo"
            )
        }
    }
}


@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,

        onValueChange =
            onValueChange,

        label = {
            Text(label)
        },

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                ),

        singleLine = false
    )
}


@Composable
private fun ReadOnlyField(
    label: String,
    value: String
) {

    OutlinedTextField(
        value = value,

        onValueChange = {},

        label = {
            Text(label)
        },

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                ),

        readOnly = true,

        singleLine = true
    )
}
