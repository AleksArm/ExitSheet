package com.highestaim.exitsheet

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.*

class PdfDocumentAdapter(var context: Context, var path: String) : PrintDocumentAdapter() {


    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        cancellationSignal?.isCanceled?.let {
            if (it) {
                callback?.onLayoutCancelled()
            } else {
                val info = PrintDocumentInfo.Builder("exit_sheet")
                info.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build()
                newAttributes?.equals(oldAttributes)?.let { attr ->
                    callback?.onLayoutFinished(info.build(), !attr)
                }
            }
        }
    }

    override fun onWrite(pages: Array<out PageRange>?, destination: ParcelFileDescriptor?, cancellationSignal: CancellationSignal?, callback: WriteResultCallback?) {
        var inputStream: InputStream? = null
        var outPutStream: OutputStream? = null

        try {
            val file = File(path)

            inputStream = FileInputStream(file)
            outPutStream = FileOutputStream(destination?.fileDescriptor)

            val buff = ByteArray(16384)
            var size = 0

            while (inputStream.read(buff).also { size = it } >= 0 && !cancellationSignal?.isCanceled!!) {
                outPutStream.write(buff, 0, size)
            }

            if (cancellationSignal?.isCanceled == true) {
                callback?.onWriteCancelled()
            } else {
                callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }

        } catch (ex : Exception) {
            callback?.onWriteFailed(ex.localizedMessage)
            ex.printStackTrace()
        }
        finally {
            try {
                inputStream?.close()
                outPutStream?.close()
            } catch (e :Exception) {
            }
        }
    }

}
