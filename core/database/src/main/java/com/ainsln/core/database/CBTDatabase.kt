package com.ainsln.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ainsln.core.database.dao.EmotionsDao
import com.ainsln.core.database.dao.NotesDao
import com.ainsln.core.database.dao.RecentSearchesDao
import com.ainsln.core.database.model.entity.EmotionEntity
import com.ainsln.core.database.model.entity.EmotionTranslationEntity
import com.ainsln.core.database.model.entity.SelectedEmotionCrossRef
import com.ainsln.core.database.model.entity.NoteEntity
import com.ainsln.core.database.model.entity.NoteFtsEntity
import com.ainsln.core.database.model.entity.RecentSearchEntity
import com.ainsln.core.database.model.entity.ThoughtEntity
import com.ainsln.core.database.utils.DateConverter
import com.ainsln.core.database.utils.LongListConverter

@Database(
    entities = [
        NoteEntity::class,
        ThoughtEntity::class,
        EmotionEntity::class,
        SelectedEmotionCrossRef::class,
        EmotionTranslationEntity::class,
        RecentSearchEntity::class,
        NoteFtsEntity::class
               ],
    version = 3
)
@TypeConverters(DateConverter::class, LongListConverter::class)
internal abstract class CBTDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun emotionsDao(): EmotionsDao
    abstract fun recentSearchesDao(): RecentSearchesDao
}

internal fun cbtDatabaseBuilder(
    context: Context,
    dbName: String,
    assetName: String
): RoomDatabase.Builder<CBTDatabase> {
    return Room.databaseBuilder(
        context,
        CBTDatabase::class.java,
        dbName
    )
        .createFromAsset(assetName)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO NoteFts(NoteFts) VALUES ('rebuild')")
            }
        })
}
