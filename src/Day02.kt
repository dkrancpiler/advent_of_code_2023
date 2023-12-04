fun main() {
    fun part1(input: List<String>): Int {
        val newList = input.map {
            val listOfGames = it.substringAfter(":").split(";")
            var isPossible = true
            listOfGames.forEach {game ->
                val singleCubeList = game.split(",")
                singleCubeList.forEach {singleCubeDraw ->
                    Cubes.values().forEach {cube ->
                        val digit = singleCubeDraw.filter { it.isDigit() }
                        if (singleCubeDraw.substringAfter("$digit ") == cube.name.lowercase() && digit.toInt() > cube.limit) isPossible = false
                    }
                }

            }
            if (isPossible) return@map it.substringBefore(":").filter { it.isDigit() }
            else ""
        }
        return  newList.filter { it.isNotEmpty() }.sumOf {
            it.toInt()
        }
    }

    fun part2 (input: List<String>): Int {
        var totalResult = 0
        input.forEach {
            val listOfGames = it.substringAfter(":").split(";")
            val cubeList: MutableList<CubeModel> = mutableListOf()
            listOfGames.forEach {game ->
                val singleCubeList = game.split(",")
                singleCubeList.forEach {singleCubeDraw ->
                    val digit = singleCubeDraw.filter { it.isDigit() }
                    val name = singleCubeDraw.substringAfter("$digit ")
                    val cube = CubeModel(name, digit.toInt())
                    cubeList.add(cube)
                }
            }
            val newList = cubeList.groupBy {
                it.color
            }
            var redResult = 0
            var blueResult = 0
            var greenResult = 0
            newList.forEach { name, cubeModels ->
                when (name) {
                    Cubes.BLUE.name.lowercase() -> {
                        cubeModels.forEach {
                           if (it.numberValue > blueResult) blueResult = it.numberValue
                        }
                    }
                    Cubes.GREEN.name.lowercase() -> {
                        cubeModels.forEach {
                            if (it.numberValue > greenResult) greenResult = it.numberValue
                        }
                    }
                    Cubes.RED.name.lowercase() -> {
                        cubeModels.forEach {
                            if (it.numberValue > redResult) redResult = it.numberValue
                        }
                    }
                }
            }
            totalResult += redResult * blueResult * greenResult
        }
        return totalResult
    }

    val testInput = readInput("input_day2")
    part1(testInput).println()
    part2(testInput).println()
}

data class CubeModel(
    val color: String,
    val numberValue: Int
)
enum class Cubes (val limit: Int) {
    BLUE(14), GREEN(13), RED(12)
}

