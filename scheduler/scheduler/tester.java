package scheduler;

public class tester {
    public static void main(String[] args) {
        Generator easyProblems = new Generator(40, 2, 500, 0.95);
        SchedulingProblem testProblem = easyProblems.generateProblem(2);
        Scheduler1 test1S = new Scheduler1();
        Student[] students = testProblem.getStudentList();
        Room[] rooms = testProblem.getRoomList();
        int size = 40;
        Course[] courses = testProblem.getCourseList();
        Course[] smallCourses = new Course[size];
        for (int i = 0; i < size; ++i) {
            smallCourses[i] = courses[i];
        }
        SchedulingProblem smallProblem = new SchedulingProblem(smallCourses, rooms, students);
        ScheduleChoice[] result = test1S.schedule(smallProblem);
        for (ScheduleChoice choice : result) {
            System.out.println(choice.toString());
        }
    }
}
