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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PWorkbookTest {
    @Test
    fun `should be able to create workbook`() {
        val xlsx = workbook { }
        assertThat(xlsx).isNotNull
    }

    @Test
    fun `should be able to add sheet`() {
        val xlsx = workbook {
            sheet { }
        }

        assertThat(xlsx.numberOfSheets).isEqualTo(1)
        assertThat(xlsx.getSheetAt(0).sheetName).isEqualTo("Sheet 1")
    }

    @Test
    fun `should be able to add sheet with name`() {
        val name = "foo"
        val xlsx = workbook {
            sheet(name) { }
        }

        assertThat(xlsx.numberOfSheets).isEqualTo(1)
        assertThat(xlsx.getSheet(name)).isNotNull
    }

    @Test
    fun `should be able to create a cell style by name`() {
        val HEADER = "header"
        workbook {
            val style = createCellStyle(HEADER)
            assertThat(getCellStyle(HEADER)).isEqualTo(style)
        }
    }

    @Test
    fun `should open existing workbook`() {
        workbook("src/test/resources/test.xlsx") {
            assertThat(this).isInstanceOf(PWorkbook::class.java)
            sheet("One") {
                assertThat(this).isInstanceOf(PSheet::class.java)
                assertThat(iterator().asSequence().count()).isEqualTo(2)
                iterator().forEach { row ->
                    assertThat(row.lastCellNum).isEqualTo(3)
                }
            }
            sheet(1) {
                assertThat(this).isInstanceOf(PSheet::class.java)
                assertThat(iterator().asSequence().count()).isEqualTo(2)
                iterator().forEach { row ->
                    assertThat(row.lastCellNum).isEqualTo(1)
                }
            }
        }
    }
}
