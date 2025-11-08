package ru.yandex.javacourse.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты для InMemoryHistoryManager")
class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();

        task1 = new Task("Задача 1", "Описание 1");
        task1.setId(1);

        task2 = new Task("Задача 2", "Описание 2");
        task2.setId(2);

        task3 = new Task("Задача 3", "Описание 3");
        task3.setId(3);
    }

    @Test
    @DisplayName("Добавление задачи в историю")
    void shouldAddTaskToHistory() {
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    @DisplayName("Пустая история задач")
    void shouldReturnEmptyHistory_whenNoTasksAdded() {
        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty());
    }

    @Test
    @DisplayName("Дублирование задач в истории - задача должна переместиться в конец")
    void shouldMoveDuplicateTaskToEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task1, history.get(2));
    }

    @Test
    @DisplayName("Удаление задачи из начала истории")
    void shouldRemoveTaskFromBeginning() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    @DisplayName("Удаление задачи из середины истории")
    void shouldRemoveTaskFromMiddle() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    @DisplayName("Удаление задачи из конца истории")
    void shouldRemoveTaskFromEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    @DisplayName("Удаление несуществующей задачи не влияет на историю")
    void shouldNotAffectHistory_whenRemovingNonExistentTask() {
        historyManager.add(task1);
        historyManager.remove(999);

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }
}
