package DAOtest;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rmit.model.Property.ResidentialProperty;

import org.rmit.Helper.*;
import org.rmit.database.*;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ResidentialPropertyDAOTest {
    public ResidentialPropertyDAOTest() throws InterruptedException {
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
    void testAddResidentialPropertyDAO() {
        assertTrue(new ResidentialPropertyDAO().add(new ResidentialProperty()));
    }

    @Test
    void testUpdateResidentialPropertyDAO() {
        assertTrue(new ResidentialPropertyDAO().update(new ResidentialProperty()));
    }

    @Test
    void testDeleteResidentialPropertyDAO() {
        assertTrue(new ResidentialPropertyDAO().delete(new ResidentialProperty()));
    }

    @Test
    void testGetResidentialPropertyDAO() {
        assertTrue(new ResidentialPropertyDAO().get(13, EntityGraphUtils::SimpleResidentialPropertyFull) != null);
    }

    @Test
    void testGetAllResidentialPropertyDAO() {
        assertNotNull(new ResidentialPropertyDAO().getAll(EntityGraphUtils::SimpleResidentialPropertyFull));
    }
}
