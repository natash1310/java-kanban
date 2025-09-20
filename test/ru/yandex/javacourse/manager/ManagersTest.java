package ru.yandex.javacourse.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.history.InMemoryHistoryManager;
import ru.yandex.javacourse.tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest extends Task {

    @Test
    void getDefault() {
        assertEquals(InMemoryTaskManager.class, Managers.getDefault().getClass());
    }

    @Test
    void getDefaultHistory() {
        assertEquals(InMemoryHistoryManager.class, Managers.getDefaultHistory().getClass());
    }
}