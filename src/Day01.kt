fun main() {
    fun part1(input: List<String>): Int {
        val numberList = input.map {line ->
            line.first {char ->
                char.isDigit()
            }.toString() + line.last { char ->
                char.isDigit() }
        }
        var result = 0
        numberList.forEach {
            result += it.toInt()
        }
        return result
    }
    fun part2Logic (line: String, isReversed: Boolean): String {
        val lineUsed = if (isReversed) line.reversed() else line
        val indexes = lineUsed.indices
        var result = ""
        for (index in indexes) {
            if (lineUsed[index].isDigit())  {
                result = lineUsed[index].toString()
            }
            numberEnum.values().onEach {enum ->
                val enumName = if (isReversed) enum.name.lowercase().reversed() else enum.name.lowercase()
                if (lineUsed.substring(index).startsWith(enumName)) {
                    result = enum.value.toString()
                    return@onEach
                }
            }
            if (result.isNotEmpty()) break
        }
        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0
        input.forEach {line ->
            var firstResult = part2Logic(line, false)
            var secondResult = part2Logic(line, true)
            result += (firstResult+secondResult).toInt()
        }
        return result
    }

    val testInput = readInput("input")
    part1(testInput).println()
    part2(testInput).println()

}
enum class numberEnum(val value: Int) {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9)
}
