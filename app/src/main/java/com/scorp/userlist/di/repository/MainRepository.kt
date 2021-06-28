package com.scorp.userlist.di.repository


import com.scorp.userlist.data.DataSourceHelper
import com.scorp.userlist.data.FetchCompletionHandler
import javax.inject.Inject



class MainRepository @Inject constructor(private val dataSourceHelper: DataSourceHelper) {

    fun fetchPersonList(next: String?, completionHandler: FetchCompletionHandler) {
        dataSourceHelper.fetchPersonList(next, completionHandler)
    }

}