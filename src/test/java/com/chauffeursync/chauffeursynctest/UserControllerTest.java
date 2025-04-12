package com.chauffeursync.chauffeursynctest;

import com.chauffeursync.controllers.UserController;
import com.chauffeursync.database.DatabaseManager;
import com.chauffeursync.models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    private UserController userController;

    @BeforeAll
    public void initTestDatabase() {
        // Zet testmodus aan zodat we een test-DB gebruiken (H2 in-memory)
        DatabaseManager.setTestMode(true);
        DatabaseManager.initializeIfNeeded();

        // Init database via bestaand SQL-initscript
        try (Connection conn = DatabaseManager.getConnection()) {

        } catch (Exception e) {
            fail("Kon testdatabase niet initialiseren: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void testRegister_Success() {
        boolean registered = userController.register("Test Gebruiker", "test1@example.com", "secure123");
        assertTrue(registered, "Registratie moet succesvol zijn");

        User user = User.findUser("test1@example.com");
        assertNotNull(user);
        assertEquals("test1@example.com", user.getEmail());
    }

    @Test
    public void testLogin_Success() {
        userController.register("Login Test", "test2@example.com", "mypassword");

        boolean loggedIn = userController.login("test2@example.com", "mypassword");
        assertTrue(loggedIn, "Login moet succesvol zijn");

        User user = userController.getCurrentUser();
        assertNotNull(user);
        assertEquals("test2@example.com", user.getEmail());
    }

    @Test
    public void testLogin_Failure() {
        boolean login = userController.login("nietbestaat@example.com", "random");
        assertFalse(login, "Login moet falen voor onbekende gebruiker");
    }

    @Test
    public void testDoubleRegistrationFails() {
        userController.register("Dubbel", "test3@example.com", "abc123");
        boolean tweedeKeer = userController.register("Dubbel", "test3@example.com", "abc123");
        assertFalse(tweedeKeer, "Dubbele registratie moet falen");
    }

    @Test
    public void testUpdateEmailAndPassword() {
        userController.register("Edit Test", "test4@example.com", "oldpass");
        User user = User.findUser("test4@example.com");
        assertNotNull(user);

        userController.updateEmailAndPassword(user.getId(), "nieuw4@example.com", "newpass123");

        User updated = User.findUser("nieuw4@example.com");
        assertNotNull(updated);
        assertEquals("nieuw4@example.com", updated.getEmail());
    }

    @Test
    public void testLogout() {
        userController.register("Logout", "test5@example.com", "loguit");
        userController.login("test5@example.com", "loguit");

        userController.logout();
        assertNull(userController.getCurrentUser(), "Gebruiker moet uitgelogd zijn");
    }

    @Test
    public void testUserStory_EmailEnWachtwoordBijwerken() {
        userController.register("Wijzig Test", "oude@example.com", "wachtwoord");
        User user = User.findUser("oude@example.com");
        assertNotNull(user);

        userController.updateEmailAndPassword(user.getId(), "nieuwe@example.com", "nieuwWW123");

        User updated = User.findUser("nieuwe@example.com");
        assertNotNull(updated);
        assertEquals("nieuwe@example.com", updated.getEmail());
    }


    @AfterEach
    public void cleanup() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM User WHERE email LIKE 'test%@example.com'");
        } catch (Exception e) {
            System.err.println("Fout bij opschonen testdata: " + e.getMessage());
        }
    }
}
