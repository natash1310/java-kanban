package ru.yandex.javacourse.tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;


    public SubTask(String title, String description, LocalDateTime startTime, long duration, int epicId) {
        super(title, description, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String title, String description,
                   int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String title, String description, Status status, int epicId, long duration,
                   LocalDateTime start, LocalDateTime end) {
        super(id, title, description, status, duration, start, end);
        this.epicId = epicId;
    }

    @Override
    public String getType() {
        return "SUBTASK";
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "\n tasks.SubTask{" +
                "id=" + super.getId() +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", title='" + super.getTitle() + '\'' +
                ", duration=" + super.getDuration() + '\'' +
                ", startTime=" + super.getStartTime() + '\'' +
                ", endTime=" + super.getEndTime() +
                ", epicId=" + epicId +
                '}';
    }

}
