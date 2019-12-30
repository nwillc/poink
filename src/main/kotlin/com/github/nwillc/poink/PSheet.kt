package com.github.nwillc.poink

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet

@PoinkDsl
class PSheet(
    workbook: PWorkbook,
    name: String,
    private val sheet: Sheet = workbook.createSheet(name)
) : Sheet by sheet {
    fun row(cells: List<String>, style: CellStyle? = null) {
        val row = sheet.createRow(sheet.physicalNumberOfRows)
        var col = 0
        cells.forEach { cellValue ->
            row.createCell(col++).apply {
                cellStyle = style
                setCellValue(cellValue)
            }
        }
    }
}
