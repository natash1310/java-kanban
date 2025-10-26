package ru.yandex.javacourse.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовый класс для проверки функционала класса FileBackedTaskManager")
class FileBackedTaskManagerTest {

    private static File file = new File("history.csv");
    private static FileBackedTaskManager fileBackedTaskManager2;

    @BeforeEach
    public void setUp() {
        file.delete();
        file = new File("history.csv");
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(file);
        fileBackedTaskManager1.resetIdCounter();
        Task task1 = new Task("Task 1", "description Task 1");
        fileBackedTaskManager1.createTask(task1);
        Task task2 = new Task("Task 2", "description Task 2");
        fileBackedTaskManager1.createTask(task2);
        Epic epic = new Epic("Epic 1", "description Epic 1");
        fileBackedTaskManager1.createEpic(epic);
        SubTask subtask = new SubTask("Subtask 1", "description Subtask 1", epic.getId());
        fileBackedTaskManager1.createSubTask(subtask);
        fileBackedTaskManager1.getTaskById(1);
        fileBackedTaskManager1.getEpicById(3);
        fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
    }

    @Test
    @DisplayName("Проверка восстановления задач из файла")
    public void allTasksNotNull() {
        assertNotNull(fileBackedTaskManager2.getAllTasks());
    }

    @Test
    @DisplayName("Проверка восстановления истории из файла")
    public void historyNotNull() {
        assertNotNull(fileBackedTaskManager2.getHistory(), "История не пустая");
        assertTrue(fileBackedTaskManager2.getHistory().contains(fileBackedTaskManager2.getEpicById(3)));
        assertTrue(fileBackedTaskManager2.getHistory().contains(fileBackedTaskManager2.getTaskById(1)));

    }

    @Test
    @DisplayName("Проверка восстановления полей задачи из файла")
    public void taskEquality() {
        assertEquals("Task 2", fileBackedTaskManager2.getTaskById(2).getTitle(),
                "Обнаружено несовпадение имени задачи");
        assertEquals("description Task 2", fileBackedTaskManager2.getTaskById(2).getDescription(),
                "Обнаружено несовпадение имени задачи");
        assertEquals(Status.NEW, fileBackedTaskManager2.getTaskById(2).getStatus(),
                "Обнаружено несовпадение имени задачи");

    }
}