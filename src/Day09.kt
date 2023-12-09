fun main() {

    fun List<Long>.getMappedList(): MutableList<Long> = this.mapIndexed { index, number ->
            if (index != 0) {
                val result = number - this[index-1]
                result
            } else 0
        }.toMutableList()

    fun List<Long>.getRequiredValue(isPart2: Boolean, dontDrop: Boolean = false) = if (isPart2)  {
        if (dontDrop)  this.first()
        else this.drop(1).first()
    } else this.last()

    fun parseInput(input:List<String>, isPart2: Boolean = false) = input.map { line ->
        val endValueList = mutableListOf<Long>()
        val separatedInput = line.split(" ").map {
            it.toLong()
        }
        endValueList.add(separatedInput.getRequiredValue(isPart2, true))
        val mappedLine = separatedInput.getMappedList()
        endValueList.add(mappedLine.getRequiredValue(isPart2))
        while (mappedLine.filter { it != 0L }.size != 0) {
            val newMappedLine = mappedLine.drop(1).getMappedList()
            endValueList.add(newMappedLine.getRequiredValue(isPart2))
            mappedLine.removeAll(mappedLine)
            mappedLine.addAll(newMappedLine)
        }
        if (isPart2) {
            val reversedEndValues = endValueList.reversed()
            val resultingList = mutableListOf<Long>()
            reversedEndValues.forEachIndexed  { index, number ->
                    if (index != 0) {
                        resultingList.add(number-resultingList.last())
                    } else resultingList.add(0)
                }
            resultingList.last()
        }
         else endValueList.sum()
    }



    fun part1(input: List<String>): Long {
        val results = parseInput(input)
        return results.sum()
    }


    fun part2(input: List<String>): Long {
        val results = parseInput(input, true)
        return results.sum()
    }

    val testInput = readInput("input_day_09")
    part1(testInput).println()
    part2(testInput).println()

}
