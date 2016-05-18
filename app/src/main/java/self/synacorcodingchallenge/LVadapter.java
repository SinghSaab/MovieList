package self.synacorcodingchallenge;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016-05-17.
 */
public class LVadapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<MovieParam> movieItems;

    public LVadapter(Activity activity, List<MovieParam> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.lvadapter, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        MovieParam m = movieItems.get(position);
        // title
        title.setText(m.getTitle());
        // release year
        year.setText(m.getYear());

        return convertView;
    }
}
