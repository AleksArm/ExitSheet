package com.highestaim.exitsheet.fragment

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.highestaim.exitsheet.Configs
import com.highestaim.exitsheet.R
import com.highestaim.exitsheet.ShitInfo
import com.highestaim.exitsheet.activity.MainActivity
import com.highestaim.exitsheet.manager.PdfManager
import com.highestaim.exitsheet.service.PreferenceService.Companion.get
import com.highestaim.exitsheet.util.Util
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.home_fragment.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.home_fragment

    override fun getTitle() = getString(R.string.home)

    override fun setupToolbar() {
        settings?.setOnClickListener {
            this.replaceFragment(SettingsFragment())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserEnteredName()

        setupToolbar()
        setExitTimeClickListener()
        setDownloadPdfClickListener()
        setOnShareClickListener()
        setOnCurrentDateClickListener()
        setOnReturnTimeClickListener()
        setCurrentDate()
        setSignOnClickListener()
        setSignPicture()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //save fragment info
        get().putUserEnteredInfo(getUserEnteredInfo())
    }

    //get saved info
    private fun setUserEnteredName() {
        val userEnteredInfo = get().getUserEnteredInfo()
        userEnteredInfo?.let {
            nameEditText?.setText(it.name)
            exitTimeEditText?.text = it.exitTime
            editAddressEditText?.setText(it.exitAddress)
            placeEditText?.setText(it.addressName)
            purposeEditText?.setText(it.purpose)
            expectedEditText?.text = it.returnTime
            currentDate?.text = it.currentDate
        }
    }


    private fun setSignPicture() {
        val imgFile = File(Configs.getSignFilePath(context))

        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            signPicture?.setImageBitmap(myBitmap)
        }
    }

    private fun setSignOnClickListener() {
        sign?.setOnClickListener {
            replaceFragment(SignFragment())
        }
    }

    private fun setOnCurrentDateClickListener() {
        currentDate?.setOnClickListener {
            context?.let { it1 -> Util.datePicker(it1, currentDate) }
        }
    }

    private fun setExitTimeClickListener() {
        exitTimeEditText?.setOnClickListener {
            context?.let { it1 -> Util.timePicker(it1, exitTimeEditText) }
        }
    }

    private fun setOnReturnTimeClickListener() {
        expectedEditText?.setOnClickListener {
            Util.timePicker(context, expectedEditText)
        }
    }

    private fun setDownloadPdfClickListener() {
        downloadPdf?.setOnClickListener {
            PdfManager((activity as MainActivity).originalContext)
                .createPdfFile(Configs.getFilePath(context), buildShitInfo())
        }
    }

    private fun setOnShareClickListener() {
        share?.setOnClickListener {
            PdfManager((activity as MainActivity).originalContext).openGeneratedPDF(Configs.getFilePath(context))
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCurrentDate() {
        val df = SimpleDateFormat("dd/MM/yyyy")
        val date = df.format(Calendar.getInstance().time)
        currentDate?.text = date
    }

    private fun getUserEnteredInfo(): ShitInfo {
        return ShitInfo(
            name = nameEditText?.text.toString(),
            exitTime = exitTimeEditText?.text.toString(),
            exitAddress = editAddressEditText?.text.toString(),
            addressName = placeEditText?.text.toString(),
            purpose = purposeEditText?.text.toString(),
            returnTime = expectedEditText?.text.toString(),
            currentDate = currentDate?.text.toString()
        )
    }


    private fun buildShitInfo(): ShitInfo {
        return ShitInfo(
            title = resources.getString(R.string.exit_shit_title),
            nameTitle = resources.getString(R.string.name),
            name = nameEditText?.text.toString(),
            exitTimeTitle = resources.getString(R.string.exit_time),
            exitTime = exitTimeEditText?.text.toString(),
            exitAddressTitle = resources?.getString(R.string.exit_address),
            exitAddress = editAddressEditText?.text.toString(),
            addressNameTitle = resources?.getString(R.string.address_and_name_of_the_place_of_visit),
            addressName = placeEditText?.text.toString(),
            purposeTitle = resources?.getString(R.string.purpose_of_the_visit),
            purpose = purposeEditText?.text.toString(),
            returnTimeTitle = resources?.getString(R.string.expected_return_time),
            returnTime = expectedEditText?.text.toString(),
            currentDate = currentDate?.text.toString(),
            sign = resources.getString(R.string.sign_pdf)
        )

    }
}