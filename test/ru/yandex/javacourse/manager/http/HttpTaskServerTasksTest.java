package ru.yandex.javacourse.manager.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для эндпоинта /tasks")
public class HttpTaskServerTasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;
    Gson gson = HttpTaskServer.getGson();
    HttpClient client;

    public HttpTaskServerTasksTest() throws IOException {
        taskServer = new HttpTaskServer(manager);
    }

    @BeforeEach
    public void setUp() {
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();
        taskServer.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    @DisplayName("Добавление новой задачи")
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                LocalDateTime.now(), 5);
        String taskJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Test 2", tasksFromManager.get(0).getTitle());
    }

    @Test
    @DisplayName("Получение всех задач")
    public void testGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description 1",
                LocalDateTime.now(), 30);
        Task task2 = new Task("Task 2", "Description 2",
                LocalDateTime.now().plusHours(2), 45);
        manager.createTask(task1);
        manager.createTask(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        assertTrue(response.body().contains("Task 1"));
        assertTrue(response.body().contains("Task 2"));
    }

    @Test
    @DisplayName("Получение задачи по ID")
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1",
                LocalDateTime.now(), 30);
        manager.createTask(task);
        int taskId = task.getId();

        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(receivedTask);
        assertEquals(taskId, receivedTask.getId());
        assertEquals("Task 1", receivedTask.getTitle());
    }

    @Test
    @DisplayName("Получение несуществующей задачи возвращает 404")
    public void testGetTaskByIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Обновление существующей задачи")
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1",
                LocalDateTime.now(), 30);
        manager.createTask(task);
        int taskId = task.getId();

        task.setTitle("Updated Task");
        task.setStatus(Status.IN_PROGRESS);
        String taskJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        Task updatedTask = manager.getTaskById(taskId);
        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    @DisplayName("Удаление задачи")
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1",
                LocalDateTime.now(), 30);
        manager.createTask(task);
        int taskId = task.getId();

        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("Удаление несуществующей задачи возвращает 404")
    public void testDeleteTaskNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Добавление задачи с пересечением по времени возвращает 406")
    public void testAddTaskWithInteraction() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description 1",
                LocalDateTime.now(), 60);
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2",
                LocalDateTime.now().plusMinutes(30), 60);
        String taskJson = gson.toJson(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

        List<Task> tasks = manager.getAllTasks();
        assertEquals(1, tasks.size());
    }
}
