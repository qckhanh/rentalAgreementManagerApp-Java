package DAOtest;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rmit.model.Property.CommercialProperty;

import org.rmit.Helper.*;
import org.rmit.database.*;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CommercialPropertyDAOTest {
    public CommercialPropertyDAOTest() throws InterruptedException {
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
    void testAddCommercialPropertyDAO() {
        assertTrue(new CommercialPropertyDAO().add(new CommercialProperty()));
    }

    @Test
    void testUpdateCommercialPropertyDAO() {
        assertTrue(new CommercialPropertyDAO().update(new CommercialProperty()));
    }

    @Test
    void testDeleteCommercialPropertyDAO() {
        assertTrue(new CommercialPropertyDAO().delete(new CommercialProperty()));
    }

    @Test
    void testGetCommercialPropertyDAO() {
        //id taken directly from the database
        assertNotNull(new CommercialPropertyDAO().get(12, EntityGraphUtils::SimpleCommercialPropertyFull));
    }

    @Test
    void testGetAllCommercialPropertyDAO() {
        assertNotNull(new CommercialPropertyDAO().getAll(EntityGraphUtils::SimpleCommercialPropertyFull));
    }
}
