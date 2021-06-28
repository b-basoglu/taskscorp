package com.scorp.userlist.data

interface DataSourceHelper {
    fun fetchPersonList(next: String?, completionHandler: FetchCompletionHandler)
}