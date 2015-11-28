/*	Author: 	Leanna Stecker
	Class:		Programming Languages
	Professor:	Jia Tao
	Date:		Nov 4th, 2015	*/

import java.util.Scanner

// Takes necessary steps to carry out defined portion of a checkers game
object Driver {	// Scala Class with all static methods
	// Handles the user input and carries out all necessary tasks for the checkers game
	def main(args: Array[String]) {
		val myBoard = new Board
		myBoard.setUpGame
		myBoard.printBoard(true)
		var move = ""
		var OTurn = true	// Inidicates whether it is the red player's (o's) turn or not. Initialiezed to true since red goes first.
		val scan = new Scanner(System.in)
		while (move != "quit") {
			println()
			println("Please enter a valid move (or \"quit\" to exit the program).")
			println("Format: RowCol, RowCol where Row is a capital letter A-H & Col is a number 1-8")
			if (OTurn)
				println("It is o's turn:")
			else
				println("It is x's turn:")
			move = scan.nextLine()
			if (move != "quit") {
				val preStartRow = move.charAt(0)
				val startRow = myBoard.coordConver(preStartRow)

				val preStartCol = move.slice(1, 2)
				val startCol = Integer.parseInt(preStartCol) - 1

				val preEndRow = move.charAt(4)
				val endRow = myBoard.coordConver(preEndRow)

				val preEndCol = move.slice(5, 6)
				val endCol = Integer.parseInt(preEndCol) - 1

				if (OTurn) {
						myBoard.move(startRow, startCol, endRow, endCol, 2)
						myBoard.rotateBoard180								// Rotate to black player's board orientation for output
						myBoard.printBoard(false)
						myBoard.rotateBoard180
						OTurn = false
				}
				else {
						myBoard.move(startRow, startCol, endRow, endCol, 1)	// Process black player's turn in red player's board orientation
						myBoard.printBoard(true)							// Rotate back to red player's board orientation for further processing
						OTurn = true
				}
			}
		}
		println()
		println("Exiting.")
	}
}

// Represents the game board for a game of checkers
class Board {
	val size = 8 // size * size is how many spaces are on the game board
	var board = Array.fill(size, size){ new CheckerPiece }	// The game board, with each space containing a checker piece

	// Prepares the board in accordance with the rules of checkers for the initial state of the game
	def setUpGame() {
		for (i <- 0 until size) {
			for (j <- 0 until size) {
				if (((i == 0 || i == 2) && (j%2 == 0)) || ((i == 1) && (j%2 != 0)))			// Black player's pieces take up the top 3 rows of the board
					board(i)(j).color = 1
				else if (((i == 5 || i == 7) && (j%2 != 0)) || ((i == 6) && (j%2 == 0)))	// Red player's pieces take up the bottom 3 rows of the board
					board(i)(j).color = 2
			}
		}
	}

	// Rotates the game board 90 degrees to the right
	def rotateBoard90() {
		board = board.transpose
		for (i <- 0 until size) {
			board(i) = board(i).reverse
		}
	}

	// Rotates the game board 180 degrees
	def rotateBoard180() {
		this.rotateBoard90
		this.rotateBoard90
	}

	/* 	Parameters: 	sr 		start row 		point 1/2 of start coordinate
						sc 		start column	point 2/2 of start coordinate
						er  	end row 		point 1/2 of end coordinate
						ec 		end column 		point 2/2 of end coordinate
						color					type of game piece

		Moves a game piece from of the (start row, start col) position of the board to the (end row, end col) position of the board */
	def move(sr: Int, sc:  Int, er: Int, ec: Int, color: Int) {
		board(sr)(sc).color = 0	// the start space will no longer contain a piece
		// Is the move a jump forard?
		if (sc - ec == 2 || ec - sc == 2) {
			if (color == 1)
				this.jump(sr, sc, er, ec, 1)
			else if (color == 2)
				this.jump(sr, sc, er, ec, 2)
		}else if (sc - ec == -2 || ec - sc == -2) { //jump backward for kings
			if (color == 3)
				this.jump(sr, sc, er, ec, 3)
			else if (color == 4)
				this.jump(sr, sc, er, ec, 4)
		}
		else {
			board(sr)(sc).color = 0
			if (color == 1)
				board(er)(ec).color = 1
			else if (color == 2)
				board(er)(ec).color = 2
		}
	}

