package com.ainsln.core.database.di

import com.ainsln.core.database.CBTDatabase
import com.ainsln.core.database.dao.EmotionsDao
import com.ainsln.core.database.dao.NotesDao
import com.ainsln.core.database.dao.RecentSearchesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    internal fun providesNotesDao(database: CBTDatabase)
            : NotesDao = database.notesDao()

    @Provides
    internal fun providesEmotionsDao(database: CBTDatabase)
            : EmotionsDao = database.emotionsDao()

    @Provides
    internal fun providesRecentSearchesDao(database: CBTDatabase)
            : RecentSearchesDao = database.recentSearchesDao()

}
