fun main() {

    fun parseInputToObjectList(input: List<String>, part2Parse: Boolean = false): List<Hand> {
        val listOfHands = mutableListOf<Hand>()
        input.forEach { line ->
            val hand = line.substringBefore(" ").trim()
            val bid = line.substringAfter(" ").trim().toLong()
            var combination: Combination? = null
            var result = 0
            var transformedDigits = hand.map {
                if (it.isDigit()) it.digitToInt()
                else when (it) {
                    'T' -> 10
                    'J' -> 11
                    'Q' -> 12
                    'K' -> 13
                    else -> 14
                }
            }
            if (transformedDigits.contains(11) && part2Parse) {
                val areAllJokers = transformedDigits.filter { it == 11 }.size == 5
                if (!areAllJokers) {
                    val newDrawList = mutableListOf<Int>()
                    val groupedHands = transformedDigits.filter { it != 11 }
                        .groupBy { it }
                        .mapValues { it.value.size }
                        .toList()
                        .sortedByDescending { (_, value) -> value }
                    val jokerChangeValue = groupedHands.first().first
                    transformedDigits.forEachIndexed { digitIndex, i ->
                        if (i == 11) newDrawList.add(digitIndex, jokerChangeValue)
                        else newDrawList.add(digitIndex, i)
                    }
                    newDrawList.groupBy { it }.forEach {
                        if (it.value.size == 5) combination = Combination.FIVE_HITS
                        if (it.value.size == 4) combination = Combination.POKER
                        else if (it.value.size > 1) result += it.value.size
                    }
                } else combination = Combination.FIVE_HITS
            } else transformedDigits.groupBy { it }.forEach {
                if (it.value.size == 5) combination = Combination.FIVE_HITS
                else if (it.value.size == 4) combination = Combination.POKER
                else if (it.value.size > 1) result += it.value.size
            }
            if (combination == null) {
                combination = when (result) {
                    5 -> Combination.FULL_HOUSE
                    4 -> Combination.TWO_PAIRS
                    3 -> Combination.THREE_OF_A_KIND
                    2 -> Combination.PAIR
                    else -> Combination.NONE
                }

            }
            listOfHands.add(Hand(transformedDigits, bid, combination!!))
            result = 0
            combination = null
        }
        return listOfHands
    }

    fun solution (handList: List<Hand>, isPart2: Boolean): Long {
        var rank = 1L
        var finalResult = 0L
        handList
            .sortedBy { it.matchingSymbols.value }
            .groupBy { it.matchingSymbols }
            .forEach { mapEntry ->
                val sortedValues =
                    mapEntry.value
                        .map {hand ->
                            if(isPart2) hand.copy(draw = hand.draw.map {singleDraw ->
                                if (singleDraw == 11) 1
                                else singleDraw
                            })
                            else hand
                        }
                        .sortedWith(compareBy<Hand> { it.draw[0] }
                            .thenBy { it.draw[1] }
                            .thenBy { it.draw[2] }
                            .thenBy { it.draw[3] }
                            .thenBy { it.draw[4] })
                sortedValues.forEach { hand ->
                    finalResult += (rank * hand.bid)
                    rank++
                }
            }
        return finalResult
    }

    val testInput = readInput("input_day07")
    val listOfObjects = parseInputToObjectList(testInput)
    val listOfObjectsPart2 = parseInputToObjectList(testInput, true)
    solution(listOfObjects, false).println()
    solution(listOfObjectsPart2, true).println()
}

data class Hand(var draw: List<Int>, val bid: Long, val matchingSymbols: Combination)

enum class Combination(val value: Int) {
    NONE(-1), PAIR(0), TWO_PAIRS(1), THREE_OF_A_KIND(2), FULL_HOUSE(3), POKER(4), FIVE_HITS(5)
}
