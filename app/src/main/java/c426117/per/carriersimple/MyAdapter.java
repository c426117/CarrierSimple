package c426117.per.carriersimple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by C426117 on 2016/4/23/023.
 */
public class MyAdapter extends ArrayAdapter<ListViewItem>
{

    private int recid ;
    public MyAdapter (Context context, int textViewResourceId, List<ListViewItem> items)
    {
        super(context,textViewResourceId,items);
        recid = textViewResourceId;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        ListViewItem item = getItem(position);
        View view = null;
        String str = item.getlujing();

        ViewHodler viewHodler = null;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(recid,null);
            viewHodler = new ViewHodler();
            viewHodler.textView = (TextView)view.findViewById(R.id.filepath);
            viewHodler.imageView = (ImageView)view.findViewById(R.id.dorfimage);
            view.setTag(viewHodler);
        }
        else
        {
            view = convertView;
            viewHodler = (ViewHodler)view.getTag();
        }
        viewHodler.textView.setText(str);
        if(str.charAt(str.length()-1)=='#')
        {
            viewHodler.imageView.setImageResource(R.drawable.ic_d);
        }
        else
        {
            viewHodler.imageView.setImageResource(R.drawable.ic_file_grey);
        }
        return view;
    }

    class ViewHodler
    {
        ImageView imageView ;
        TextView textView;
    }
}
