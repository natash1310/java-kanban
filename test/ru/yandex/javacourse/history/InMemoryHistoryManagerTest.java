package ru.yandex.javacourse.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовый класс для проверки методов класса InMemoryHistoryManager")
class InMemoryHistoryManagerTest extends Task {

    private final InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private final String titleOfTask = "Задача 1";
    private final String descriptionOfTask = "Описание задачи 1";
    private final String titleOfEpic = "Эпик 1";
    private final String descriptionOfEpic = "Описание эпика 1";
    private final String titleOfSubTask = "Подзадача 1";
    private final String descriptionOfSubTask = "Описание подзадачи 1";
    private final String titleOfSubTask2 = "Подзадача 2";
    private final String descriptionOfSubTask2 = "Описание подзадачи 2";
    private final int epicId = 1;
    private final int countOfTask = 3;

    @Test
    @DisplayName("Проверка добавления разных задач в историю и исключение дублей")
    void addTasksToEmptyHistoryTest() {
        //given
        assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
        Epic epic = new Epic(titleOfEpic, descriptionOfEpic);
        Task task = new Task(titleOfTask, descriptionOfTask);
        SubTask subTask = new SubTask(titleOfSubTask, descriptionOfSubTask, epicId);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createSubTask(subTask);
        //when
        inMemoryHistoryManager.add(inMemoryTaskManager.getTaskById(2));
        inMemoryHistoryManager.add(inMemoryTaskManager.getEpicById(1));
        inMemoryHistoryManager.add(inMemoryTaskManager.getSubTaskById(3));
        inMemoryHistoryManager.add(inMemoryTaskManager.getTaskById(2));
        List<Task> tasks = new ArrayList<>();
        tasks.add(inMemoryTaskManager.getEpicById(1));
        tasks.add(inMemoryTaskManager.getSubTaskById(3));
        tasks.add(inMemoryTaskManager.getTaskById(2));
        //then
        assertEquals(countOfTask, inMemoryHistoryManager.getHistory().size());
        assertArrayEquals(tasks.toArray(), inMemoryHistoryManager.getHistory().toArray());
    }

    @Test
    @DisplayName("Проверка получения списка истории")
    void getHistoryTest() {
        //given
        assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
        Task task = new Task();
        //when
        inMemoryHistoryManager.add(task);
        //then
        assertFalse(inMemoryHistoryManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Проверка удаления подзадач в истории при удалении подзадачи или эпика")
    void removeSubTaskFromEpicInHistoryTest() {
        Epic epic = new Epic(titleOfEpic, descriptionOfEpic);
        SubTask subTask = new SubTask(titleOfSubTask, descriptionOfSubTask, epicId);
        SubTask subTask2 = new SubTask(titleOfSubTask2, descriptionOfSubTask2, epicId);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubTask(subTask);
        inMemoryTaskManager.createSubTask(subTask2);

        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getSubTaskById(2);
        inMemoryTaskManager.getSubTaskById(3);
        assertEquals(countOfTask, inMemoryTaskManager.getHistory().size());
        inMemoryTaskManager.removeSubTaskByIdAndEpicId(2, epicId);
        assertEquals(countOfTask - 1, inMemoryTaskManager.getHistory().size());
        inMemoryTaskManager.removeEpicById(epicId);
        assertEquals(0, inMemoryTaskManager.getHistory().size());
    }

}