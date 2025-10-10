package ru.yandex.javacourse.history;


import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList taskCustomLinkedList;
    private final HashMap<Integer, Node> taskHashMap;

    public InMemoryHistoryManager() {
        taskCustomLinkedList = new CustomLinkedList();
        taskHashMap = new HashMap<>();
    }

    public class CustomLinkedList {
        private Node head;
        private Node tail;

        public void insert(Task data) {
            if (head == null) {
                head = new Node(data, null, null);
                tail = head;
            } else {
                Node oldTail = tail;
                tail = new Node(data, null, oldTail);
                oldTail.setNextNode(tail);
            }
        }

        public void removeNode(Node node) {
            Node oldPrevious = node.getPreviousNode();
            Node oldNext = node.getNextNode();
            if (node == head) {
                if (oldNext == null) {
                    head = null;
                    tail = null;
                } else {
                    oldNext.setPreviousNode(null);
                    head = oldNext;
                }
            } else if (oldNext != null) {
                oldPrevious.setNextNode(oldNext);
                oldNext.setPreviousNode(oldPrevious);
            } else {
                oldPrevious.setNextNode(null);
                tail = oldPrevious;
            }
        }

        public void removeAll() {
            head = null;
        }

        public Node getHead() {
            return head;
        }


        public Node getTail() {
            return tail;
        }

    }

    private ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskHistory = new ArrayList<>();
        if (taskCustomLinkedList.getHead() == null) {
            return taskHistory;
        } else {
            Node currentNode = taskCustomLinkedList.getHead();
            while (currentNode != null) {
                taskHistory.add(currentNode.getData());
                currentNode = currentNode.getNextNode();
            }
        }

        return taskHistory;
    }

    @Override
    public void add(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskCustomLinkedList.removeNode(taskHashMap.get(task.getId()));
        }
        taskCustomLinkedList.insert(task);
        taskHashMap.put(task.getId(), taskCustomLinkedList.getTail());
    }

    @Override
    public void remove(int id) {
        if (taskHashMap.containsKey(id)) {
            taskCustomLinkedList.removeNode(taskHashMap.get(id));
            taskHashMap.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getAllTasks();
    }

    public HashMap<Integer, Node> getTaskHashMap() {
        return taskHashMap;
    }

    public CustomLinkedList getTaskCustomLinkedList() {
        return taskCustomLinkedList;
    }

}


