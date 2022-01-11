package fr.neige_i.todoc_kotlin.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task WHERE id = :taskId")
    fun getTaskById(taskId: Long): Flow<Task>

    @Query("SELECT * FROM Task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM Task ORDER BY name COLLATE NOCASE ASC")
    fun getTasksByNameAsc(): Flow<List<Task>>

    @Query("SELECT * FROM Task ORDER BY name COLLATE NOCASE DESC")
    fun getTasksByNameDesc(): Flow<List<Task>>

    @Query("""
        SELECT Task.id, Task.project_id, Task.name, Task.creation_timestamp FROM Task
        INNER JOIN Project ON Project.id = Task.project_id
        ORDER BY Project.name COLLATE NOCASE ASC, Task.name COLLATE NOCASE ASC
        """)
    fun getTasksByProjectNameAsc(): Flow<List<Task>>

    @Query("""
        SELECT Task.id, Task.project_id, Task.name, Task.creation_timestamp FROM Task
        INNER JOIN Project ON Project.id = Task.project_id
        ORDER BY Project.name COLLATE NOCASE DESC, Task.name COLLATE NOCASE ASC
        """)
    fun getTasksByProjectNameDesc(): Flow<List<Task>>

    @Query("SELECT * FROM Task ORDER BY creation_timestamp ASC")
    fun getTasksByDateAsc(): Flow<List<Task>>

    @Query("SELECT * FROM Task ORDER BY creation_timestamp DESC")
    fun getTasksByDateDesc(): Flow<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Query("DELETE FROM Task WHERE id = :taskId")
    suspend fun deleteWithId(taskId: Long)
}