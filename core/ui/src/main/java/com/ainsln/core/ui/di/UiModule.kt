package com.ainsln.core.ui.di

import com.ainsln.core.ui.utils.BaseIntentSender
import com.ainsln.core.ui.utils.IntentSender
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UiModule {

    @Binds
    internal abstract fun bindsIntentSender(
        baseIntentSender: BaseIntentSender
    ) : IntentSender

}
