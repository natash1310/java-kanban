package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.TypeOfTask;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader b = new BufferedReader(new FileReader(file))) {
            b.readLine();

            while (b.ready()) {
                String line = b.readLine();
                if (line == null || line.trim().isEmpty()) {
                    if (b.ready()) {
                        String idString = b.readLine();
                        if (idString != null && !idString.trim().isEmpty()) {
                            List<Integer> idList = CSVFormatter.historyFromString(idString);
                            for (Integer integer : idList) {
                                if (manager.tasks.containsKey(integer)) {
                                    manager.historyManager.add(manager.tasks.get(integer));
                                } else if (manager.subTasks.containsKey(integer)) {
                                    manager.historyManager.add(manager.subTasks.get(integer));
                                } else if (manager.epics.containsKey(integer)) {
                                    manager.historyManager.add(manager.epics.get(integer));
                                }
                            }
                        }
                    }
                    break;
                }

                String[] lines = line.split(",");
                if (lines.length > 1) {
                    if (lines[1].equals(TypeOfTask.TASK.toString())) {
                        manager.restoreTask(CSVFormatter.taskFromString(lines));
                    } else if (lines[1].equals(TypeOfTask.EPIC.toString())) {
                        manager.restoreEpic((Epic) CSVFormatter.taskFromString(lines));
                    } else if (lines[1].equals(TypeOfTask.SUBTASK.toString())) {
                        manager.restoreSubtask((SubTask) Objects.requireNonNull(CSVFormatter.taskFromString(lines)));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + file.getName());
        }
        return manager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskByIdAndEpicId(int id, int epicId) {
        super.removeSubTaskByIdAndEpicId(id, epicId);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    protected void restoreTask(Task task) {
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        if (task.getId() >= idCounter) {
            idCounter = task.getId() + 1;
        }
    }

    protected void restoreEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        if (epic.getId() >= idCounter) {
            idCounter = epic.getId() + 1;
        }
    }

    protected void restoreSubtask(SubTask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.setSubTask(subtask.getId());
            subTasks.put(subtask.getId(), subtask);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
            if (subtask.getId() >= idCounter) {
                idCounter = subtask.getId() + 1;
            }
        }
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVFormatter.getHeader());
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }

            for (SubTask subTask : getAllSubTask()) {
                writer.write(CSVFormatter.toString(subTask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(CSVFormatter.historyToString(getHistory()));
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла: " + file.getName());
        }
    }
}
