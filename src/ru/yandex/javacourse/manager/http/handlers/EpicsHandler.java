package ru.yandex.javacourse.manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.manager.http.BaseHttpHandler;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            List<Epic> epics = taskManager.getAllEpics();
            String response = gson.toJson(epics);
            sendText(exchange, response);
        } else if (pathParts.length == 3) {
            Integer id = getIdFromPath(pathParts);
            if (id == null) {
                sendNotFound(exchange, "Некорректный ID");
                return;
            }
            Epic epic = taskManager.getEpicById(id);
            String response = gson.toJson(epic);
            sendText(exchange, response);
        } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
            int id;
            try {
                id = Integer.parseInt(pathParts[2]);
            } catch (NumberFormatException e) {
                sendNotFound(exchange, "Некорректный ID");
                return;
            }
            Epic epic = taskManager.getEpicById(id);
            List<SubTask> subtasks = taskManager.getSubTasksByEpic(epic);
            String response = gson.toJson(subtasks);
            sendText(exchange, response);
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange, String[] pathParts) throws IOException {
        String body = readText(exchange);
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == 0) {
            taskManager.createEpic(epic);
            String response = gson.toJson(epic);
            sendCreated(exchange, response);
        } else {
            taskManager.getEpicById(epic.getId());
            taskManager.updateEpic(epic);
            String response = gson.toJson(epic);
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
            taskManager.getEpicById(id);
            taskManager.removeEpicById(id);
            sendText(exchange, "{\"message\": \"Эпик удален\"}");
        }
    }
}
