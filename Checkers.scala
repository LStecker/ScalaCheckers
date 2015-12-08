/*	Author: 	Leanna Stecker, Ben Meyer, John Andreula, Graham Roberts
	Class:		Programming Languages
	Professor:	Jia Tao
	Date:		Nov 4th, 2015	*/

import java.util.Scanner
import scala.math.{abs}
import scala.collection.mutable.Stack

// Takes necessary steps to carry out defined portion of a checkers game
object Driver {	// Scala Class with all static methods
	// Handles the user input and carries out all necessary tasks for the checkers game
	def main(args: Array[String]) {
		val myBoard = new Board
		myBoard.setUpGame
		myBoard.printBoard(true)
		var remainingX = 12
		var remainingO = 12
		var lastMove : (Int, Int, Int, Int, Int) = (0, 0, 0, 0, 0)	//represents the most recent move
		var moveStack = Stack[Tuple5[Int, Int, Int, Int, Int]] ()
		var redoStack = Stack[Tuple5[Int, Int, Int, Int, Int]] ()
		var move = ""
		var OTurn = true	// Inidicates whether it is the red player's (o's) turn or not. Initialiezed to true since red goes first.
		var Valid = true	// Whether user input follows requested format or not
		val scan = new Scanner(System.in)
		while (move != "quit") {
			Valid = true	//assume at start of loop input will be valid and check if false later on
			println()
			println("Please enter a valid move (or \"quit\" to exit the program).")
			println("Format: RowCol, RowCol where Row is a capital letter A-H & Col is a number 1-8")
			if (OTurn)
				println("It is o's turn (unless x wants to \"undo\" a move):")
			else
				println("It is x's turn (unless o wants to \"undo\" a move):")
			move = scan.nextLine()

			//redo stuff start
			if (move == "redo" && redoStack.size == 0){
				println("No moves to redo")
			}
			if (move == "redo" && redoStack.size > 0){
				var move = redoStack.pop
				moveStack.push(move)
				var startRow = move._1 
				var startCol = move._2
				var endRow = move._3 
				var endCol = move._4
				var turn = move._5
				myBoard.move(startRow, startCol, endRow, endCol, turn)
				if (OTurn) {
						if (startCol - endCol == 2 || endCol - startCol == 2){
							remainingX = remainingX -1
						}
						myBoard.rotateBoard180								// Rotate to black player's board orientation for output
						myBoard.printBoard(false)
						myBoard.rotateBoard180
						OTurn = false
				}
				else {
						if (startCol - endCol == 2 || endCol - startCol == 2){
							remainingO = remainingO -1
						}
						myBoard.printBoard(true)							// Rotate back to red player's board orientation for further processing
						OTurn = true
				}
				println()
				println ("Remaining pieces for X:  " + remainingX)
				println ("Remaining pieces for O:  " + remainingO)
			}
			//redo stuff end

			//undo stuff start
			if (move == "undo" && moveStack.size == 0) {
                println("No moves to undo")
            }
            if (move == "undo" && moveStack.size > 0) {
                var move = moveStack.pop
                redoStack.push(move)
                var endRow = move._1
                var endCol = move._2
                var startRow = move._3
                var startCol = move._4
                var turn = move._5
                /** Debug only
                println(endCol)
                println(endRow)
                */

                myBoard.move(startRow, startCol, endRow, endCol, turn)
                if ((startCol - endCol == 2 || endCol - startCol == 2)&& (OTurn)){
                    remainingO = remainingO +1
                 if(turn == 1){   
                    if ((endCol < startCol) && (endRow < startRow)){
                        myBoard.setBoardO(endRow + 1, endCol + 1)
                    }
                    if ((endCol > startCol) && (endRow < startRow)){
                        myBoard.setBoardO(endCol - 1, endRow + 1)
                    }
                }
                 else{
                    if ((endCol > startCol) && (endRow > startRow)){
                        myBoard.setBoardO(endRow - 1, endCol - 1)
                    }
                    if ((endCol < startCol) && (endRow > startRow)){
                        myBoard.setBoardO(endRow - 1, endCol + 1)
                    }
                }
                }
                else if ((startCol - endCol == 2 || endCol - startCol == 2)&& (!OTurn)){
                    remainingX = remainingX +1
                  if(turn == 2){  
                    if ((endCol > startCol) && (endRow > startRow)){
                        myBoard.setBoardX(endRow - 1, endCol - 1)
                    }
                    if ((endCol < startCol) && (endRow > startRow)){
                        myBoard.setBoardX(endRow - 1, endCol + 1)
                    }
                    }
                  else{
                    if ((endCol < startCol) && (endRow < startRow)){
                        myBoard.setBoardX(endRow + 1, endCol + 1)
                    }
                    if ((endCol > startCol) && (endRow < startRow)){
                        myBoard.setBoardX(endCol - 1, endRow + 1)
                    }
                }

                }

                if (OTurn) {

                        myBoard.rotateBoard180                                // Rotate to black player's board orientation for output
                        myBoard.printBoard(false)
                        myBoard.rotateBoard180
                        OTurn = false
                }
                else {

                        myBoard.printBoard(true)                            // Rotate back to red player's board orientation for further processing
                        OTurn = true
                }
                println()
                println ("Remaining pieces for X:  " + remainingX)
                println ("Remaining pieces for O:  " + remainingO)
            }
            //undo stuff end

            if (move == "restart") {
	        	myBoard.clearBoard
	            myBoard.setUpGame
                myBoard.printBoard(true)
                moveStack.clear
                redoStack.clear
                OTurn = true
                remainingX = 12
                remainingO = 12
            }

			if (move != "quit" && move != "restart" && move != "undo" && move!= "redo" && move.length() == 6) {
				val preStartRow = move.charAt(0)
				val startRow = myBoard.coordConver(preStartRow)
				if (startRow > 7 || startRow < 0) {
					Valid = false
				}

				val preStartCol = move.slice(1, 2)
				val startCol = Integer.parseInt(preStartCol) - 1
				if (startCol > 7 || startCol < 0) {
					Valid = false
				}

				val preEndRow = move.charAt(4)
				val endRow = myBoard.coordConver(preEndRow)
				if (endRow > 7 || endRow < 0) {
					Valid = false
				}

				val preEndCol = move.slice(5, 6)
			    val endCol = Integer.parseInt(preEndCol) - 1
				if(endCol > 7 || endCol < 0) {
					Valid = false
				}

	            if(Valid) {
	            	val currentColor = myBoard.getColor(startRow, startCol)
					if (OTurn) {
						if(currentColor == 2 && myBoard.validMove(startRow, startCol, endRow, endCol, currentColor)) {	//red regular
							myBoard.move(startRow, startCol, endRow, endCol, currentColor)
							if(startCol - endCol == 2 || endCol - startCol == 2) {
								remainingX = remainingX - 1
								myBoard.anotherTurn(endRow, endCol, 2, false)
							}
							myBoard.rotateBoard180								// Rotate to black player's board orientation for output
							myBoard.printBoard(false)
							myBoard.rotateBoard180
							OTurn = false
							lastMove = (startRow, startCol, endRow, endCol, 2)
							moveStack.push(lastMove)
						}
						else if(currentColor == 4 && myBoard.validMove(startRow, startCol, endRow, endCol, currentColor)){	//red king
							myBoard.move(startRow, startCol, endRow, endCol, currentColor)
							if(startCol - endCol == 2 || endCol - startCol == 2) {
								remainingX = remainingX - 1
								myBoard.anotherTurn(endRow, endCol, 4, true)
							}
							myBoard.rotateBoard180								// Rotate to black player's board orientation for output
							myBoard.printBoard(false)
							myBoard.rotateBoard180
							OTurn = false
							lastMove = (startRow, startCol, endRow, endCol, 4)
							moveStack.push(lastMove)
						}
						else {
							myBoard.invalidMove(OTurn, 1)
						}
					}
					else {
						if(currentColor == 1 && myBoard.validMove(startRow, startCol, endRow, endCol, currentColor)) {	//black regular
							myBoard.move(startRow, startCol, endRow, endCol, currentColor)	// Process black player's turn in red player's board orientation
							if(startCol - endCol == 2 || endCol - startCol == 2){
								remainingO = remainingO - 1
								myBoard.anotherTurn(endRow, endCol, 1, false)
							}
							myBoard.printBoard(true)							// Rotate back to red player's board orientation for further processing
							OTurn = true
							lastMove = (startRow, startCol, endRow, endCol, 1)
							moveStack.push(lastMove)
						}
						else if(currentColor == 3 && myBoard.validMove(startRow, startCol, endRow, endCol, currentColor)){	//black king
							myBoard.move(startRow, startCol, endRow, endCol, currentColor)	// Process black player's turn in red player's board orientation
							if(startCol - endCol == 2 || endCol - startCol == 2){
								remainingO = remainingO - 1
								myBoard.anotherTurn(endRow, endCol, 3, true)
							}
							myBoard.printBoard(true)							// Rotate back to red player's board orientation for further processing
							OTurn = true
							lastMove = (startRow, startCol, endRow, endCol, 3)
							moveStack.push(lastMove)
						}
						else {
							if(move != "quit") {
								myBoard.invalidMove(OTurn, 1)
							}
						}
					}
					println()
					println("Remaining pieces for X: " + remainingX)
					println("Remaining pieces for O: " + remainingO)
					if (remainingO == 0){
						println("X has won")
						move = "quit"
					}
					if (remainingX == 0){
						println("O has won")
						move = "quit"
					}
				}
				else{
					if(move != "quit") {
						myBoard.invalidMove(OTurn, 2)
					}
				}
			}
			else if(move != "restart" && move != "quit" && move != "redo" && move != "undo" && move.length() < 6){
				myBoard.invalidMove(OTurn, 2)
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

	// Rotates the game board 90 degrees to the right. Only to be used as a helper function for rotateBoard180
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
		/*if(validMove(sr, sc, er, ec, color)) {*/
			board(sr)(sc).color = 0	// the start space will no longer contain a piece
			// Is the move a jump forard?
			if (sc - ec == 2 || ec - sc == 2) {
				if (color == 1)
					this.jump(sr, sc, er, ec, 1)
				else if (color == 2)
					this.jump(sr, sc, er, ec, 2)
				else if (color == 3)
					this.jump(sr, sc, er, ec, 3)
				else if (color == 4)
					this.jump(sr, sc, er, ec, 4)
			}
			else {
				board(sr)(sc).color = 0
				if (color > -1)
					board(er)(ec).color = color
			}
			//king the piece if it makes it across the board
			if(color == 2 && er == 0) {
				board(er)(ec).color = 4
			}else if(color == 1 && er == 7) {
				board(er)(ec).color = 3
			}
		/*}*/
	}

	//if a piece exists to move and it is of the right color
	def validMove(sr: Int, sc:  Int, er: Int, ec: Int, color: Int): Boolean = {
		var opposingColor1 = -1 	//arbitrary
		var opposingColor2 = -1
		if(color%2 != 0) {			//if black then opponent is o or O (kinged version)
			opposingColor1 = 2 		
			opposingColor2 = 4
		}
		else if(color%2 == 0){		//if red then opponent is x or X (kinged version)
			opposingColor1 = 1
			opposingColor2 = 3
		}
		//if start cell color matches turn color, not trying to move in same row, not trying to move in same column, space moving to needs to be empty, diagonal movement 1 or 2 spaces away
		if(getColor(sr, sc) == color && sr != er && sc != ec && getColor(er, ec) == 0 && (sr - er == -1 || sr - er == 1 || sr - er == -2 || sr - er == 2) && (sc - ec == -1 || sc - ec == 1 || sc - ec == -2 || sc - ec == 2)) {
			//
			if((sr - er == 2 && sc - ec == -2) && (getColor(sr-1, sc+1) != opposingColor1 && getColor(sr-1, sc+1) != opposingColor2)) {// valid diagonal right jump
				println("part 1")
				return false
			}

			else if((sr - er == 2 && sc - ec == 2) && (getColor(sr-1, sc-1) != opposingColor1 && getColor(sr-1, sc-1) != opposingColor2)) {
				println("part 2")
				return false
			}
			else if((sr - er == -2 && sc - ec == 2) && (getColor(sr+1, sc-1) != opposingColor1 && getColor(sr+1, sc-1) != opposingColor2)) {
				println("part 3")
				return false
			}
			else if((sr - er == -2 && sc - ec == -2) && (getColor(sr+1, sc+1) != opposingColor1 && getColor(sr+1, sc+1) != opposingColor2)) {
				println("part 4")
				return false
			}
			else {
				println("part 5")
				println(color)
				return true
			}
		}
		println("part 6")
		println(color)
		return false
	}

	//handles printing of the board in the event of an invalid move
	// errCode of 1 means illegal move
	// errCode of 2 means improper format
	def invalidMove(OTurn: Boolean, errCode: Int) {
		var outString = ""
		outString += "Invalid move. "
		if(!OTurn)
		{
			this.rotateBoard180
			this.printBoard(OTurn)
			this.rotateBoard180
		}
		else{
			this.printBoard(OTurn)
		}
		if(errCode == 1) {
			outString += "That move is not possible or legal. "
		}
		else {
			outString += "Check the format instructions. "
		}
		println()
		println(outString)
	}

	/* 	Parameters: 	sr 		start row 		point 1/2 of start coordinate
						sc 		start column	point 2/2 of start coordinate
						er  	end row 		point 1/2 of end coordinate
						ec 		end column 		point 2/2 of end coordinate
						color					type of game piece

		Moves a game piece from of the (start row, start col) position of the board to the (end row, end col) position of the board in a unique case */
	def jump(sr: Int, sc:  Int, er: Int, ec: Int, color: Int) {
		println(color)
		board(sr)(sc).color = 0							// the start space will no longer contain a piece
		if (color < 3) {								// regular piece jumping
			if ((sr - er == -2) && (sc - ec == 2)) { 		// x is jumping o to the left
				board(er)(ec).color = color
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
		else if(color > 2) {								//king piece jumping
			if ((sr - er == -2) && (sc - ec == 2)) { 		// x is jumping o to the left
				board(er)(ec).color = color
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
	}

	// allows for multijumps after checking if possible after a player makes a move that results in a jump and checking
	def anotherTurn(sr: Int, sc: Int, color: Int, king: Boolean){
		var srVar = sr 	
		var scVar = sc
		var choseJump = false	//did player choose to make another jump?

		do{	
			val moreJumps = this.adjacencyChecker(srVar, scVar, color, king)
			if(moreJumps._1 == true && !choseJump) {
				choseJump = this.multiMove(srVar, scVar, srVar-2, scVar-2, color, "NW")
				if(choseJump) {
					srVar = srVar-2
					scVar = scVar-2
				}
			}
			if(moreJumps._2 == true && !choseJump) {
				choseJump = this.multiMove(srVar, scVar, srVar-2, scVar+2, color, "NE")
				if(choseJump) {
					srVar = srVar-2
					scVar = scVar+2
				}
			}
			//sw & se will never be true for reg pieces since they cannot move that way
			if(king){
				if(moreJumps._3 == true && !choseJump) {
					choseJump = this.multiMove(srVar, scVar, srVar+2, scVar-2, color, "SW")
					if(choseJump) {
						srVar = srVar+2
						scVar = scVar-2
					}
				}
				if(moreJumps._4 == true && !choseJump) {
					choseJump = this.multiMove(srVar, scVar, srVar+2, scVar+2, color, "SE")
					if(choseJump) {
						srVar = srVar+2
						scVar + scVar+2
					}
				}
			}
		}
		while(choseJump)
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
			case  _  => 8                 // greater than 8 is invalid
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

	def multiMove(sr: Int, sc:  Int, er: Int, ec: Int, color: Int, direction: String): Boolean = {	//if adjacency checker
		val multiScan = new Scanner(System.in)
		var anotherMove = ""
		println("Would you like to jump again to the " + direction + "? (y/n)")
		anotherMove = multiScan.nextLine()
		if(anotherMove == "y"){
			this.move(sr, sc, er, sc, color)
			return true
		} 
		return false
	}

	def adjacencyChecker(r: Int, c: Int, color: Int, king: Boolean): Tuple4[Boolean, Boolean, Boolean, Boolean] = {
		var red = false							//bool for color of piece
		if(color%2 == 0)
			red = true

		var nw = false							//what corners contain pieces?
		var ne = false
		var sw = false
		var se = false

		val nwC = this.getColor(r-1, c-1)		//all 1-space away diagonals
		val neC = this.getColor(r-1, c+1)
		val swC = this.getColor(r+1, c-1)
		val seC = this.getColor(r+1, c+1)

		val nwC2 = this.getColor(r-2, c-2)		//all 2-spaces away diagonals
		val neC2 = this.getColor(r-2, c+2)
		val swC2 = this.getColor(r+2, c-2)
		val seC2 = this.getColor(r+2, c+2)

		if(red) {
			//if any diagonals are black
			if(nwC%2 != 0 && nwC2 == 0) 
				nw = true
			if(neC%2 != 0 && neC2 == 0)
				ne = true
			if(king) {
				if(swC%2 != 0 && swC2 == 0)
					sw = true
				if(seC%2 != 0 && seC2 == 0)
					se = true
			}
		}
		else {
			//if any diagonals are white
			if(nwC%2 == 0 && nwC2 == 0)
				nw = true
			if(neC%2 == 0 && neC2 == 0)
				ne = true
			if(king) {
				if(swC%2 == 0 && swC2 == 0)
					sw = true
				if(seC%2 == 0 && seC2 == 0)
					se = true
			}
		}
		return (nw, ne, sw, se)
	}

	def setBoardO(x: Int, y: Int) {
		board(x)(y).color = 2
	}

	def setBoardX(x: Int, y: Int) {
		board(x)(y).color = 1
	}

	//what kind of piece is at a certain coordinate?
	def getColor(x: Int, y: Int): Int = {
		var pieceColor = board(x)(y).color
		return pieceColor
	}

	def clearBoard() {
		for ( i <- 0 until size) {
			for (j <- 0 until size) {
				board(i)(j).color = 0
			}
		}
	}
}

// Represents a game piece for a game of checkers
class CheckerPiece {
	var color = 0 // type of checker piece (ternary value): 0 for none, 1 for x (black), 2 for o (red), 3 for X (black king), 4 for O (red king)
}