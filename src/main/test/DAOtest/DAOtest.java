package DAOtest;

import jakarta.persistence.EntityGraph;
import org.junit.jupiter.api.Test;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.Helper.*;
import org.rmit.database.*;
import org.rmit.model.Persons.Renter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DAOtest {

    //Owner DAO
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


    //Host DAO
    @Test
    void testGetHostDAO() {
        HostDAO hostDAO = new HostDAO();

        //Sample host example to test  (information from database)
        Host hostSample = new Host();
        hostSample.setId(99);
        hostSample.setContact("1111");
        hostSample.setName("11");
        LocalDate ownerSampleDOB = LocalDate.of(2025, 1, 1);
        hostSample.setDateOfBirth(ownerSampleDOB);
        hostSample.setUsername("1");
        hostSample.setPassword("1");

        //Test get owner
        Host hostTest = hostDAO.get(99, EntityGraphUtils::SimpleHost);
        assertEquals(hostSample, hostTest);
        assertEquals(hostSample.getId(), hostTest.getId());
        assertEquals(hostSample.getName(), hostTest.getName());
        assertEquals(hostSample.getContact(), hostTest.getContact());
        assertEquals(hostSample.getDateOfBirth(), hostTest.getDateOfBirth());
        assertEquals(hostSample.getUsername(), hostTest.getUsername());
        assertEquals(hostSample.getPassword(), hostTest.getPassword());
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
}
