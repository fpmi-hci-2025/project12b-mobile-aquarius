package com.example.bookstore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BookstoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Здесь можно инициализировать что-то глобально
    }
}