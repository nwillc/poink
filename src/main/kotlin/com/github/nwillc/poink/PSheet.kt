package com.github.nwillc.poink

import org.apache.poi.ss.usermodel.CellStyle

@PoinkDsl
class PSheet(workbook: PWorkbook, val name: String) {
    val sheet = workbook.workbook.createSheet(name)
    private var row = 0

    fun row(cells: List<String>, style: CellStyle? = null) {
        val row = sheet.createRow(row++)
        var col = 0
        cells.forEach { cellValue ->
            row.createCell(col++).apply {
                setCellStyle(style)
                setCellValue(cellValue)
            }
        }
    }
}
