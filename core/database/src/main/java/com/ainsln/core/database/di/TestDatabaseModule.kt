package com.ainsln.core.database.di

import android.content.Context
import com.ainsln.core.database.CBTDatabase
import com.ainsln.core.database.cbtDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
internal object TestDatabaseModule {

    @Provides
    @Singleton
    internal fun providesTestDatabase(
        @ApplicationContext context: Context
    ): CBTDatabase {
        context.deleteDatabase("test_db")
        return cbtDatabaseBuilder(
            context = context,
            dbName = "test_db",
            assetName = "test_cbt_db.db"
        )
            .allowMainThreadQueries()
            .build()
    }
}
