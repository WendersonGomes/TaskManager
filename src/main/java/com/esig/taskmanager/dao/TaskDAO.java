package com.esig.taskmanager.dao;

import com.esig.taskmanager.model.Task;
import jakarta.annotation.PostConstruct;
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
        entityManager.getTransaction().begin();
        entityManager.persist(task);
        entityManager.getTransaction().commit();
    }

    public void update(Task task) {
        entityManager.getTransaction().begin();
        entityManager.merge(task);
        entityManager.getTransaction().commit();
    }

    public void delete(Task task) {
        entityManager.getTransaction().begin();
        Task managed = entityManager.merge(task);
        entityManager.remove(managed);
        entityManager.getTransaction().commit();
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
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Task t WHERE t.status = :status")
                .setParameter("status", Task.Status.COMPLETE)
                .executeUpdate();
        entityManager.getTransaction().commit();
    }
}
