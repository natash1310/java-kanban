package ru.yandex.javacourse.tasks;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest extends Task {

    private Task task;

    @BeforeEach
    void beforeEach() {
        task = new Task("Test task", "Test description");

    }

    @Test
    void testGetTitle() {
        assertEquals("Test task", task.getTitle());
    }


    @Test
    void testSetTitle() {
        task.setTitle("Another test task");
        assertEquals("Another test task", task.getTitle());

    }

    @Test
    void testGetDescription() {
        assertEquals("Test description", task.getDescription());
    }

    @Test
    void testSetDescription() {
        task.setDescription("Another test task");
        assertEquals("Another test task", task.getDescription());
    }

    @Test
    void testGetStatus() {
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    void testSetStatus() {
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus());
    }
}