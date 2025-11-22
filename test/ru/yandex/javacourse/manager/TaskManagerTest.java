package ru.yandex.javacourse.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.exception.NotFoundException;
import ru.yandex.javacourse.exception.TaskValidationException;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Базовый тестовый класс для проверки методов TaskManager")
abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected SubTask subTask1;
    protected SubTask subTask2;

    protected abstract T createManager();

    @BeforeEach
    void setUp() {
        manager = createManager();

        task1 = new Task("Задача 1", "Описание задачи 1");
        task2 = new Task("Задача 2", "Описание задачи 2");

        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic2 = new Epic("Эпик 2", "Описание эпика 2");
    }

    protected void createSubTasksForEpic1() {
        subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
    }

    @Test
    @DisplayName("Создание задачи - стандартный случай")
    void shouldCreateTask() {
        manager.createTask(task1);

        assertNotNull(manager.getTaskById(task1.getId()));
        assertEquals("Задача 1", manager.getTaskById(task1.getId()).getTitle());
        assertEquals(Status.NEW, manager.getTaskById(task1.getId()).getStatus());
    }

    @Test
    @DisplayName("Создание эпика - стандартный случай")
    void shouldCreateEpic() {
        manager.createEpic(epic1);

        assertNotNull(manager.getEpicById(epic1.getId()));
        assertEquals("Эпик 1", manager.getEpicById(epic1.getId()).getTitle());
        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    @DisplayName("Создание подзадачи - стандартный случай")
    void shouldCreateSubTask() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();

        manager.createSubTask(subTask1);

        assertNotNull(manager.getSubTaskById(subTask1.getId()));
        assertEquals(epic1.getId(), manager.getSubTaskById(subTask1.getId()).getEpicId());
        assertTrue(manager.getEpicById(epic1.getId()).getSubTasks().contains(subTask1.getId()));
    }

    @Test
    @DisplayName("Получение всех задач")
    void shouldGetAllTasks() {
        manager.createTask(task1);
        manager.createTask(task2);

        assertEquals(2, manager.getAllTasks().size());
    }

    @Test
    @DisplayName("Получение всех эпиков")
    void shouldGetAllEpics() {
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        assertEquals(2, manager.getAllEpics().size());
    }

    @Test
    @DisplayName("Получение всех подзадач")
    void shouldGetAllSubTasks() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();

        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        assertEquals(2, manager.getAllSubTask().size());
    }

    @Test
    @DisplayName("Удаление всех задач")
    void shouldClearAllTasks() {
        manager.createTask(task1);

        manager.clearTasks();

        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    @DisplayName("Удаление всех эпиков")
    void shouldClearAllEpics() {
        manager.createEpic(epic1);

        manager.clearEpics();

        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    @DisplayName("Удаление всех подзадач")
    void shouldClearAllSubTasks() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);

        manager.clearSubTasks();

        assertTrue(manager.getAllSubTask().isEmpty());
        assertTrue(manager.getEpicById(epic1.getId()).getSubTasks().isEmpty());
    }

    @Test
    @DisplayName("Получение задачи по ID - задача существует")
    void shouldGetTaskById_whenTaskExists() {
        manager.createTask(task1);

        Task retrievedTask = manager.getTaskById(task1.getId());

        assertNotNull(retrievedTask);
        assertEquals(task1.getId(), retrievedTask.getId());
    }

    @Test
    @DisplayName("Получение задачи по ID - задача не существует")
    void shouldThrowNotFoundException_whenTaskDoesNotExist() {
        assertThrows(NotFoundException.class, () -> manager.getTaskById(999));
    }

    @Test
    @DisplayName("Получение эпика по ID - эпик существует")
    void shouldGetEpicById_whenEpicExists() {
        manager.createEpic(epic1);

        Epic retrievedEpic = manager.getEpicById(epic1.getId());

        assertNotNull(retrievedEpic);
        assertEquals(epic1.getId(), retrievedEpic.getId());
    }

    @Test
    @DisplayName("Получение эпика по ID - эпик не существует")
    void shouldThrowNotFoundException_whenEpicDoesNotExist() {
        assertThrows(NotFoundException.class, () -> manager.getEpicById(999));
    }

    @Test
    @DisplayName("Получение подзадачи по ID - подзадача существует")
    void shouldGetSubTaskById_whenSubTaskExists() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);

        SubTask retrievedSubTask = manager.getSubTaskById(subTask1.getId());

        assertNotNull(retrievedSubTask);
        assertEquals(subTask1.getId(), retrievedSubTask.getId());
        assertEquals(epic1.getId(), retrievedSubTask.getEpicId());
    }

    @Test
    @DisplayName("Получение подзадачи по ID - подзадача не существует")
    void shouldThrowNotFoundException_whenSubTaskDoesNotExist() {
        assertThrows(NotFoundException.class, () -> manager.getSubTaskById(999));
    }

    @Test
    @DisplayName("Обновление задачи")
    void shouldUpdateTask() {
        manager.createTask(task1);

        task1.setTitle("Обновленная задача");
        task1.setStatus(Status.DONE);
        manager.updateTask(task1);

        assertEquals("Обновленная задача", manager.getTaskById(task1.getId()).getTitle());
        assertEquals(Status.DONE, manager.getTaskById(task1.getId()).getStatus());
    }

    @Test
    @DisplayName("Обновление эпика")
    void shouldUpdateEpic() {
        manager.createEpic(epic1);

        epic1.setTitle("Обновленный эпик");
        manager.updateEpic(epic1);

        assertEquals("Обновленный эпик", manager.getEpicById(epic1.getId()).getTitle());
    }

    @Test
    @DisplayName("Обновление подзадачи")
    void shouldUpdateSubTask() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);

        subTask1.setTitle("Обновленная подзадача");
        subTask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask1);

        assertEquals("Обновленная подзадача", manager.getSubTaskById(subTask1.getId()).getTitle());
        assertEquals(Status.IN_PROGRESS, manager.getSubTaskById(subTask1.getId()).getStatus());
    }

    @Test
    @DisplayName("Удаление задачи по ID")
    void shouldRemoveTaskById() {
        manager.createTask(task1);
        manager.removeTaskById(task1.getId());
        assertThrows(NotFoundException.class, () -> manager.getTaskById(task1.getId()));
    }

    @Test
    @DisplayName("Удаление эпика по ID удаляет все его подзадачи")
    void shouldRemoveEpicById_andAllSubTasks() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.removeEpicById(epic1.getId());
        assertThrows(NotFoundException.class, () -> manager.getEpicById(epic1.getId()));
        assertThrows(NotFoundException.class, () -> manager.getSubTaskById(subTask1.getId()));
        assertThrows(NotFoundException.class, () -> manager.getSubTaskById(subTask2.getId()));
    }

    @Test
    @DisplayName("Удаление подзадачи по ID")
    void shouldRemoveSubTaskById() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);
        manager.removeSubTaskByIdAndEpicId(subTask1.getId(), epic1.getId());
        assertThrows(NotFoundException.class, () -> manager.getSubTaskById(subTask1.getId()));
        assertFalse(manager.getEpicById(epic1.getId()).getSubTasks().contains(subTask1.getId()));
    }

    @Test
    @DisplayName("Получение подзадач эпика")
    void shouldGetSubTasksByEpic() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        List<SubTask> subTasks = manager.getSubTasksByEpic(epic1);

        assertEquals(2, subTasks.size());
        assertTrue(subTasks.contains(subTask1));
        assertTrue(subTasks.contains(subTask2));
    }


    @Test
    @DisplayName("Статус эпика NEW, когда все подзадачи NEW")
    void shouldSetEpicStatusToNew_whenAllSubTasksAreNew() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    @DisplayName("Статус эпика DONE, когда все подзадачи DONE")
    void shouldSetEpicStatusToDone_whenAllSubTasksAreDone() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);

        assertEquals(Status.DONE, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    @DisplayName("Статус эпика IN_PROGRESS, когда подзадачи NEW и DONE")
    void shouldSetEpicStatusToInProgress_whenSubTasksAreNewAndDone() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    @DisplayName("Статус эпика IN_PROGRESS, когда подзадачи IN_PROGRESS")
    void shouldSetEpicStatusToInProgress_whenSubTasksAreInProgress() {
        manager.createEpic(epic1);
        createSubTasksForEpic1();
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        subTask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask1);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    @DisplayName("Статус эпика NEW, когда у него нет подзадач")
    void shouldSetEpicStatusToNew_whenEpicHasNoSubTasks() {
        manager.createEpic(epic1);

        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus());
    }


    @Test
    @DisplayName("Должно выбросить исключение при пересечении временных интервалов задач")
    void shouldThrowException_whenTaskTimesOverlap() {
        LocalDateTime startTime = LocalDateTime.of(2024, 11, 8, 10, 0);
        Task taskWithTime1 = new Task("Задача 1", "Описание", startTime, 60);
        manager.createTask(taskWithTime1);

        Task taskWithTime2 = new Task("Задача 2", "Описание", startTime, 30);

        assertThrows(TaskValidationException.class, () -> manager.createTask(taskWithTime2),
                "Должно быть выброшено исключение при создании задачи с совпадающим временем старта");
    }

    @Test
    @DisplayName("Не должно быть исключения при создании задач с разным временем")
    void shouldNotThrowException_whenTaskTimesDoNotOverlap() {
        LocalDateTime startTime1 = LocalDateTime.of(2024, 11, 8, 10, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 11, 8, 12, 0);

        Task taskWithTime1 = new Task("Задача 1", "Описание", startTime1, 60);
        Task taskWithTime2 = new Task("Задача 2", "Описание", startTime2, 30);

        assertDoesNotThrow(() -> {
            manager.createTask(taskWithTime1);
            manager.createTask(taskWithTime2);
        }, "Не должно быть исключения при создании задач с разным временем");
    }

    @Test
    @DisplayName("Получение приоритизированных задач - пустой список")
    void shouldReturnEmptyPrioritizedTasks_whenNoTasksWithTime() {
        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertTrue(prioritizedTasks.isEmpty());
    }

    @Test
    @DisplayName("Получение приоритизированных задач - задачи отсортированы по времени начала")
    void shouldReturnPrioritizedTasksSortedByStartTime() {
        LocalDateTime now = LocalDateTime.of(2024, 11, 8, 10, 0);

        Task task1 = new Task("Задача 1", "Описание", now.plusHours(2), 60);
        Task task2 = new Task("Задача 2", "Описание", now, 30);
        Task task3 = new Task("Задача 3", "Описание", now.plusHours(1), 45);

        manager.createTask(task2);
        manager.createTask(task1);
        manager.createTask(task3);

        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
        List<Task> taskList = new ArrayList<>(prioritizedTasks);

        assertEquals(3, taskList.size());
        assertEquals(task2, taskList.get(0));
        assertEquals(task3, taskList.get(1));
        assertEquals(task1, taskList.get(2));
    }

    @Test
    @DisplayName("Получение приоритизированных задач - включая подзадачи")
    void shouldIncludeSubTasksInPrioritizedList() {
        LocalDateTime now = LocalDateTime.of(2024, 11, 8, 10, 0);

        Task task = new Task("Задача", "Описание", now.plusHours(1), 60);
        manager.createTask(task);

        manager.createEpic(epic1);
        SubTask subTask = new SubTask("Подзадача", "Описание", now, 30, epic1.getId());
        manager.createSubTask(subTask);

        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
        List<Task> taskList = new ArrayList<>(prioritizedTasks);

        assertEquals(2, taskList.size());
        assertEquals(subTask, taskList.get(0));
        assertEquals(task, taskList.get(1));
    }

    @Test
    @DisplayName("Получение приоритизированных задач - эпики не включаются")
    void shouldNotIncludeEpicsInPrioritizedList() {
        manager.createEpic(epic1);

        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertTrue(prioritizedTasks.isEmpty());
    }

    @Test
    @DisplayName("Удаление задачи удаляет её из приоритизированного списка")
    void shouldRemoveTaskFromPrioritizedListWhenDeleted() {
        LocalDateTime now = LocalDateTime.of(2024, 11, 8, 10, 0);

        Task task1 = new Task("Задача 1", "Описание", now, 60);
        Task task2 = new Task("Задача 2", "Описание", now.plusHours(1), 60);

        manager.createTask(task1);
        manager.createTask(task2);

        assertEquals(2, manager.getPrioritizedTasks().size());

        manager.removeTaskById(task1.getId());

        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size());
        assertFalse(prioritizedTasks.contains(task1));
        assertTrue(prioritizedTasks.contains(task2));
    }

    @Test
    @DisplayName("Удаление подзадачи удаляет её из приоритизированного списка")
    void shouldRemoveSubTaskFromPrioritizedListWhenDeleted() {
        LocalDateTime now = LocalDateTime.of(2024, 11, 8, 10, 0);

        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", now, 30, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", now.plusHours(1), 45, epic1.getId());

        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        assertEquals(2, manager.getPrioritizedTasks().size());

        manager.removeSubTaskByIdAndEpicId(subTask1.getId(), epic1.getId());

        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size());
        assertFalse(prioritizedTasks.contains(subTask1));
        assertTrue(prioritizedTasks.contains(subTask2));
    }

    @Test
    @DisplayName("Очистка задач очищает приоритизированный список")
    void shouldClearPrioritizedListWhenClearingTasks() {
        LocalDateTime now = LocalDateTime.of(2024, 11, 8, 10, 0);

        Task task1 = new Task("Задача 1", "Описание", now, 60);
        Task task2 = new Task("Задача 2", "Описание", now.plusHours(1), 60);

        manager.createTask(task1);
        manager.createTask(task2);

        assertEquals(2, manager.getPrioritizedTasks().size());

        manager.clearTasks();

        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    @DisplayName("Очистка подзадач удаляет их из приоритизированного списка")
    void shouldRemoveSubTasksFromPrioritizedListWhenClearing() {
        LocalDateTime now = LocalDateTime.of(2024, 11, 8, 10, 0);

        manager.createEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", now, 30, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", now.plusHours(1), 45, epic1.getId());

        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        assertEquals(2, manager.getPrioritizedTasks().size());

        manager.clearSubTasks();

        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

}
