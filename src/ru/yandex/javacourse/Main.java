package ru.yandex.javacourse;

import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.manager.http.HttpTaskServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.start();
    }
}
