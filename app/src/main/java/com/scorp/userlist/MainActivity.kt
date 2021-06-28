package com.scorp.userlist

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.scorp.userlist.databinding.MainActivityBinding
import com.scorp.userlist.ui.main.MainBaseFragment
import com.scorp.userlist.ui.main.MainFragment
import com.scorp.userlist.utils.FragmentUtils
import dagger.hilt.android.AndroidEntryPoint

/* @AndroidEntryPoint annotation needed. Otherwise Fragment @AndroidEntryPoint will give error
* */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainBaseFragment.MainBaseControllerInterface {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        openMainFragment()
    }

    /** Make invisible title when CollapsingToolbarLayout expanded
     **/


    private fun openMainFragment() {
        FragmentUtils.replaceFragment(
            supportFragmentManager, MainFragment.newInstance(),
            binding.container.id, null, false
        )
    }


    /** Called from MainBaseFragment instance via Controller Interface to add toolbar title
     * @param title title that wanted to show
     * */
    override fun updateToolbarTitle(title: String) {
        binding.toolbar.title = title
    }
}