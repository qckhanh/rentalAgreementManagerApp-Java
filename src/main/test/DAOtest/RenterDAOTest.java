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

        //Sample renter example to test  (information from database)
        Renter renterSample = new Renter();
        renterSample.setId(1);
        renterSample.setContact("0948865333");
        renterSample.setName("Khanh Hii");
        LocalDate ownerSampleDOB = LocalDate.of(1990, 5, 14);
        renterSample.setDateOfBirth(ownerSampleDOB);
        renterSample.setUsername("renter");
        renterSample.setPassword("11022005");

        //Test get owner
        Renter renterTest = renterDAO.get(1, EntityGraphUtils::SimpleRenter);
        assertEquals(renterSample, renterTest);
        assertEquals(renterSample.getId(), renterTest.getId());
        assertEquals(renterSample.getName(), renterTest.getName());
        assertEquals(renterSample.getContact(), renterTest.getContact());
        assertEquals(renterSample.getDateOfBirth(), renterTest.getDateOfBirth());
        assertEquals(renterSample.getUsername(), renterTest.getUsername());
        assertEquals(renterSample.getPassword(), renterTest.getPassword());
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
