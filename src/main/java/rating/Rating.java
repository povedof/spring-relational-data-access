package rating;

import java.util.Date;

/**
 * Created by povedOf on 10/21/16.
 */
public class Rating {

    private int rID, mID, stars;
    private Date ratingDate;

    public Rating(int rID, int mID, int stars, Date date)
    {
        this.rID = rID;
        this.mID = mID;
        this.stars = stars;
        this.ratingDate = date;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date date) {
        this.ratingDate = date;
    }

    public String toString()
    {
        if(ratingDate!=null)
            return String.format("Rating[rID=%d, movie='%d', stars='%d', date='%s']", rID, mID, stars, ratingDate.toString());
        else
            return String.format("Rating[rID=%d, movie='%d', stars='%d', date=NULL]", rID, mID, stars);
    }
}
