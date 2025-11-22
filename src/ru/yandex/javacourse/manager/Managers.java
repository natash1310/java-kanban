package ru.yandex.javacourse.manager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.javacourse.history.HistoryManager;
import ru.yandex.javacourse.history.InMemoryHistoryManager;
import ru.yandex.javacourse.manager.http.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
