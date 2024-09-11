package com.ainsln.core.datastore.di

import com.ainsln.core.datastore.DistortionsDataSource
import com.ainsln.core.datastore.DistortionsResourcesDataSource
import com.ainsln.core.datastore.model.DistortionStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    internal fun providesDistortionsDataStore(): DistortionsDataSource<DistortionStore> =
        DistortionsResourcesDataSource

}
