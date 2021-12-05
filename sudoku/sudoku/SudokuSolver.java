package sudoku;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Place for your code.
 */
public class SudokuSolver {

	private int[] ROWS = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private int[] COLUMNS = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private ArrayList<Integer>[][] options_mat = new ArrayList[9][9];

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
	public int[][] solve(int[][] board) throws RuntimeException {
		// Point nexVar = next_variable_MRV((board));
		// System.out.println(nexVar.row);
		// System.out.println(nexVar.col);
		// int deg = get_degree(board, nexVar);
		// System.out.println(deg);
		// return board;

		Point currentLocation = get_next_variable(board);
		int[][] solved_board = recursive_solve(board, currentLocation);
		if (solved_board == null) {
			throw new RuntimeException("AHHHHH");
		}
		return (solved_board != null) ? solved_board : board;
	}

	public int[][] recursive_solve(int[][] board, Point curr) {
		if (curr == null)
			return board;

		int next_val = get_next_value(board, curr);

		while (next_val != -1) {
			board[curr.row][curr.col] = next_val;
			if (is_valid_placement(board, curr)) {
				Point next_variable = get_next_variable(board);
				if (is_arc_consistent(board)) {
					int[][] result = recursive_solve(board, next_variable);
					if (result != null)
						return result;
				}

			}

			next_val = get_next_value(board, curr);
		}
		board[curr.row][curr.col] = 0;
		return null;
	}

	private boolean is_arc_consistent(int[][] board) {
		for (int col : COLUMNS) {
			for (int row : ROWS) {
				if (board[row][col] == 0 && options_mat[row][col].size() == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean is_valid_placement(int[][] board, Point curr) {
		return is_valid_row(board, curr) && is_valid_column(board, curr) && is_valid_subsection(board, curr);
	}

	public boolean is_valid_row(int[][] board, Point curr) {
		for (int col : COLUMNS) {
			if (board[curr.row][curr.col] == board[curr.row][col] && col != curr.col) {
				return false;
			}
		}
		return true;
	}

	public boolean is_valid_column(int[][] board, Point curr) {
		for (int row : ROWS) {
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

	public int get_num_matches_subsection(int[][] board, Point curr) {
		int count = 0;
		int row_base = (curr.row / 3) * 3;
		int col_base = (curr.col / 3) * 3;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int row = row_base + i;
				int col = col_base + j;
				if (board[row][col] == board[curr.row][curr.col] && col != curr.col && row != curr.row) {
					count++;
				}
			}
		}
		return count;
	}

	private int get_num_matches_col(int[][] board, Point curr) {
		int count = 0;
		for (int row : ROWS) {
			if (board[curr.row][curr.col] == board[row][curr.col] && row != curr.row) {
				count++;
			}
		}
		return count;
	}

	private int get_num_matches_row(int[][] board, Point curr) {
		int count = 0;
		for (int col : COLUMNS) {
			if (board[curr.row][curr.col] == board[curr.row][col] && col != curr.col) {
				count++;
			}
		}
		return count;
	}

	private Point get_next_variable(int[][] board) {
		return next_variable_MRV(board);
	}

	private Point next_variable_basic(int[][] board) {
		for (int row : ROWS) {
			for (int column : COLUMNS) {
				if (board[row][column] == 0) {
					return new Point(row, column);
				}
			}
		}
		return null;
	}

	private Point next_variable_MRV(int[][] board) {
		int tempRating;
		int bestRating = Integer.MIN_VALUE;
		Point mrvPoint = new Point(-1, -1);
		Point currPoint = new Point(-1, -1);
		for (int row : ROWS) {
			for (int column : COLUMNS) {
				if (board[row][column] == 0) {
					currPoint.col = column;
					currPoint.row = row;
					ArrayList<Integer> possible_values = get_possible_values(board, currPoint);

					// FORWARD CHECKING STEP
					options_mat[currPoint.row][currPoint.col] = possible_values;
					tempRating = (9 - possible_values.size()) * get_degree(board, currPoint);
					if (tempRating > bestRating) {
						bestRating = tempRating;
						mrvPoint.col = column;
						mrvPoint.row = row;
					}
				}
			}
		}
		if (mrvPoint.col < 0)
			return null;
		return mrvPoint;
	}

	private ArrayList<Integer> get_possible_values(int[][] board, Point curr) {
		ArrayList<Integer> remaingValues = new ArrayList<Integer>();
		// Get the number of remaining values
		for (int i = 9; i > 0; --i) {
			board[curr.row][curr.col] = i;
			if (is_valid_placement(board, curr)) {
				remaingValues.add(i);
			}
		}
		board[curr.row][curr.col] = 0; // Resetting Location
		return remaingValues;
	}

	private int get_degree(int[][] board, Point curr) {
		int total = get_num_matches_col(board, curr) +
				get_num_matches_row(board, curr) +
				get_num_matches_subsection(board, curr);
		return total;
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

	private int next_value_smarter(int[][] board, Point curr) {
		ArrayList<Integer> list_of_possible_vals = options_mat[curr.row][curr.col];
		if (list_of_possible_vals.size() > 0) {
			int next_val = list_of_possible_vals.get(0);
			list_of_possible_vals.remove(0);
			return next_val;
		} else {
			return -1;
		}
	}

}
