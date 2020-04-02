package com.highestaim.exitsheet.fragment

import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.highestaim.exitsheet.Configs
import com.highestaim.exitsheet.R
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.sign_fragment.*
import java.io.File
import java.io.FileOutputStream


class SignFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.sign_fragment

    override fun getTitle() = getString(R.string.sign)

    override fun setupToolbar() {
        icBack?.visibility = View.VISIBLE
        settings?.visibility = View.GONE
        saveImageView?.visibility = View.VISIBLE
        clearImageView?.visibility = View.VISIBLE

        setOnClearOnClickListener(clearImageView)
        setOnSaveSignClickListener(saveImageView)
        icBack?.setOnClickListener {
            replaceFragment(HomeFragment())
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        signInDrawView.init(metrics)
    }


    private fun setOnClearOnClickListener(clearImageView: AppCompatImageView?) {
        clearImageView?.setOnClickListener {
            signInDrawView?.clear()
        }
    }

    private fun setOnSaveSignClickListener(saveImageView: AppCompatImageView?) {
        saveImageView?.setOnClickListener {
            saveSign()
        }
    }

    private fun saveSign() {
        val path = Configs.getSignFilePath(context)
        val file = File(path)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val ostream = FileOutputStream(file)
            getSignAsBitmap().compress(CompressFormat.PNG, 10, ostream)
            ostream.close()
            signInDrawView.invalidate()
            replaceFragment(HomeFragment())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            signInDrawView.isDrawingCacheEnabled = false
        }
    }


    private fun getSignAsBitmap(): Bitmap {
        val well: Bitmap? = signInDrawView?.bitmap
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.color = Color.WHITE
        val now = Canvas(bitmap)
        now.drawRect(Rect(0, 0, 100, 100), paint)
        well?.let {
            now.drawBitmap(
                it,
                Rect(0, 0, well.width, well.height),
                Rect(0, 0, 100, 100),
                null
            )
        }
        if (bitmap == null) {
            println("NULL bitmap save\n")
        }
        return bitmap
    }

}