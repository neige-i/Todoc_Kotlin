package fr.neige_i.todoc_kotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.neige_i.todoc_kotlin.R

class TaskListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
    }
}