import time


class Layout:

    def __init__(self, forward_checking=False, arc_consistent=False, mrv=False):
        self.forward_checking = forward_checking
        self.arc_consistent = arc_consistent
        if mrv:
            self.building_options = [
                'recreational_area', 'housing_complex', 'big_hotel', 'garbage_dump']
        else:
            self.building_options = [
                'housing_complex', 'big_hotel', 'recreational_area', 'garbage_dump']
        self.building_location_options = {
            'housing_complex': [],
            'big_hotel': [],
            'recreational_area': [],
            'garbage_dump': []
        }
        self.grid = [[None for _ in range(3)] for _ in range(3)]
        self.grid[0][0] = "cemetery"
        self.grid[1][2] = 'lake'

    def solve_grid(self):
        start = time.time()
        result = self.recursive_solve_basic(0)
        end = time.time()
        if result:
            self.print_grid()
        else:
            print('Unable to get a solution :(')

        print('This result took: ' + str(end - start))

    def recursive_solve_basic(self, buildings_placed):
        if buildings_placed != 4:
            for building in self.get_building_options():
                for loc in self.get_next_locations(building):
                    self.place_building(loc, building)
                    if self.valid_board_state():
                        if not self.arc_consistent or self.update_building_location_options:
                            if self.recursive_solve_basic(buildings_placed + 1):
                                return True
                    self.revert_building(loc)
            return False
        return True

    def place_building(self, loc, building):
        self.grid[loc[0]][loc[1]] = building

    def revert_building(self, loc):
        self.set_grid(loc, None)

    def valid_board_state(self):
        return (
            self.valid_big_hotel()
            and self.valid_recreational_area()
            and self.valid_garbage_dump()
            and self.valid_housing_complex()
            and self.no_duplicates()
        )

    def no_duplicates(self):
        for building in self.building_options:
            if self.count(building) > 1:
                return False
        return True

    def count(self, item):
        count = 0
        for row in self.grid:
            for building in row:
                if item == building:
                    count += 1
        return count

    def valid_big_hotel(self):
        loc = self.find_first_location('big_hotel')
        if loc != None:
            return (self.is_close_to(loc, 'recreational_area')
                    and not self.is_close_to(loc, 'cemetery')
                    and not self.is_close_to(loc, 'graveyard'))
        else:
            return True

    def valid_recreational_area(self):
        loc = self.find_first_location('recreational_area')
        if loc != None:
            return self.is_close_to(loc, 'lake')
        else:
            return True

    def valid_garbage_dump(self):
        return True

    def valid_housing_complex(self):
        loc = self.find_first_location('housing_complex')
        if loc != None:
            return (self.is_close_to(loc, 'recreational_area')
                    and not self.is_close_to(loc, 'cemetery')
                    and not self.is_close_to(loc, 'graveyard'))
        else:
            return True

    def update_building_location_options(self):
        return (
            self.update_garbage_dump()
            and self.update_recreational_area()
            and self.update_housing_complex()
            and self.update_big_hotel()
        )

    def update_garbage_dump(self):
        if self.find_first_location('garbage_dump') == None:
            return True
        possibilities = []
        for loc in self.next_locations_basic():
            if not(self.is_close_to(loc, 'housing_complex') or self.is_close_to(loc, 'big_hotel')):
                possibilities.append((loc))
        if len(possibilities) > 0:
            self.building_location_options['garbage_dump'] = possibilities
            return True
        else:
            return False

    def update_recreational_area(self):
        if self.find_first_location('recreational_area') == None:
            return True
        possibilities = []
        for loc in self.next_locations_basic():
            if self.is_close_to(loc, 'lake'):
                possibilities.append(loc)
        if len(possibilities) > 0:
            self.building_location_options['recreational_area'] = possibilities
            return True
        else:
            return False

    def update_housing_complex(self):
        if self.find_first_location('housing_complex') == None:
            return True
        possibilities = []
        for loc in self.next_locations_basic():
            if self.is_close_to(loc, 'recreational_area') and not self.is_close_to(loc, 'garbage_dump') and not self.is_close_to(loc, 'cemetery'):
                possibilities.append(loc)
        if len(possibilities) > 0:
            self.building_location_options['housing_complex']
            return True
        else:
            return False

    def update_big_hotel(self):
        if self.find_first_location('big_hotel') == None:
            return True
        possibilities = []
        for loc in self.next_locations_basic():
            if self.is_close_to(loc, 'recreational_area') and not self.is_close_to(loc, 'garbage_dump') and not self.is_close_to(loc, 'cemetery'):
                possibilities.append(loc)
        if len(possibilities) > 0:
            self.building_location_options['big_hotel'] = possibilities
            return True
        else:
            return False

    def get_next_locations(self, building):
        if self.forward_checking:
            return self.next_locations_forward_checking(building)
        else:
            return self.next_locations_basic()

    def next_locations_forward_checking(self, building):
        return self.building_location_options[building]

    def next_locations_basic(self):
        locations = []
        for i in range(3):
            for j in range(3):
                if self.grid[i][j] == None:
                    locations.append((i, j))
        return locations

    def find_first_location(self, target):
        for row in range(3):
            for col in range(3):
                if self.grid[row][col] == target:
                    return (row, col)
        return None

    def is_close_to(self, loc, target):
        for pair in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            x, y = loc[0] + pair[0], loc[1] + pair[1]
            if self.in_bounds(x, y) and self.grid[x][y] == target:
                return True
        return False

    def in_bounds(self, x, y):
        return (x >= 0 and x < 3 and y >= 0 and y < 3)

    def get_building_options(self):
        if self.forward_checking:
            return self.next_building_forward_checking()
        else:
            return self.next_buildings_basic()

    def next_building_forward_checking(self):
        options = [building for building in self.building_options]
        for row in self.grid:
            for item in row:
                if item in self.building_options:
                    options.remove(item)
        return options

    def next_buildings_basic(self):
        return self.building_options

    def print_grid(self):
        for line in self.grid:
            print(line)

    def set_grid(self, loc, item):
        self.grid[loc[0]][loc[1]] = item
