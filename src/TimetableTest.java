import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(1, monday.size());
        assertEquals(singleTrainingSession, monday.get(0));
        assertTrue(tuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size());
        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());

        List<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(new TimeOfDay(13, 0), thursday.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(20, 0), thursday.get(1).getTimeOfDay());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> at13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        List<TrainingSession> at14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));

        assertEquals(1, at13.size());
        assertEquals(singleTrainingSession, at13.get(0));
        assertTrue(at14.isEmpty());
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Group g1 = new Group("Группа 1", Age.CHILD, 60);
        Group g2 = new Group("Группа 2", Age.CHILD, 45);
        Coach c1 = new Coach("Иванов", "Иван", "Иванович");
        Coach c2 = new Coach("Петров", "Пётр", "Петрович");

        TrainingSession s1 = new TrainingSession(g1, c1, DayOfWeek.WEDNESDAY,
                new TimeOfDay(15, 0));
        TrainingSession s2 = new TrainingSession(g2, c2, DayOfWeek.WEDNESDAY,
                new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(s1);
        timetable.addNewTrainingSession(s2);

        List<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY,
                new TimeOfDay(15, 0));
        assertEquals(2, result.size());
        assertTrue(result.contains(s1));
        assertTrue(result.contains(s2));
    }

    @Test
    void testGetCountByCoaches_SortedDescending() {
        Timetable timetable = new Timetable();
        Group group = new Group("Тест", Age.ADULT, 60);
        Coach coach1 = new Coach("А", "А", "А"); // 5 тренировок
        Coach coach2 = new Coach("Б", "Б", "Б"); // 2 тренировки
        Coach coach3 = new Coach("В", "В", "В"); // 1 тренировка

        for (int i = 0; i < 5; i++) {
            timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.values()[i % 7],
                    new TimeOfDay(10, 0)));
        }
        for (int i = 0; i < 2; i++) {
            timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.values()[i % 7],
                    new TimeOfDay(11, 0)));
        }
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SUNDAY,
                new TimeOfDay(12, 0)));

        List<Timetable.CoachTrainingCount> result = timetable.getCountByCoaches();
        assertEquals(3, result.size());
        assertEquals(5, result.get(0).getCount());
        assertEquals(2, result.get(1).getCount());
        assertEquals(1, result.get(2).getCount());
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();
        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)).isEmpty());
        assertTrue(timetable.getCountByCoaches().isEmpty());
    }
}