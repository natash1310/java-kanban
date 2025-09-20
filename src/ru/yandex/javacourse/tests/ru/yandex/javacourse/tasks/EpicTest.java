package ru.yandex.javacourse.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest extends Task {

    private Epic epic;

    @BeforeEach
    void beforeEach() {
        epic = new Epic("Test epic", "Test description");
    }

    @Test
    void setSubTask() {
        int subTaskId = 2;
        epic.setSubTask(subTaskId);
        assertFalse(epic.getSubTasks().isEmpty());
    }

    @Test
    void getSubTasks() {
        int subTaskId = 2;
        epic.setSubTask(subTaskId);
        assertTrue(epic.getSubTasks().contains(subTaskId));
    }


    @Test
    void removeSubTask() {
        int subTaskId = 2;
        int subTaskId2 = 3;
        epic.setSubTask(subTaskId);
        epic.setSubTask(subTaskId2);
        assertTrue(epic.getSubTasks().contains(subTaskId));
        assertTrue(epic.getSubTasks().contains(subTaskId2));
        epic.removeSubTask(subTaskId);
        assertFalse(epic.getSubTasks().contains(subTaskId));
        assertEquals(1, epic.getSubTasks().size());
    }

    @Test
    void clearSubtasks() {
        int subTaskId = 2;
        int subTaskId2 = 3;
        epic.setSubTask(subTaskId);
        epic.setSubTask(subTaskId2);
        assertTrue(epic.getSubTasks().contains(subTaskId));
        assertTrue(epic.getSubTasks().contains(subTaskId2));
        epic.clearSubtasks();
        assertTrue(epic.getSubTasks().isEmpty());
    }
}