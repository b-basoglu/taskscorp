package com.scorp.userlist.di.datasource

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.scorp.userlist.data.FetchResponse
import com.scorp.userlist.data.Person
import com.scorp.userlist.di.repository.MainRepository
import com.scorp.userlist.di.response.Response
import java.util.*

/** DataSource uses repository fetchPersonList method in both loadInitial and loadAfter methods
 *  to get data
 * */
class UserListDataSource(private val repository: MainRepository) :
    PageKeyedDataSource<String?, Person>() {

    private val TAG = "DataSource"
    private var next: String? = null

    //This variable passed viewmodel with Transformation.switchmap to listen newcoming data states
    val userListMLD = MutableLiveData<Response<FetchResponse?>>()

    //Keep hashset to identify newcomings are unique or not
    private val userIdSet = HashSet<Int>()

    //Interval for fetch as specified retrying aggressively is bad for our "backend service"
    val FETCH_INTERVAL = 2000L
    var handler =  Handler(Looper.getMainLooper())


    override fun loadInitial(
        params: LoadInitialParams<String?>,
        callback: LoadInitialCallback<String?, Person>
    ) {
        userListMLD.postValue(Response.loading(null,null))
        repository.fetchPersonList(
            null,
            completionHandler = { fetchResponse, fetchError ->
                if (fetchResponse == null) {
                    Log.d(TAG,  "Error fetch data" + fetchError?.errorDescription)
                    userListMLD.postValue(Response.error(null,fetchError?.errorDescription))

                } else {
                    next = fetchResponse.next
                    Log.d(TAG, "Success Response $fetchResponse")
                    callback.onResult(removeMultipleOnes(fetchResponse),null,fetchResponse.next)
                    Log.d(TAG,  "Success first fetch data " + userIdSet.size)
                    userListMLD.postValue(Response.success(fetchResponse))
                }
            })
    }

    override fun loadBefore(
        params: LoadParams<String?>,
        callback: LoadCallback<String?, Person>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<String?>,
        callback: LoadCallback<String?, Person>
    ) {
        userListMLD.postValue(Response.loading(null,null))
        handler.postDelayed({
            repository.fetchPersonList(
                next,
                completionHandler = { fetchResponse, fetchError ->
                    if (fetchResponse == null) {
                        Log.d(TAG,  "Error fetch data" + fetchError?.errorDescription)
                        userListMLD.postValue(Response.error(null,fetchError?.errorDescription))
                    } else {
                        Log.d(TAG, "Success fetch data with next =  $next")
                        next = fetchResponse.next
                        Log.d(TAG, "Success Response $fetchResponse")
                        callback.onResult(removeMultipleOnes(fetchResponse),fetchResponse.next)
                        Log.d(TAG,  "Success total list size" + userIdSet.size)
                        userListMLD.postValue(Response.success(fetchResponse))
                    }
                })
        }, FETCH_INTERVAL )

    }
    /** Identify unique ones to add callback and userIdSet
     *  @return List<Person> for callback to pass rcyclerview
     * */
    private fun removeMultipleOnes(fetchResponse: FetchResponse): List<Person> {
        val persons =  ArrayList<Person>()
        for (person: Person in fetchResponse.people) {
            if (!userIdSet.contains(person.id)) {
                persons.add(person)
                userIdSet.add(person.id)
            }
        }
        return persons.toList()
    }

}