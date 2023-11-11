package tictactoe

import kotlin.math.abs
import kotlin.system.exitProcess

enum class GameState {
    NO_FINISH,
    DRAW,
    X_WIN,
    O_WIN,
    IMPOSSIBLE,
}


class Board() {

    private val data = Array(3) { Array(3) { "_" } }

    constructor(setup: String) : this() {
        var i = 0
        for (c in setup.chars()) {
            data[i / 3][i % 3] = "" + c.toChar()
            i++
        }
    }

    fun show() {
        println("---------")
        for (x in 0 until 3) {
            print("| ")
            for (y in 0 until 3) {
                if (data[x][y] == "_") print("  ") //print empty
                else print("${data[x][y]} ")
            }
            print("|\n")
        }
        println("---------")
    }

    private fun isColumnX(y: Int) =
        data[0][y] + data[1][y] + data[2][y] == "XXX"

    private fun isColumnO(y: Int) =
        data[0][y] + data[1][y] + data[2][y] == "OOO"

    private fun isLineX(x: Int) =
        data[x][0] + data[x][1] + data[x][2] == "XXX"

    private fun isLineO(x: Int) =
        data[x][0] + data[x][1] + data[x][2] == "OOO"

    private fun isDiagonalX() =
        data[0][0] + data[1][1] + data[2][2] == "XXX" || data[0][2] + data[1][1] + data[2][0] == "XXX"

    private fun isDiagonalO() =
        data[0][0] + data[1][1] + data[2][2] == "OOO" || data[0][2] + data[1][1] + data[2][0] == "OOO"

    private fun isEmptySpace(): Boolean {
        for (x in 0 until 3) {
            for (y in 0 until 3) {
                if (data[x][y] == "_") return true
            }
        }
        return false
    }

    private fun isLineX3() = isLineX(0) || isLineX(1) || isLineX(2)
    private fun isLineO3() = isLineO(0) || isLineO(1) || isLineO(2)
    private fun isColumnX3() = isColumnX(0) || isColumnX(1) || isColumnX(2)
    private fun isColumnO3() = isColumnO(0) || isColumnO(1) || isColumnO(2)

    private fun isImpossible(): Boolean {
        var countX = 0
        var countO = 0
        for (x in 0 until 3)
            for (y in 0 until 3) {
                if (data[x][y] == "X") countX++
                if (data[x][y] == "O") countO++
            }
        val impossibleCount = abs(countX - countO) >= 2
        val impossibleLine = isLineX3() && isLineO3()
        val impossibleColumn = isColumnX3() && isColumnO3()
        return impossibleCount || impossibleLine || impossibleColumn
    }


    fun analyze(): GameState {
        if (isImpossible()) return GameState.IMPOSSIBLE
        if (isLineX3() || isColumnX3() || isDiagonalX()) return GameState.X_WIN
        if (isLineO3() || isColumnO3() || isDiagonalO()) return GameState.O_WIN
        if (isEmptySpace()) return GameState.NO_FINISH
        return GameState.DRAW
    }

    fun markCell(x: Int, y: Int, mark : String): Boolean {
        if (!(x in 1..3 && y in 1..3)) {
            println("Coordinates should be from 1 to 3!")
            return false
        }
        if (data[x - 1][y - 1] == "X" || data[x - 1][y - 1] == "O") println("This cell is occupied! Choose another one!")
        else {
            data[x - 1][y - 1] = mark
            return true
        }
        return false
    }

}

fun main() {

    println("----------------------- BEGIN ---------------------")
    // write your code here
    //val input = readln() //"_XXOO_OX_" , //"O_OXXO_XX"
    //val board = Board(input)
    val board = Board()


    board.show()
    /*
    when(board.analyze()) {
        GameState.IMPOSSIBLE -> println("Impossible")
        GameState.NO_FINISH -> println("Game not finished")
        GameState.X_WIN -> println("X wins")
        GameState.O_WIN -> println("O wins")
        GameState.DRAW -> println("Draw")
    }
*/

    var isTurnX = true
    do {
        try {
            var (x, y) = readln().split(" ").map { s -> s.toInt() }
            var res = false
            //println("$x $y")
            if (isTurnX)
                res = board.markCell(x, y, "X")
            else
                res = board.markCell(x, y, "O")

            if (res) {
                isTurnX = !isTurnX
                board.show()
            }

            when(board.analyze()) {
                GameState.IMPOSSIBLE -> println("Impossible")
                //GameState.NO_FINISH -> println("Game not finished")
                GameState.X_WIN -> {
                    //println("Draw")   // HACK for TEST Correct
                    println("X wins")  // This is Game over
                    exitProcess(0)
                }
                GameState.O_WIN -> {
                    //println("Draw")   // HACK for TEST Correct
                    println("O wins")  // This is Game over
                    exitProcess(0)
                }
                GameState.DRAW -> {
                    println("Draw")
                    exitProcess(0)
                }
            }

        } catch (e: Exception) {
            println("You should enter numbers!")
        }
    } while (true)
}