# ScalaCheckers
Programming Languages 2015 Scala Group

## Compilation and Running
To compile the program, navigate to the Checkers.scala file and type `scalac Checkers.scala`.

Once the program has compiled, it can be run with the command `scala Driver`.

To exit the game, type `quit` as a command within the game.

## Checkers Implementation
In this project, checkers is implemented as a command line game where the move that a player wants to make is typed into the command line.

## Gameplay
Each player will take turns moving one of their piece's on the board. The board will rotate according the current player's turn. The coordinates will always match and will rotate with the board.

### Moving
The commands for moving a piece are formatted as follows: `A1, B2`. This would move a piece that is at A1 to position B2, if it is empty.

If a jump is to be performed for that move, you must specify the ending square that the piece will land on. For example, if you had a piece at B3 and you wanted to jump over your opponent's piece at C4 and land on D5, the command to make this move would be `B3, D5`.

### Kings
On the board, kings are shown via an uppercase 'X' or 'O'.

Kings are represented via the color variable in the CheckerPiece class. A 3 represents a black king (X) and a 4 represents and red king (O).
