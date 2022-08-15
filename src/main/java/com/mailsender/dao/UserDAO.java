package com.mailsender.dao;

import com.mailsender.HibernateSessionFactoryUtil;
import com.mailsender.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDAO {

    private SessionFactory sessionFactory = HibernateSessionFactoryUtil.getOrCreateSessionFactory();
    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);

    public User getUserById(int id) throws Exception {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from User c where c.id = :userid");
        query.setParameter("userid", id);
        List<User> users = query.list();
        if (users.size() < 1) {
            String errorMsg = "No user with such ID";
            LOGGER.error(errorMsg);
            throw new Exception(errorMsg);
        } else if (users.size() > 1) {
            String errorMsg = "There are more then 1 users with such ID";
            LOGGER.error(errorMsg);
            throw new IllegalAccessException(errorMsg);
        } else {
            return users.get(0);
        }
    }

    public void addUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    public void deleteUserById(int userId) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(getUserById(userId));
        transaction.commit();
        session.close();
    }
}
