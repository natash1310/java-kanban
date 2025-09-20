package ru.yandex.javacourse.history;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends Task {

    private final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Test
    void add() {
        assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
        Task task = new Task("Задача 1", "Описание задачи 1");
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        SubTask subTask = new SubTask("Подзадача 1", "Описание подхадачи 1", 1);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subTask);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(epic);
        tasks.add(subTask);
        assertEquals(3, inMemoryHistoryManager.getHistory().size());
        assertArrayEquals(tasks.toArray(), inMemoryHistoryManager.getHistory().toArray());
    }

    @Test
    void getHistory() {
        assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
        Task task = new Task();
        inMemoryHistoryManager.add(task);
        assertFalse(inMemoryHistoryManager.getHistory().isEmpty());
    }
}