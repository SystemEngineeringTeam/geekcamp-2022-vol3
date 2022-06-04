package io.github.com.harutiro.tempmanager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.com.harutiro.tempmanager.welcome_ui.acount.ui.login.AcountFragment
import io.github.com.harutiro.tempmanager.welcome_ui.stater.StaterFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position){
            0 -> StaterFragment()
            1 -> AcountFragment()
            else -> throw IllegalArgumentException()
        }


}