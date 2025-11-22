package ru.yandex.javacourse.manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.manager.http.BaseHttpHandler;
import ru.yandex.javacourse.tasks.SubTask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            List<SubTask> subtasks = taskManager.getAllSubTask();
            String response = gson.toJson(subtasks);
            sendText(exchange, response);
        } else if (pathParts.length == 3) {
            Integer id = getIdFromPath(pathParts);
            if (id == null) {
                sendNotFound(exchange, "Некорректный ID");
                return;
            }
            SubTask subtask = taskManager.getSubTaskById(id);
            String response = gson.toJson(subtask);
            sendText(exchange, response);
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange, String[] pathParts) throws IOException {
        String body = readText(exchange);
        SubTask subtask = gson.fromJson(body, SubTask.class);

        if (subtask.getId() == 0) {
            taskManager.createSubTask(subtask);
            String response = gson.toJson(subtask);
            sendCreated(exchange, response);
        } else {
            taskManager.getSubTaskById(subtask.getId());
            taskManager.updateSubTask(subtask);
            String response = gson.toJson(subtask);
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
            taskManager.getSubTaskById(id);
            taskManager.removeSubTaskById(id);
            sendText(exchange, "{\"message\": \"Подзадача удалена\"}");
        }
    }
}
