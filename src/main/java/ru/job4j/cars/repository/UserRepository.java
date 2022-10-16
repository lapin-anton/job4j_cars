package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserRepository {

    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("update User as u set u.login = :fLogin, u.password = :fPassword where u.id = :fId")
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("fPassword", user.getPassword())
                    .setParameter("fId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE User WHERE id = :fId")
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> rsl = new ArrayList<>();
        Session session = sf.openSession();
        try {
            Query query = session.createQuery("from User order by id");
            query.list().forEach(el -> rsl.add((User) el));
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
        return rsl;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Optional<User> user = Optional.empty();
        Session session = sf.openSession();
        try {
            Query<User> query = session
                    .createQuery("from User as u where u.id = :fId", User.class)
                    .setParameter("fId", userId);
            user = Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
        return user;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> rsl = new ArrayList<>();
        Session session = sf.openSession();
        try {
            Query query = session
                    .createQuery("from User as u where u.login like :fKey")
                    .setParameter("fKey", "%" + key + "%");
            query.list().forEach(el -> rsl.add((User) el));
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
        return rsl;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> user = Optional.empty();
        Session session = sf.openSession();
        try {
            Query<User> query = session
                    .createQuery("from User as u where u.login = :fLogin", User.class)
                    .setParameter("fLogin", login);
            user = Optional.ofNullable(query.uniqueResult());
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
        return user;
    }
}
