package com.esig.taskmanager.controller;

import com.esig.taskmanager.model.Task;
import com.esig.taskmanager.service.TaskService;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Named("taskBean")
@ViewScoped
public class TaskBean implements Serializable {

    @Inject
    private TaskService taskService;

    private Task task = new Task();
    private List<Task> tasks;
    private String filterTitle;
    private String filterResponsible;
    private Task.Status filterStatus;

    @PostConstruct
    public void init() {
        tasks = taskService.findAll();
    }

    public void save() {
        if (task.getId() == 0) {
            taskService.save(task);
        } else {
            taskService.update(task);
        }
        task = new Task();
        tasks = taskService.findAll();
    }

    public void edit(Task t) {
        this.task = t;
    }

    public void delete(Task t) {
        taskService.delete(t);
        tasks = taskService.findAll();
    }

    public void filter() {
        tasks = taskService.filter(filterTitle, filterResponsible, filterStatus);
    }

    public void clearCompleted() {
        taskService.deleteCompleted();
        tasks = taskService.findAll();
    }
}
