package ru.yandex.javacourse.manager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тесты для эндпоинта /prioritized")
public class HttpTaskServerPrioritizedTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;
    Gson gson = HttpTaskServer.getGson();
    HttpClient client;

    public HttpTaskServerPrioritizedTest() throws IOException {
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
    @DisplayName("Получение приоритизированного списка задач")
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description 1",
                LocalDateTime.now().plusHours(2), 30);
        Task task2 = new Task("Task 2", "Description 2",
                LocalDateTime.now(), 45);
        Task task3 = new Task("Task 3", "Description 3",
                LocalDateTime.now().plusHours(4), 60);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskListType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> prioritized = gson.fromJson(response.body(), taskListType);

        assertNotNull(prioritized);
        assertEquals(3, prioritized.size());

        assertEquals("Task 2", prioritized.get(0).getTitle());
        assertEquals("Task 1", prioritized.get(1).getTitle());
        assertEquals("Task 3", prioritized.get(2).getTitle());
    }

    @Test
    @DisplayName("Получение пустого приоритизированного списка")
    public void testGetEmptyPrioritizedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskListType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> prioritized = gson.fromJson(response.body(), taskListType);

        assertNotNull(prioritized);
        assertEquals(0, prioritized.size());
    }
}
