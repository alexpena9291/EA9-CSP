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

## Notes on Performance

The perfomance got a little better as we added arc consistency but the rest of the constraints didn't actually affect it much. The basic model actually seemed to perform very well

# Writeup for Spatial Layout CSP

### Notation

> **|** means _close to_.

> **@(x, y)** means _located at_.

#### Unary Constraints

> cemetery @(0, 0)

> lake @ (1, 2)

#### Binary Constraints for Spatial Layout

> housing complex !| cemetery

> housing complex !| garbage dump

> big hotel !| cemetery

> big hotel !| garbage dump

> recreational area | lake

> big hotel | recreational area

> housing complex | recreational area

## Notes on performance

The constraints for the Spatial Layout problem seemed to have a lot more impact on the time taken to solve the problem. As we added MRV it sped up a little but once we added in forward checking and arc-consistecy the problem speed up by a magnitude of 1000x. This was very cool to see.

# Writeup for Scheduling CSP

## Scheduler 1

We were able to write the schedular using the same strategies as we did above. As we added in MRV, forward checking, and arc-consistency the algorithm began to perfoam better. It still did not do as well on the medium difficulty problems.

## Scheduler 2

We were a bit stumped on the second type of scheduler and ran out of time trying to implement it.

### Reflection

In retrospect alot of time was spent making the code conform to good coding practice and less on the algorithms, perhaps next time it would be better to develop the algorithms and then refactor the code. Who knows, maybe with a large project we would appreciate the code.
