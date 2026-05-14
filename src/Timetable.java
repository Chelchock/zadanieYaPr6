import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        timetable.putIfAbsent(day, new TreeMap<>());
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        daySchedule.putIfAbsent(time, new ArrayList<>());
        daySchedule.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null || daySchedule.isEmpty()) {
            return Collections.emptyList();
        }
        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            result.addAll(daySchedule.get(time));
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        return sessions != null ? new ArrayList<>(sessions) : Collections.emptyList();
    }

    public List<CoachTrainingCount> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounts.put(coach, coachCounts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CoachTrainingCount> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            result.add(new CoachTrainingCount(entry.getKey(), entry.getValue()));
        }

        // Сортировка по убыванию количества тренировок
        result.sort((o1, o2) -> Integer.compare(o2.getCount(), o1.getCount()));
        return result;
    }

    public static class CoachTrainingCount {
        private final Coach coach;
        private final int count;

        public CoachTrainingCount(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public int getCount() {
            return count;
        }

        @Override
        public String toString() {
            return coach.getSurname() + " " + coach.getName() + " " + coach.getMiddleName() + ": " + count;
        }
    }
}