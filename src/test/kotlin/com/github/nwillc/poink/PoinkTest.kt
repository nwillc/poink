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

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.junit.jupiter.api.Test

class PoinkTest {
    @Test
    fun `create an example xlsx`() {
        workbook {
            val headerStyle = cellStyle("header") {
                fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
            }
            sheet("One") {
                row(listOf("a", "b", "c"), headerStyle)
                val cells = row(listOf("1", "2", "3"))
                cells[0].cellStyle = headerStyle
            }
            sheet {
                row(listOf("A very wide cell"))
                row(listOf("An even wider column"))
                autoSizeColumn(0)
            }
            sheet("Types") {
                val list = listOf(
                    "hello",
                    42,
                    3.142,
                    LocalDateTime.now(),
                    LocalDate.now(),
                    Calendar.getInstance(),
                    "key" to "value"
                )
                // Without style
                row(list)
                // With style
                row(list, headerStyle)
            }
        }.write("test.xlsx")
    }
}
