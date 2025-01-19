package com.ainsln.core.database.di

import android.content.Context
import com.ainsln.core.database.CBTDatabase
import com.ainsln.core.database.cbtDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
internal object TestDatabaseModule {

    const val DB_NAME = "test_db"

    @Provides
    @Singleton
    internal fun providesTestDatabaseWithTestNotes(
        @ApplicationContext context: Context
    ): CBTDatabase {
        context.deleteDatabase(DB_NAME)
        return createTestDatabase(context, "test_cbt_db.db")
    }

    @Provides
    @Named("without_notes")
    internal fun providesTestDatabase(
        @ApplicationContext context: Context
    ): CBTDatabase {
        context.deleteDatabase(DB_NAME)
        return createTestDatabase(context, "cbt_db.db")
    }

    private fun createTestDatabase(
        context: Context,
        assetName: String
    ): CBTDatabase {
        context.deleteDatabase(DB_NAME)
        return cbtDatabaseBuilder(
            context = context,
            dbName = DB_NAME,
            assetName = assetName
        )
            .allowMainThreadQueries()
            .build()
    }
}
