fun main() {

    fun parseInput(input: List<String>): List<Field> {
        val listOfRanges = mutableListOf<FieldRange>()
        var newFieldType: MapType? = null
        val listOfMappingTypes = mutableListOf<Field>()
        input.forEachIndexed { index, line ->
            if (index > 0) {
                if (line.isBlank()) {
                    newFieldType?.let {
                        val newList = listOfRanges.map { it -> it}
                        listOfMappingTypes.add(Field(newList, it))
                    }
                    listOfRanges.removeAll { true }
                    newFieldType = null
                }
                else if (line.firstOrNull { it.isDigit() } != null) {
                    val numbers = line.split(" ").map { it.trim().toLong() }
                    val range = numbers[1]..(numbers[1] + numbers[2] - 1)
                    val step = numbers[0] - numbers[1]
                    listOfRanges.add(FieldRange(range, step))
                    if (index == input.size-1) newFieldType?.let {
                        listOfMappingTypes.add(Field(listOfRanges, it))
                    }
                } else {
                    MapType.values().forEach {
                        if (line.contains(it.stringValue)) newFieldType = it
                    }
                }
            }
        }
        return listOfMappingTypes
    }

    fun part1(seeds: List<String>, fieldTypes: List<Field>): Long {
        var result: Long? = null
        seeds.forEach {seed ->
            var currentValue = seed.toLong()
            MapType.values().forEach outer@{ mapType ->
                val currentField = fieldTypes.first { it.type == mapType }
                currentField.listOfRanges.forEach inner@{fieldRange ->
                    if (currentValue in fieldRange.range) {
                        currentValue += fieldRange.step
                        return@outer
                    }
                }
            }
            if (result == null) result = currentValue
            result?.let {
                if (it > currentValue) result = currentValue
            }
        }
        return result!!
    }

    fun part2reverse(locations: List<LongRange>, fieldTypes: List<Field>, seedRange: List<LongRange>): Long {
        locations.forEach {locationsRange ->
            println(locations)
            locationsRange.onEach {
                var currentValue = it
                var locationResult = 0L
                var resultFound = false
                MapType.values().reversed().forEachIndexed outer@{ index, mapType ->
                    if (mapType != MapType.HUMIDITY_TO_LOCATION) {
                        val currentField = fieldTypes.first { it.type == mapType }
                        currentField.listOfRanges.forEach inner@{fieldRange ->
                            val reversedRange = LongRange(fieldRange.range.first + fieldRange.step, fieldRange.range.last + fieldRange.step)
                            if (currentValue in reversedRange) {
                                currentValue += fieldRange.step
                                if (currentField.type == MapType.TEMPERATURE_TO_HUMIDITY) locationResult = it
                                if (index == MapType.values().size-1) resultFound = true
                            } else if (index == MapType.values().size-1) {
                                resultFound = false
                            }
                        }
                    }
                }
                if (resultFound) {
                    seedRange.forEach {
                        if (currentValue in it) return locationResult
                    }
                }
            }
        }
        return 0L
    }

    fun part2(seeds: List<LongRange>, fieldTypes: List<Field>): Long {
        var result: Long? = null
        seeds.forEach {seedRange ->
            seedRange.onEach {
                var currentValue = it
                MapType.values().forEach outer@{ mapType ->
                    val currentField = fieldTypes.first { it.type == mapType }
                    currentField.listOfRanges.forEach inner@{fieldRange ->
                        if (currentValue in fieldRange.range) {
                            currentValue += fieldRange.step
                            return@outer
                        }
                    }
                }
                if (result == null) result = currentValue
                result?.let {
                    if (it > currentValue) result = currentValue
                }
            }
        }
        return result!!
    }

    val testInput = readInput("input_day5_test")
    val seedsPart1 = testInput.first().substringAfter(":").split( " ").filter { it.isNotEmpty() }
    var seedsPart2 = mutableListOf<LongRange>()
    (seedsPart1.indices step 2).asSequence().forEach {
        val seedStartValue = seedsPart1[it].toLong()
        seedsPart2.add(seedStartValue..seedStartValue + seedsPart1[it+1].toLong())
    }
    val fieldTypes = parseInput(testInput)
    val locationRangePart2 = fieldTypes.first { it.type == MapType.HUMIDITY_TO_LOCATION }.listOfRanges.map {
        println(it)
        val range = LongRange(it.range.first + it.step, it.range.last + it.step)
        println(range)
        range
    }
//    println(seedsPart2)
//    part1(seedsPart1, fieldTypes).println()
//    part2(seedsPart2.toList(), fieldTypes).println()
    part2reverse(locationRangePart2.reversed(), fieldTypes, seedsPart2).println()

}

data class Field(val listOfRanges: List<FieldRange>, val type: MapType)
data class FieldRange(val range: LongRange, val step: Long)

enum class MapType(val stringValue: String) {
    SEED_TO_SOIL("seed-to-soil"),
    SOIL_TO_FERTILIZER("soil-to-fertilizer"),
    FERTILIZER_TO_WATER("fertilizer-to-water"),
    WATER_TO_LIGHT("water-to-light"),
    LIGHT_TO_TEMPERATURE("light-to-temperature"),
    TEMPERATURE_TO_HUMIDITY("temperature-to-humidity"),
    HUMIDITY_TO_LOCATION("humidity-to-location")
}
