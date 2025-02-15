package com.kt.uptodo.di

import android.content.Context
import com.kt.uptodo.data.InternalDatabase
import com.kt.uptodo.data.UptodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : UptodoDatabase {
        return InternalDatabase.newInstance(context)
    }
}