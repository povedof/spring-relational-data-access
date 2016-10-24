package rating;

/**
 * Created by povedOf on 10/21/16.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");

        //ELIMINAR TABLAS SI EXISTEN
        jdbcTemplate.execute("DROP TABLE Movie IF EXISTS");
        jdbcTemplate.execute("DROP TABLE Reviewer IF EXISTS");
        jdbcTemplate.execute("DROP TABLE Rating IF EXISTS");

        //CREAR TABLAS
        jdbcTemplate.execute("CREATE TABLE Movie(mID NUMBER(5), title VARCHAR(255), year NUMBER(5), director VARCHAR(255))");
        jdbcTemplate.execute("CREATE TABLE Reviewer(rID NUMBER(5), name VARCHAR(255))");
        jdbcTemplate.execute("CREATE TABLE Rating(rID NUMBER(5), mID NUMBER(5), stars NUMBER(5), ratingDate DATE)");

        //INSERCIONES MOVIES
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "101", "Gone with the Wind", "1939", "Victor Fleming");
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "102", "Star Wars", "1977", "George Lucas");
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "103", "The Sound of Music", "1965", "Robert Wise");
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "104", "E.T.", "1982", "Steven Spielberg");
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "105", "Titanic", "1997", "James Cameron");
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "106", "Snow White", "1937", null);
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "107", "Avatar", "2009", "James Cameron");
        this.jdbcTemplate.update("insert into Movie (mID, title, year, director) values (?, ?, ?, ?)", "108", "Raiders of the Lost Ark", "1981", "Steven Spielberg");

        //INSERCIONES REVIEWERS
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "201", "Sarah Martinez");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "202", "Daniel Lewis");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "203", "Brittany Harris");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "204", "Mike Anderson");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "205", "Chris Jackson");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "206", "Elizabeth Thomas");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "207", "James Cameron");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "208", "Ashley White");

        //INSERCIONES RATINGS
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "201", "101", "2", "2011-01-22");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "201", "101", "4", "2011-01-27");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "202", "106", "4", null);
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "203", "103", "2", "2011-01-20");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "203", "108", "4", "2011-01-12");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "203", "108", "2", "2011-01-30");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "204", "101", "3", "2011-01-09");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "205", "103", "3", "2011-01-27");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "205", "104", "2", "2011-01-22");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "205", "108", "4", null);
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "206", "107", "3", "2011-01-15");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "206", "106", "5", "2011-01-19");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "207", "107", "5", "2011-01-20");
        this.jdbcTemplate.update("insert into Rating (rID, mID, stars, ratingDate) values (?, ?, ?, ?)", "208", "104", "3", "2011-01-02");

        //QUERIES
        /*log.info("MOVIES");
        int rowCount = this.jdbcTemplate.queryForObject("select count(*) from Movie", Integer.class);
        log.info("Count Movies: " + rowCount);

        jdbcTemplate.query(
                "SELECT mID, title, year, director FROM Movie",
                (rs, rowNum) -> new Movie(rs.getInt("mID"), rs.getInt("year"), rs.getString("title"),rs.getString("director"))
        ).forEach(movie -> log.info(movie.toString()));

        log.info("REVIEWERS");
        jdbcTemplate.query(
                "SELECT rID, name FROM Reviewer",
                (rs, rowNum) -> new Reviewer(rs.getInt("rID"), rs.getString("name"))
        ).forEach(reviewer -> log.info(reviewer.toString()));

        log.info("RATINGS");
        jdbcTemplate.query(
                "SELECT rID, mID, stars, ratingDate FROM Rating",
                (rs, rowNum) -> new Rating(rs.getInt("rID"), rs.getInt("mID"), rs.getInt("stars"), rs.getDate("ratingDate"))
        ).forEach(rating -> log.info(rating.toString()));*/

        //1.) Find the titles of all movies directed by Steven Spielberg.
        //select title from Movie where director='Steven Spielberg’;
        log.info("Ejercicio 1:");
        jdbcTemplate.query(
                "SELECT mID, title, year, director FROM Movie WHERE director = ?", new Object[] { "Steven Spielberg" },
                (rs, rowNum) -> new Movie(rs.getInt("mID"), rs.getInt("year"), rs.getString("title"),rs.getString("director"))
        ).forEach(movie -> log.info(movie.getTitle().toString()));
        log.info("---------------------------");

        //2.) Find all years that have a movie that received a rating of 4 or 5, and sort them in increasing order.
        //select year from Movie where mID in (select mID from Rating where stars=4 or stars=5) order by year;
        log.info("Ejercicio 2:");
        List<Integer> mIDs_2 = jdbcTemplate.queryForList("SELECT DISTINCT mID FROM Rating WHERE stars = 4 OR stars = 5 ORDER BY mID",Integer.class);
        //log.info("Cantidad: " + mIDs.size());
        //log.info("mID's: " + mIDs);
        String sql_2 = "SELECT year FROM Movie WHERE mID IN (:mIDs) ORDER BY year";
        Map<String, List> paramMap_2 = Collections.singletonMap("mIDs", mIDs_2);
        List<Integer> list_2 = namedParameterJdbcTemplate.queryForList(sql_2, paramMap_2, Integer.class);
        log.info("years: " + list_2);
        log.info("---------------------------");

        //3.) Find the titles of all movies that have no ratings.
        //select title from Movie where mID not in (select mID from Rating);
        log.info("Ejercicio 3:");
        List<Integer> mIDs_3 = jdbcTemplate.queryForList("SELECT mID FROM Rating ORDER BY mID",Integer.class);
        //log.info("Cantidad: " + mIDs.size());
        //log.info("mID's: " + mIDs);
        String sql_3 = "SELECT title FROM Movie WHERE mID not IN (:mIDs) ORDER BY year";
        Map<String, List> paramMap_3 = Collections.singletonMap("mIDs", mIDs_3);
        List<String> list_3 = namedParameterJdbcTemplate.queryForList(sql_3, paramMap_3, String.class);
        log.info("movies: " + list_3);
        log.info("---------------------------");

        //4.) Some reviewers didn't provide a date with their rating. Find the names of all reviewers who have ratings with a NULL value for the date.
        //select name from Reviewer where rID in (select rID from Rating where ratingDate is null) order by name;
        log.info("Ejercicio 4:");
        List<Integer> rIDs_4 = jdbcTemplate.queryForList("SELECT rID FROM Rating where ratingDate is null ORDER BY mID",Integer.class);
        //log.info("Cantidad: " + rIDs.size());
        //log.info("mID's: " + rIDs);
        String sql_4 = "SELECT name FROM Reviewer WHERE rID IN (:rIDs) ORDER BY name";
        Map<String, List> paramMap_4 = Collections.singletonMap("rIDs", rIDs_4);
        List<String> list_4 = namedParameterJdbcTemplate.queryForList(sql_4, paramMap_4, String.class);
        log.info("names: " + list_4);
        log.info("---------------------------");

        //5.) Write a query to return the ratings data in a more readable format: reviewer name, movie title, stars, and ratingDate. Also, sort the data, first by reviewer name, then by movie title, and lastly by number of stars.
        //select re.name,m.title,r.stars,r.ratingDate from Rating r inner join Reviewer re on r.rID = re.rID inner join Movie m on r.mID = m.mID order by name,title,stars;
        log.info("Ejercicio 5:");
        String sql = "SELECT re.name,m.title,r.stars,r.ratingDate FROM Rating r INNER JOIN Reviewer re ON r.rID = re.rID INNER JOIN Movie m ON r.mID = m.mID ORDER BY name,title,stars";
        jdbcTemplate.query(sql,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        log.info(rs.getString("name") + " " + rs.getString("title") + " " + rs.getInt("stars") + " " + rs.getDate("ratingDate"));
                        return null;
                    }
                });}
}
