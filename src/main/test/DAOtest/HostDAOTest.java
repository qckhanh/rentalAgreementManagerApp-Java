package DAOtest;


import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rmit.model.Persons.Host;

import org.rmit.Helper.*;
import org.rmit.database.*;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class HostDAOTest {

    public HostDAOTest() throws InterruptedException {
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
    void testGetHostDAO() {
        HostDAO hostDAO = new HostDAO();
        //Sample host example to test  (information from database)
        assertNotNull(hostDAO.get(2, EntityGraphUtils::SimpleHost));
    }

    @Test
    void testGetAllHostDAO() {
        HostDAO hostDAO = new HostDAO();
        assertNotNull(hostDAO.getAll(EntityGraphUtils::SimpleHost));
    }

    @Test
    void testAddHostDAO() {
        assertTrue(new HostDAO().add(new Host()));
    }

    @Test
    void testUpdateHostDAO() {
        assertTrue(new HostDAO().update(new Host()));
    }

    @Test
    void testDeleteHostDAO() {
        assertTrue(new HostDAO().delete(new Host()));
    }

}
