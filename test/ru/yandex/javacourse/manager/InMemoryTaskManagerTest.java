package ru.yandex.javacourse.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестовый класс для проверки методов класса InMemoryTaskManager")
class InMemoryTaskManagerTest extends Task {

    private final InMemoryTaskManager manager = new InMemoryTaskManager();
    private final String titleOfTask1 = "Задача 1";
    private final String titleOfTask2 = "Задача 2";
    private final String titleOfTask3 = "Задача 3";
    private final String titleOfEpic1 = "Эпик 1";
    private final String titleOfEpic2 = "Эпик 2";
    private final String titleOfEpic3 = "Эпик 3";
    private final String titleOfSubTask1 = "Подзадача 1";
    private final String titleOfSubTask2 = "Подзадача 2";
    private final String titleOfSubTask3 = "Подзадача 3";
    private final String titleOfSubTask4 = "Подзадача 4";
    private final String descriptionOfTask1 = "Описание задачи 1";
    private final String descriptionOfTask2 = "Описание задачи 2";
    private final String descriptionOfTask3 = "Описание задачи 3";
    private final String descriptionOfEpic1 = "Описание эпика 1";
    private final String descriptionOfEpic2 = "Описание эпика 2";
    private final String descriptionOfEpic3 = "Описание эпика 3";
    private final String descriptionOfSubTask1 = "Описание подзадачи 1";
    private final String descriptionOfSubTask2 = "Описание подзадачи 2";
    private final String descriptionOfSubTask3 = "Описание подзадачи 3";
    private final String descriptionOfSubTask4 = "Описание подзадачи 4";
    private final int taskId1 = 1;
    private final int taskId2 = 2;
    private final int epicId1 = 3;
    private final int epicId2 = 4;
    private final int subTaskId1 = 5;
    private final int subTaskId2 = 6;
    private final int subTaskId3 = 7;
    private final int countOfTask = 2;
    private final int countOfEpic = 2;
    private final int countOfSubTask = 3;

    @BeforeEach
    public void setUp() {
        //given
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();
        manager.resetIdCounter();
        Task task1 = new Task(titleOfTask1, descriptionOfTask1);
        Task task2 = new Task(titleOfTask2, descriptionOfTask2);
        manager.createTask(task1);
        manager.createTask(task2);
        Epic epic1 = new Epic(titleOfEpic1, descriptionOfEpic1);
        Epic epic2 = new Epic(titleOfEpic2, descriptionOfEpic2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        SubTask subTask1 = new SubTask(titleOfSubTask1, descriptionOfSubTask1, epic1.getId());
        SubTask subTask2 = new SubTask(titleOfSubTask2, descriptionOfSubTask2, epic2.getId());
        SubTask subTask3 = new SubTask(titleOfSubTask3, descriptionOfSubTask3, epic2.getId());
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);
    }

    @Test
    @DisplayName("Проверка получения списка всех задач")
    void shouldGetAllTasksTest() {
        assertEquals(countOfTask, manager.getAllTasks().size());
    }

    @Test
    @DisplayName("Проверка получения списка всех эпиков")
    void shouldGetAllEpicsTest() {
        assertEquals(countOfEpic, manager.getAllEpics().size());
    }

    @Test
    @DisplayName("Проверка получения списка всех подзадач")
    void shouldGetAllSubTaskTest() {
        assertEquals(countOfSubTask, manager.getAllSubTask().size());
    }

