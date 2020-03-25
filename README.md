[![Coverage](https://codecov.io/gh/nwillc/poink/branch/master/graphs/badge.svg?branch=master)](https://codecov.io/gh/nwillc/poink)
[![license](https://img.shields.io/github/license/nwillc/poink.svg)](https://tldrlegal.com/license/-isc-license)
[![Travis](https://img.shields.io/travis/nwillc/poink.svg)](https://travis-ci.org/nwillc/poink)
[![Download](https://api.bintray.com/packages/nwillc/maven/poink/images/download.svg)](https://bintray.com/nwillc/maven/poink/_latestVersion)
---
![Poink!](poink.png)
# POI iN Kotlin

A Kotlin DSL to read/write XLSX files via Apache POI.

Really almost nothing here, the DSL is a facade for Apache POI, providing for somewhat simpler use.

## Generating

```kotlin
        workbook {
            // Create a named cell style
            val headerStyle = cellStyle("Header") {
                fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
            }
            sheet("One") {
                // Add a row with a style
                row(listOf("a", "b", "c"), headerStyle)
                // Add a row without style
                row(listOf(1, 2.0, 3L))
            }
            sheet {
                row(listOf("A very wide cell"))
                row(listOf("An even wider column"))
                // Auto size the width of a column
                autoSizeColumn(0)
            }
        }.write("test.xlsx")
```

## Reading

```kotlin
   workbook("test.xlsx") {
            sheet("One") {
               iterator().forEach { row ->
                    row.iterator().forEach { cell ->
                        println(cell.stringCellValue)
                    }
               }
            }
        }
```

## See Also
- [Apache POI](https://poi.apache.org/)
- [Poink API Docs](https://nwillc.github.io/poink/dokka/poink)
