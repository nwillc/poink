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

import java.io.FileOutputStream
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * The _poink_ DSL facade for the Apache POI [Workbook].
 */
@PoinkDsl
class PWorkbook(private val workbook: XSSFWorkbook = XSSFWorkbook()) : Workbook by workbook {
    private val styles: MutableMap<String, CellStyle> = mutableMapOf()

    /**
     * Add a named sheet to the workbook.
     * @param name of the new sheet, will default to "Sheet #"
     */
    fun sheet(name: String = "Sheet ${numberOfSheets + 1}", block: PSheet.() -> Unit) =
        PSheet(createSheet(name)).apply(block)

    /**
     * Create a named [CellStyle] in the workbook for future use.
     * @param name to use for created style.
     * @param block Lambda to apply to the created style.
     * @return The created [CellStyle]
     */
    fun createCellStyle(name: String, block: CellStyle.() -> Unit = {}): CellStyle {
        val cellStyle = createCellStyle()
        cellStyle.apply(block)
        styles[name] = cellStyle
        return cellStyle
    }

    /**
     * Get a [CellStyle] by name.
     * @param name of the style to look up.
     */
    fun getCellStyle(name: String) = styles[name]

    /**
     * Write the workbook out to a file.
     * @param path name of the output file.
     */
    fun write(path: String) = FileOutputStream(path).use { workbook.write(it) }
}

/**
 * Create a workbook.
 */
fun workbook(block: PWorkbook.() -> Unit): PWorkbook = PWorkbook().apply(block)
