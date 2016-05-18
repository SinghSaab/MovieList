package self.synacorcodingchallenge;

import java.util.Comparator;

/**
 * Created by Administrator on 2016-05-17.
 */
public class NewComparator implements Comparator<MovieParam> {
    private String choice;

    NewComparator(int id) {
        if (id == R.id.action_sortTitle) {
            choice = "Title";
        } else {
            choice = "Year";
        }

    }


    @Override
    public int compare(MovieParam lhs, MovieParam rhs) {
        if(choice == "Title")
            return lhs.getTitle().compareTo(rhs.getTitle());
        else
            return rhs.getYear().compareTo(lhs.getYear());
    }



}
