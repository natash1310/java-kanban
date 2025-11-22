package ru.yandex.javacourse.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для FileBackedTaskManager")
class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File testFile;
    private Task testTask;
    private Epic testEpic;
    private SubTask testSubTask;

    @Override
    protected FileBackedTaskManager createManager() {
        try {
            testFile = File.createTempFile("test_history", ".csv");
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл", e);
        }
        return new FileBackedTaskManager(testFile);
    }

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();

        manager.resetIdCounter();

        testTask = new Task("Тестовая задача", "Описание");
        testEpic = new Epic("Тестовый эпик", "Описание эпика");
    }

    @AfterEach
    void cleanUp() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    @DisplayName("Сохранение и загрузка задач из файла")
    void shouldSaveAndLoadTasksFromFile() {
        manager.createTask(testTask);
        manager.createEpic(testEpic);
        testSubTask = new SubTask("Тестовая подзадача", "Описание подзадачи", testEpic.getId());
        manager.createSubTask(testSubTask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubTask().size());

        assertEquals(testTask.getTitle(), loadedManager.getTaskById(testTask.getId()).getTitle());
        assertEquals(testEpic.getTitle(), loadedManager.getEpicById(testEpic.getId()).getTitle());
        assertEquals(testSubTask.getTitle(), loadedManager.getSubTaskById(testSubTask.getId()).getTitle());
    }

    @Test
    @DisplayName("Сохранение и загрузка истории из файла")
    void shouldSaveAndLoadHistoryFromFile() {
        manager.createTask(testTask);
        manager.createEpic(testEpic);

        manager.getTaskById(testTask.getId());
        manager.getEpicById(testEpic.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        assertEquals(2, loadedManager.getHistory().size());
        assertTrue(loadedManager.getHistory().stream()
                .anyMatch(t -> t.getId() == testTask.getId()));
        assertTrue(loadedManager.getHistory().stream()
                .anyMatch(t -> t.getId() == testEpic.getId()));
    }

    @Test
    @DisplayName("Загрузка из пустого файла")
    void shouldLoadFromEmptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubTask().isEmpty());
        assertTrue(loadedManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Сохранение эпика без подзадач")
    void shouldSaveAndLoadEpicWithoutSubTasks() {
        manager.createEpic(testEpic);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        Epic loadedEpic = loadedManager.getEpicById(testEpic.getId());
        assertNotNull(loadedEpic);
        assertEquals(testEpic.getTitle(), loadedEpic.getTitle());
        assertTrue(loadedEpic.getSubTasks().isEmpty());
        assertNull(loadedEpic.getStartTime());
        assertNull(loadedEpic.getEndTime());
    }

    @Test
    @DisplayName("Должно выбросить исключение при проблемах с файлом")
    void shouldThrowException_whenFileIsInvalid() {
        File invalidFile = new File("/invalid/path/history.csv");

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager invalidManager = new FileBackedTaskManager(invalidFile);
            Task task = new Task("Задача", "Описание");
            invalidManager.createTask(task);
        }, "Должно быть выброшено исключение при попытке записи в недоступный файл");
    }

    @Test
    @DisplayName("Не должно быть исключения при корректной работе с файлом")
    void shouldNotThrowException_whenFileIsValid() {
        assertDoesNotThrow(() -> {
            manager.createTask(testTask);
            FileBackedTaskManager.loadFromFile(testFile);
        }, "Не должно быть исключения при корректной работе с файлом");
    }
}
