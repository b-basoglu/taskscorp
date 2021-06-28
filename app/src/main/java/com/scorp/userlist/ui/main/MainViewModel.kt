package com.scorp.userlist.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.scorp.userlist.data.DataSource
import com.scorp.userlist.data.FetchResponse
import com.scorp.userlist.data.Person
import com.scorp.userlist.di.datasource.UserListDataSource
import com.scorp.userlist.di.datasource.UserListDataSourceFactory
import com.scorp.userlist.di.repository.MainRepository
import com.scorp.userlist.di.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** Constructor injection needed since with @HiltViewModel annotation
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val TAG = "MainViewModel"
    var itemPagedList: LiveData<PagedList<Person>>
    var userListMLD : LiveData<Response<FetchResponse?>> = MutableLiveData()

    //Since we have used handler to make refreshing a bit late we keep this mutablelivedata to keep track refreshing state
    var refreshing : MutableLiveData<Boolean> = MutableLiveData(false)

    private val dataSourceFactory : UserListDataSourceFactory =
        UserListDataSourceFactory(mainRepository)
    private var liveDataSource: MutableLiveData<UserListDataSource>

    init {
        liveDataSource = dataSourceFactory.itemLiveDataSource
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(DataSource.Constants.fetchCountRange.endInclusive)
            .setPageSize(DataSource.Constants.fetchCountRange.endInclusive).build()
        val builder  = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
        itemPagedList = builder.build()
        userListMLD = Transformations.switchMap(
            dataSourceFactory.itemLiveDataSource,
            UserListDataSource::userListMLD
        )
    }

    //Invalidate datasource and retry data for rcyclerview
    fun refreshData(){
        if (liveDataSource.value != null) {
            liveDataSource.value!!.invalidate()
        }
    }
}