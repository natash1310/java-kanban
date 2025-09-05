import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, SubTask> subTasks;

    public Epic(String title, String description) {
        super(title, description);
        this.subTasks = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTask(SubTask subTask) {
        this.subTasks.put(subTask.getId(), subTask);
    }

    public void removeSubTask(int id){
        this.subTasks.remove(id);
    }

    public void clearSubtasks(){
        subTasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
               "id=" + super.getId() +
               ", title='" + super.getTitle() + '\'' +
               ", description='" + super.getDescription() + '\'' +
               ", status=" + super.getStatus() +
               "}\n";
    }
}
