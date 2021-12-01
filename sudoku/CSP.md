# Writeup for SudokuSolver

## CSP used to solve the Sudoku Problem

The sudoku board is a 9x9 grid consiting of 81 squares divided into a 3x3 grid of 9 subsections of 9 squares. (fix wording?)
In order to have a successfully solved sudoku board one must place numbers in all 81 squares such that no row, column, or subsection contains duplicate numbers.

### Notation

> For a square X(i)(j), **i** will denote the row that the square is in and **j** will denote the column.

> The notation **//** will represent integer division. See [Integer Division](https://mathworld.wolfram.com/IntegerDivision.html)

### Binary Constraints - Is there a better way to write this?

#### Row Constraint

> Given X(a)(b), for j in range 1,2,...,9: X(a)(b) != X(a)(j) unless b == j

#### Column Constraint

> Given X(a)(b), for i in range 1,2,...,9: X(a)(b) != X(i)(b) unless a == i

#### Subsection Constraint

> Given X(a)(b)
> for i in range a//3 + 1, a//3 + 2, a//3 + 3
> and j in range b//3 + 1, b//3 + 2, b//3 + 3:
> X(a)(b) != X(i)(j) unless b == j and a == i
