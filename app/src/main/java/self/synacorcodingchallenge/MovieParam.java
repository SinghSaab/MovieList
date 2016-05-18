package self.synacorcodingchallenge;

/**
 * Created by Administrator on 2016-05-17.
 */
public class MovieParam {

    private String title, year;

    public MovieParam() {
    }

    public MovieParam(String name, String year) {
        this.title = name;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
