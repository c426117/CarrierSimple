package c426117.per.carriersimple;


/**
 * Created by C426117 on 2016/4/23/023.
 */
public class ListViewItem
{
    private int imageid = R.drawable.ic_d;
    private String lujing;

    ListViewItem(String lujing)
    {
        this.lujing = lujing;
        this.imageid = imageid;
    }

    public String getlujing ()
    {
        return lujing;
    }

    public int getImageid ()
    {
        return imageid;
    }
}
