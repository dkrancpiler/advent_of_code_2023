fun main() {
    fun part1(times: List<String>, distances: List<String>): Long {
        var result = 0
        var multipliedResult = 1L
        times.forEachIndexed { index, time ->
            val timeLong = time.toLong()
            val timeRange = 0..timeLong
            timeRange.forEach {
                val distanceTraveled = it * (timeLong - it)
                if (distanceTraveled > distances[index].toLong()) result += 1
                else if (distanceTraveled < distances[index].toLong() && result > 0) return@forEach
            }
            if (result != 0) {
                multipliedResult *= result
                result = 0
            }
        }
        return multipliedResult
    }

    fun part2(times: List<String>, distances: List<String>): Long {
        val singleTime = times.joinToString("") { it }
        val singleDistance = distances.joinToString("") { it }
        return part1(listOf(singleTime), listOf(singleDistance))

    }


    val testInput = readInput("input_day06")
    val times = testInput.first().substringAfter(":").split(" ").filter { it.isNotBlank() }
    val distances = testInput[1].substringAfter(":").split(" ").filter { it.isNotBlank() }
    part1(times, distances).println()
    part2(times, distances).println()


}
