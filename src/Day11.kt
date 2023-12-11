import java.lang.Integer.max
import java.math.BigInteger
import kotlin.math.absoluteValue
import kotlin.math.min

fun main() {

    val multiplier = 1000000

    fun solution(input: List<String>, isPart2: Boolean): BigInteger {
        val mapOfGalaxies = input.flatMapIndexed { lineIndex, string ->
            val listOfGalaxies = mutableListOf<Galaxy>()
            string.forEachIndexed { columnIndex, c ->
                if (c == '#') listOfGalaxies.add(Galaxy(lineIndex, columnIndex))
            }
            listOfGalaxies
        }
        val listOfIndexesToAddColumn = mutableListOf<Int>()
        input.first().indices.forEach { index ->
            if (mapOfGalaxies.none { it.columnIndex == index }) listOfIndexesToAddColumn.add(index)
        }
        val listOfIndexesToAddRow = mutableListOf<Int>()
        input.forEachIndexed { index, s ->
            if (!s.contains("#")) {
                listOfIndexesToAddRow.add(index)
            }
        }
        val expandedMapOfGalaxies = input.flatMapIndexed { lineIndex, string ->
            val listOfGalaxies = mutableListOf<Galaxy>()
            string.forEachIndexed { columnIndex, c ->
                if (c == '#') listOfGalaxies.add(Galaxy(lineIndex, columnIndex))
            }
            listOfGalaxies
        }
        val distances = expandedMapOfGalaxies.mapIndexed { expandedGalaxiesIndex, initialGalaxy ->
            val galaxiesToCheckDistance = expandedMapOfGalaxies.toMutableList()
                .filterIndexed { index, _ -> index > expandedGalaxiesIndex }
            val result = galaxiesToCheckDistance.map { comparisonGalaxy ->
                var increaseLineMultiplier = 0
                var increaseColumnMultiplier = 0
                listOfIndexesToAddRow.forEach {
                    if (it in min(comparisonGalaxy.lineIndex, initialGalaxy.lineIndex)..max(
                            comparisonGalaxy.lineIndex,
                            initialGalaxy.lineIndex
                        )
                    ) increaseLineMultiplier++
                }
                listOfIndexesToAddColumn.forEach {
                    if (it in min(comparisonGalaxy.columnIndex, initialGalaxy.columnIndex)..max(
                            comparisonGalaxy.columnIndex,
                            initialGalaxy.columnIndex
                        )
                    ) increaseColumnMultiplier++
                }
                val finalResult =
                    if (isPart2) (increaseColumnMultiplier * multiplier - increaseColumnMultiplier).toBigInteger() + (increaseLineMultiplier * multiplier - increaseLineMultiplier).toBigInteger()
                    else (increaseColumnMultiplier + increaseLineMultiplier).toBigInteger()
                val distance =
                    ((comparisonGalaxy.columnIndex - initialGalaxy.columnIndex).absoluteValue
                            + (comparisonGalaxy.lineIndex - initialGalaxy.lineIndex).absoluteValue).toBigInteger() + finalResult
                distance
            }
            result.sumOf { it }
        }
        return distances.sumOf { it }
    }


    val testInput = readInput("input_day_11")
    solution(testInput, false).println()
    solution(testInput, true).println()
}

data class Galaxy(val lineIndex: Int, val columnIndex: Int)

