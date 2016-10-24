package rating;

/**
 * Created by povedOf on 10/21/16.
 */
public class Movie {

    private int mID,year;
    private String title, director;

    public Movie(int mID, int year, String title, String director)
    {
        this.mID = mID;
        this.year = year;
        this.title = title;
        this.director = director;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString()
    {
        return String.format("Movie[mID=%d, year=%d, title='%s', director='%s']", mID, year, title, director);
    }

}
