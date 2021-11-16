package Game;

import Pieces.*;
import Potision.Position;
import Potision.Uci;

import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {
    private final Piece[][] board;
    private final Scanner scanner;
    private final Uci uci;
    private boolean isWhiteTurn;

    public GameLogic() {
        this.scanner = new Scanner(System.in);
        this.board = new Piece[8][8];
        this.uci = new Uci(8);
        this.init();
    }

    private void init() {
        for (int i = 0; i < this.board.length; i++) {
            Piece[] row = this.board[i];

            if (i == 1 || i == 6) {
                boolean isWhite = i == 6;
                for (int j = 0; j < row.length; j++) {
                    this.board[i][j] = new Pawn(isWhite, new Position(i, j));
                }
            }

            if (i == 0 || i == 7) {
                boolean isWhite = i == 7;
                this.board[i][0] = new Rook(isWhite, new Position(i, 0));
                this.board[i][1] = new Knight(isWhite, new Position(i, 1));
                this.board[i][2] = new Bishop(isWhite, new Position(i, 2));
                this.board[i][3] = new Queen(isWhite, new Position(i, 3));
                this.board[i][4] = new King(isWhite, new Position(i, 4));
                this.board[i][5] = new Bishop(isWhite, new Position(i, 5));
                this.board[i][6] = new Knight(isWhite, new Position(i, 6));
                this.board[i][7] = new Rook(isWhite, new Position(i, 7));
            }
        }
    }

    public void start() {
        isWhiteTurn = true;
        printBoard();
        while (!isGameOver()) {
            printTurn();
            String userInput = askWannaDo();
            if (userInput.equals("help")) {
                printHelp();
            } else if (userInput.equals("board")) {
                printBoard();
            } else if (userInput.equals("resign")) {
                printResign();
            } else if (userInput.equals("moves")) {
                printPossibleMove();
            } else if (userInput.length() == 2 && uci.validate(userInput)) {
                printPossibleMove(userInput);
            } else if (userInput.length() == 4 && uci.validate(userInput.substring(0, 2)) && uci.validate(userInput.substring(2, 4))) {
                if (move(userInput)) {
                    printBoard();
                    promotionPawn(userInput);
                    isWhiteTurn = !isWhiteTurn;
                }
            }
            else {
                printInvalidInput();
            }
        }
    }

    public void promotionPawn(String uci) {
        Position to = this.uci.getPositionFromUci(uci.substring(2, 4));
        if (to.getRow() != 0 && to.getRow() != 7) {
            return;
        }

        if (isWhiteTurn && to.getRow() != 0 || !isWhiteTurn && to.getRow() != 7) {
            return;
        }

        if (board[to.getRow()][to.getCol()].getClass() == Pawn.class) {
            Pawn pawn = (Pieces.Pawn) board[to.getRow()][to.getCol()];
            pawn.promoted();
            board[to.getRow()][to.getCol()] = pawn.newPiece();
        }
    }

    private void printResign(){
        int winCount = 0;
        int loseCount = 0;
        if (isWhiteTurn) {
            winCount++;
            System.out.println(
                    "Game over - " +
                    winCount + "-" + loseCount +
                    " White resigned. Black won by resignation"
            );
        } else {
            winCount++;
            System.out.println(
                    "Game over - " +
                    loseCount + "-" + winCount +
                    " Black resigned. White won by resignation"
            );
        }
        System.exit(0);
    }

    private void printBoard() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j <= this.board.length; j++) {
                if (j == 8) {
                    System.out.print(" " + (i + 1));
                } else if (board[i][j] != null) {
                    System.out.print(board[i][j].getPiece() + " ");
                } else {
                    System.out.print("▢ ");
                }
            }
            System.out.println();
        }
        System.out.println("\na  b  c d  e  f  g  h\n");
    }

    private String askWannaDo() {
        System.out.println();
        System.out.print("Enter UCI (type 'help' for help): ");
        return scanner.next();
    }

    private void printTurn() {
        System.out.println((isWhiteTurn ? "White" : "Black") + " to move");
    }

    private void printHelp() {
        System.out.println(
                "* type 'help' for help\n" +
                        "* type 'board' to see the board again\n" +
                        "* type 'resign' to resign\n" +
                        "* type 'moves' to list all possible moves\n" +
                        "* type a square (e.g. b1, e2) to list possible moves for that square\n" +
                        "* type UCI (e.g. b1c3, e7e8) to make a move"
        );
    }

    private void printPossibleMove() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null || board[i][j].getIsWhite() != isWhiteTurn) {
                    continue;
                }
                printPossibleMove(uci.convertToUci(new Position(i, j)));
            }
        }
    }

    private void printPossibleMove(String from) {
        Position targetPosition = this.uci.getPositionFromUci(from);
        Piece targetPiece = board[targetPosition.getRow()][targetPosition.getCol()];

        ArrayList<String> possibleMoves = new ArrayList<>();

        System.out.println("Possible moves for " + from + ":");
        if (targetPiece == null) {
            System.out.print("{");
            System.out.print(String.join(", ", possibleMoves));
            System.out.println("}");
            return;
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Position checkPosition = new Position(i, j);
                if (targetPiece.getIsWhite() == isWhiteTurn && targetPiece.isValidMove(checkPosition, board)) {
                    possibleMoves.add(uci.convertToUci(checkPosition));
                }
            }
        }

        System.out.print("{");
        System.out.print(String.join(", ", possibleMoves));
        System.out.println("}");
    }

    private boolean move(String uci) {
        Position from = this.uci.getPositionFromUci(uci.substring(0, 2));
        Position to = this.uci.getPositionFromUci(uci.substring(2, 4));

        Piece targetPiece = board[from.getRow()][from.getCol()];
        if (targetPiece == null || targetPiece.getIsWhite() != isWhiteTurn) {
            System.out.println("Invalid move");
            return false;
        }

        return targetPiece.move(to, board);
    }

    private void printInvalidInput() {
        System.out.println("Invalid input, please try again");
    }

    private boolean isGameOver() {
        int kingCount = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Piece piece = board[i][j];
                if (piece == null || piece.getValue() != 1000) {
                    continue;
                }
                kingCount++;
            }
        }

        if (kingCount != 2) {
            System.out.println((isWhiteTurn ? "Black" : "White") + " won!");
            return true;
        }

        return false;
    }

}