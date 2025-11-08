package ru.yandex.javacourse.manager;


import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.history.HistoryManager;
import ru.yandex.javacourse.history.InMemoryHistoryManager;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        try {
            return new FileBackedTaskManager(File.createTempFile("tasks", ".csv"));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать временный файл для FileBackedTaskManager");
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
