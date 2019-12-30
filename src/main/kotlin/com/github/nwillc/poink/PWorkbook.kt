package com.github.nwillc.poink

import java.io.FileOutputStream
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

@PoinkDsl
class PWorkbook(private val workbook: XSSFWorkbook = XSSFWorkbook()) : Workbook by workbook {
    fun sheet(name: String = "Sheet ${numberOfSheets + 1}", block: PSheet.() -> Unit) =
        PSheet(this, name).apply(block)
    fun write(path: String) = FileOutputStream(path).use { workbook.write(it) }
}

fun workbook(block: PWorkbook.() -> Unit): PWorkbook = PWorkbook().apply(block)
