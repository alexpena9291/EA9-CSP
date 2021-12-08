package scheduler;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * A stub for your first scheduler code
 */
public class Scheduler1 implements Scheduler {

	private int[][] possibleTimeSlots;
	private Room[] possibleRooms;
	private Course[] courses;
	private ArrayList<Course> reamining_courses = new ArrayList<Course>();
	private ArrayList<ScheduleChoice> remaining_choices = new ArrayList<ScheduleChoice>();
	private ScheduleChoice[] possibleChoices;

	/**
	 * @see scheduler.Scheduler#authors()
	 */
	public String authors() {
		return "Martin Pena and Meghan Callandriello";
	}

	/**
	 * @see scheduler.Scheduler#schedule(scheduler.SchedulingProblem)
	 */
	public ScheduleChoice[] schedule(SchedulingProblem pProblem) {
		this.possibleTimeSlots = get_possible_time_slots(pProblem);
		this.possibleRooms = pProblem.getRoomList();
		this.possibleChoices = get_posible_choices(pProblem.getRoomList(), get_possible_time_slots(pProblem));
		this.courses = pProblem.getCourseList();
		for (Course course : this.courses) {
			this.reamining_courses.add(course);
		}
		for (ScheduleChoice choice : this.possibleChoices) {
			this.remaining_choices.add(choice);
		}

		ArrayList<ScheduleChoice> schedule = new ArrayList<ScheduleChoice>();
		boolean res = recursive_solve(schedule, pProblem);
		ScheduleChoice[] result = choices_to_list(schedule);
		return result;
	}

	private ScheduleChoice[] get_posible_choices(Room[] rooms, int[][] timeslots) {
		ScheduleChoice[] all_schedule_choices = new ScheduleChoice[rooms.length * timeslots.length];
		int count = 0;
		for (Room room : rooms) {
			for (int[] timeslot : timeslots) {
				all_schedule_choices[count++] = new ScheduleChoice(null, room, timeslot[0], timeslot[1]);
			}
		}
		return all_schedule_choices;
	}

	private boolean recursive_solve(ArrayList<ScheduleChoice> schedule, SchedulingProblem pProblem) {
		// System.out.println(schedule.size());
		if (schedule.size() == pProblem.getCourseList().length)
			return true;
		for (Course course : get_next_courses()) {
			for (ScheduleChoice choice : get_next_choices(course)) {
				ScheduleChoice new_choice = new ScheduleChoice(course, choice.getRoom(), choice.getDay(),
						choice.getTimeSlot());
				enroll_students(new_choice, pProblem.getStudentList());
				if (is_valid_schedule_choice(new_choice, schedule)) {
					schedule.add(new_choice);
					this.reamining_courses.remove(new_choice.getCourse());
					this.remaining_choices.remove(choice);
					if (recursive_solve(schedule, pProblem))
						return true;
					this.reamining_courses.add(new_choice.getCourse());
					this.remaining_choices.add(choice);
					schedule.remove(new_choice);
				}
			}
		}
		return false;
	}

	private boolean is_valid_schedule_choice(ScheduleChoice new_choice, ArrayList<ScheduleChoice> schedule) {
		if (course_conflict(new_choice.getCourse(), schedule))
			return false;
		if (room_conflict(new_choice, schedule))
			return false;
		if (student_conflict(new_choice, schedule))
			return false;
		return true;
	}

	private boolean student_conflict(ScheduleChoice new_choice, ArrayList<ScheduleChoice> schedule) {
		ListIterator<ScheduleChoice> litr = schedule.listIterator();
		while (litr.hasNext()) {
			ScheduleChoice schedule_choice = litr.next();
			if (at_same_time(new_choice, schedule_choice))
				if (has_student_overlap(new_choice, schedule_choice))
					return true;
		}
		return false;
	}

	private boolean has_student_overlap(ScheduleChoice new_choice, ScheduleChoice schedule_choice) {
		ListIterator<Student> schedule_choice_student_itr = schedule_choice.getCourse().getStudentList().listIterator();
		while (schedule_choice_student_itr.hasNext()) {
			Student schedule_student = schedule_choice_student_itr.next();
			if (schedule_student.goesTo(new_choice.getCourse())) {
				return true;
			}
		}
		return false;
	}

	private boolean at_same_time(ScheduleChoice new_choice, ScheduleChoice schedule_choice) {
		return (schedule_choice.getDay() == new_choice.getDay()
				&& schedule_choice.getTimeSlot() == new_choice.getTimeSlot());
	}

	private boolean room_conflict(ScheduleChoice new_choice, ArrayList<ScheduleChoice> schedule) {
		ListIterator<ScheduleChoice> litr = schedule.listIterator();
		while (litr.hasNext()) {
			ScheduleChoice schedule_choice = litr.next();
			if (at_same_time(new_choice, schedule_choice))
				if (new_choice.getRoom().getRoomName().equals(schedule_choice.getRoom().getRoomName()))
					return true;
		}
		return false;
	}

	private boolean course_conflict(Course course, ArrayList<ScheduleChoice> schedule) {
		ListIterator<ScheduleChoice> litr = schedule.listIterator();
		while (litr.hasNext()) {
			ScheduleChoice schedule_choice = litr.next();
			if (schedule_choice.getCourse().getCourseName().equals(course.getCourseName()))
				return true;
		}
		return false;
	}

	private void enroll_students(ScheduleChoice choice, Student[] students) {
		for (Student student : students) {
			if (student.goesTo(choice.getCourse())) {
				choice.getCourse().enroll(student);
			}
		}
	}

	private Course[] get_next_courses() {
		return next_courses_advanced();
	}

	private Course[] next_courses_basic() {
		return this.courses;
	}

	private Course[] next_courses_advanced() {
		return courses_to_list(this.reamining_courses);
	}

	private Course[] courses_to_list(ArrayList<Course> reamining_courses) {
		Course[] converted = new Course[reamining_courses.size()];
		ListIterator<Course> litr = reamining_courses.listIterator();
		int count = 0;
		while (litr.hasNext()) {
			Course course = litr.next();
			converted[count++] = (Course) course;
		}
		return converted;
	}

	private ScheduleChoice[] get_next_choices(Course course) {
		return next_choices_advanced();
	}

	private ScheduleChoice[] next_choices_advanced() {
		return choices_to_list(this.remaining_choices);
	}

	private ScheduleChoice[] next_choices_basic() {
		return this.possibleChoices;
	}

	private int[][] get_possible_time_slots(SchedulingProblem pProblem) {
		int timeSlots = 20;
		int[][] possibleTimeSlots = new int[timeSlots][2];
		int count = 0;
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 4; ++j) {
				possibleTimeSlots[count][0] = i;
				possibleTimeSlots[count][1] = j;
				count++;
			}
		}
		return possibleTimeSlots;
	}

	private ScheduleChoice[] choices_to_list(ArrayList<ScheduleChoice> schedule) {
		ScheduleChoice[] converted = new ScheduleChoice[schedule.size()];
		ListIterator<ScheduleChoice> litr = schedule.listIterator();
		int count = 0;
		while (litr.hasNext()) {
			ScheduleChoice schedule_choice = litr.next();
			converted[count++] = (ScheduleChoice) schedule_choice;
		}
		return converted;
	}
}
