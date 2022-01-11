package fr.neige_i.todoc_kotlin.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM Project")
    fun getAllProjects(): Flow<List<Project>>

    @Insert
    suspend fun insert(project: Project)
}