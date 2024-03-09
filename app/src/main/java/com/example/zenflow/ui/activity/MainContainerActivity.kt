package com.example.zenflow.ui.activity

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
//import com.example.weis.R
//import com.example.weis.databinding.ActivityMainContainerBinding
import com.example.zenflow.adapters.ViewPagerAdapter
//import com.example.weis.ui.fragment.HomeFragment
//import com.example.weis.ui.fragment.FocusFragment
//import com.example.weis.ui.fragment.GoalsFragment
import com.example.zenflow.R
import com.example.zenflow.databinding.ActivityMainContainerBinding
import com.example.zenflow.ui.fragment.GuidedFragment
import com.example.zenflow.ui.fragment.RecommendedFragment
import com.example.zenflow.ui.fragment.UnguidedFragment
import com.example.zenflow.ui.fragment.UserDetailsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MainContainerActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = intent.getStringExtra("userEmail")
        val userName = intent.getStringExtra("userName")
        val imageUri = intent.getStringExtra("imageUri")
        val id=intent.getStringExtra("id")
        val userMobileNumber = intent.getStringExtra("userMobileNumber")
        val bundle = Bundle()
        bundle.putString("userEmail", userEmail)
        bundle.putString("userName",userName)
//        imageUri?.let {
//            bundle.putString("imageUri", it.toString())
//        }
        bundle.putString("ImageUri",imageUri)
        bundle.putString("id",id)
        bundle.putString("userMobileNumber",userMobileNumber)
//        Toast.makeText(this, "$imageUri", Toast.LENGTH_LONG).show()

        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)

        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.addFragment(
            RecommendedFragment(),
            getString(R.string.recommended),
            getColorIcon(R.drawable.ic_recommended, R.color.grey2),
            getColorIcon(R.drawable.ic_recommended, R.color.primary),data = bundle
        )
        viewPagerAdapter.addFragment(
            GuidedFragment(),
            getString(R.string.guided),
            getColorIcon(R.drawable.ic_guided, R.color.grey2),
            getColorIcon(R.drawable.ic_guided, R.color.primary),data = bundle
        )
        viewPagerAdapter.addFragment(
            UnguidedFragment(),
            getString(R.string.unguided),
            getColorIcon(R.drawable.ic_unguided, R.color.grey2),
            getColorIcon(R.drawable.ic_unguided, R.color.primary),data = bundle
        )
        viewPagerAdapter.addFragment(
            UserDetailsFragment(),
            getString(R.string.user),
            getColorIcon(R.drawable.ic_user, R.color.grey2),
            getColorIcon(R.drawable.ic_user, R.color.primary),data = bundle
        )

        binding.viewPagerMain.adapter = viewPagerAdapter
        binding.viewPagerMain.setCurrentItem(1, true)

        TabLayoutMediator(binding.cardTabLay, binding.viewPagerMain) { tab, position ->
            val customTabView = viewPagerAdapter.getTabView(position)
            tab.customView = customTabView
            if(position == 1){
                tab.let {
                    val customTabView = it.customView
                    val tabIconImageView = customTabView?.findViewById<ImageView>(R.id.tabIcon)
                    val tabNameTextView = customTabView?.findViewById<TextView>(R.id.tabName)

                    tabIconImageView?.setColorFilter(ContextCompat.getColor(this@MainContainerActivity, R.color.primary))
                    tabNameTextView?.setTextColor(ContextCompat.getColor(this@MainContainerActivity, R.color.primary))
                }
            }
        }.attach()
        binding.cardTabLay.getTabAt(1)?.select()

        binding.cardTabLay.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val customTabView = it.customView
                    val tabIconImageView = customTabView?.findViewById<ImageView>(R.id.tabIcon)
                    val tabNameTextView = customTabView?.findViewById<TextView>(R.id.tabName)

                    tabIconImageView?.setColorFilter(ContextCompat.getColor(this@MainContainerActivity, R.color.primary))
                    tabNameTextView?.setTextColor(ContextCompat.getColor(this@MainContainerActivity, R.color.primary))
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    val customTabView = it.customView
                    val tabIconImageView = customTabView?.findViewById<ImageView>(R.id.tabIcon)
                    val tabNameTextView = customTabView?.findViewById<TextView>(R.id.tabName)

                    tabIconImageView?.setColorFilter(ContextCompat.getColor(this@MainContainerActivity, R.color.grey2))
                    tabNameTextView?.setTextColor(ContextCompat.getColor(this@MainContainerActivity, R.color.grey2))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    val customTabView = it.customView
                    val tabIconImageView = customTabView?.findViewById<ImageView>(R.id.tabIcon)
                    val tabNameTextView = customTabView?.findViewById<TextView>(R.id.tabName)

                    tabIconImageView?.setColorFilter(ContextCompat.getColor(this@MainContainerActivity, R.color.primary))
                    tabNameTextView?.setTextColor(ContextCompat.getColor(this@MainContainerActivity, R.color.primary))
                }
            }

        })
    }

    private fun getColorIcon(icon: Int, color: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(this, icon)?.mutate()
        drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_IN)
        return drawable
    }
}