package ru.yandex.javacourse.tasks;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тестовый класс для проверки методов класса Task")
class TaskTest extends Task {

    private Task task;
    private final String titleOfTask = "Test task";
    private final String descriptionOfTask = "Test description";

    @BeforeEach
    void beforeEach() {
        //given
        task = new Task("Test task", "Test description");

    }

    @Test
    @DisplayName("Получение заголовка задачи возвращает корректное значение")
    void shouldReturnCorrectTitle() {
        //when
        String actualTitle = task.getTitle();
        //then
        assertEquals(titleOfTask, actualTitle, "Заголовок задачи должен совпадать с ожидаемым");
    }


    @Test
    @DisplayName("Установка заголовка задачи корректно изменяет значение")
    void shouldSetTitleCorrectly() {
        //given
        String newTitle = "Another test task";
        //when
        task.setTitle(newTitle);
        //then
        assertEquals(newTitle, task.getTitle(), "Заголовок задачи должен быть обновлен корректно");
    }


    @Test
    @DisplayName("Получение описания задачи возвращает корректное значение")
    void shouldReturnCorrectDescription() {
        //when
        String actualDescription = task.getDescription();
        //then
        assertEquals(descriptionOfTask, actualDescription, "Описание задачи должно совпадать с ожидаемым");
    }

    @Test
    @DisplayName("Установка описания задачи корректно изменяет значение")
    void shouldSetDescriptionCorrectly() {
        //given
        String newDescription = "Another test task";
        //when
        task.setDescription(newDescription);
        //then
        assertEquals(newDescription, task.getDescription(), "Описание задачи должно быть обновлено корректно");
    }


    @Test
    @DisplayName("Получение статуса задачи возвращает корректное значение")
    void shouldReturnCorrectStatus() {
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    @DisplayName("Установка статуса задачи корректно обновляет значение")
    void shouldSetStatusCorrectly() {
        //when
        task.setStatus(Status.DONE);
        //then
        assertEquals(Status.DONE, task.getStatus());
    }
}