package com.highestaim.exitsheet.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.highestaim.exitsheet.R
import com.highestaim.exitsheet.fragment.HomeFragment
import com.highestaim.exitsheet.manager.LocaleManager
import com.highestaim.exitsheet.util.Util.verifyStoragePermissions


class MainActivity : AppCompatActivity() {

    var originalContext: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        verifyStoragePermissions(this)
        openCheckInFragment()

    }

    private fun openCheckInFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, HomeFragment())
            .addToBackStack(null).commitAllowingStateLoss()
    }

    override fun attachBaseContext(base: Context) {
        originalContext = base
        super.attachBaseContext(LocaleManager.setLocale(base))
        // Log.d(TAG, "attachBaseContext");
    }
}
