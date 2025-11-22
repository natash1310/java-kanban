package ru.yandex.javacourse.manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.manager.http.BaseHttpHandler;
import ru.yandex.javacourse.tasks.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            List<Task> tasks = taskManager.getAllTasks();
            String response = gson.toJson(tasks);
            sendText(exchange, response);
        } else if (pathParts.length == 3) {
            Integer id = getIdFromPath(pathParts);
            if (id == null) {
                sendNotFound(exchange, "Некорректный ID");
                return;
            }
            Task task = taskManager.getTaskById(id);
            String response = gson.toJson(task);
            sendText(exchange, response);
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange, String[] pathParts) throws IOException {
        String body = readText(exchange);
        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0) {
            taskManager.createTask(task);
            String response = gson.toJson(task);
            sendCreated(exchange, response);
        } else {
            taskManager.getTaskById(task.getId());
            taskManager.updateTask(task);
            String response = gson.toJson(task);
            sendCreated(exchange, response);
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            Integer id = getIdFromPath(pathParts);
            if (id == null) {
                sendNotFound(exchange, "Некорректный ID");
                return;
            }
            taskManager.getTaskById(id);
            taskManager.removeTaskById(id);
            sendText(exchange, "{\"message\": \"Задача удалена\"}");
        }
    }
}
