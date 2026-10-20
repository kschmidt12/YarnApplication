package persistence;

import entity.User;
import entity.Yarn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenericDaoTest {

    GenericDao<Yarn> yarnDao;
    GenericDao<User> userDao;


    @BeforeEach
    void setUp() {

        Database database = Database.getInstance();
        database.runSQL("clean.sql");
        yarnDao = new GenericDao<>(Yarn.class);
        userDao = new GenericDao<>(User.class);
    }


    @Test
    void getAllUsers() {
        List<User> users = userDao.getAll();
        assertEquals(2, users.size());
    }

    @Test
    void getAllYarn() {
        List<Yarn> yarns = yarnDao.getAll();
        assertEquals(2, yarns.size());
    }

    @Test
    void getByIdUser() {
        User retrievedUser = userDao.getById(1);
        assertEquals("Katie Schmidt", retrievedUser.getName());
    }

    @Test
    void getByIdYarn() {
        Yarn retrievedYarn = yarnDao.getById(1);
        assertEquals("Lions Brand", retrievedYarn.getBrandName());
    }

    @Test
    void deleteYarn() {
        Yarn yarn = new Yarn();
        Yarn retrievedYarn = yarnDao.getById(1);

        yarnDao.delete(retrievedYarn);
        Yarn deletedYarn = yarnDao.getById(yarn.getId());
        assertNull(deletedYarn);

        assertNotNull(userDao.getById(1));
    }

    @Test
    void deleteUser() {
        User user = new User();

        User retrievedUser = userDao.getById(1);
        Yarn retrievedYarn = yarnDao.getById(2);

        assertEquals(retrievedUser.getId(), retrievedYarn.getUser().getId());

        userDao.delete(retrievedUser);
        User deletedUser = userDao.getById(user.getId());
        assertNull(deletedUser);

        Yarn yarnDeleted = yarnDao.getById(2);
        assertNull(yarnDeleted);
    }

    @Test
    void insertUser() {
        User user = new User();
        user.setName("Harper Schmidt");
        user.setEmail("harper@gmail.com");
        user.setUserName("HarperSchmidt");
        user.setPassword("ILoveChickenNuggets");
        userDao.insert(user);

        User newUser = userDao.getById(user.getId());

        assertEquals("Harper Schmidt", newUser.getName());
    }

    @Test
    void insertYarn() {
        User user = userDao.getById(1);
        Yarn yarn = new Yarn();
        yarn.setBrandName("I Love This Yarn");
        yarn.setColor("pink");
        yarn.setYarnAmount("4");
        yarn.setYarnSize("3");
        yarn.setHookSize("5 to 6");
        yarn.setBoughtFrom("Hobby Lobby");
        yarn.setUser(user);
        yarnDao.insert(yarn);

        Yarn newYarn = yarnDao.getById(yarn.getId());

        assertEquals("I Love This Yarn", newYarn.getBrandName());

        assertEquals(user.getId(), newYarn.getUser().getId());
    }

    @Test
    void updateUser() {
        User retrievedUser = userDao.getById(1);
        retrievedUser.setUserName("changedUsername");
        userDao.update(retrievedUser);
        User changedUser = userDao.getById(1);
        assertEquals("changedUsername", changedUser.getUserName());
    }

    @Test
    void updateYarn() {
        Yarn retrievedYarn = yarnDao.getById(1);
        retrievedYarn.setColor("green");
        yarnDao.update(retrievedYarn);
        Yarn changedYarn = yarnDao.getById(1);
        assertEquals("green", changedYarn.getColor());
    }

    @Test
    void findByPropertyEqualUser() {
        List<User> user = userDao.findByPropertyEqual("name", "Katie Schmidt");
        assertEquals(1, user.size());
        assertEquals(1, user.get(0).getId());
    }

    @Test
    void findByPropertEqualYarn() {
        List<Yarn> yarn = yarnDao.findByPropertyEqual("color", "purple");
        assertEquals(1, yarn.size());
        assertEquals(1, yarn.get(0).getId());
    }

    @Test
    void testFindByPropertyEqualUser() {
        List<User> user = userDao.findByPropertyEqual("name", "Katie Schmidt");
        assertEquals(1, user.size());
        assertEquals(1, user.get(0).getId());
    }

    @Test
    void testFindByPropertyEqualYarn() {
        List<Yarn> yarn = yarnDao.findByPropertyEqual("color", "purple");
        assertEquals(1, yarn.size());
        assertEquals(1, yarn.get(0).getId());
    }
}