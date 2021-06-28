package com.scorp.userlist.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scorp.userlist.R
import com.scorp.userlist.data.FetchResponse
import com.scorp.userlist.databinding.FragmentUserListBinding
import com.scorp.userlist.di.response.Response
import com.scorp.userlist.di.response.Status
import dagger.hilt.android.AndroidEntryPoint

/* @AndroidEntryPoint annotation needed. Hilt @AndroidEntryPoint's directly inject with this annotation
* */
@AndroidEntryPoint
class MainFragment : MainBaseFragment() {
    private val TOTAL_SECONDS = 20  //seconds for count down
    private val COUNT_DOWN_INTERVAL = 1000L //interval for count down DO NOT CHANGE
    private val TOTAL_TIME = TOTAL_SECONDS * COUNT_DOWN_INTERVAL // total time that counted down
    private val REFRESH_TIME = TOTAL_SECONDS * COUNT_DOWN_INTERVAL / 4 // 5 sec delay for refresh

    private lateinit var binding: FragmentUserListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: UserListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivRefresh: ImageView
    private lateinit var rlNoData: RelativeLayout
    private val handler:Handler = Handler()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        this.binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        this.binding.viewModel = viewModel
        initViews()
    }

    private fun initViews() {
        initRecyclerview()
        initOtherViews()
        setupDataObservers()
        setupLayoutListeners()
    }
    private fun initRecyclerview(){
        recyclerView = binding.rvRecycler
        recyclerView.setHasFixedSize(true)
        adapter = UserListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
    }

    private fun initOtherViews(){
        swipeRefreshLayout = binding.srlRvlistHolder
        ivRefresh = binding.ivRefresh
        rlNoData = binding.rlNoData
    }
    private fun setupDataObservers(){
        viewModel.itemPagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.userListMLD.observe(viewLifecycleOwner, Observer {
            if (swipeRefreshLayout.isRefreshing){
                swipeRefreshLayout.isRefreshing = false
            }
            incomingUserListProcess(it)
        })
        viewModel.refreshing.observe(viewLifecycleOwner, Observer {
            if (it){
                hideErrorViewAndRefreshButton()
            }
        })
    }
    private fun setupLayoutListeners(){
        //Refreshes recyclerview
        swipeRefreshLayout.setOnRefreshListener {
            refreshLayout()
        }
        //Refreshes recyclerview
        ivRefresh.setOnClickListener {
            refreshLayout()
        }
    }
    private fun incomingUserListProcess(response : Response<FetchResponse?>){
        //When status loading hide ivRefresh but show No person text
        if (response.status == Status.LOADING){
            if (adapter.itemCount <= 0){
                rlNoData.visibility = View.VISIBLE
            }
            ivRefresh.visibility = View.INVISIBLE
        }
        //When status success hide rlnodata which is root of ivrefresh view
        //but in advance since loading state is finished  we should make visible ivrefresh
        //to be able to use it again whether root is visible or not
        else if (response.status == Status.SUCCESS ){
            if (adapter.itemCount >0){
                rlNoData.visibility = View.INVISIBLE
            }else{
                ivRefresh.visibility = View.VISIBLE
            }
        }
        //When status error no action for rlnodata which is root of ivrefresh view
        //because there is not change in data set but loading is finished so
        // we should make visible ivrefresh to be able to use it
        // again whether root visible or not
        else if(response.status == Status.ERROR){
            ivRefresh.visibility = View.VISIBLE
            timeOutRemoveTimer.start()
        }
    }

    //To set toolbar title
    override fun getTitle(): String {
        return resources.getString(R.string.USER_LIST)
    }

    /*
     * CountDownTimer takes two arguments TOTAL_TIME is seconds long and COUNT_DOWN_INTERVAL one is interval
     * in milliseconds long. When error occurred if user do nothing it will refresh layout
     */
    private var timeOutRemoveTimer = object : CountDownTimer(TOTAL_TIME, COUNT_DOWN_INTERVAL) {
        override fun onFinish() {
            binding.circleProgress.progress = 0f
            refreshLayout()
        }

        override fun onTick(millisUntilFinished: Long) {
            val resultantFloat: Float = (TOTAL_TIME - millisUntilFinished).toFloat() / TOTAL_TIME
            binding.circleProgress.progress = resultantFloat
        }
    }

    //Refresh recyclerview. viewModel.refreshing keep state of refreshing to block user from retrying aggressively
    private fun refreshLayout(){
        if (!viewModel.refreshing.value!!){
            viewModel.refreshing.value = true
            postDelayedRefresh()
        }
    }
    //Handler used for post delay to block user from retrying aggressively
    private fun postDelayedRefresh(){
        handler.postDelayed({
            adapter = UserListAdapter()
            recyclerView.adapter = adapter
            viewModel.refreshData()
            viewModel.refreshing.value = false
        }, REFRESH_TIME )
    }
    //While refreshing refresh mechanism should stop and loading should start
    private fun hideErrorViewAndRefreshButton(){
        val errorView = binding.errorView
        if(errorView.visibility!=View.GONE){
            timeOutRemoveTimer.cancel()
            errorView.visibility = View.GONE
        }
        ivRefresh.visibility = View.INVISIBLE
        binding.clpbLoading.visibility = View.VISIBLE
    }
}