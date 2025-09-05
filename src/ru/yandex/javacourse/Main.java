package ru.yandex.javacourse;

import ru.yandex.javacourse.manager.Manager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.SubTask;
import ru.yandex.javacourse.tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.createTask(task1);
        manager.createTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic2.getId());
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic2.getId());
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);

        System.out.println("ЗАДАЧИ:");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTask());

        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(subTask2);

        System.out.println("Выполнили одну подзадачу:");
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTask());

        manager.removeTaskById(1);
        manager.removeEpicById(3);
        System.out.println("Удалили задачу с id 1 и эпик с id 3");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTask());

        manager.removeSubTaskByIdAndEpicId(6, 4);
        System.out.println("У эпика с id 4 удалили подзадачу с id 6");
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTask());

    }
}
