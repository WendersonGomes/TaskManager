package com.esig.taskmanager.dao;

import com.esig.taskmanager.model.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class TaskDAO {

    @PersistenceContext(unitName = "tasksPU")
    private EntityManager entityManager;

    public void save(Task task) {
        entityManager.persist(task);
    }

    public void update(Task task) {
        entityManager.merge(task);
    }

    public void delete(Task task) {
        Task managed = entityManager.merge(task);
        entityManager.remove(managed);
    }

    public Task findById(int id) {
        return entityManager.find(Task.class, id);
    }

    public List<Task> findAll() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    public List<Task> filter(String title, String responsible, Task.Status status) {
        String jpql = "SELECT t FROM Task t WHERE " +
                "(:title IS NULL OR t.title LIKE :title) AND " +
                "(:responsible IS NULL OR t.responsible LIKE :responsible) AND " +
                "(:status IS NULL OR t.status = :status)";

        return entityManager.createQuery(jpql, Task.class)
                .setParameter("title", title == null ? null : "%" + title + "%")
                .setParameter("responsible", responsible == null ? null : "%" + responsible + "%")
                .setParameter("status", status)
                .getResultList();
    }

    public void deleteCompleted() {
        entityManager.createQuery("DELETE FROM Task t WHERE t.status = :status")
                .setParameter("status", Task.Status.COMPLETE)
                .executeUpdate();
    }
}
