package Pieces;

import Potision.Position;

public class Queen extends Piece{

    private static final int VALUE = 9;

    public Queen(boolean isWhite, Position position) {
        super(VALUE, isWhite, position);
    }

    @Override
    public String getPiece() {
        if (getIsWhite()) {
            return "♛";
        }
        return "♕";
    }

    @Override
    public boolean move(Position newPosition, Piece[][] board) {

        int newCol = newPosition.getCol();
        int newRow = newPosition.getRow();
        int col = this.position.getCol();
        int row = this.position.getRow();

        if (this.isValidMove(newPosition, board)) {
            board[row][col] = null;
            this.position = newPosition;
            board[newRow][newCol] = this;
            return true;
        } else {
            System.out.println("Invalid movement!");
            System.out.println("Queen only moves like bishop or rook");
            return false;
        }

    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {

        if(!super.isValidMove(position, board)){
            return false;
        }

        if (position.getRow() == newPosition.getRow() && position.getCol() == newPosition.getCol()) {
            return false;
        }

        if(
            Math.abs(newPosition.getCol() - this.position.getCol()) ==
            Math.abs(newPosition.getRow() - this.position.getRow())
        ) {
            int crossPosition = Math.abs(newPosition.getCol() - this.position.getCol()) - 1;
            if (
                newPosition.getRow() > this.position.getRow() &&
                newPosition.getCol() > this.position.getCol())
            {
                int row = this.position.getRow() + 1;
                int col = this.position.getCol() + 1;
                if (crossPosition == 0 && board[row][col] != null && board[row][col].getIsWhite() == getIsWhite()) {
                    return false;
                }
                while(crossPosition > 0) {
                    if(board[row][col] != null) {
                        return false;
                    }
                    row += 1;
                    col += 1;
                    crossPosition -= 1;
                }
                return true;
            }
            else if(
               newPosition.getRow() > this.position.getRow() &&
               newPosition.getCol() <= this.position.getCol())
            {
                int row = this.position.getRow() + 1;
                int col = this.position.getCol() - 1;
                if (crossPosition == 0 && board[row][col] != null && board[row][col].getIsWhite() == getIsWhite()) {
                    return false;
                }
                while(crossPosition > 0) {
                    if(board[row][col] != null) {
                        return false;
                    }
                    row += 1;
                    col -= 1;
                    crossPosition -= 1;
                }
                return true;
            }
            else if(
                newPosition.getRow() <= this.position.getRow() &&
                newPosition.getCol() > this.position.getCol())
            {
                int row = this.position.getRow() - 1;
                int col = this.position.getCol() + 1;
                if (crossPosition == 0 && board[row][col] != null && board[row][col].getIsWhite() == getIsWhite()) {
                    return false;
                }
                while(crossPosition > 0) {
                    if(board[row][col] != null) {
                        return false;
                    }
                    row -= 1;
                    col += 1;
                    crossPosition -= 1;
                }
                return true;

            }else{
                int row = newPosition.getRow() + 1;
                int col = newPosition.getCol() + 1;
                if (crossPosition == 0 && board[row][col] != null && board[row][col].getIsWhite() == getIsWhite()) {
                    return false;
                }
                while(crossPosition > 0) {
                    if(board[row][col] != null) {
                        return false;
                    }
                    row += 1;
                    col += 1;
                    crossPosition -= 1;
                }
                return true;
            }

        }else if(newPosition.getCol() == this.position.getCol()) {
            int minRow;
            int maxRow;
            if(newPosition.getRow() >= this.position.getRow()) {
                minRow = this.position.getRow();
                maxRow = newPosition.getRow();
            }else{
                minRow = newPosition.getRow();
                maxRow = this.position.getRow();
            }
            if (Math.abs(minRow - maxRow) == 1
                    && board[newPosition.getRow()][newPosition.getCol()] != null
                    && board[newPosition.getRow()][newPosition.getCol()].getIsWhite() == getIsWhite()
            ) {
                return false;
            }
            for(int r = minRow + 1; r < maxRow; r++) {
                if(board[r][this.position.getCol()] != null) {
                    return false;
                }
            }
            return true;

        }else if(newPosition.getRow() == this.position.getRow()){
            int minCol;
            int maxCol;
            if(newPosition.getCol() >= this.position.getCol()) {
                minCol = this.position.getCol();
                maxCol = newPosition.getCol();
            }else{
                minCol = newPosition.getCol();
                maxCol = this.position.getCol();
            }
            if (Math.abs(minCol - maxCol) == 1
                    && board[newPosition.getRow()][newPosition.getCol()] != null
                    && board[newPosition.getRow()][newPosition.getCol()].getIsWhite() == getIsWhite()
            ) {
                return false;
            }
            for(int col = minCol + 1; col < maxCol; col++) {
                if(board[this.position.getRow()][col] != null) {
                    return false;
                }
            }
            return true;

        }else{
            return false;
        }
    }
}
