package ru.yandex.javacourse.manager;


import ru.yandex.javacourse.history.HistoryManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int idCounter = 1;

    public void resetIdCounter() {
        this.idCounter = 1;
    }

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    public List<Task> getAllTasks() {
        if (tasks.isEmpty()) { // проверяем, есть ли элементы в списке
            System.out.println("Нет сохранённых задач.");
        }
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("Нет сохранённых эпиков.");
        }
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }


    public void clearTasks() {
        tasks.clear();
    }


    public void clearEpics() {
        epics.clear();
    }

    public void clearSubTasks() {
        if (!getAllEpics().isEmpty()) {
            for (Epic epic : getAllEpics()) {
                epic.clearSubtasks();
            }
        }
        subTasks.clear();
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        } else {
            System.out.println("Задачи с id: " + id + " нет");
            return null;
        }

    }

    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            historyManager.add(subTask);
            return subTask;
        } else {
            System.out.println("Подзадачи с id: " + id + " нет");
            return null;
        }

    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        } else {
            System.out.println("Эпика с id: " + id + " нет");
            return null;
        }
    }

    public List<SubTask> getSubTasksByEpic(Epic epic) {
        ArrayList<SubTask> result = new ArrayList<>();
        if (epics.containsKey(epic.getId())) {
            for (SubTask subTask : subTasks.values()) {
                if (subTask.getEpicId() == epic.getId()) {
                    result.add(subTask);
                }
            }
            return result;
        } else {
            System.out.println("Такого эпика нет.");
            return result;
        }
    }

    public void createTask(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            subTask.setId(idCounter++);
            epic.setSubTask(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epic);
        }
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    public void updateSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        epic.setSubTask(subTask.getId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        subTasks.put(subTask.getId(), subTask);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subTaskId : epic.getSubTasks()) {
            if (subTasks.get(subTaskId).getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subTasks.get(subTaskId).getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с id: " + id + " нет");
        }
    }

    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subTaskId : epic.getSubTasks()) {
                subTasks.remove(subTaskId);
            }
            epic.clearSubtasks();
            epics.remove(id);
        } else {
            System.out.println("Эпика с id: " + id + " нет");
        }
    }

    public void removeSubTaskByIdAndEpicId(int id, int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            if (epic.getSubTasks().contains(id)) {
                epic.removeSubTask(id);
                updateEpicStatus(epic);
                subTasks.remove(id);
            } else {
                System.out.println("У эпика с id: " + epicId + " нет подзадачи с id: " + id);
            }

        } else {
            System.out.println("Эпика с id: " + epicId + " нет");
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
