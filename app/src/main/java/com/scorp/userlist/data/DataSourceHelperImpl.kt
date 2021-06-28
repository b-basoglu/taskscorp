package com.scorp.userlist.data

import android.util.Log
import javax.inject.Inject

class DataSourceHelperImpl @Inject constructor(private val dataSource : DataSource): DataSourceHelper {

    override fun fetchPersonList(next: String?, completionHandler: FetchCompletionHandler) {
        dataSource.fetch(next, completionHandler)
    }

}