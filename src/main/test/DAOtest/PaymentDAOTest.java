package DAOtest;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.rmit.Helper.DatabaseUtil;
import org.junit.jupiter.api.Test;
import org.rmit.model.Agreement.Payment;
import org.rmit.database.*;
import org.rmit.Helper.*;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentDAOTest {

    public PaymentDAOTest() throws InterruptedException {
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
    void testDeletePaymentDAO() {
        assertTrue(new PaymentDAO().delete(new Payment()));
    }

    @Test
    void testGetPaymentDAO() {
        assertNotNull(new PaymentDAO().get(2, EntityGraphUtils::SimplePaymentFull));
    }

}
