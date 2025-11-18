package com.zybooks.myrecipe.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.zybooks.myrecipe.data.model.AppUserData
import com.zybooks.myrecipe.data.repository.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AppUser(
    val uid: String,
    val email: String,
    val username: String
)

class ProfileVM : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _user = MutableStateFlow<AppUser?>(null)
    val user: StateFlow<AppUser?> = _user

    fun loadUser() {
        val auth = AuthRepo.getCurrentUser()

        val uid = auth?.uid ?: run {
            return
        }

        viewModelScope.launch {
            try {
                val snapshot = db.collection("users")
                    .document(uid)
                    .get()
                    .await()

                val data = snapshot.toObject(AppUserData::class.java)

                if (data != null) {
                    _user.value = AppUser(
                        uid = data.uid,
                        email = data.email.ifEmpty { auth?.email ?: "" },
                        username = data.username
                    )
                } else {
                    _user.value = AppUser(uid, "", "")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUsername(username: String, onDone: () -> Unit = {}) {
        val auth = AuthRepo.getCurrentUser()
        val uid = auth?.uid ?: return

        viewModelScope.launch {
            db.collection("users")
                .document(uid)
                .update("username", username)
                .await()

            _user.value = _user.value?.copy(username = username)
            onDone()
        }
    }

    fun changePassword(newPassword: String, onDone: (Boolean) -> Unit) {
        val auth = AuthRepo.getCurrentUser()
        auth?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                onDone(task.isSuccessful)
            }
    }

    fun logout(nav: androidx.navigation.NavController) {
        AuthRepo.logout()
        nav.navigate("login") {
            popUpTo("recipes") { inclusive = true }
        }
    }
}
