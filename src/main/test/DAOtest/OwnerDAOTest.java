package DAOtest;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rmit.model.Persons.Owner;
import org.rmit.Helper.*;
import org.rmit.database.*;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class OwnerDAOTest {

    public OwnerDAOTest() throws InterruptedException {
        DatabaseUtil.getSession();
        System.out.println("Session created");
        Thread.sleep(1000);
    }

    @BeforeAll
    public static void warmUp() {
        System.out.println("Warming up");
        try (Session session = DatabaseUtil.getSession()) {
            session.createNativeQuery("SELECT 1").getSingleResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetOwnerDAO() {
        OwnerDAO ownerDAO = new OwnerDAO();
        assertNotNull(ownerDAO.get(1, EntityGraphUtils::SimpleOwner));
    }

    @Test
    void testGetAllOwnerDAO() {
        OwnerDAO ownerDAO = new OwnerDAO();
        assertNotNull(ownerDAO.getAll(EntityGraphUtils::SimpleOwner));
    }

    @Test
    void testAddOwnerDAO() {
        assertTrue(new OwnerDAO().add(new Owner()));
    }

    @Test
    void testUpdateOwnerDAO() {
        assertTrue(new OwnerDAO().update(new Owner()));
    }

    @Test
    void testDeleteOwnerDAO() {
        assertTrue(new OwnerDAO().delete(new Owner()));
    }
}
