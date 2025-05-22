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
    private List<Task> taskList;
    private List<String> responsibleList;

    private String filterTitle;
    private String filterResponsible;
    private Task.Status filterStatus;

    private boolean editing = false;

    @PostConstruct
    public void init() {
        taskList = taskService.findAll();
        responsibleList = List.of("John", "Maria", "Carlos", "Anna");
    }

    public List<Task.Priority> getPriorities() {
        return List.of(Task.Priority.values());
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
            // Validação simples manual (além do front-end)
            if (task.getTitle() == null || task.getTitle().isBlank() ||
                    task.getDescription() == null || task.getDescription().isBlank() ||
                    task.getResponsible() == null || task.getResponsible().isBlank() ||
                    task.getPriority() == null || task.getDeadLine() == null) {

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos os campos devem ser preenchidos.", null));
                return;
            }

            task.setStatus(Task.Status.PROGRESS); // status inicial
            taskService.save(task);

            context.getExternalContext().redirect("taskList.xhtml"); // redireciona após salvar

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar tarefa.", null));
        }
    }

    public String prepareCreate() {
        this.task = new Task();
        this.editing = false;
        return "index.xhtml?faces-redirect=true";
    }



    public void updateTask() {
        taskService.update(task);
        resetForm();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("taskList.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String editTask(Task t) {
        this.task = t;
        this.editing = true;
        return "index.xhtml?faces-redirect=true";
    }

    public void deleteTask(Task t) {
        taskService.delete(t);
        taskList = taskService.findAll();
    }

    public String prepareEdit(int id) {
        this.task = taskService.findById(id);  // busca a tarefa pelo id
        this.editing = true;
        return "index.xhtml?faces-redirect=true"; // redireciona para a página do formulário
    }


    public void applyFilter() {
        taskList = taskService.filter(filterTitle, filterResponsible, filterStatus);
    }

    public void clearCompletedTasks() {
        taskService.deleteCompleted();
        taskList = taskService.findAll();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Tarefas concluídas removidas com sucesso.", null));
    }


    private void resetForm() {
        this.task = new Task();
        this.editing = false;
        this.taskList = taskService.findAll();
    }

    public String resetFormAndRedirectToInitialPage() {
        this.task = new Task();
        this.editing = false;
        return "index.xhtml?faces-redirect=true";
    }

    public String resetFormAndRedirectToTaskListPage() {
        return "taskList.xhtml?faces-redirect=true";
    }

    public List<Task.Status> getStatusList() {
        return List.of(Task.Status.values());
    }

    public void completeTask(Task t) {
        t.setStatus(Task.Status.COMPLETE);
        taskService.update(t);
        taskList = taskService.findAll();
    }

}
