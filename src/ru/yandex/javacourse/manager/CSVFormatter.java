package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static List<Integer> historyFromString(String s) {
        String[] lines = s.split(",");
        List<Integer> history = new ArrayList<>(lines.length);
        for (String line : lines) {
            history.add(Integer.parseInt(line));
        }
        return history;
    }

    public static String historyToString(List<Task> history) {
        if (history.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(history.getFirst().getId());
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i);
            sb.append(",");
            sb.append(task.getId());
        }
        return sb.toString();
    }

    public static Task taskFromString(String[] value) {
        if (TypeOfTask.valueOf(value[1]).equals(TypeOfTask.TASK)) {
            return new Task(Integer.parseInt(value[0]), value[2], value[4], Status.valueOf(value[3]));
        }
        if (TypeOfTask.valueOf(value[1]).equals(TypeOfTask.EPIC)) {
            return new Epic(Integer.parseInt(value[0]), value[2], value[4], Status.valueOf(value[3]));
        }
        if (TypeOfTask.valueOf(value[1]).equals(TypeOfTask.SUBTASK)) {
            return new SubTask(Integer.parseInt(value[0]), value[2], value[4], Status.valueOf(value[3]), Integer.parseInt(value[5]));
        }
        return null;
    }

    public static String toString(Task task) {
        String line;
        line = task.getId() + ","
                + task.getType() + ","
                + task.getTitle() + ","
                + task.getStatus() + ","
                + task.getDescription() + ","
                + (task instanceof SubTask ? ((SubTask) task).getEpicId() : "");
        return line;
    }
}
