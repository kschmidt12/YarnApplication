package persistence;

import entity.User;
import entity.Yarn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    YarnDao yarnDao;
    UserDao userDao;


    @BeforeEach
    void setUp() {

        Database database = Database.getInstance();
        database.runSQL("clean.sql");
        yarnDao = new YarnDao();
        userDao = new UserDao();
    }

    @Test
    void getById() {
        User retrievedUser = userDao.getById(1);
        assertEquals("Katie Schmidt", retrievedUser.getName());
    }

    @Test
    void update() {
        User retrievedUser = userDao.getById(1);
        retrievedUser.setUserName("changedUsername");
        userDao.update(retrievedUser);
        User changedUser = userDao.getById(1);
        assertEquals("changedUsername", changedUser.getUserName());
    }

    @Test
    void insert() {

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
    void delete() {

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
    void getAll() {
        List<User> users = userDao.getAll();
        assertEquals(2, users.size());

    }

    @Test
    void getByPropertyEqual() {
        List<User> user = userDao.getByPropertyEqual("name", "Katie Schmidt");
        assertEquals(1, user.size());
        assertEquals(1, user.get(0).getId());
    }

    @Test
    void getByPropertyLike() {
        List<User> user = userDao.getByPropertyLike("name", "K");
        assertEquals(1, user.size());
        assertEquals(1, user.get(0).getId());
    }


}