package com.esig.taskmanager.service;

import com.esig.taskmanager.dao.TaskDAO;
import com.esig.taskmanager.model.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.util.List;

@Named
@ApplicationScoped
public class TaskService {

    @Inject
    private TaskDAO taskDAO;

    @Transactional
    public void save(Task task) {
        taskDAO.save(task);
    }

    @Transactional
    public void update(Task task) {
        taskDAO.update(task);
    }

    @Transactional
    public void delete(Task task) {
        taskDAO.delete(task);
    }

    public Task findById(int id) {
        return taskDAO.findById(id);
    }

    public List<Task> findAll() {
        return taskDAO.findAll();
    }

    public List<Task> filter(String title, String responsible, String description, Task.Status status) {
        return taskDAO.filter(title, responsible, description, status);
    }

    @Transactional
    public void deleteCompleted() {
        taskDAO.deleteCompleted();
    }

    @Transactional
    public void deleteProgress() {
        taskDAO.deleteProgress();
    }
}
