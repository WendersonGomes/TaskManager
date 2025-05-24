package com.esig.taskmanager.service;

import com.esig.taskmanager.dao.TaskDAO;
import com.esig.taskmanager.model.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Named
@ApplicationScoped
public class TaskService {

    @Inject
    private TaskDAO taskDAO;

    @Transactional
    public void save(Task task) {
        if (isInvalidTask(task)) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }
        task.setStatus(Task.Status.PROGRESS);
        taskDAO.save(task);
    }

    @Transactional
    public void update(Task task) {
        taskDAO.update(task);
    }

    @Transactional
    public void completeTask(Task task) {
        task.setStatus(Task.Status.COMPLETE);
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

    public List<Task> filter(Integer id, String title, String responsible, String description, Task.Status status) {
        if (id != null) {
            Task result = taskDAO.findById(id);
            return result != null ? List.of(result) : Collections.emptyList();
        } else {
            return taskDAO.filter(title, responsible, description, status);
        }
    }

    public boolean isInvalidTask(Task t) {
        return t == null ||
                t.getTitle() == null || t.getTitle().isBlank() ||
                t.getDescription() == null || t.getDescription().isBlank() ||
                t.getResponsible() == null || t.getResponsible().isBlank() ||
                t.getPriority() == null || t.getDeadLine() == null;
    }

    @Transactional
    public void deleteCompleted() {
        taskDAO.deleteCompleted();
    }

    @Transactional
    public void deleteProgress() {
        taskDAO.deleteProgress();
    }

    public void deleteAll() {
        taskDAO.deleteAll();
    }
}