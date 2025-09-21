package ru.yandex.javacourse.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовый класс для проверки методов класса Epic")
class EpicTest extends Task {

    private Epic epic;
    private final String titleOfEpic = "Test epic";
    private final String descriptionOfEpic = "Test description";
    private final int subTaskId = 2;
    private final int subTaskId2 = 3;

    @BeforeEach
    void beforeEach() {
        //given
        epic = new Epic(titleOfEpic, descriptionOfEpic);
    }

    @Test
    @DisplayName("Метод корректно добавляет новый идентификатор в подзадачи")
    void shouldAddSubTaskToEpic() {
        //when
        epic.setSubTask(subTaskId);
        //then
        assertFalse(epic.getSubTasks().isEmpty(), "Список подзадач не должен быть пуст после добавления");
        assertTrue(epic.getSubTasks().contains(subTaskId), "Список подзадач должен содержать добавленную подзадачу");
    }

    @Test
    @DisplayName("Получение списка подзадач эпика после добавления подзадачи")
    void shouldReturnSubTasksContainingAddedSubTask() {
        //when
        epic.setSubTask(subTaskId);
        //then
        assertTrue(epic.getSubTasks().contains(subTaskId), "Список подзадач должен содержать добавленную подзадачу");
    }


    @Test
    @DisplayName("Удаление подзадачи из эпика корректно обновляет список подзадач")
    void shouldRemoveSubTaskFromEpic() {
        epic.setSubTask(subTaskId);
        epic.setSubTask(subTaskId2);
        assertTrue(epic.getSubTasks().contains(subTaskId), "Список должен содержать первую подзадачу");
        assertTrue(epic.getSubTasks().contains(subTaskId2), "Список должен содержать вторую подзадачу");
        //when
        epic.removeSubTask(subTaskId);
        //then
        assertFalse(epic.getSubTasks().contains(subTaskId), "Первая подзадача должна быть удалена");
        assertEquals(1, epic.getSubTasks().size(), "В списке должна остаться одна подзадача");
    }

    @Test
    @DisplayName("Чистка всех подзадач эпика должна опустошать список подзадач")
    void shouldClearAllSubTasksFromEpic() {
        epic.setSubTask(subTaskId);
        epic.setSubTask(subTaskId2);
        assertTrue(epic.getSubTasks().contains(subTaskId), "Список должен содержать первую подзадачу");
        assertTrue(epic.getSubTasks().contains(subTaskId2), "Список должен содержать вторую подзадачу");
        //when
        epic.clearSubtasks();
        //then
        assertTrue(epic.getSubTasks().isEmpty(), "Список подзадач должен быть пуст после очистки");
    }

}