package com.highestaim.exitsheet.manager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.highestaim.exitsheet.Configs
import com.highestaim.exitsheet.Configs.getFilePath
import com.highestaim.exitsheet.PdfDocumentAdapter
import com.highestaim.exitsheet.ShitInfo
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import java.io.File
import java.io.FileOutputStream


class PdfManager(private val context: Context?) {

    fun openGeneratedPDF(path: String) {

        if (File(path).exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            val apkURI = context?.let {
                FileProvider.getUriForFile(
                    it, context.applicationContext
                        ?.packageName.toString() + ".provider", File(path)
                )
            }
            intent.setDataAndType(apkURI, "application/pdf")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                context?.startActivity(Intent.createChooser(intent, "Share File"))
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "No Application available to view pdf",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    fun createPdfFile(path: String, info: ShitInfo) {
        if (File(path).exists())
            File(path).delete()
        try {
            val document = Document()

            //save
            PdfWriter.getInstance(document, FileOutputStream(path))

            //open to write
            document.open()

            //settings
            document.pageSize = PageSize.A4
            document.addCreationDate()

            //font settings

            val subTitleFontSize = 20.0f
            val valueFontSize = 22.0f
            val titleFontSize = 25.0f

            val baseFont = BaseFont.createFont(
                "/assets/fonts/FreeSans.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
            )

            val titleFont = Font(baseFont, titleFontSize, Font.NORMAL)
            val subTitleFont = Font(baseFont, subTitleFontSize, Font.NORMAL, BaseColor.BLACK)
            val valueFont = Font(baseFont, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            val currentDate = Font(baseFont, valueFontSize, Font.NORMAL, BaseColor.RED)

            //add main title
            addNewItem(document, info.title, Element.ALIGN_CENTER, titleFont)

            //add current title
            addNewItem(document, info.currentDate, Element.ALIGN_RIGHT, currentDate)

            addLineSpace(document)

            //add items
            addNewItem(document, info.nameTitle, Element.ALIGN_LEFT, subTitleFont)

            addNewItem(document, info.name, Element.ALIGN_LEFT, valueFont)

            addLineSeparator(document)

            //add items exitTime
            addNewItem(document, info.exitTimeTitle, Element.ALIGN_LEFT, subTitleFont)

            addNewItem(document, info.exitTime, Element.ALIGN_LEFT, valueFont)

            addLineSeparator(document)

            //add items exit Address
            addNewItem(document, info.exitAddressTitle, Element.ALIGN_LEFT, subTitleFont)

            addNewItem(document, info.exitAddress, Element.ALIGN_LEFT, valueFont)

            addLineSeparator(document)

            //add items address Name
            addNewItem(document, info.addressNameTitle, Element.ALIGN_LEFT, subTitleFont)

            addNewItem(document, info.addressName, Element.ALIGN_LEFT, valueFont)

            addLineSeparator(document)

            //add items purpose
            addNewItem(document, info.purposeTitle, Element.ALIGN_LEFT, subTitleFont)

            addNewItem(document, info.purpose, Element.ALIGN_LEFT, valueFont)

            addLineSeparator(document)

            //add items return Time
            addNewItem(document, info.returnTimeTitle, Element.ALIGN_LEFT, subTitleFont)

            addNewItem(document, info.returnTime, Element.ALIGN_LEFT, valueFont)

            addLineSeparator(document)

            addNewItem(document, info.sign, Element.ALIGN_RIGHT, subTitleFont)

            addImage(document, Element.ALIGN_RIGHT)

            document.close()

            printPdf()

        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }


    private fun addNewItemToLeftAndRight(
        document: Document,
        textLeft: String?,
        textRight: String?,
        textFont: Font
    ) {
        val leftChunk = Chunk(textLeft, textFont)
        val rightChunk = Chunk(textRight, textFont)

        val paragraph = Paragraph(leftChunk)
        paragraph.add(Chunk(VerticalPositionMark()))
        paragraph.add(rightChunk)
        document.add(paragraph)
    }

    private fun printPdf() {
        val printManager = context?.getSystemService(Context.PRINT_SERVICE) as PrintManager
        try {
            val printDocumentAdapter = PdfDocumentAdapter(context, getFilePath(context))
            printManager.print("Document", printDocumentAdapter, PrintAttributes.Builder().build())
        } catch (e: java.lang.Exception) {
            e.localizedMessage
        }
    }


    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
        addLineSpace(document)
        document.add(lineSeparator)
    }

    private fun addLineSpace(document: Document) {
        document.add(Paragraph("  "))
    }

    private fun addNewItem(document: Document, text: String?, align: Int, font: Font) {
        val chunk = Chunk(text, font)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = align
        document.add(paragraph)
    }

    private fun addImage(document: Document, align: Int) {
        if (File(Configs.getSignFilePath(context = context)).exists()) {
            val image = Image.getInstance(Configs.getSignFilePath(context = context))
            image.alignment = align
            document.add(image)
        }
    }

}