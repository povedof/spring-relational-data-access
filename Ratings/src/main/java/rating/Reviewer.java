package rating;

/**
 * Created by povedOf on 10/21/16.
 */
public class Reviewer {

    private int rID;
    private String name;

    public Reviewer(int rID, String name)
    {
        this.rID = rID;
        this.name = name;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return String.format("Reviewer[rID=%d, name='%s']", rID, name);
    }
}
