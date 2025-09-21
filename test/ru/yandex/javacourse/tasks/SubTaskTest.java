package ru.yandex.javacourse.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тестовый класс для проверки методов класса SubTask")
class SubTaskTest extends Task {
    private SubTask subTask;
    private final String titleOfSubTask = "SubTask";
    private final String descriptionOfSubTask = "Test description";
    private final int epicId = 1;


    @BeforeEach
    void beforeEach() {
        //given
        subTask = new SubTask(titleOfSubTask, descriptionOfSubTask, epicId);
    }

    @Test
    @DisplayName("Получение идентификатора эпика из подзадачи")
    void shouldReturnCorrectEpicIdFromSubTask() {
        //when
        int actualEpicId = subTask.getEpicId();
        //then
        assertEquals(epicId, actualEpicId, "Идентификатор эпика должен совпадать с ожидаемым");
    }


    @Test
    @DisplayName("Установка идентификатора эпика должна корректно обновлять значение")
    void shouldSetEpicIdCorrectly() {
        //given
        int expectedEpicId = 2;
        //when
        subTask.setEpicId(expectedEpicId);
        //then
        assertEquals(expectedEpicId, subTask.getEpicId(), "Идентификатор эпика должен быть обновлен корректно");
    }

}