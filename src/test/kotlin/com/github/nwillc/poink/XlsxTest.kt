package com.github.nwillc.poink

import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class XlsxTest {
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
    fun `should be able to create an xlsx`() {
        workbook {
            val headerStyle = createCellStyle().apply {
                fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
            }
            sheet("One") {
                row(listOf("a", "b", "c"), headerStyle)
                row(listOf("1", "2", "3"))
            }
            sheet {
                row(listOf("A very wide cell"))
                row(listOf("An even wider column"))
                autoSizeColumn(0)
            }
        }.write("test.xlsx")
    }
}
