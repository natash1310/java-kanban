package ru.yandex.javacourse.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends Task {
    private final InMemoryTaskManager manager = new InMemoryTaskManager();

    @BeforeEach
    public void setUp() {
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();
        manager.resetIdCounter();

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.createTask(task1);
        manager.createTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic2.getId());
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic2.getId());
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);
    }

    @Test
    void getAllTasks() {
        assertEquals(2, manager.getAllTasks().size());
    }

    @Test
    void getAllEpics() {
        assertEquals(2, manager.getAllEpics().size());
    }

    @Test
    void getAllSubTask() {
        assertEquals(3, manager.getAllSubTask().size());
    }

    @Test
    void clearTasks() {
        assertEquals(2, manager.getAllTasks().size());
        manager.clearTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void clearEpics() {
        assertEquals(2, manager.getAllEpics().size());
        manager.clearEpics();
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    void clearSubTasks() {
        assertEquals(3, manager.getAllSubTask().size());
        manager.clearSubTasks();
        assertTrue(manager.getAllSubTask().isEmpty());
    }

    @Test
    void getTaskById() {
        Task task = manager.getTaskById(1);
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals("Задача 1", task.getTitle());
        assertEquals("Описание задачи 1", task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    void getSubTaskById() {
        SubTask subTask = manager.getSubTaskById(5);
        assertNotNull(subTask);
        assertEquals(5, subTask.getId());
        assertEquals("Подзадача 1", subTask.getTitle());
        assertEquals("Описание подзадачи 1", subTask.getDescription());
        assertEquals(Status.NEW, subTask.getStatus());
        assertEquals(3, subTask.getEpicId());
    }

    @Test
    void getEpicById() {
        Epic epic = manager.getEpicById(3);
        assertNotNull(epic);
        assertEquals(3, epic.getId());
        assertEquals("Эпик 1", epic.getTitle());
        assertEquals("Описание эпика 1", epic.getDescription());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(1, epic.getSubTasks().size());
        assertTrue(epic.getSubTasks().contains(5));
    }

    @Test
    void getSubTasksByEpic() {
        Epic epic = manager.getEpicById(4);
        List<SubTask> subTasks = manager.getSubTasksByEpic(epic);
        assertNotNull(subTasks);
        assertEquals(2, subTasks.size());
        assertEquals(6, subTasks.getFirst().getId());
        assertEquals(7, subTasks.getLast().getId());
    }

    @Test
    void createTask() {
        Task task = new Task("Задача 3", "Описание задачи 3");
        manager.createTask(task);
        assertEquals(3, manager.getAllTasks().size());
        Task taskFromManager = manager.getTaskById(8);
        assertNotNull(taskFromManager);
        assertEquals(task.getTitle(), taskFromManager.getTitle());
        assertEquals(task.getDescription(), taskFromManager.getDescription());
        assertEquals(Status.NEW, taskFromManager.getStatus());
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Эпик 3", "Описание эпика 3");
        manager.createEpic(epic);
        assertEquals(3, manager.getAllEpics().size());
        Epic epicFromManager = manager.getEpicById(8);
        assertNotNull(epicFromManager);
        assertEquals(epic.getTitle(), epicFromManager.getTitle());
        assertEquals(epic.getDescription(), epicFromManager.getDescription());
        assertEquals(Status.NEW, epicFromManager.getStatus());
    }

    @Test
    void createSubTask() {
        SubTask subTask = new SubTask("Подзадача 4", "Описание подзадачи 4", 3);
        manager.createSubTask(subTask);
        assertEquals(4, manager.getAllSubTask().size());
        SubTask subTaskFromManager = manager.getSubTaskById(8);
        assertNotNull(subTaskFromManager);
        assertEquals(subTask.getTitle(), subTaskFromManager.getTitle());
        assertEquals(subTask.getDescription(), subTaskFromManager.getDescription());
        assertEquals(Status.NEW, subTaskFromManager.getStatus());
        assertEquals(subTask.getEpicId(), subTaskFromManager.getEpicId());

        subTask.setEpicId(6);
        manager.createSubTask(subTask);
        assertEquals(4, manager.getAllSubTask().size());
    }

    @Test
    void updateTask() {
        Task updatedTask = manager.getTaskById(1);
        updatedTask.setDescription("Новое название задачи 1");
        updatedTask.setTitle("Новое описание задачи 1");
        updatedTask.setStatus(Status.DONE);
        manager.updateTask(updatedTask);
        Task taskFromManager = manager.getTaskById(1);
        assertNotNull(taskFromManager);
        assertEquals(updatedTask.getTitle(), taskFromManager.getTitle());
        assertEquals(updatedTask.getDescription(), taskFromManager.getDescription());
        assertEquals(updatedTask.getStatus(), taskFromManager.getStatus());
    }

    @Test
    void updateEpic() {
        Epic updatedEpic = manager.getEpicById(3);
        updatedEpic.setDescription("Новое название эпика 1");
        updatedEpic.setTitle("Новое описание эпика 1");
        updatedEpic.setStatus(Status.DONE);
        manager.updateEpic(updatedEpic);
        Epic epicFromManager = manager.getEpicById(3);
        assertNotNull(epicFromManager);
        assertEquals(updatedEpic.getTitle(), epicFromManager.getTitle());
        assertEquals(updatedEpic.getDescription(), epicFromManager.getDescription());
        assertEquals(updatedEpic.getStatus(), epicFromManager.getStatus());
    }

    @Test
    void updateSubTask() {
        SubTask updatedSubTask = manager.getSubTaskById(5);
        updatedSubTask.setDescription("Новое название подзадачи 1");
        updatedSubTask.setTitle("Новое описание подзадачи 1");
        updatedSubTask.setStatus(Status.DONE);
        manager.updateSubTask(updatedSubTask);
        SubTask subTaskFromManager = manager.getSubTaskById(5);
        assertNotNull(subTaskFromManager);
        assertEquals(updatedSubTask.getTitle(), subTaskFromManager.getTitle());
        assertEquals(updatedSubTask.getDescription(), subTaskFromManager.getDescription());
        assertEquals(updatedSubTask.getStatus(), subTaskFromManager.getStatus());
    }

    @Test
    void removeTaskById() {
        assertEquals(2, manager.getAllTasks().size());
        manager.removeTaskById(1);
        assertEquals(1, manager.getAllTasks().size());
        assertEquals(2, manager.getAllTasks().getFirst().getId());
    }

    @Test
    void removeEpicById() {
        assertEquals(2, manager.getAllEpics().size());
        manager.removeEpicById(3);
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(4, manager.getAllEpics().getFirst().getId());
    }

    @Test
    void removeSubTaskByIdAndEpicId() {
        manager.removeSubTaskByIdAndEpicId(5, 3);
        assertNull(manager.getSubTaskById(5));
        assertFalse(manager.getEpicById(3).getSubTasks().contains(5));
    }

    @Test
    void getHistory() {
        List<Task> history = manager.getHistory();
        assertTrue(history.isEmpty());
        Task task = manager.getTaskById(1);
        Epic epic = manager.getEpicById(3);
        SubTask subTask = manager.getSubTaskById(5);
        history = manager.getHistory();
        assertFalse(history.isEmpty());
        assertEquals(3, history.size());
        assertTrue(history.contains(task));
        assertTrue(history.contains(epic));
        assertTrue(history.contains(subTask));
    }

    @Test
    void tasksShouldBeEquals(){
        Task task = manager.getTaskById(1);
        Task task2 = manager.getTaskById(2);
        task2.setId(1);
        assertEquals(task, task2);
    }

    @Test
    void subTasksShouldBeEquals(){
        SubTask subTask = manager.getSubTaskById(5);
        SubTask subTask2 = manager.getSubTaskById(6);
        subTask2.setId(5);
        assertEquals(subTask, subTask2);
    }

    @Test
    void epicsShouldBeEquals(){
        Epic epic = manager.getEpicById(3);
        Epic epic2 = manager.getEpicById(4);
        epic2.setId(3);
        assertEquals(epic, epic2);
    }

}