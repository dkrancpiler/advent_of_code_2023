import java.math.BigInteger

fun main() {

    val currentKeyList = mutableListOf<String>()

    fun parseInput(input: List<String>): MutableMap<String, Pair<String, String>> {
        val map:  MutableMap<String, Pair<String, String>> = mutableMapOf()
        input.forEach {
            val inputSplit = it.split("=")
            val key = inputSplit.first().trim()
            if (key.last() == 'A') currentKeyList.add(key)
            val pairInput = inputSplit.last().replace("(","").replace(")", "").trim()
            val splittedPair = pairInput.split(",")
            val pair = Pair(splittedPair.first(), splittedPair.last())
            map["$key"] = pair
        }
        return map
    }
// INITIAL FUNCTION
//    fun part2Math(distanceList: List<Long>): BigInteger {
//        var gcd = 0L
//        val numberlist = distanceList.toMutableList()
//        while (numberlist.filter { it == numberlist.first() }.size != numberlist.size) {
//            val maxNumber = numberlist.max()
//            val lowestNumber = numberlist.min()
//            numberlist.remove(maxNumber)
//            numberlist.add(maxNumber-lowestNumber)
//        }
//        gcd = numberlist.first()
//        val transformedDistanceList = distanceList.map {
//            it/gcd
//        }
//        var distanceMultiplication: BigInteger = 1L.toBigInteger()
//        transformedDistanceList.forEach {
//            distanceMultiplication *= it.toBigInteger()
//        }
//        return distanceMultiplication * gcd.toBigInteger()
//    }

    fun checkCombinations(combinationInput: String, mappingInput: MutableMap<String, Pair<String, String>>): Int {
        var numberOfRuns: Int = 0
        var result = 0
        var currentKey = ""
        while (result == 0) {
            combinationInput.forEachIndexed { index, character ->
                val takeLeftValue: Boolean = character == 'L'
                if(currentKey == "ZZZ") {
                    result = if(numberOfRuns != 0) numberOfRuns*combinationInput.length + index else index
                }
                if (index == 0 && currentKey.isEmpty()) {
                    val firstInput = mappingInput["AAA"]
                    currentKey = if (takeLeftValue) firstInput!!.first.trim() else firstInput!!.second.trim()
                } else {
                    val pair = mappingInput[currentKey]!!
                    currentKey = if (takeLeftValue) pair.first.trim() else pair.second.trim()
                    if (index == combinationInput.length-1) numberOfRuns += 1
                }
            }
        }
        return result
    }

    fun part1(combinationInput: String, mappingInput: MutableMap<String, Pair<String, String>>): Int {
        return checkCombinations(combinationInput, mappingInput)
    }

    fun part2(combinationInput: String, mappingInput: MutableMap<String, Pair<String, String>>): BigInteger {
        val resultList = currentKeyList.map { key ->
            var numberOfRuns: Int = 0
            var result = key
            var resultingNumberOfTries = 0L
            while (result.last() != 'Z') {
                run returningPoint@{
                    combinationInput.forEachIndexed {index, character ->
                        val takeLeftValue: Boolean = character == 'L'
                        val pair = mappingInput[result]!!
                        result = if (takeLeftValue) pair.first.trim() else pair.second.trim()
                        if (result.last() == 'Z')  {
                            resultingNumberOfTries = if (numberOfRuns != 0) (numberOfRuns * combinationInput.length + index+1).toLong() else index+1.toLong()
                            numberOfRuns = 0
                            return@returningPoint
                        }
                    }
                }
                if (result.last() != 'Z') numberOfRuns += 1
            }
            resultingNumberOfTries
        }
        return resultList
            .map { it.toBigInteger() }
            .reduce { acc, bigInteger ->
                acc * bigInteger / acc.gcd(bigInteger)
            }

    }

    val testInput = readInput("input_day_08")
    val combinationInput = testInput.first()
    val mappingInput = parseInput(testInput.drop(2))
    part1(combinationInput, mappingInput).println()
    part2(combinationInput, mappingInput).println()

}


