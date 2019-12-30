package com.github.nwillc.poink

import java.io.FileOutputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook

@PoinkDsl
class PWorkbook {
    val workbook: XSSFWorkbook = XSSFWorkbook()
    val sheets = mutableListOf<PSheet>()

    fun sheet(name: String = "Sheet ${sheets.count() + 1}", block: PSheet.() -> Unit) {
        sheets.add(PSheet(this, name).apply(block))
    }

    fun write(path: String) = FileOutputStream(path).use { workbook.write(it) }
}

fun workbook(block: PWorkbook.() -> Unit): PWorkbook = PWorkbook().apply(block)
