package com.esig.taskmanager.controller;

import com.esig.taskmanager.model.Task;
import com.esig.taskmanager.service.TaskService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
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
    private Integer taskId;
    private List<Task> taskList;
    private List<String> responsibleList;

    private String filterId;
    private String filterTitle;
    private String filterResponsible;
    private String filterDescription;
    private Task.Status filterStatus;

    private boolean editing = false;

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");

        if (idParam != null && !idParam.isBlank()) {
            try {
                this.taskId = Integer.parseInt(idParam);
                this.task = taskService.findById(taskId);
                this.editing = true;
            } catch (NumberFormatException e) {
                this.task = new Task();
                this.editing = false;
            }
        } else {
            this.task = new Task();
            this.editing = false;
        }

        refreshTaskList();

        responsibleList = List.of("João", "Maria", "Carlos", "Ana");
    }

    public List<Task.Priority> getPriorities() {
        return List.of(Task.Priority.values());
    }

    public List<Task.Status> getStatusList() {
        return List.of(Task.Status.values());
    }

    public void save() {
        if (editing) {
            updateTask();
        } else {
            createTask();
        }
    }

    public void createTask() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            taskService.save(task);
            context.getExternalContext().redirect("taskList.xhtml");

        } catch (IllegalArgumentException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar tarefa: " + e.getMessage(), null));
        }
    }

    public void updateTask() {
        try {
            taskService.update(task);
            resetForm();
            FacesContext.getCurrentInstance().getExternalContext().redirect("taskList.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar tarefa: " + e.getMessage(), null));
        }
    }

    public String prepareCreate() {
        this.task = new Task();
        this.editing = false;
        return "index.xhtml?faces-redirect=true";
    }

    public String prepareEdit(Task t) {
        this.task = t;
        this.editing = true;
        return "index.xhtml?id=" + t.getId() + "&faces-redirect=true";
    }

    public void deleteTask(Task t) {
        taskService.delete(t);
        refreshTaskList();
    }

    public void completeTask(Task t) {
        taskService.completeTask(t);
        refreshTaskList();
    }

    public void applyFilter() {
        FacesContext context = FacesContext.getCurrentInstance();
        Integer numericFilterId = null;

        if (filterId != null && !filterId.trim().isEmpty()) {
            try {
                String digitsOnly = filterId.replaceAll("\\D", "");
                if (!digitsOnly.isEmpty()) {
                    numericFilterId = Integer.parseInt(digitsOnly);
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo 'Número' deve conter apenas números.", null));
                    return;
                }
            } catch (NumberFormatException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Número inválido.", null));
                return;
            }
        }

        this.taskList = taskService.filter(numericFilterId, filterTitle, filterResponsible, filterDescription, filterStatus);
    }


    public void clearFilters() {
        this.filterId = null;
        this.filterTitle = null;
        this.filterResponsible = null;
        this.filterDescription = null;
        this.filterStatus = null;

        refreshTaskList();
    }

    public void clearCompletedTasks() {
        taskService.deleteCompleted();
        refreshTaskList();
    }

    public void clearProgressTasks() {
        taskService.deleteProgress();
        refreshTaskList();
    }

    public void clearAllTasks() {
        taskService.deleteAll();
        refreshTaskList();
    }

    private void resetForm() {
        this.task = new Task();
        this.editing = false;
        refreshTaskList();
    }

    public String resetFormAndRedirectToTaskListPage() {
        clearFilters();
        return "taskList.xhtml?faces-redirect=true";
    }

    private void refreshTaskList() {
        this.taskList = taskService.findAll();
    }
}
