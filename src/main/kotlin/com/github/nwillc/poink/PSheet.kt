package com.github.nwillc.poink

@PoinkDsl
class PSheet(workbook: PWorkbook, val name: String) {
    val sheet = workbook.workbook.createSheet(name)
    private var row = 0

    fun row(cells: List<String>) {
        val row = sheet.createRow(row++)
        var col = 0
        cells.forEach { row.createCell(col++).setCellValue(it) }
    }
}
