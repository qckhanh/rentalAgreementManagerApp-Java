package DAOtest;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.database.RentalAgreementDAO;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.Helper.*;

import static org.junit.jupiter.api.Assertions.*;

public class RentalAgreementDAOTest {

    public RentalAgreementDAOTest() throws InterruptedException {
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
    void testDeleteRentalAgreementDAO() {
        assertTrue(new RentalAgreementDAO().delete(new RentalAgreement()));
    }

    @Test
    void testGetRentalAgreementDAO() {
        assertTrue(new RentalAgreementDAO().get(1002, EntityGraphUtils::SimpleRentalAgreementFull) != null);
    }

    @Test
    void testGetAllRentalAgreementDAO() {
        assertNotNull(new RentalAgreementDAO().getAll(EntityGraphUtils::SimpleRentalAgreementFull));
    }




}
