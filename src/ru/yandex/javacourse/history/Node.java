package ru.yandex.javacourse.history;

import ru.yandex.javacourse.tasks.Task;

import java.util.Objects;

public class Node {

    private final Task data;
    private Node next;
    private Node previous;

    @Override
    public String toString() {
        return "Node{" +
                "task=" + data;
    }

    public Node getPreviousNode() {
        return previous;
    }

    public void setPreviousNode(Node previous) {
        this.previous = previous;
    }

    public Node(Task task, Node next, Node previous) {
        this.data = task;
        this.next = next;
        this.previous = previous;
    }

    public Task getData() {
        return data;
    }

    public Node getNextNode() {
        return next;

    }

    public void setNextNode(Node nextNode) {
        this.next = nextNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(data, node.data) && Objects.equals(next, node.next) && Objects.equals(previous, node.previous);

    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next, previous);
    }

}