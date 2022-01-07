package fr.neige_i.todoc_kotlin.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.neige_i.todoc_kotlin.data.data_source.AppDatabase
import fr.neige_i.todoc_kotlin.data.data_source.ProjectDao
import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProvidingModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext appContext: Context,
        projectDaoProvider: Provider<ProjectDao>,
    ): AppDatabase {

        return Room
            .databaseBuilder(
                appContext,
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
                    insert(Project(1L, "Projet Tartampion", 0xEADAD1))
                    insert(Project(2L, "Projet Lucidia", 0xB4CDBA))
                    insert(Project(3L, "Projet Circus", 0xA3CED2))
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideProjectDao(appDatabase: AppDatabase) = appDatabase.projectDao()

    @Singleton
    @Provides
    fun provideTaskDao(appDatabase: AppDatabase) = appDatabase.taskDao()
}