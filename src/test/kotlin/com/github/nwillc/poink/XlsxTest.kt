package com.github.nwillc.poink

import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class XlsxTest {
    @Test
    fun `should be able to create workbook`() {
        val xlsx = workbook { }
        assertThat(xlsx.workbook).isNotNull
    }

    @Test
    fun `should be able to add sheet`() {
        val xlsx = workbook {
            sheet { }
        }

        assertThat(xlsx.sheets).hasSize(1)
        assertThat(xlsx.sheets.first().name).isEqualTo("Sheet 1")
    }

    @Test
    fun `should be able to add sheet with name`() {
        val name = "foo"
        val xlsx = workbook {
            sheet(name) { }
        }

        assertThat(xlsx.sheets).hasSize(1)
        assertThat(xlsx.sheets.first().name).isEqualTo(name)
    }

    @Test
    fun `should be able to create an xlsx`() {
        workbook {
            val headerStyle = workbook.createCellStyle()
            headerStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
            sheet("One") {
                row(listOf("a", "b", "c"), headerStyle)
                row(listOf("1", "2", "3"))
            }
            sheet("Two") {

                row(listOf("Foo", "bar"))
            }
        }.write("test.xlsx")
    }
}
