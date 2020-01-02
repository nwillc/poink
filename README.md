[![Coverage](https://codecov.io/gh/nwillc/poink/branch/master/graphs/badge.svg?branch=master)](https://codecov.io/gh/nwillc/poink)
[![license](https://img.shields.io/github/license/nwillc/poink.svg)](https://tldrlegal.com/license/-isc-license)
[![Travis](https://img.shields.io/travis/nwillc/poink.svg)](https://travis-ci.org/nwillc/poink)
[![Download](https://api.bintray.com/packages/nwillc/maven/poink/images/download.svg)](https://bintray.com/nwillc/maven/poink/_latestVersion)
---
![Poink!](poink.png)
# Poi In Kotlin DSL

A Kotlin DSL to generate XLSX files via Apache POI.

Really almost nothing here, the DSL is a facade for Apache POI, providing for this sort of use:

```kotlin
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
```
