package fr.neige_i.todoc_kotlin.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Task::class, Project::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Expose DAOs
    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase =
            INSTANCE ?: initDatabase(context, scope)

        private fun initDatabase(context: Context, scope: CoroutineScope) = synchronized(this) {
            Room
                .databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database.db",
                )
                .addCallback(CreateDatabaseCallback(scope))
                .build()
        }
    }

    private class CreateDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.projectDao()?.let { it: ProjectDao ->
                scope.launch {
                    it.insert(Project(1L, "Projet Tartampion", 0xEADAD1))
                    it.insert(Project(2L, "Projet Lucidia", 0xB4CDBA))
                    it.insert(Project(3L, "Projet Circus", 0xA3CED2))
                }
            }
        }
    }
}