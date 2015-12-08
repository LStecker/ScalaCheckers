 # ScalaCheckers
Programming Languages 2015 Scala Group

Ben Meyer, Leanna Stecker, Graham Roberts, John Andreula

Created as a final project for CSC435 - Programming Languages

		Compilation and Running

To compile the program, navigate to the Checkers.scala file and type scalac Checkers.scala.

Once the program has compiled, it can be run with the command scala Driver.

To exit the game, type quit as a command within the game.

		Checkers Implementation

In this project, checkers is implemented as a command line game where the move that a player wants to make is typed into the command line.

The 'o' (black) player will always make the first move.

		Gameplay

Each player will take turns moving one of their pieces on the board. The board will rotate according the current player's turn. The coordinates will always match and will rotate with the board.

	Moving

The commands for moving a piece are formatted as follows: A1, B2. This would move a piece that is at A1 to position B2, if it is empty.

If a jump is to be performed for that move, you will be prompted to take another turn if and only if you are able to make another move after that one. Then, you can take another move with the piece that was just moved. 

	Kings

On the board, kings are shown via an uppercase 'X' or 'O'. A piece will be made a king once it reaches the opposite side of the board from where it started. Once it has been made a king, it can travel both forwards and backwards.

Kings are represented via the color variable in the CheckerPiece class. A 3 represents a black king (X) and a 4 represents a red king (O).

	Score
	
The program keeps track of the score of the game for each team. Using teo distinct scores, players can tell how many pieces are left until the game is over. 

	Undo
	
By typing 'undo' in the command line, the program will reset the board state and turn that was present just before the last command was issued. 

	Redo

Similarly, by typing 'redo', the program will reenact the move made just prior to an 'undo' command. 