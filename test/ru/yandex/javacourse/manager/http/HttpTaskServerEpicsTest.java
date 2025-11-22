package ru.yandex.javacourse.manager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для эндпоинта /epics")
public class HttpTaskServerEpicsTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;
    Gson gson = HttpTaskServer.getGson();
    HttpClient client;

    public HttpTaskServerEpicsTest() throws IOException {
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
    @DisplayName("Добавление нового эпика")
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        String epicJson = gson.toJson(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager);
        assertEquals(1, epicsFromManager.size());
        assertEquals("Epic 1", epicsFromManager.get(0).getTitle());
    }

    @Test
    @DisplayName("Получение всех эпиков")
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        assertTrue(response.body().contains("Epic 1"));
        assertTrue(response.body().contains("Epic 2"));
    }

    @Test
    @DisplayName("Получение эпика по ID")
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Description 1");
        manager.createEpic(epic);
        int epicId = epic.getId();

        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Epic receivedEpic = gson.fromJson(response.body(), Epic.class);
        assertNotNull(receivedEpic);
        assertEquals(epicId, receivedEpic.getId());
        assertEquals("Epic 1", receivedEpic.getTitle());
    }

    @Test
    @DisplayName("Получение несуществующего эпика возвращает 404")
    public void testGetEpicByIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Получение подзадач эпика")
    public void testGetEpicSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        manager.createEpic(epic);
        int epicId = epic.getId();

        SubTask subtask1 = new SubTask("Subtask 1", "Description 1",
                LocalDateTime.now(), 30, epicId);
        SubTask subtask2 = new SubTask("Subtask 2", "Description 2",
                LocalDateTime.now().plusHours(2), 45, epicId);
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);

        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type subtaskListType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> subtasks = gson.fromJson(response.body(), subtaskListType);

        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
    }

    @Test
    @DisplayName("Получение подзадач несуществующего эпика возвращает 404")
    public void testGetEpicSubtasksNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/999/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Удаление эпика")
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Description 1");
        manager.createEpic(epic);
        int epicId = epic.getId();

        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> epics = manager.getAllEpics();
        assertEquals(0, epics.size());
    }

    @Test
    @DisplayName("Удаление несуществующего эпика возвращает 404")
    public void testDeleteEpicNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}
