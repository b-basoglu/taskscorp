package com.scorp.userlist.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.scorp.userlist.R

object FragmentUtils {
    /**
     * @param fragmentManager use [AppCompatActivity.getSupportFragmentManager] if calling from activity,
     * [Fragment.getChildFragmentManager] if calling from fragment
     * @param fragment        fragment to show
     * @param containerViewId id of the container view (e.g. binding.container.getId())
     * @param tag             provide a unique tag if you need this fragment to be added to back stack
     * @param animate         fragment will appear with animation if true
     */
    @JvmStatic
    fun replaceFragment(
        fragmentManager: FragmentManager, fragment: Fragment,
        containerViewId: Int, tag: String?, animate: Boolean
    ) {
        val transaction = fragmentManager.beginTransaction()
        if (animate) {
            transaction.setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right
            )
        }
        transaction.replace(containerViewId, fragment, tag)
        if (tag != null) {
            transaction.addToBackStack(tag)
            transaction.commit()
        } else {
            transaction.commitNow()
        }
    }
}