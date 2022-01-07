package fr.neige_i.todoc_kotlin.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task

@Database(entities = [Task::class, Project::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Expose DAOs
    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao
}