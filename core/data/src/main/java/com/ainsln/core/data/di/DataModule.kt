package com.ainsln.core.data.di

import com.ainsln.core.data.repository.DefaultDistortionsRepository
import com.ainsln.core.data.repository.DistortionsRepository
import com.ainsln.core.data.repository.EmotionsRepository
import com.ainsln.core.data.repository.NotesRepository
import com.ainsln.core.data.repository.RoomEmotionsRepository
import com.ainsln.core.data.repository.RoomNotesRepository
import com.ainsln.core.data.util.BaseResourceManager
import com.ainsln.core.data.util.ResourceManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    internal abstract fun bindsNotesRepository(
        roomRepository: RoomNotesRepository
    ): NotesRepository

    @Binds
    internal abstract fun bindsDistortionsRepository(
        defaultRepository: DefaultDistortionsRepository
    ) : DistortionsRepository

    @Binds
    internal abstract fun bindsEmotionsRepository(
        defaultRepository: RoomEmotionsRepository
    ) : EmotionsRepository

    @Binds
    internal abstract fun bindsResourceManager(
        baseResourceManager: BaseResourceManager
    ): ResourceManager
}
