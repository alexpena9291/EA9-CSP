package sudoku;

public class tester {
    public static void main(String[] args) {
        SudokuSolver test = new SudokuSolver();
        try {
            int[][] board = SudokuUtil
                    .readInBoard("/Users/alexpena/Personal/Vanderbilt/Fall2021/AI/EA9-CSP/sudoku/easy.sud", 9);
            test.solve(board);
            System.out.println(tt);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
