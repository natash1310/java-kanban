package ru.yandex.javacourse.tasks;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {

    private final Set<Integer> subTaskIds;

    public Epic(String title, String description) {
        super(title, description);
        this.subTaskIds = new HashSet<>();
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        this.subTaskIds = new HashSet<>();
    }

    public Epic(int id, String title, String description, Status status, long duration,
                LocalDateTime start, LocalDateTime end) {
        super(id, title, description, status, duration, start, end);
        this.subTaskIds = new HashSet<>();
    }


    public Set<Integer> getSubTasks() {
        return subTaskIds;
    }

    public void setSubTask(int id) {
        this.subTaskIds.add(id);
    }

    public void removeSubTask(int id) {
        this.subTaskIds.remove(id);
    }

    public void clearSubtasks() {
        subTaskIds.clear();
    }

    @Override
    public String getType() {
        return "EPIC";
    }

    @Override
    public String toString() {
        return "\n tasks.Epic{" +
                "id=" + super.getId() + '\'' +
                ", name='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status='" + super.getStatus() + '\'' +
                ", Duration=" + super.getDuration() + '\'' +
                ", Start=" + super.getStartTime() + '\'' +
                ", End=" + super.getEndTime() + '\'' +
                '}';
    }
}
