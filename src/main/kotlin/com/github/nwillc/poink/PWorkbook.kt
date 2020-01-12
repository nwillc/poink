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

import java.io.FileInputStream
import java.io.FileOutputStream
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * The _poink_ DSL facade for the Apache POI [Workbook].
 * @param workbook The [Workbook] to act as a facade for.
 */
@PoinkDsl
class PWorkbook(private val workbook: XSSFWorkbook = XSSFWorkbook()) : Workbook by workbook {
    private val styles: MutableMap<String, XSSFCellStyle> = mutableMapOf()

    /**
     * Get a named sheet, or create if absent, in the workbook.
     * @param name of the new sheet, will default to "Sheet #"
     * @param block Code to perform on the sheet.
     * @return The existing sheet, or a new one if named sheet doesn't exit.
     */
    fun sheet(name: String = "Sheet ${numberOfSheets + 1}", block: PSheet.() -> Unit) =
        PSheet(getSheet(name) ?: workbook.createSheet(name)).apply(block)

    /**
     * Get an existing sheet by its index.
     * @param index The index of the sheet
     * @param block Code to perform on the sheet.
     * @return Sheet at a given index.
     */
    fun sheet(index: Int, block: PSheet.() -> Unit) = PSheet(workbook.getSheetAt(index)).apply(block)

    /**
     * Get a named [CellStyle], or create if absent, in the workbook for future use.
     * @param name to use for created style.
     * @param block Lambda to apply to the created style.
     * @return The created [CellStyle]
     */
    fun cellStyle(name: String, block: CellStyle.() -> Unit = {}): CellStyle =
        styles.computeIfAbsent(name) { workbook.createCellStyle() as XSSFCellStyle }
            .apply(block)

    /**
     * Write the workbook out to a file.
     * @param path name of the output file.
     */
    fun write(path: String) = FileOutputStream(path).use { workbook.write(it) }
}

/**
 * Create a workbook.
 * @param block Code to perform on the workbook.
 * @return A new workbook.
 */
fun workbook(block: PWorkbook.() -> Unit): PWorkbook = PWorkbook().apply(block)

/**
 * Open existing workbook.
 * @param path File path to an existing workbook.
 * @param block Code to perform on the workbook.
 * @return The loaded from the provided file.
 */
fun workbook(path: String, block: PWorkbook.() -> Unit): PWorkbook =
    FileInputStream(path).use { PWorkbook(XSSFWorkbook(it)).apply(block) }
