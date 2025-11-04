package com.offerfactory.topandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Главная Activity приложения (Launcher Activity).
 * 
 * В простом приложении можно сразу перенаправлять на MovieDetailActivity.
 * В реальном приложении здесь мог бы быть список фильмов или главный экран.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Сразу открываем MovieDetailActivity
        // Явный Intent указывает конкретный класс Activity
        val intent = Intent(this, MovieDetailActivity::class.java)
        startActivity(intent)
        
        // Закрываем MainActivity, чтобы при нажатии Back не возвращаться сюда
        finish()
    }
}
