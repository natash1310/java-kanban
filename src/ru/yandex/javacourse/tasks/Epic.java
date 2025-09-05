package ru.yandex.javacourse.tasks;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {

    private final Set<Integer> subTaskIds;

    public Epic(String title, String description) {
        super(title, description);
        this.subTaskIds = new HashSet<>();
    }

    public Set<Integer> getSubTasks() {
        return subTaskIds;
    }

    public void setSubTask(int id) {
        this.subTaskIds.add(id);
    }

    public void removeSubTask(int id){
        this.subTaskIds.remove(id);
    }

    public void clearSubtasks(){
        subTaskIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
               "id=" + super.getId() +
               ", title='" + super.getTitle() + '\'' +
               ", description='" + super.getDescription() + '\'' +
               ", status=" + super.getStatus() +
               "}\n";
    }
}
