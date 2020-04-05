package com.highestaim.exitsheet.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import com.highestaim.exitsheet.BuildConfig
import com.highestaim.exitsheet.Configs
import com.highestaim.exitsheet.R
import com.highestaim.exitsheet.activity.MainActivity
import com.highestaim.exitsheet.enum.LanguagesEnum
import com.highestaim.exitsheet.manager.LocaleManager
import com.highestaim.exitsheet.service.PreferenceService.Companion.get
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.settings_fragment.*
import java.io.File

class SettingsFragment : BaseFragment() {

    private var selectedLanguageIndex: Int = 1

    override fun getLayoutId() = R.layout.settings_fragment

    override fun getTitle() = getString(R.string.settings)

    override fun setupToolbar() {
        icBack?.visibility = VISIBLE
        settings?.visibility = GONE
        icBack?.setOnClickListener {
            replaceFragment(HomeFragment())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setLanguage()
        setOnChangeLanguageClickListener()
        setVersion()
    }

    private fun setVersion() {
        versionCode?.text = BuildConfig.VERSION_NAME
    }

    private fun setOnChangeLanguageClickListener() {
        languageInputEditText?.setOnClickListener {
            showChangeLanguageAlertDialog()
        }
    }

    private fun setLanguage() {
        languageInputEditText?.text = get().checkLanguageName()
    }


    private fun showChangeLanguageAlertDialog() {
        val languageChangeDialogBuilder = activity?.let { AlertDialog.Builder(it) }
        languageChangeDialogBuilder?.setTitle(R.string.choose_language)

        val languagesArray = resources.getStringArray(R.array.language_array)

        get().getLngCode()?.let {
            languageChangeDialogBuilder?.setSingleChoiceItems(languagesArray, it) { _, which ->
                selectedLanguageIndex = which
                saveLanguage(languagesArray[which], selectedLanguageIndex)
            }
        }

        setChangeLanguageDialogButtonsClickListener(languageChangeDialogBuilder)


        languageChangeDialogBuilder?.create()
        languageChangeDialogBuilder?.show()
    }

    private fun setChangeLanguageDialogButtonsClickListener(languageChangeDialog: AlertDialog.Builder?) {
        languageChangeDialog?.setPositiveButton(resources.getString(R.string.ok)) { _, _ ->

            when (selectedLanguageIndex) {
                0 -> {
                    changeLanguage(LanguagesEnum.ENGLISH.language);//deleteFile()
                }
                1 -> {
                    changeLanguage(LanguagesEnum.ARMENIAN.language);//deleteFile()
                }
                2 -> {
                    changeLanguage(LanguagesEnum.RUSSIAN.language);//deleteFile()
                }
            }
        }
        languageChangeDialog?.setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
        }
    }

    private fun deleteFile() {
        val signFilePath = Configs.getFilePath((activity as MainActivity).originalContext)
        if (File(signFilePath).exists()) {
            File(signFilePath).delete()
        }
    }

    private fun changeLanguage(languageCode: String?) {
        (activity as MainActivity).originalContext?.let {
            languageCode?.let { it1 ->
                LocaleManager.setNewLocale(
                    it,
                    it1
                )
            }
        }
        startActivity(
            Intent.makeRestartActivityTask(
                Intent(
                    activity,
                    MainActivity::class.java
                ).component
            )
        )
    }

    private fun saveLanguage(selectedLanguage: String?, selectedLanguageIndex: Int?) {
        get().saveLanguage(selectedLanguage, selectedLanguageIndex)
    }


}