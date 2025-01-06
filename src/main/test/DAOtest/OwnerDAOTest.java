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

        //Sample owner example to test  (information from database)
        Owner ownerSample = new Owner();
        ownerSample.setId(21);
        ownerSample.setName("Ella Carter");
        ownerSample.setContact("9081234567");
        LocalDate ownerSampleDOB = LocalDate.of(1991, 5, 17);
        ownerSample.setDateOfBirth(ownerSampleDOB);
        ownerSample.setUsername("ella");
        ownerSample.setPassword("11022005");


        //Test get owner
        Owner ownerTest = ownerDAO.get(21, EntityGraphUtils::SimpleOwner);
        assertEquals(ownerSample.getId(), ownerTest.getId());
        assertEquals(ownerSample.getName(), ownerTest.getName());
        assertEquals(ownerSample.getContact(), ownerTest.getContact());
        assertEquals(ownerSample.getDateOfBirth(), ownerTest.getDateOfBirth());
        assertEquals(ownerSample.getUsername(), ownerTest.getUsername());
        assertEquals(ownerSample.getPassword(), ownerTest.getPassword());
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
