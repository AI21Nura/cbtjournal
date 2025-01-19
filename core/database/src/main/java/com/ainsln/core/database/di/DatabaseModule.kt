package com.ainsln.core.database.di

import android.content.Context
import com.ainsln.core.database.CBTDatabase
import com.ainsln.core.database.cbtDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    internal fun providesCBTDatabase(
        @ApplicationContext context: Context
    ): CBTDatabase {
        return cbtDatabaseBuilder(
            context = context,
            dbName = "cbtjournal_db",
            assetName = "cbt_db.db"
        )
            .build()
    }

}
