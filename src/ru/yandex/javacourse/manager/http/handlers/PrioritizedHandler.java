package ru.yandex.javacourse.manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.manager.http.BaseHttpHandler;
import ru.yandex.javacourse.tasks.Task;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange exchange, String[] pathParts) throws IOException {
        TreeSet<Task> prioritized = taskManager.getPrioritizedTasks();
        String response = gson.toJson(prioritized);
        sendText(exchange, response);
    }

    @Override
    protected void handlePost(HttpExchange exchange, String[] pathParts) throws IOException {
        exchange.sendResponseHeaders(405, -1);
    }

    @Override
    protected void handleDelete(HttpExchange exchange, String[] pathParts) throws IOException {
        exchange.sendResponseHeaders(405, -1);
    }
}
