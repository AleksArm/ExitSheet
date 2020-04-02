package com.highestaim.exitsheet


import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.highestaim.exitsheet.manager.LocaleManager
import com.highestaim.exitsheet.service.PreferenceService

class AppApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        PreferenceService.get().injectContext(applicationContext)
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
        // Log.d(TAG, "attachBaseContext");
    }
}