package ru.yandex.javacourse;


import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.createTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", 3);
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", 3);
        taskManager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", 3);
        taskManager.createSubTask(subTask3);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic2);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(4);
        taskManager.getTaskById(2);
        System.out.println(taskManager.getHistory());

        taskManager.getSubTaskById(5);
        taskManager.getEpicById(3);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(4);
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);
        System.out.println(taskManager.getHistory());
    }
}
