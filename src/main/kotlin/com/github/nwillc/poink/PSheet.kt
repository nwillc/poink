/*
 * Copyright (c) 2020, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.poink

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

/**
 * The _poink_ DSL facade for the Apache POI [Sheet].
 * @param sheet The [Sheet] to act as a facade for.
 */
@PoinkDsl
class PSheet(
    private val sheet: Sheet
) : Sheet by sheet {
    private val format = workbook.createDataFormat()
    private val dateFormat = format.getFormat("MM/DD/YYYY")
    private val timeStampFormat = format.getFormat("MM/DD/YYYY HH:MM:SS")
    private val calendarFormat = format.getFormat("MMM D, YYYY")

    /**
     * Add objects as a new row at bottom of sheet. These objects will be intelligently rendered based on [String],
     * [Number], [LocalDate], [LocalDateTime], [Calendar] or else the [Object.toString].
     * @param cells A [List] of [Any] to add to [Sheet] as a row.
     * @param style An optional [CellStyle] for the cells in the row.
     * @return The [List] of [Cell] added as a row.
     */
    fun row(cells: List<Any>, style: CellStyle? = null): List<Cell> {
        val cellList = mutableListOf<Cell>()
        val row = sheet.createRow(sheet.physicalNumberOfRows)
        var col = 0
        cells.forEach { cellValue ->
            cellList.add(
                row.createCell(col++).apply {
                    cellStyle = style
                    when (cellValue) {
                        is String -> setCellValue(cellValue)
                        is Number -> setCellValue(cellValue.toDouble())
                        is LocalDateTime -> {
                            cellStyle = cloneAndFormat(style, timeStampFormat)
                            setCellValue(cellValue)
                        }
                        is LocalDate -> {
                            cellStyle = cloneAndFormat(style, dateFormat)
                            setCellValue(cellValue)
                        }
                        is Calendar -> {
                            cellStyle = cloneAndFormat(style, calendarFormat)
                            setCellValue(cellValue)
                        }
                        else -> setCellValue(cellValue.toString())
                    }
                }
            )
        }
        return cellList
    }

    private fun cloneAndFormat(style: CellStyle?, format: Short) = workbook.createCellStyle().apply {
        if (style != null) {
            cloneStyleFrom(style)
        }
        dataFormat = format
    }
}
