fun main() {

    fun getNumberOfMatches(line: String): Int {
        var numberMatches = 0
        val results = line.substringAfter(":").split("|")
        val winningNumbers = results.first().split(" ").filter { it.trim().isNotEmpty() }
        val pulledNumbers = results[1].split(" ").filter { it.trim().isNotEmpty() }
        winningNumbers.forEach {
            if (pulledNumbers.contains(it)) numberMatches++
        }
        return numberMatches
    }

    fun part1(input: List<String>): Int {
        var finalResult = 0
        input.forEach { line ->
            var lineResult = 0
            val numberMatches = getNumberOfMatches(line)
            if (numberMatches > 0) {
                for (i in 1..numberMatches) {
                    lineResult = if (lineResult == 0) 1
                    else lineResult * 2
                }
            }
            finalResult += lineResult
        }
        return finalResult
    }

    fun part2(input: List<String>): Int {
        val numberOfCopies = mutableListOf<Int>()
        input.forEachIndexed { index, _ ->
            numberOfCopies.add(index, 1)
        }
        var finalResult = 0
        input.forEachIndexed  { index, line ->
            val numberOfCurrentCopies = numberOfCopies[index]
            var numberMatches = getNumberOfMatches(line)
            if (numberMatches > 0) {
                for (i in 1..numberMatches) {
                    numberOfCopies[index + i] = numberOfCopies[index + i] + (1 * numberOfCurrentCopies)
                }
            }
            finalResult += numberOfCurrentCopies
        }
        return finalResult
    }

    val testInput = readInput("input_day4")
    part1(testInput).println()
    part2(testInput).println()

}