	/* 	Parameters: 	sr 		start row 		point 1/2 of start coordinate
						sc 		start column	point 2/2 of start coordinate
						er  	end row 		point 1/2 of end coordinate
						ec 		end column 		point 2/2 of end coordinate
						color					type of game piece

		Moves a game piece from of the (start row, start col) position of the board to the (end row, end col) position of the board in a unique case */
	def jump(sr: Int, sc:  Int, er: Int, ec: Int, color: Int) {
		board(sr)(sc).color = 0							// the start space will no longer contain a piece
		if (color < 3) {								//regular piece jumping
			if ((sr - er == -2) && (sc - ec == 2)) { 		// x is jumping o to the left
				board(er)(er).color = color
				board(sr+1)(sc-1).color = 0
			}
			else if ((sr - er == -2) && (sc - ec == -2)) { 	// x is jumping o to the right
				board(er)(ec).color = color
				board(sr+1)(sc+1).color = 0
			}
			else if ((sr - er == 2) && (sc - ec == 2)) { 	// o is jumping x to the left
				board(er)(ec).color = color
				board(sr-1)(sc-1).color = 0
			}
			else if ((sr - er == 2) && (sc - ec == -2)) { 	// o is jumping x to the right
				board(er)(ec).color = color
				board(sr-1)(sc+1).color = 0
			}
		}else if(color > 2) {								//king piece jumping
			if ((sr - er == -2) && (sc - ec == 2)) { 		// x is jumping o to the left
				board(er)(er).color = color
				board(sr+1)(sc-1).color = 0
			}
			else if ((sr - er == -2) && (sc - ec == -2)) { 	// x is jumping o to the right
				board(er)(ec).color = color
				board(sr+1)(sc+1).color = 0
			}
			else if ((sr - er == 2) && (sc - ec == 2)) { 	// o is jumping x to the left
				board(er)(ec).color = color
				board(sr-1)(sc-1).color = 0
			}
			else if ((sr - er == 2) && (sc - ec == -2)) { 	// o is jumping x to the right
				board(er)(ec).color = color
				board(sr-1)(sc+1).color = 0
			}
		}
		//king the piece if it mkaes it across the board
		if(color == 2 && er == 0) {
			board(er)(ec).color = 4
		}else if(color == 1 && er == 7) {
			board(er)(ec).color = 3
		}
	}

	// Converts the parts of the intput coordinate that are letters to the corresponding array position for the game board
	def coordConver(letter: Char): Int = {
		val coord = letter match {
			case 'A' => 0
			case 'B' => 1
			case 'C' => 2
			case 'D' => 3
			case 'E' => 4
			case 'F' => 5
			case 'G' => 6
			case 'H' => 7
		}
		return coord
	}

	/* 	Paramter:	defaultOrientation	is board from o's perspective?
		Outputs a representation of the current state of the board neatly */
	def printBoard(defaultOrientation: Boolean) {
		println()
		if (defaultOrientation)
			println(raw"\  1 2 3 4 5 6 7 8")
		else
			println(raw"\  8 7 6 5 4 3 2 1")
		println()
		for (i <- 0 until size) {
			if (defaultOrientation) {
				i match {
					case 0 => print("A  ")
					case 1 => print("B  ")
					case 2 => print("C  ")
					case 3 => print("D  ")
					case 4 => print("E  ")
					case 5 => print("F  ")
					case 6 => print("G  ")
					case 7 => print("H  ")
				}
			}
			else {
				i match {
					case 0 => print("H  ")
					case 1 => print("G  ")
					case 2 => print("F  ")
					case 3 => print("E  ")
					case 4 => print("D  ")
					case 5 => print("C  ")
					case 6 => print("B  ")
					case 7 => print("A  ")
				}
			}
			for (j <- 0 until size) {
				board(i)(j).color match {
					case 0 => print("- ")
					case 1 => print("x ")
					case 2 => print("o ")
					case 3 => print("X ")
					case 4 => print("O ")
				}
			}
			println()
		}
	}
}

// Represents a game piece for a game of checkers
class CheckerPiece {
	var color = 0 // type of checker piece (ternary value): 0 for none, 1 for x (black), 2 for o (red), 3 for X (black king), 4 for O (red king)
}