    @Test
    @DisplayName("Удаление всех задач")
    void shouldClearAllTasksTest() {
        //when
        assertEquals(countOfTask, manager.getAllTasks().size());
        manager.clearTasks();
        //then
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    @DisplayName("Удаление всех эпиков")
    void shouldClearAllEpicsTest() {
        //when
        assertEquals(countOfEpic, manager.getAllEpics().size());
        manager.clearEpics();
        //then
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    @DisplayName("Удаление всех подзадач")
    void shouldClearAllSubTasks_whenCalled() {
        //when
        assertEquals(countOfSubTask, manager.getAllSubTask().size());
        manager.clearSubTasks();
        //then
        assertTrue(manager.getAllSubTask().isEmpty());
    }

    @Test
    @DisplayName("Получение задачи по идентификатору успешно")
    void shouldGetTaskById_whenTaskExists() {
        //when
        Task task = manager.getTaskById(taskId1);
        //then
        assertNotNull(task);
        assertEquals(taskId1, task.getId());
        assertEquals(titleOfTask1, task.getTitle());
        assertEquals(descriptionOfTask1, task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
    }


    @Test
    @DisplayName("Получение задач по индентификатору")
    void shouldGetSubTaskById_whenSubTaskExists() {
        //when
        SubTask subTask = manager.getSubTaskById(subTaskId1);
        //then
        assertNotNull(subTask);
        assertEquals(subTaskId1, subTask.getId());
        assertEquals(titleOfSubTask1, subTask.getTitle());
        assertEquals(descriptionOfSubTask1, subTask.getDescription());
        assertEquals(Status.NEW, subTask.getStatus());
        assertEquals(epicId1, subTask.getEpicId());
    }

    @Test
    @DisplayName("Получение эпика по идентификатору")
    void shouldGetEpicById_whenEpicExists() {
        //when
        Epic epic = manager.getEpicById(epicId1);
        //then
        assertNotNull(epic, "Эпик не должен быть null");
        assertEquals(epicId1, epic.getId(), "Неверный id эпика");
        assertEquals(titleOfEpic1, epic.getTitle(), "Неверный заголовок эпика");
        assertEquals(descriptionOfEpic1, epic.getDescription(), "Неверное описание эпика");
        assertEquals(Status.NEW, epic.getStatus(), "Неверный статус эпика");
        assertEquals(1, epic.getSubTasks().size(), "Неверное количество подзадач в эпике");
        assertTrue(epic.getSubTasks().contains(subTaskId1), "Эпик должен содержать подзадачу с id=5");
    }


    @Test
    @DisplayName("Получение списка подзадач по эпику")
    void shouldGetSubTasksByEpic() {
        //given
        Epic epic = manager.getEpicById(epicId2);
        //when
        List<SubTask> subTasks = manager.getSubTasksByEpic(epic);
        //then
        assertNotNull(subTasks, "Список подзадач не должен быть null");
        assertEquals(2, subTasks.size(), "Неверное количество подзадач");
        assertEquals(subTaskId2, subTasks.get(0).getId(), "Неверный id первой подзадачи");
        assertEquals(subTaskId3, subTasks.get(1).getId(), "Неверный id последней подзадачи");
    }


    @Test
    @DisplayName("Создание новой задачи")
    void shouldCreateTask() {
        //given
        Task task = new Task(titleOfTask3, descriptionOfTask3);
        //when
        manager.createTask(task);
        // then
        assertEquals(countOfTask + 1, manager.getAllTasks().size(), "Общее количество задач должно быть 3");
        Task taskFromManager = manager.getTaskById(task.getId());
        assertNotNull(taskFromManager, "Задача должна быть найдена по id");
        assertEquals(task.getTitle(), taskFromManager.getTitle(), "Заголовок задачи должен совпадать");
        assertEquals(task.getDescription(), taskFromManager.getDescription(), "Описание задачи должно совпадать");
        assertEquals(Status.NEW, taskFromManager.getStatus(), "Статус новой задачи должен быть NEW");
    }


    @Test
    @DisplayName("Создание нового эпика")
    void shouldCreateEpic() {
        //given
        Epic epic = new Epic(titleOfEpic3, descriptionOfEpic3);
        //when
        manager.createEpic(epic);
        //then
        assertEquals(countOfEpic + 1, manager.getAllEpics().size(), "Количество эпиков должно быть равно 3");
        Epic epicFromManager = manager.getEpicById(epic.getId());
        assertNotNull(epicFromManager, "Эпик должен быть найден по id");
        assertEquals(epic.getTitle(), epicFromManager.getTitle(), "Заголовок эпика должен совпадать");
        assertEquals(epic.getDescription(), epicFromManager.getDescription(), "Описание эпика должно совпадать");
        assertEquals(Status.NEW, epicFromManager.getStatus(), "Статус нового эпика должен быть NEW");
    }

    @Test
    @DisplayName("Создание новой подзадачи")
    void shouldCreateSubTask() {
        // given
        SubTask subTask = new SubTask(titleOfSubTask4, descriptionOfSubTask4, 3);
        // when
        manager.createSubTask(subTask);
        // then
        assertEquals(countOfSubTask + 1, manager.getAllSubTask().size(), "Количество подзадач должно быть 4");
        SubTask subTaskFromManager = manager.getSubTaskById(subTask.getId());
        assertNotNull(subTaskFromManager, "Подзадача должна быть найдена по id");
        assertEquals(subTask.getTitle(), subTaskFromManager.getTitle(), "Заголовок подзадачи должен совпадать");
        assertEquals(subTask.getDescription(), subTaskFromManager.getDescription(), "Описание подзадачи должно совпадать");
        assertEquals(Status.NEW, subTaskFromManager.getStatus(), "Статус новой подзадачи должен быть NEW");
        assertEquals(subTask.getEpicId(), subTaskFromManager.getEpicId(), "Идентификатор эпика должен совпадать");
        subTask.setEpicId(6);
        manager.createSubTask(subTask);
        assertEquals(countOfSubTask + 1, manager.getAllSubTask().size(), "Количество подзадач не должно увеличиваться при повторном добавлении");
    }


    @Test
    @DisplayName("Обновление существующей задачи")
    void shouldUpdateTask() {
        //given
        Task updatedTask = manager.getTaskById(taskId1);
        updatedTask.setDescription("Новое описание задачи 1");
        updatedTask.setTitle("Новое название задачи 1");
        updatedTask.setStatus(Status.DONE);
        // when
        manager.updateTask(updatedTask);
        // then
        Task taskFromManager = manager.getTaskById(taskId1);
        assertNotNull(taskFromManager, "Задача не должна быть null после обновления");
        assertEquals(updatedTask.getTitle(), taskFromManager.getTitle(), "Заголовок задачи не обновился");
        assertEquals(updatedTask.getDescription(), taskFromManager.getDescription(), "Описание задачи не обновилось");
        assertEquals(updatedTask.getStatus(), taskFromManager.getStatus(), "Статус задачи не обновился");
    }

    @Test
    @DisplayName("Обновление существующего эпика")
    void shouldUpdateEpic() {
        // given
        Epic updatedEpic = manager.getEpicById(epicId1);
        updatedEpic.setDescription("Новое описание эпика 1");
        updatedEpic.setTitle("Новое название эпика 1");
        updatedEpic.setStatus(Status.DONE);
        // when
        manager.updateEpic(updatedEpic);
        // then
        Epic epicFromManager = manager.getEpicById(epicId1);
        assertNotNull(epicFromManager, "Эпик не должен быть null после обновления");
        assertEquals(updatedEpic.getTitle(), epicFromManager.getTitle(), "Заголовок эпика не обновился");
        assertEquals(updatedEpic.getDescription(), epicFromManager.getDescription(), "Описание эпика не обновилось");
        assertEquals(updatedEpic.getStatus(), epicFromManager.getStatus(), "Статус эпика не обновился");
    }


    @Test
    @DisplayName("Обновление существующей подзадачи")
    void shouldUpdateSubTask() {
        // given
        SubTask updatedSubTask = manager.getSubTaskById(subTaskId1);
        updatedSubTask.setDescription("Новое описание подзадачи 1");
        updatedSubTask.setTitle("Новое название подзадачи 1");
        updatedSubTask.setStatus(Status.DONE);
        // when
        manager.updateSubTask(updatedSubTask);
        // then
        SubTask subTaskFromManager = manager.getSubTaskById(subTaskId1);
        assertNotNull(subTaskFromManager, "Подзадача не должна быть null после обновления");
        assertEquals(updatedSubTask.getTitle(), subTaskFromManager.getTitle(), "Заголовок подзадачи не обновился");
        assertEquals(updatedSubTask.getDescription(), subTaskFromManager.getDescription(), "Описание подзадачи не обновилось");
        assertEquals(updatedSubTask.getStatus(), subTaskFromManager.getStatus(), "Статус подзадачи не обновился");
    }


    @Test
    @DisplayName("Удаление задачи по идентификатору")
    void shouldRemoveTaskById() {
        //given
        assertEquals(countOfTask, manager.getAllTasks().size(), "Изначально должно быть 2 задачи");
        //when
        manager.removeTaskById(taskId1);
        //then
        assertEquals(1, manager.getAllTasks().size(), "После удаления должно остаться 1 задача");
        assertEquals(taskId2, manager.getAllTasks().getFirst().getId(), "Оставшаяся задача должна иметь id=2");
    }


    @Test
    @DisplayName("Удаление эпика по идентификатору")
    void shouldRemoveEpicById() {
        //given
        assertEquals(countOfEpic, manager.getAllEpics().size(), "Изначально должно быть 2 эпика");
        //when
        manager.removeEpicById(epicId1);
        //then
        assertEquals(1, manager.getAllEpics().size(), "После удаления должен остаться 1 эпик");
        assertEquals(epicId2, manager.getAllEpics().getFirst().getId(), "Оставшийся эпик должен иметь id=4");
    }


    @Test
    @DisplayName("Удаление подзадачи по идентификатору и идентификатору эпика")
    void shouldRemoveSubTaskByIdAndEpicId() {
        //when
        manager.removeSubTaskByIdAndEpicId(subTaskId1, epicId1);
        //then
        assertNull(manager.getSubTaskById(subTaskId1), "Подзадача должна быть удалена");
        assertFalse(manager.getEpicById(epicId1).getSubTasks().contains(subTaskId1), "Подзадача не должна присутствовать в эпике");
    }


    @Test
    @DisplayName("Проверка получения истории")
    void shouldGetHistory() {
        //given
        List<Task> history = manager.getHistory();
        assertTrue(history.isEmpty(), "История изначально должна быть пустой");
        //when
        Task task = manager.getTaskById(taskId1);
        Epic epic = manager.getEpicById(epicId1);
        SubTask subTask = manager.getSubTaskById(subTaskId1);
        //then
        history = manager.getHistory();
        assertFalse(history.isEmpty(), "История не должна быть пустой после получения задач");
        assertEquals(3, history.size(), "История должна содержать 3 задачи");
        assertTrue(history.contains(task), "История должна содержать задачу");
        assertTrue(history.contains(epic), "История должна содержать эпик");
        assertTrue(history.contains(subTask), "История должна содержать подзадачу");
    }


    @Test
    @DisplayName("Задачи с одинаковым id считаются равными")
    void tasksShouldBeEquals() {
        Task task = manager.getTaskById(taskId1);
        Task task2 = manager.getTaskById(taskId2);
        task2.setId(taskId1);
        assertEquals(task, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    @DisplayName("Подзадачи с одинаковым id считаются равными")
    void subTasksShouldBeEquals() {
        SubTask subTask = manager.getSubTaskById(subTaskId1);
        SubTask subTask2 = manager.getSubTaskById(subTaskId2);
        subTask2.setId(subTaskId1);
        assertEquals(subTask, subTask2, "Подзадачи с одинаковым id должны быть равны");
    }


    @Test
    @DisplayName("Эпики с одинаковым id считаются равными")
    void epicsShouldBeEquals() {
        Epic epic = manager.getEpicById(epicId1);
        Epic epic2 = manager.getEpicById(epicId2);
        epic2.setId(epicId1);
        assertEquals(epic, epic2, "Эпики с одинаковым id должны быть равны");
    }


}