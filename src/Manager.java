import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private int idCounter = 1;

    public Manager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public List<Task> getAllTasks() {
        if (tasks.isEmpty()) { // проверяем, есть ли элементы в списке
            System.out.println("Нет сохранённых задач.");
        }
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        if (epics.isEmpty()) { // проверяем, есть ли элементы в списке
            System.out.println("Нет сохранённых эпиков.");
        }
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getAllSubTask() {
        List<SubTask> subTasks = new ArrayList<>();
        List<Epic> allEpics = getAllEpics();
        if (allEpics.isEmpty()) { // проверяем, есть ли элементы в списке
            System.out.println("Нет сохранённых эпиков.");
        } else {
            for (Epic epic : allEpics) {
                if (!epic.getSubTasks().isEmpty()) {
                    subTasks.addAll(epic.getSubTasks().values());
                }
            }
        }
        return subTasks;
    }

    //Очистить все задачи
    public void clearTasks() {
        tasks.clear();
    }

    // Очистить все главные задачи
    public void clearEpics() {
        epics.clear();
    }

    public void clearSubTasks() {
        if (!getAllEpics().isEmpty()) {
            for (Epic epic : getAllEpics()) {
                epic.clearSubtasks();
            }
        }
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            System.out.println("Задачи с id: " + id + " нет");
            return null;
        }

    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Эпика с id: " + id + " нет");
            return null;
        }
    }

    public List<SubTask> getSubTasksByEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            return new ArrayList<>(getEpicById(epic.getId()).getSubTasks().values());
        } else {
            System.out.println("Такого эпика нет.");
            return new ArrayList<>();
        }
    }

    public void createTask(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        if (epic != null) {
            subTask.setId(idCounter++);
            epic.setSubTask(subTask);
        }
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    public void updateSubTask(SubTask subTask) {
        Epic epic = getEpicById(subTask.getEpicId());
        epic.setSubTask(subTask);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (SubTask subTask : epic.getSubTasks().values()) {
            if (subTask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subTask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с id: " + id + " нет");
        }
    }

    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            epics.get(id).clearSubtasks();
            epics.remove(id);
        } else {
            System.out.println("Эпика с id: " + id + " нет");
        }
    }

    public void removeSubTaskByIdAndEpicId(int id, int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = getEpicById(epicId);
            if (epic.getSubTasks().containsKey(id)) {
                epics.get(epicId).removeSubTask(id);
                updateEpicStatus(getEpicById(epicId));
            } else {
                System.out.println("У эпика с id: " + epicId + " нет подзадачи с id: " + id);
            }

        } else {
            System.out.println("Эпика с id: " + epicId + " нет");
        }
    }
}
