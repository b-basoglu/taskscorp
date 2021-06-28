package com.scorp.userlist.di.module

import com.scorp.userlist.data.DataSource
import com.scorp.userlist.data.DataSourceHelper
import com.scorp.userlist.data.DataSourceHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDataSource(): DataSource = DataSource()

    @Provides
    @Singleton
    fun provideDataSourceHelper(dataSourceHelper: DataSourceHelperImpl): DataSourceHelper = dataSourceHelper

}