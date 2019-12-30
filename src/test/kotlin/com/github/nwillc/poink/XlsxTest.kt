package com.github.nwillc.poink

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
        val xlsx = workbook {
            sheet("One") {
                row(listOf("a", "b", "c"))
                row(listOf("1", "2", "3"))
            }
            sheet("Two") {
                row(listOf("Foo", "bar"))
            }
        }

        xlsx.write("test.xlsx")
    }
}
