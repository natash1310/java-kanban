package ru.yandex.javacourse.manager;


import ru.yandex.javacourse.history.HistoryManager;
import ru.yandex.javacourse.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
