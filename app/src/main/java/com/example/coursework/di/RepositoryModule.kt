package com.example.coursework.di

import com.example.coursework.model.datasource.RemoteDataSource
import com.example.coursework.model.repository.Repository
import com.example.coursework.model.repository.RepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository =
        RepositoryImpl(remoteDataSource = remoteDataSource)
}