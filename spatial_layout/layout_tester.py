from Layout import *

basic_layout = Layout()
forward_layout = Layout(forward_checking=True)
mrv_layout = Layout(mrv=True)
arc_layout = Layout(arc_consistent=True)
three_layout = Layout(forward_checking=True, mrv=True, arc_consistent=True)

basic_layout.solve_grid()
forward_layout.solve_grid()
mrv_layout.solve_grid()
three_layout.solve_grid()
