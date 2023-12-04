fun main() {

    fun parseInputToIndexedSymbols(input: List<String>, getOnlyStars:Boolean = false): List<Symbol> {
        var number = ""
        var numberStartIndex = -1
        val listOfSymbolsAndNumbers = mutableListOf<Symbol>()
        input.forEachIndexed { rowIndex, string ->
            string.forEachIndexed { characterIndex, c ->
                if (c.isDigit()) {
                    number += c
                    if (numberStartIndex == -1) numberStartIndex = characterIndex
                }
                else {
                    if (c != '.') {
                        if (getOnlyStars) {
                            if (c == '*') listOfSymbolsAndNumbers.add(Symbol(c.toString(), rowIndex, characterIndex))
                        } else listOfSymbolsAndNumbers.add(Symbol(c.toString(), rowIndex, characterIndex))
                    }
                    if (number.isNotEmpty()) {
                        listOfSymbolsAndNumbers.add(Symbol(number, rowIndex, numberStartIndex))
                        number = ""
                        numberStartIndex = -1
                    }
                }
            }
        }
        return listOfSymbolsAndNumbers
    }

    fun part1(input: List<String>): Int {
        var result = 0
        val elementList = parseInputToIndexedSymbols(input)
        val numberList = elementList.filter { it.isSymbolNumber() }
        val symbolList = elementList.filter { !it.isSymbolNumber() }
        numberList.forEach { number ->
            val filteredSymbolList = symbolList.filter {
                it.startIndex in number.getIndexRange() && it.row in number.getRowRange()
            }
            if (filteredSymbolList.isNotEmpty()) result += number.value.toInt()
        }
        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0
        val elementList = parseInputToIndexedSymbols(input, true)
        val numberList = elementList.filter { it.isSymbolNumber() }
        val symbolList = elementList.filter { !it.isSymbolNumber() }
        symbolList.forEach { symbol ->
            val filteredNumberList = numberList.filter {number ->
                val numberIndexRange = number.getIndexRange(1)
                var filterOutByIndex = true
                numberIndexRange.forEach {
                    if (it in symbol.getIndexRange()) {
                        filterOutByIndex = false
                    }
                }
                symbol.row in number.getRowRange() && !filterOutByIndex
            }
            if (filteredNumberList.size == 2) {
                result += filteredNumberList[0].value.toInt() * filteredNumberList[1].value.toInt()
            }
        }
        return result
    }

    val testInput = readInput("input_day3")
    part1(testInput).println()
    part2(testInput).println()

}
data class Symbol (val value: String, val row: Int, val startIndex: Int) {

    fun getRowRange(): IntRange = row - 1 .. row + 1
    fun getIndexRange(reduceRangeBy: Int = 0): IntRange = startIndex - 1 + reduceRangeBy ..startIndex + value.length - reduceRangeBy
    fun isSymbolNumber(): Boolean = value.firstOrNull { it.isDigit() } != null
}
