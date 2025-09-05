public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager mng = new Manager();
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        mng.createTask(task1);
        mng.createTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        mng.createEpic(epic1);
        mng.createEpic(epic2);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic2.getId());
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic2.getId());
        mng.createSubTask(subTask1);
        mng.createSubTask(subTask2);
        mng.createSubTask(subTask3);

        System.out.println("ЗАДАЧИ:");
        System.out.println(mng.getAllTasks());
        System.out.println(mng.getAllEpics());
        System.out.println(mng.getAllSubTask());

        subTask2.setStatus(Status.DONE);
        mng.updateSubTask(subTask2);

        System.out.println("Выполнили одну подзадачу:");
        System.out.println(mng.getAllEpics());
        System.out.println(mng.getAllSubTask());

        mng.removeTaskById(1);
        mng.removeEpicById(3);
        System.out.println("Удалили задачу с id 1 и эпик с id 3");
        System.out.println(mng.getAllTasks());
        System.out.println(mng.getAllEpics());
        System.out.println(mng.getAllSubTask());

        mng.removeSubTaskByIdAndEpicId(6, 4);
        System.out.println("У эпика с id 4 удалили подзадачу с id 6");
        System.out.println(mng.getAllEpics());
        System.out.println(mng.getAllSubTask());

    }
}
