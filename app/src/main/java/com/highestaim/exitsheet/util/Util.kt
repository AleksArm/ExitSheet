package com.highestaim.exitsheet.util

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import com.highestaim.exitsheet.R
import java.util.*

object Util {

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = activity?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }

    fun timePicker(context: Context?, textView: AppCompatTextView?) {
        val mCurrentTime = Calendar.getInstance()
        val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mCurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(context, R.style.DatePickerTheme,
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
                val minute1: String = if (selectedMinute < 10) {
                    "0$selectedMinute"
                } else {
                    selectedMinute.toString()
                }
                val selTime = "$selectedHour:$minute1"
                textView?.text = selTime
            }, hour, minute, true
        ) //Yes 24 hour time
        mTimePicker.show()
    }

    fun datePicker(context: Context?, dateInput: AppCompatTextView?) {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog: DatePickerDialog
            datePickerDialog = context?.let {
                DatePickerDialog(
                    it, R.style.DatePickerTheme,
                    DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        val month = monthOfYear + 1
                        val monthStr: String
                        monthStr = if (month < 10) {
                            "0$month"
                        } else {
                            month.toString()
                        }
                        val date = "$dayOfMonth/$monthStr/$year"
                        dateInput?.text = date
                    }, mYear, mMonth, mDay)
            }!!
            datePickerDialog.show()
        }

}