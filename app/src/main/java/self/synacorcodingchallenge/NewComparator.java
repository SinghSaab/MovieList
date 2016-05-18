package self.synacorcodingchallenge;

import java.util.Comparator;

/**
 * Created by Administrator on 2016-05-17.
 */
public class NewComparator implements Comparator<MovieParam> {
    private String sortChoice;

//    Depending upon the menu id; sorting method to be changed
    NewComparator(int id) {
        if (id == R.id.action_sortTitle) {
            sortChoice = "Title";
        } else {
            sortChoice = "Year";
        }
    }

    @Override
    public int compare(MovieParam lhs, MovieParam rhs) {
        if(sortChoice.equals("Title"))
//            Ascending Title sort
            return lhs.getTitle().compareTo(rhs.getTitle());
        else
//        Descending Date sort
            return rhs.getYear().compareTo(lhs.getYear());
    }



}
