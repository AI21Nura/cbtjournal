package com.ainsln.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ainsln.core.database.dao.EmotionsDao
import com.ainsln.core.database.dao.NotesDao
import com.ainsln.core.database.model.entity.EmotionEntity
import com.ainsln.core.database.model.entity.EmotionTranslationEntity
import com.ainsln.core.database.model.entity.SelectedEmotionCrossRef
import com.ainsln.core.database.model.entity.NoteEntity
import com.ainsln.core.database.model.entity.ThoughtEntity
import com.ainsln.core.database.utils.DateConverter
import com.ainsln.core.database.utils.LongListConverter

@Database(
    entities = [
        NoteEntity::class,
        ThoughtEntity::class,
        EmotionEntity::class,
        SelectedEmotionCrossRef::class,
        EmotionTranslationEntity::class
               ],
    version = 1
)
@TypeConverters(DateConverter::class, LongListConverter::class)
internal abstract class CBTDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun emotionsDao(): EmotionsDao
}
