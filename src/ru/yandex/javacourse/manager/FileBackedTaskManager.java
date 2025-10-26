package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.tasks.TypeOfTask;

import java.io.*;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
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

    public void getSubtaskById(int id) {
        super.getSubTaskById(id);
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
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    public void restoreTask(Task task) {
        super.createTask(task);
    }

    public void restoreEpic(Epic epic) {
        super.createEpic(epic);
    }

    public void restoreSubtask(SubTask subtask) {
        super.createSubTask(subtask);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader b = new BufferedReader(new FileReader("history.csv"))) {
            b.readLine();
            while (b.ready()) {
                String[] lines = b.readLine().split(",");
                if (lines.length > 1) {
                    if (lines[1].equals(TypeOfTask.TASK.toString())) {
                        manager.restoreTask(((CSVFormatter.taskFromString(lines))));
                    } else if (lines[1].equals(TypeOfTask.EPIC.toString())) {
                        manager.restoreEpic((Epic) CSVFormatter.taskFromString(lines));
                    } else if (lines[1].equals(TypeOfTask.SUBTASK.toString())) {
                        manager.restoreSubtask((SubTask) CSVFormatter.taskFromString(lines));
                    }
                }
                if (lines.length == 1) {
                    String idString = b.readLine();
                    List<Integer> idList = CSVFormatter.historyFromString(idString);
                    for (Integer integer : idList) {
                        if (manager.getTaskById(integer) != null) {
                            manager.getTaskById(integer);
                        }
                        if (manager.getSubTaskById(integer) != null) {
                            manager.getSubtaskById(integer);
                        }
                        if (manager.getEpicById(integer) != null) {
                            manager.getEpicById(integer);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла:" + file.getName());
        }
        return manager;
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
