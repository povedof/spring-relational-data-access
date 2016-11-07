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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final static String separator = "---------------------------";

    @Override
    public void run(String... strings) throws Exception {

        log.info(separator);

        // creates Movie, Rating and Reviewer tables from resource file
        createTables();

        log.info(separator);

        //Exercise 1
        //1.) Find the titles of all movies directed by Steven Spielberg.
        exerciseOne();

        //2.) Find all years that have a movie that received a rating of 4 or 5, and sort them in increasing order.
        exerciseTwo();

        //3.) Find the titles of all movies that have no ratings.
        exerciseThree();


        //4.) Some reviewers didn't provide a date with their rating. Find the names of all reviewers who have ratings with a NULL value for the date.
        exerciseFour();


        //5.) Write a query to return the ratings data in a more readable format: reviewer name, movie title, stars, and ratingDate. Also, sort the data, first by reviewer name, then by movie title, and lastly by number of stars.
        exerciseFive();


        //6.) For all cases where the same reviewer rated the same movie twice and gave it a higher rating the second time, return the reviewer's name and the title of the movie.
        exerciseSix();


        //7.) For each movie that has at least one rating, find the highest number of stars that movie received. Return the movie title and number of stars. Sort by movie title.
        exerciseSeven();


        //8.)For each movie, return the title and the 'rating spread', that is, the difference between highest and lowest ratings given to that movie. Sort by rating spread from highest to lowest, then by movie title.
        exerciseEight();


        //9.)Find the difference between the average rating of movies released before 1980 and the average rating of movies released after 1980.
        exerciseNine();


        //10.) Add the reviewer Roger Ebert to your database, with an rID of 209.
        exerciseTen();


        //11.) Insert 5-star ratings by James Cameron for all movies in the database. Leave the review date as NULL.
        exerciseEleven();


        //12.) For all movies that have an average rating of 4 stars or higher, add 25 to the release year. (Update the existing tuples; don't insert new tuples.)
        exerciseTwelve();

    }

    /* Exercise 1
       1.) Find the titles of all movies directed by Steven Spielberg.
     */
    private void exerciseOne() {
        //select title from Movie where director='Steven Spielberg’;
        log.info("Exercise 1:");
        jdbcTemplate.query(
                "SELECT mID, title, year, director FROM Movie WHERE director = ?", new Object[]{"Steven Spielberg"},
                (rs, rowNum) -> new Movie(rs.getInt("mID"), rs.getInt("year"), rs.getString("title"), rs.getString("director"))
        ).forEach(movie -> log.info(movie.getTitle().toString()));
        log.info(separator);
    }

    /* Exercise 2
       2.) Find all years that have a movie that received a rating of 4 or 5, and sort them in increasing order.
     */
    private void exerciseTwo() {
        //select year from Movie where mID in (select mID from Rating where stars=4 or stars=5) order by year;
        log.info("Exercise 2:");
        List<Integer> mIDs = jdbcTemplate.queryForList("SELECT DISTINCT mID FROM Rating WHERE stars = 4 OR stars = 5 ORDER BY mID", Integer.class);
        //log.info("Cantidad: " + mIDs.size());
        //log.info("mID's: " + mIDs);
        String sql = "SELECT year FROM Movie WHERE mID IN (:mIDs) ORDER BY year";
        Map<String, List> paramMap = Collections.singletonMap("mIDs", mIDs);
        List<Integer> list = namedParameterJdbcTemplate.queryForList(sql, paramMap, Integer.class);
        log.info("years: " + list);
        log.info(separator);
    }

    /* Exercise 3
       3.) Find the titles of all movies that have no ratings.
     */
    private void exerciseThree() {
        //select title from Movie where mID not in (select mID from Rating);
        log.info("Exercise 3:");
        List<Integer> mIDs = jdbcTemplate.queryForList("SELECT mID FROM Rating ORDER BY mID", Integer.class);
        //log.info("Cantidad: " + mIDs.size());
        //log.info("mID's: " + mIDs);
        String sql = "SELECT title FROM Movie WHERE mID not IN (:mIDs) ORDER BY year";
        Map<String, List> paramMap = Collections.singletonMap("mIDs", mIDs);
        List<String> list = namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);
        log.info("movies: " + list);
        log.info(separator);
    }

    /* Exercise 4
      4.) Some reviewers didn't provide a date with their rating. Find the names of all reviewers who have ratings with a NULL value for the date.
     */
    private void exerciseFour() {
        //select name from Reviewer where rID in (select rID from Rating where ratingDate is null) order by name;
        log.info("Exercise 4:");
        List<Integer> rIDs = jdbcTemplate.queryForList("SELECT rID FROM Rating where ratingDate is null ORDER BY mID", Integer.class);
        //log.info("Cantidad: " + rIDs.size());
        //log.info("mID's: " + rIDs);
        String sql = "SELECT name FROM Reviewer WHERE rID IN (:rIDs) ORDER BY name";
        Map<String, List> paramMap = Collections.singletonMap("rIDs", rIDs);
        List<String> list = namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);
        log.info("names: " + list);
        log.info(separator);
    }

    /* Exercise 5
      5.) Write a query to return the ratings data in a more readable format: reviewer name, movie title, stars, and ratingDate. Also, sort the data, first by reviewer name, then by movie title, and lastly by number of stars.
     */
    private void exerciseFive() {
        //select re.name,m.title,r.stars,r.ratingDate from Rating r inner join Reviewer re on r.rID = re.rID inner join Movie m on r.mID = m.mID order by name,title,stars;
        log.info("Exercise 5:");
        String sql = "SELECT re.name,m.title,r.stars,r.ratingDate FROM Rating r INNER JOIN Reviewer re ON r.rID = re.rID INNER JOIN Movie m ON r.mID = m.mID ORDER BY name,title,stars";
        jdbcTemplate.query(sql,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        log.info(rs.getString("name") + " " + rs.getString("title") + " " + rs.getInt("stars") + " " + rs.getDate("ratingDate"));
                        return null;
                    }
                });
        log.info(separator);
    }

    /* Exercise 6
      6.) For all cases where the same reviewer rated the same movie twice and gave it a higher rating the second time, return the reviewer's name and the title of the movie.
     */
    private void exerciseSix() {
        //select name, title from Reviewer, Movie, Rating, Rating r2 where Rating.mID=Movie.mID and Reviewer.rID=Rating.rID and Rating.rID = r2.rID and r2.mID = Movie.mID
        //and Rating.stars < r2.stars and Rating.ratingDate < r2.ratingDate;
        log.info("Exercise 6:");
        String sql = "SELECT name, title from Reviewer, Movie, Rating, Rating r2 WHERE Rating.mID=Movie.mID AND Reviewer.rID=Rating.rID AND Rating.rID = r2.rID AND r2.mID = Movie.mID AND Rating.stars < r2.stars AND Rating.ratingDate < r2.ratingDate";
        jdbcTemplate.query(sql,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        log.info(rs.getString("name") + " " + rs.getString("title"));
                        return null;
                    }
                });
        log.info(separator);
    }

    /* Exercise 7
     7.) For each movie that has at least one rating, find the highest number of stars that movie received. Return the movie title and number of stars. Sort by movie title.
     */
    private void exerciseSeven() {
        //SELECT title,MAX(stars) FROM Movie,Rating WHERE Movie.mID=Rating.mID GROUP BY Movie.title;
        log.info("Exercise 7:");
        String sql = "SELECT title,MAX(stars) AS maxStars FROM Movie, Rating WHERE Movie.mID=Rating.mID GROUP BY Movie.title ORDER BY Movie.title";
        jdbcTemplate.query(sql,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        log.info(rs.getString("title") + " " + rs.getString("maxStars"));
                        return null;
                    }
                });
        log.info(separator);
    }

    /* Exercise 8
       8.)For each movie, return the title and the 'rating spread', that is, the difference between highest and lowest ratings given to that movie. Sort by rating spread from highest to lowest, then by movie title.
     */
    private void exerciseEight() {
        //SELECT title,MAX(stars)-MIN(stars) as spread FROM Movie,Rating WHERE Movie.mID=Rating.mID GROUP BY Movie.title ORDER BY spread DESC;
        log.info("Exercise 8:");
        String sql = "SELECT title,MAX(stars)-MIN(stars) AS spread FROM Movie,Rating WHERE Movie.mID=Rating.mID GROUP BY Movie.title ORDER BY spread DESC, Movie.title";
        jdbcTemplate.query(sql,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        log.info(rs.getString("title") + " " + rs.getString("spread"));
                        return null;
                    }
                });
        log.info(separator);
    }

    /* Exercise 9
       9.)Find the difference between the average rating of movies released before 1980 and the average rating of movies released after 1980.
     */
    private void exerciseNine() {
        //SELECT down.down1980 - up.up1980 AS resultFROM (SELECT AVG(avgst) AS down1980 FROM (SELECT title, AVG(stars) AS avgst FROM Movie, Rating WHERE Movie.mID=Rating.mID AND year < 1980 GROUP BY Movie.title) AS d) as down,
        //(SELECT AVG(avgst2) AS up1980 FROM (SELECT title, AVG(stars) AS avgst2 FROM Movie, Rating WHERE Movie.mID=Rating.mID AND year > 1980 GROUP BY Movie.title) AS u) as up;
        log.info("Exercise 9:");
        String sql = "SELECT down.down1980 - up.up1980 AS result FROM (SELECT AVG(avgst) AS down1980 FROM (SELECT title, AVG(stars) AS avgst FROM Movie, Rating WHERE Movie.mID=Rating.mID AND year < 1980 GROUP BY Movie.title) AS d) as down, (SELECT AVG(avgst2) AS up1980 FROM (SELECT title, AVG(stars) AS avgst2 FROM Movie, Rating WHERE Movie.mID=Rating.mID AND year > 1980 GROUP BY Movie.title) AS u) as up";
        jdbcTemplate.query(sql,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        log.info(rs.getString("result"));
                        return null;
                    }
                });
        log.info(separator);
    }

    /* Exercise 10
       10.) Add the reviewer Roger Ebert to your database, with an rID of 209.
     */
    private void exerciseTen() {
        //insert into Reviewer values(209, ‘Roger Ebert');
        log.info("Exercise 10:");
        this.jdbcTemplate.update("insert into Reviewer (rID, name) values (?, ?)", "209", "Roger Ebert");
        selectAllReviewer();
        log.info(separator);
    }

    /* Exercise 11
       11.) Insert 5-star ratings by James Cameron for all movies in the database. Leave the review date as NULL.
     */
    private void exerciseEleven() {
        //INSERT INTO Rating SELECT r.rID,m.mID,5,null FROM Reviewer r,Movie m where rID=207
        log.info("Exercise 11:");
        this.jdbcTemplate.update("INSERT INTO Rating SELECT r.rID,m.mID,5,null FROM Reviewer r,Movie m where rID=207");
        selectAllRating();
        log.info(separator);
    }

    /* Exercise 12
       12.) For all movies that have an average rating of 4 stars or higher, add 25 to the release year. (Update the existing tuples; don't insert new tuples.)
      */
    private void exerciseTwelve() {
        //UPDATE Movie SET Movie.year = Movie.year + 25  WHERE mID in  (SELECT mID FROM    (SELECT Movie.mID, AVG(stars) AS avgst FROM Rating, Movie where Movie.mID = Rating.mID GROUP BY Movie.mID) AS s1  WHERE avgst >= 4);
        log.info("Exercise 12:");
        this.jdbcTemplate.update("UPDATE Movie SET Movie.year = Movie.year + 25 WHERE mID in (SELECT mID FROM (SELECT Movie.mID, AVG(stars) AS avgst FROM Rating, Movie where Movie.mID = Rating.mID GROUP BY Movie.mID) AS s1 WHERE avgst >= 4)");
        selectAllMovie();
        log.info(separator);
    }

    // creates Movie, Rating and Reviewer tables from resource file
    private void createTables() throws SQLException {
        log.info("Creating tables");
        Resource filePath = resourceLoader.getResource("classpath:rating.sql");
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),filePath);
    }

    // prints all reviewers
    private void selectAllReviewer()
    {
        log.info("REVIEWERS");
        jdbcTemplate.query(
                "SELECT rID, name FROM Reviewer",
                (rs, rowNum) -> new Reviewer(rs.getInt("rID"), rs.getString("name"))
        ).forEach(reviewer -> log.info(reviewer.toString()));
    }

    // prints all movies
    private void selectAllMovie()
    {
        log.info("MOVIES");
        jdbcTemplate.query(
                "SELECT mID, title, year, director FROM Movie",
                (rs, rowNum) -> new Movie(rs.getInt("mID"), rs.getInt("year"), rs.getString("title"),rs.getString("director"))
        ).forEach(movie -> log.info(movie.toString()));
    }

    //prints all ratings
    private void selectAllRating()
    {
        log.info("RATING");
        jdbcTemplate.query(
                "SELECT rID, mID, stars, ratingDate FROM Rating",
                (rs, rowNum) -> new Rating(rs.getInt("rID"), rs.getInt("mID"), rs.getInt("stars"), rs.getDate("ratingDate"))
        ).forEach(rating -> log.info(rating.toString()));
    }
}
