import kotlin.math.absoluteValue

fun main() {

    val testInput = readInput("input_day_10")
    val pathList = parseInputAndGetAllCharacters(testInput)
    part1(pathList).println()
    part2(pathList).println()
    part2SurfaceSolution(pathList).println()
}

fun part2SurfaceSolution(input: List<PipeField>): Int {
//    MY INITIAL SOLUTION
    val lineSequence = input.map {
        it.lineIndex
    }.asSequence()
    val rowSequence = input.map {
        it.rowIndex
    }.asSequence()
    val mappedLineSequence = lineSequence.mapIndexed { index, i ->
        val newIndex = if (index < lineSequence.count()-1) index + 1 else 0
        i * rowSequence.elementAt(newIndex)

    }
    val mappedRowSequence = rowSequence.mapIndexed { index, i ->
        val newIndex = if (index < rowSequence.count()-1) index + 1 else 0
        i * lineSequence.elementAt(newIndex)
    }
    val surfaceResult = (mappedLineSequence.sum() - mappedRowSequence.sum())/2
    return surfaceResult.absoluteValue - input.size/2
}

fun parseInputAndGetAllCharacters(input: List<String>): List<PipeField> {
    val lineWithAnimal = input.first{it.contains("S")}
    val lineIndexThatHasAnimal = input.indexOf(lineWithAnimal)
    val rowIndexOfAnimal = lineWithAnimal.indexOf(lineWithAnimal.first{it == 'S'})
    val listOfPossibleInitialPipes = mutableListOf<PipeField>()
    if (lineIndexThatHasAnimal != 0) {
        val charAbove = input.get(lineIndexThatHasAnimal - 1).elementAt(rowIndexOfAnimal)
        if (charAbove == '|' || charAbove == 'F' || charAbove == '7') listOfPossibleInitialPipes.add(PipeField(charAbove, lineIndexThatHasAnimal-1, rowIndexOfAnimal, charAbove.getCharDirection(Direction.NORTH)))
    }
    if (lineIndexThatHasAnimal != input.size - 1) {
        val charBelow = input.get(lineIndexThatHasAnimal + 1).elementAt(rowIndexOfAnimal)
        if (charBelow == '|' || charBelow == 'L' || charBelow == 'J') listOfPossibleInitialPipes.add(PipeField(charBelow, lineIndexThatHasAnimal+1, rowIndexOfAnimal, charBelow.getCharDirection(Direction.SOUTH)))
    }

    if (rowIndexOfAnimal != 0) {
        val previousChar = lineWithAnimal.get(rowIndexOfAnimal - 1)
        if (previousChar == '-' || previousChar == 'L' || previousChar == 'F') listOfPossibleInitialPipes.add(PipeField(previousChar, lineIndexThatHasAnimal, rowIndexOfAnimal - 1, previousChar.getCharDirection(Direction.WEST)))
    }
    if (rowIndexOfAnimal != lineWithAnimal.length -1) {
        val nextChar = lineWithAnimal.get(rowIndexOfAnimal + 1)
        if (nextChar == '-' || nextChar == 'J' || nextChar == '7') listOfPossibleInitialPipes.add(PipeField(nextChar, lineIndexThatHasAnimal, rowIndexOfAnimal + 1, nextChar.getCharDirection(Direction.EAST)))
    }
    val getStartField = listOfPossibleInitialPipes.filter { it.direction != null }.first()
    val resultingFields = mutableListOf(getStartField)
    var isEndReached = false
    while (resultingFields.size == 1 || !isEndReached) {
        val currentField = resultingFields.last()
        currentField.direction?.let { direction ->
            val copiedField = currentField.getIndexesForNextValue(direction)
            copiedField?.let { copiedFieldNonNull ->
                val newChar = input.get(copiedFieldNonNull.lineIndex).get(copiedFieldNonNull.rowIndex)
                val newDirection = newChar.getCharDirection(direction)
                resultingFields.add(copiedFieldNonNull.copy(value = newChar, direction = newDirection))
            }
        }
        isEndReached = listOfPossibleInitialPipes.filter {
            val direction = it.direction != null
            val lastResult = resultingFields.last()
            direction && it.rowIndex == lastResult.rowIndex && it.lineIndex == lastResult.lineIndex
        }.isNotEmpty()
    }
    return resultingFields
}

fun part2(input:List<PipeField>): Int {
    val test = input.fold(0 to 0) { (sum, d), field ->
        when (field.direction!!) {
            Direction.NORTH -> sum to d +1
            Direction.SOUTH -> sum to d -1
            Direction.WEST -> sum - d to d
            Direction.EAST -> sum +d to d
        }
    }
    return test.first.absoluteValue - (input.size/2)

}

fun part1(input: List<PipeField>): Int {
    var result = input.size/2
    if (input.size % 2 == 1) result++
    return result
}

fun PipeField.getIndexesForNextValue(direction: Direction?): PipeField? {
    return when (direction) {
        Direction.NORTH -> this.copy(lineIndex = lineIndex-1)
        Direction.SOUTH -> this.copy(lineIndex = lineIndex+1)
        Direction.EAST -> this.copy(rowIndex = rowIndex+1)
        Direction.WEST -> this.copy(rowIndex = rowIndex-1)
        null -> null
    }
}

fun Char.getCharDirection(comingFrom: Direction): Direction? {
    return when (this) {
        '|' -> if (comingFrom == Direction.SOUTH) Direction.SOUTH else Direction.NORTH
        '-' -> if (comingFrom == Direction.WEST) Direction.WEST else Direction.EAST
        'L' -> if (comingFrom == Direction.WEST) Direction.NORTH else Direction.EAST
        'J' -> if (comingFrom == Direction.EAST) Direction.NORTH else Direction.WEST
        '7' -> if (comingFrom == Direction.EAST) Direction.SOUTH else Direction.WEST
        'F' -> if (comingFrom == Direction.WEST) Direction.SOUTH else Direction.EAST
        else ->  null
    }
}

enum class Direction () {
    NORTH, SOUTH, EAST, WEST
}

data class PipeField(val value: Char, val lineIndex: Int, val rowIndex: Int, val direction: Direction?)

