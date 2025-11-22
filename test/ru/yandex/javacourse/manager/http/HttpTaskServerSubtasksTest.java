package ru.yandex.javacourse.manager.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для эндпоинта /subtasks")
public class HttpTaskServerSubtasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;
    Gson gson = HttpTaskServer.getGson();
    HttpClient client;

    public HttpTaskServerSubtasksTest() throws IOException {
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
    @DisplayName("Добавление новой подзадачи")
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.createEpic(epic);
        int epicId = epic.getId();

        SubTask subtask = new SubTask("Subtask 1", "Subtask description",
                LocalDateTime.now(), 30, epicId);
        String subtaskJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<SubTask> subtasksFromManager = manager.getAllSubTask();
        assertNotNull(subtasksFromManager);
        assertEquals(1, subtasksFromManager.size());
        assertEquals("Subtask 1", subtasksFromManager.get(0).getTitle());
    }

    @Test
    @DisplayName("Получение всех подзадач")
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.createEpic(epic);
        int epicId = epic.getId();

        SubTask subtask1 = new SubTask("Subtask 1", "Description 1",
                LocalDateTime.now(), 30, epicId);
        SubTask subtask2 = new SubTask("Subtask 2", "Description 2",
                LocalDateTime.now().plusHours(2), 45, epicId);
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        assertTrue(response.body().contains("Subtask 1"));
        assertTrue(response.body().contains("Subtask 2"));
    }

    @Test
    @DisplayName("Получение подзадачи по ID")
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Subtask 1", "Description 1",
                LocalDateTime.now(), 30, epic.getId());
        manager.createSubTask(subtask);
        int subtaskId = subtask.getId();

        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        SubTask receivedSubtask = gson.fromJson(response.body(), SubTask.class);
        assertNotNull(receivedSubtask);
        assertEquals(subtaskId, receivedSubtask.getId());
        assertEquals("Subtask 1", receivedSubtask.getTitle());
    }

    @Test
    @DisplayName("Получение несуществующей подзадачи возвращает 404")
    public void testGetSubtaskByIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Обновление существующей подзадачи")
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Subtask 1", "Description 1",
                LocalDateTime.now(), 30, epic.getId());
        manager.createSubTask(subtask);
        int subtaskId = subtask.getId();

        subtask.setTitle("Updated Subtask");
        subtask.setStatus(Status.DONE);
        String subtaskJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        SubTask updatedSubtask = manager.getSubTaskById(subtaskId);
        assertEquals("Updated Subtask", updatedSubtask.getTitle());
        assertEquals(Status.DONE, updatedSubtask.getStatus());
    }

    @Test
    @DisplayName("Удаление подзадачи")
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.createEpic(epic);

        SubTask subtask = new SubTask("Subtask 1", "Description 1",
                LocalDateTime.now(), 30, epic.getId());
        manager.createSubTask(subtask);
        int subtaskId = subtask.getId();

        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<SubTask> subtasks = manager.getAllSubTask();
        assertEquals(0, subtasks.size());
    }

    @Test
    @DisplayName("Удаление несуществующей подзадачи возвращает 404")
    public void testDeleteSubtaskNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}
