package com.zybooks.myrecipe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zybooks.myrecipe.viewmodel.ProfileVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileVM = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    val user by viewModel.user.collectAsState()
    var username by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(user) {
        username = user?.username ?: ""
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Username")
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                onClick = {
                    viewModel.updateUsername(username) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Username updated!")
                        }
                    }
                }
            ) { Text("Update Username") }

            Spacer(Modifier.height(30.dp))

            Text("Change Password")

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("New password") }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                onClick = {
                    viewModel.changePassword(newPassword) { ok ->
                        scope.launch {
                            if (ok)
                                snackbarHostState.showSnackbar("Password changed!")
                            else
                                snackbarHostState.showSnackbar("Failed to change password")
                        }
                    }
                }
            ) { Text("Change Password") }

            Spacer(Modifier.height(40.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                onClick = { viewModel.logout(navController) }
            ) {
                Text("Logout")
            }
        }
    }
}
