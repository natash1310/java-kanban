package ru.yandex.javacourse.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.history.InMemoryHistoryManager;
import ru.yandex.javacourse.tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тестовый класс для проверки методов класса Manager")
class ManagersTest extends Task {

    @Test
    @DisplayName("Получение дефолтного менеджера задач")
    void getDefault() {
        assertEquals(InMemoryTaskManager.class, Managers.getDefault().getClass());
    }

    @Test
    @DisplayName("Получение дефолтного менеджера истории")
    void getDefaultHistory() {
        assertEquals(InMemoryHistoryManager.class, Managers.getDefaultHistory().getClass());
    }
}
