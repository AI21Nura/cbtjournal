package com.ainsln.core.database.di

import android.content.Context
import androidx.room.Room
import com.ainsln.core.database.CBTDatabase
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
        return Room.databaseBuilder(
            context,
            CBTDatabase::class.java,
            "cbtjournal_db"
        )
            .createFromAsset("cbt_db.db")
            .fallbackToDestructiveMigration()
            .build()
    }

}
