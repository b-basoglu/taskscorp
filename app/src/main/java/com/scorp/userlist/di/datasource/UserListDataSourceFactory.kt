package com.scorp.userlist.di.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scorp.userlist.data.Person
import com.scorp.userlist.di.datasource.UserListDataSource
import com.scorp.userlist.di.repository.MainRepository

/** Create UserListDataSource
 *
 */
class UserListDataSourceFactory(var mainRepository: MainRepository) :
    DataSource.Factory<String, Person>() {
    val itemLiveDataSource =
        MutableLiveData<UserListDataSource>()

    override fun create(): UserListDataSource {
        val userListDataSource =
            UserListDataSource(mainRepository)
        itemLiveDataSource.postValue(userListDataSource)
        return userListDataSource
    }

}