package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.exception.NotFoundException;
import ru.yandex.javacourse.exception.TaskValidationException;
import ru.yandex.javacourse.history.HistoryManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, SubTask> subTasks;
    protected final TreeSet<Task> prioritizedTasks;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int idCounter = 1;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.prioritizedTasks = new TreeSet<>();
    }

    public void resetIdCounter() {
        this.idCounter = 1;
    }

    public List<Task> getAllTasks() {
        if (tasks.isEmpty()) {
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
        getAllTasks().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
    }

    public void clearSubTasks() {
        if (!getAllEpics().isEmpty()) {
            getAllEpics().forEach(Epic::clearSubtasks);
        }
        getAllSubTask().forEach(prioritizedTasks::remove);
        subTasks.clear();
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        } else {
            throw new NotFoundException("Задачи с id: " + id + " нет");
        }
    }

    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            historyManager.add(subTask);
            return subTask;
        } else {
            throw new NotFoundException("Подзадачи с id: " + id + " нет");
        }
    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        } else {
            throw new NotFoundException("Эпика с id: " + id + " нет");
        }
    }

    public List<SubTask> getSubTasksByEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            return subTasks.values().stream()
                    .filter(subTask -> subTask.getEpicId() == epic.getId())
                    .toList();
        } else {
            System.out.println("Такого эпика нет.");
            return new ArrayList<>();
        }
    }

    public void createTask(Task task) {
        checkTaskTime(task);
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    public void createEpic(Epic epic) {
        if (epic.getDuration() != 0 && epic.getStartTime() != null) {
            checkTaskTime(epic);
        }
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            checkTaskTime(subTask);
            subTask.setId(idCounter++);
            epic.setSubTask(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
            updateEpicStatus(epic);
            if (subTask.getStartTime() != null) {
                prioritizedTasks.add(subTask);
            }
            resetEpicTime(epic);
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

        boolean allNew = epic.getSubTasks().stream()
                .map(subTasks::get)
                .allMatch(subTask -> subTask.getStatus() == Status.NEW);

        boolean allDone = epic.getSubTasks().stream()
                .map(subTasks::get)
                .allMatch(subTask -> subTask.getStatus() == Status.DONE);

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        resetEpicTime(epic);
    }

    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            prioritizedTasks.remove(task);
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задачи с id: " + id + " нет");
        }
    }

    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epic.getSubTasks().forEach(subTaskId -> {
                SubTask subTask = subTasks.get(subTaskId);
                if (subTask != null) {
                    prioritizedTasks.remove(subTask);
                }
                subTasks.remove(subTaskId);
                historyManager.remove(subTaskId);
            });
            epic.clearSubtasks();
            epics.remove(id);
            historyManager.remove(id);
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
                SubTask subTask = subTasks.get(id);
                if (subTask != null) {
                    prioritizedTasks.remove(subTask);
                }
                subTasks.remove(id);
                historyManager.remove(id);
            } else {
                System.out.println("У эпика с id: " + epicId + " нет подзадачи с id: " + id);
            }
        } else {
            System.out.println("Эпика с id: " + epicId + " нет");
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            Epic epic = epics.get(subTasks.get(id).getEpicId());
            if (epic.getSubTasks().contains(id)) {
                epic.removeSubTask(id);
                subTasks.remove(id);
                historyManager.remove(id);
            } else {
                System.out.println("У эпика с id: " + epic.getId() + " нет подзадачи с id: " + id);
            }
        } else {
            System.out.println("Подзадачи с id: " + id + " нет");
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void checkTaskTime(Task task) {
        final LocalDateTime startTime = task.getStartTime();
        final LocalDateTime endTime = task.getEndTime();

        if (startTime == null) {
            return;
        }

        boolean hasOverlap = tasks.values().stream()
                .filter(t -> t.getStartTime() != null && t.getEndTime() != null)
                .anyMatch(existingTask -> isOverlapping(startTime, endTime,
                        existingTask.getStartTime(), existingTask.getEndTime()));

        if (!hasOverlap) {
            hasOverlap = subTasks.values().stream()
                    .filter(t -> t.getStartTime() != null && t.getEndTime() != null)
                    .anyMatch(existingTask -> isOverlapping(startTime, endTime,
                            existingTask.getStartTime(), existingTask.getEndTime()));
        }

        if (hasOverlap) {
            throw new TaskValidationException("Задача пересекается по времени с существующей задачей");
        }
    }

    private boolean isOverlapping(LocalDateTime start1, LocalDateTime end1,
                                  LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private void resetEpicTime(Epic epic) {
        List<SubTask> subTasksList = getSubTasksByEpic(epic);

        if (subTasksList.isEmpty()) {
            epic.setDuration(0);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }

        long sumDuration = subTasksList.stream()
                .mapToLong(SubTask::getDuration)
                .sum();

        LocalDateTime earliestStart = subTasksList.stream()
                .map(SubTask::getStartTime)
                .filter(java.util.Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime latestEnd = subTasksList.stream()
                .map(SubTask::getEndTime)
                .filter(java.util.Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        epic.setDuration(sumDuration);
        epic.setStartTime(earliestStart);
        epic.setEndTime(latestEnd);
    }
}
