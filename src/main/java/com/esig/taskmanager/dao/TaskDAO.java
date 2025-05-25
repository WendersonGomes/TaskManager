package com.esig.taskmanager.dao;

import com.esig.taskmanager.model.Task;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

@ApplicationScoped
public class TaskDAO {

    private EntityManagerFactory emf;
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        emf = Persistence.createEntityManagerFactory("tasksPU");
        entityManager = emf.createEntityManager();
    }

    public void save(Task task) {
        try {
            beginTransaction();
            if (task.getId() == 0) {
                entityManager.persist(task);
            } else {
                entityManager.merge(task);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    public void update(Task task) {
        try {
            beginTransaction();
            entityManager.merge(task);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    public void delete(Task task) {
        try {
            beginTransaction();
            Task managed = entityManager.merge(task);
            entityManager.remove(managed);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    public void deleteAll() {
        try {
            beginTransaction();
            entityManager.createQuery("DELETE FROM Task").executeUpdate();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    public Task findById(int id) {
        return entityManager.find(Task.class, id);
    }

    public List<Task> findAll() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    public List<Task> filter(String title, String responsible, String description, Task.Status status) {
        String jpql = "SELECT t FROM Task t WHERE " +
                "(:title IS NULL OR t.title LIKE :title) AND " +
                "(:responsible IS NULL OR t.responsible LIKE :responsible) AND " +
                "(:description IS NULL OR t.description LIKE :description) AND " +
                "(:status IS NULL OR t.status = :status)";

        return entityManager.createQuery(jpql, Task.class)
                .setParameter("title", title == null ? null : "%" + title + "%")
                .setParameter("responsible", responsible == null ? null : "%" + responsible + "%")
                .setParameter("description", description == null ? null : "%" + description + "%")
                .setParameter("status", status)
                .getResultList();
    }

    public void deleteCompleted() {
        try {
            beginTransaction();
            entityManager.createQuery("DELETE FROM Task t WHERE t.status = :status")
                    .setParameter("status", Task.Status.COMPLETE)
                    .executeUpdate();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    public void deleteProgress() {
        try {
            beginTransaction();
            entityManager.createQuery("DELETE FROM Task t WHERE t.status = :status")
                    .setParameter("status", Task.Status.PROGRESS)
                    .executeUpdate();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    private void beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
    }

    private void commitTransaction() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().commit();
        }
    }

    private void rollbackTransaction() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @PreDestroy
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}