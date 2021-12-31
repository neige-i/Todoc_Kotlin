package fr.neige_i.todoc_kotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.neige_i.todoc_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }
}