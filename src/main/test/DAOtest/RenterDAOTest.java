package DAOtest;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rmit.Helper.*;
import org.rmit.database.*;
import org.rmit.model.Persons.Renter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RenterDAOTest {
    public RenterDAOTest() throws InterruptedException {
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

    //Renter DAO
    @Test
    void testGetRenterDAO() {
        RenterDAO renterDAO = new RenterDAO();

        assertNotNull(renterDAO.get(5, EntityGraphUtils::SimpleRenter));
    }

    @Test
    void testGetAllRenterDAO() {
        RenterDAO renterDAO = new RenterDAO();
        assertNotNull(renterDAO.getAll(EntityGraphUtils::SimpleRenter));
    }

    @Test
    void testAddRenterDAO() {
        assertTrue(new RenterDAO().add(new Renter()));
    }


    @Test
    void testUpdateRenterDAO() {
        assertTrue(new RenterDAO().update(new Renter()));
    }

    @Test
    void testDeleteRenterDAO() {
        assertTrue(new RenterDAO().delete(new Renter()));
    }
}
