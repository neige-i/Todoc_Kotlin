package fr.neige_i.todoc_kotlin.data.model

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey val id: Long,
    val name: String,
    @ColorInt val color: Int,
)
