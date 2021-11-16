package Pieces;

import Potision.Position;

import java.util.Objects;
import java.util.Scanner;

public class Pawn extends Piece{
    private Piece newPiece;
    private static final int VALUE = 1;

    public Pawn(boolean isWhite, Position position) {
        super(VALUE, isWhite, position);
    }

    public static String getUserInput(String prompt) {
        System.out.println(prompt);
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    public void promoted() {
        while (true) {
            try{
                String userOptionInput = getUserInput(
                        "Select the desired Piece to promote your Pawn:\n" +
                                "Q = Queen\n" +
                                "R = Rook\n" +
                                "B = Bishop\n" +
                                "K = Knight"
                );

                if (Objects.equals(userOptionInput, "Q") || Objects.equals(userOptionInput, "q")){
                    this.newPiece =  new Queen(this.getIsWhite(), this.position);
                    break;
                }

                else if (Objects.equals(userOptionInput, "R") || Objects.equals(userOptionInput, "r")){
                    this.newPiece =  new Rook(this.getIsWhite(), this.position);
                    break;
                }

                else if (Objects.equals(userOptionInput, "B") || Objects.equals(userOptionInput, "b")){
                    this.newPiece =  new Bishop(this.getIsWhite(), this.position);
                    break;
                }

                else if (Objects.equals(userOptionInput, "K") || Objects.equals(userOptionInput, "k")){
                    this.newPiece =  new Knight(this.getIsWhite(), this.position);
                    break;
                }
            }
            catch (Exception e){
                System.out.println("Invalid Input! Choose a correct Piece.");
            }
        }
    }

    public Piece newPiece() {
        return newPiece;
    }

    @Override
    public String getPiece() {
        if (getIsWhite()) {
            return "♟";
        }
        return "♙";
    }

    @Override
    public boolean move(Position newPosition, Piece[][] board) {
        int newCol = newPosition.getCol();
        int newRow = newPosition.getRow();
        int col = this.position.getCol();
        int row = this.position.getRow();

        if (isValidMove(newPosition, board)) {
            board[row][col] = null;
            this.position = newPosition;
            board[newRow][newCol] = this;
            return true;
        } else {
            System.out.println("Invalid move!");
            System.out.println("Pawn moves only forward by 1 or en passant");
            return false;
        }
    }


    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        if(!super.isValidMove(position, board)){
            return false;
        }

        int newCol = newPosition.getCol();
        int newRow = newPosition.getRow();
        int col = this.position.getCol();
        int row = this.position.getRow();
        Piece pawn = board[newRow][newCol];
        if (pawn != null) {
            if (pawn.getIsWhite() == this.getIsWhite()) {
                return false;
            }
        }
        if (pawn == null) {
            if (this.getIsWhite()) {
                return col == newCol && ((newRow == row - 1) || (row == 6 && newRow == row - 2));
            }
            else {
                return col == newCol && ((newRow == row + 1) || (row == 1 && newRow == row + 2));
            }
        }
        else {

            boolean a = (newCol == col + 1) || (newCol == col - 1);
            if (this.getIsWhite()) {
                return newRow == row - 1 && a;
            }
            else {
                return newRow == row + 1 && a;
            }

        }
    }
}
