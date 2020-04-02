package com.highestaim.exitsheet.service

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.highestaim.exitsheet.ShitInfo


class PreferenceService {

    private var savedRestaurants: SharedPreferences? = null
    private val gson by lazy { Gson() }
    private var context: Context? = null

    fun injectContext(context: Context): PreferenceService {
        if (this.context == null) {
            this.context = context
            initSharedPreferences()
        }
        return get()
    }

    private fun initSharedPreferences() {
        if (savedRestaurants == null) {
            savedRestaurants = context?.getSharedPreferences("saveUserEnteredInfo",
                Context.MODE_PRIVATE
            )
        }
    }

    fun putUserEnteredInfo(sortByRestaurantChain: ShitInfo) {
        savedRestaurants?.edit()?.putString("entered_info", Gson().toJson(sortByRestaurantChain))?.apply()
    }


    fun getUserEnteredInfo(): ShitInfo? {
        val stringData = savedRestaurants?.getString("entered_info", null)
        if (stringData != null) {
            val type = object : TypeToken<ShitInfo>() {
            }.type
            return gson.fromJson(stringData, type) as ShitInfo

        }
        return null
    }

    fun saveLanguage(selectedLanguage: String?, selectedLanguageIndex: Int?) {
        val editor = context?.getSharedPreferences("lng", Context.MODE_PRIVATE)?.edit()
        selectedLanguageIndex?.let { editor?.putInt("saveLng", it) }
        editor?.putString("saveLngTxt", selectedLanguage)
        editor?.apply()
    }

    fun getLngCode(): Int? {
        val sharedPreferencesLanguage = context?.getSharedPreferences("lng", Context.MODE_PRIVATE)
        return sharedPreferencesLanguage?.getInt("saveLng", 0)
    }

    fun checkLanguageName(): String {
        var language = "English"
        val sharedPreferencesLanguage = context?.getSharedPreferences("lng", Context.MODE_PRIVATE)
        when (sharedPreferencesLanguage?.getInt("saveLng", -1)) {
            0, -1 ->
                language = "English"
            1 ->
                language = "Armenian"
            2 ->
                language = "Russian"
        }
        return language
    }


    companion object {

        private var preferenceService: PreferenceService? = null

        fun get(): PreferenceService {
            if (preferenceService == null) {
                preferenceService = PreferenceService()
            }
            return preferenceService as PreferenceService
        }
    }
}