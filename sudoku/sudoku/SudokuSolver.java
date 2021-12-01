package sudoku;

import java.lang.reflect.Method;

/**
 * Place for your code.
 */
public class SudokuSolver {

	private static int[] rows = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private static int[] columns = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

	public class Point {
		int row;
		int col;

		public Point(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

	/**
	 * @return names of the authors and their student IDs (1 per line).
	 */
	public String authors() {
		// TODO write it;
		return "Martin Pena: Penama1\nMeghan Callandriello: \n";
	}

	/**
	 * Performs constraint satisfaction on the given Sudoku board using Arc
	 * Consistency and Domain Splitting.
	 * 
	 * @param board the 2d int array representing the Sudoku board. Zeros indicate
	 *              unfilled cells.
	 * @return the solved Sudoku board
	 */
	public int[][] solve(int[][] board) {
		Point currentLocation = get_next_variable(board);
		int[][] solved_board = recursive_solve(board, currentLocation);
		return (solved_board != null) ? solved_board : board;
	}

	public int[][] recursive_solve(int[][] board, Point curr) {
		if (curr == null)
			return board;

		int next_val = get_next_value(board, curr);

		while (next_val != -1) {
			board[curr.row][curr.col] = next_val;
			if (is_valid_placement(board, curr)) {
				int[][] result = recursive_solve(board, get_next_variable(board));
				if (result != null)
					return result;
			}

			next_val = get_next_value(board, curr);
		}
		return null;
	}

	public boolean is_valid_placement(int[][] board, Point curr) {
		return is_valid_row(board, curr) && is_valid_column(board, curr) && is_valid_subsection(board, curr);
	}

	public boolean is_valid_row(int[][] board, Point curr) {
		for (int col : columns) {
			if (board[curr.row][curr.col] == board[curr.row][col] && col != curr.col) {
				return false;
			}
		}
		return true;
	}

	public boolean is_valid_column(int[][] board, Point curr) {
		for (int row : rows) {
			if (board[curr.row][curr.col] == board[row][curr.col] && row != curr.row) {
				return false;
			}
		}
		return true;
	}

	public boolean is_valid_subsection(int[][] board, Point curr) {
		int row_base = (curr.row / 3) * 3;
		int col_base = (curr.col / 3) * 3;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int row = row_base + i;
				int col = col_base + j;
				if (board[row][col] == board[curr.row][curr.col] && col != curr.col && row != curr.row) {
					return false;
				}
			}
		}
		return true;
	}

	private Point get_next_variable(int[][] board) {
		for (int row : rows) {
			for (int column : columns) {
				if (board[row][column] == 0) {
					return new Point(row, column);
				}
			}
		}
		return null;
	}

	private int get_next_value(int[][] board, Point curr) {
		return next_value_basic(board, curr);
	}

	private int next_value_basic(int[][] board, Point curr) {
		int nextValue = board[curr.row][curr.col] + 1;
		if (nextValue > 9)
			return -1;
		return nextValue;

	}

}
