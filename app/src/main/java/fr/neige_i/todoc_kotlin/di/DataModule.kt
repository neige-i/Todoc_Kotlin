package fr.neige_i.todoc_kotlin.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.neige_i.todoc_kotlin.data.repository.ProjectRepository
import fr.neige_i.todoc_kotlin.data.repository.ProjectRepositoryImpl
import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
import fr.neige_i.todoc_kotlin.data.repository.TaskRepositoryImpl
import fr.neige_i.todoc_kotlin.data.data_source.AppDatabase
import fr.neige_i.todoc_kotlin.data.data_source.ProjectDao
import fr.neige_i.todoc_kotlin.data.data_source.TaskDao
import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindingModule {

    @Singleton
    @Binds
    abstract fun bindProjectRepository(
        projectRepositoryImpl: ProjectRepositoryImpl,
    ): ProjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl,
    ): TaskRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceProvidingModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context,
        projectDaoProvider: Provider<ProjectDao>,
    ): AppDatabase {

        return Room
            .databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "app_database.db",
            )
            .addCallback(CreateDatabaseCallback(projectDaoProvider))
            .build()
    }

    private class CreateDatabaseCallback(
        private val projectDaoProvider: Provider<ProjectDao>,
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // DO NOT BLOCK the current thread when inserting the initial projects
            // to let Room finish creating the database first
            CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {

                projectDaoProvider.get().apply {
                    insert(Project(1L, "Projet Tartampion", 0xFFEADAD1.toInt()))
                    insert(Project(2L, "Projet Lucidia", 0xFFB4CDBA.toInt()))
                    insert(Project(3L, "Projet Circus", 0xFFA3CED2.toInt()))
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideProjectDao(appDatabase: AppDatabase): ProjectDao = appDatabase.projectDao()

    @Singleton
    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao = appDatabase.taskDao()
}