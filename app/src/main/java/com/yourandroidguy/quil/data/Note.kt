package com.yourandroidguy.quil.data

import androidx.compose.runtime.saveable.listSaver
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val body: String,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val isFavorite: Boolean = false
)

val NoteSaver = listSaver<Note, Any>(
    save = { listOf(
        it.id,
        it.title,
        it.body,
        it.date,
        it.isFavorite
    )},
    restore = {
        Note(
            id = it[0] as Long,
            title = it[1] as String,
            body = it[2] as String,
            date = it[3] as String,
            isFavorite = it[4] as Boolean,
        )
    }
)

/**
 * For testing purposes
 */
fun generateDummyData(): List<Note> =
    List(100){
        Note(id = it.toLong(), title = "Title $it", body = "He has a large brain but can\'t offer full nine subjects, may all science students pass science courses e.g, physics, chemistry and biology.", date = "Today")
    }

