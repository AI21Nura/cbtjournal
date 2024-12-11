package com.ainsln.core.datastore.di

import com.ainsln.core.datastore.distortion.DistortionsDataSource
import com.ainsln.core.datastore.distortion.DistortionsResourcesDataSource
import com.ainsln.core.datastore.info.ContentReader
import com.ainsln.core.datastore.info.InfoContentReader
import com.ainsln.core.datastore.info.model.GuideData
import com.ainsln.core.datastore.info.model.InfoData
import com.ainsln.core.datastore.model.DistortionStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    internal fun providesDistortionsDataStore(): DistortionsDataSource<DistortionStore> =
        DistortionsResourcesDataSource
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ContentReaderModule  {
    @Binds
    internal abstract fun bindsContentReader(
        infoContentReader: InfoContentReader
    ) : ContentReader<InfoData, GuideData>
}
