package ru.yandex.javacourse.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest extends Task {
    private SubTask subTask;

    @BeforeEach
    void beforeEach() {
        subTask = new SubTask("SubTask", "Test description", 1);
    }

    @Test
    void getEpicId() {
        assertEquals(1, subTask.getEpicId());
    }

    @Test
    void setEpicId() {
        subTask.setEpicId(2);
        assertEquals(2, subTask.getEpicId());
    }
}