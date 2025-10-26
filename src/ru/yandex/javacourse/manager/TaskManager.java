package ru.yandex.javacourse.manager;


import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTask();

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    List<SubTask> getSubTasksByEpic(Epic epic);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskByIdAndEpicId(int id, int epicId);

    List<Task> getHistory();
}
