package persistence;

import entity.TavilyResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TavilyDaoTest {

    @Test
    void searchProjects() throws Exception {

        TavilyDao tavilyDao = new TavilyDao();

        TavilyResponse response = tavilyDao.searchProjects("4");

        assertNotNull(response.getResults());
    }
}