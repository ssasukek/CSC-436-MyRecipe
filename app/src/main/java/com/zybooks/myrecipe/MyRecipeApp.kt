package com.zybooks.myrecipe

import android.app.Application
import com.google.firebase.FirebaseApp

class MyRecipeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
