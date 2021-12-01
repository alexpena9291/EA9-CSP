package sudoku;

public class tester {
    public static void main(String[] args) {
        SudokuSolver test = new SudokuSolver();
        try {
            int[][] board = SudokuUtil
                    .readInBoard("/Users/mcallandriello/Documents/Vanderbilt/Fall2021/CS4260/EA9-CSP/sudoku/easy.sud", 9);
            test.solve(board);
            int[][] result = test.solve(board);
            System.out.println(SudokuUtil.formatBoard(result));
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
