package com.esig.taskmanager.controller;

import com.esig.taskmanager.model.Task;
import com.esig.taskmanager.service.TaskService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Named("taskBean")
@ViewScoped
public class TaskBean implements Serializable {

    @Inject
    private TaskService taskService;

    private Task task = new Task();
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
            if (taskService.isInvalidTask(task)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos os campos devem ser preenchidos.", null));
                return;
            }

            taskService.save(task);

            context.getExternalContext().redirect("taskList.xhtml");

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

    public String editTask(Task t) {
        this.task = t;
        this.editing = true;
        return "index.xhtml?faces-redirect=true";
    }

    public String prepareEdit(int id) {
        this.task = taskService.findById(id);
        this.editing = true;
        return "index.xhtml?faces-redirect=true";
    }

    public void deleteTask(Task t) {
        taskService.delete(t);
        refreshTaskList();
    }

    public void completeTask(Task t) {
        taskService.update(t);
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
                    // Se digitou algo mas não tinha números
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo 'Número' deve conter apenas números.", null));
                    return;
                }
            } catch (NumberFormatException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Número inválido.", null));
                return;
            }
        }

        // Aplica filtro normalmente
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
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Tarefas concluídas removidas com sucesso.", null));
    }

    public void clearProgressTasks() {
        taskService.deleteProgress();
        refreshTaskList();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Tarefas em andamento removidas com sucesso.", null));
    }

    public void clearAllTasks() {
        taskService.deleteAll();
        refreshTaskList();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Todas as tarefas foram removidas.", null));
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
