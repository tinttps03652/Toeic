package han.project.toeic;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Han on 11/11/2015.
 */
public class WordAdapter extends BaseAdapter{
    List<WordModel> list;
    Activity ac;

    public WordAdapter(Activity ac,List<WordModel> list){
        this.ac = ac;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class word_item{
        ImageView img;
        TextView word,meaning,vietnamese;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = ac.getLayoutInflater();
        final word_item item;
        if(view == null){
            item = new word_item();
            view = inflater.inflate(R.layout.word_item,null);
            item.img = (ImageView)view.findViewById(R.id.imageView);
            item.word = (TextView)view.findViewById(R.id.textView);
            item.meaning = (TextView)view.findViewById(R.id.textView2);
            item.vietnamese = (TextView)view.findViewById(R.id.textView3);
            view.setTag(item);
        }else {
            item = (word_item)view.getTag();
        }
        WordModel word  = list.get(position);
        item.word.setText(word.getWord());
        item.meaning.setText(word.getMeaning());
        item.vietnamese.setText(word.getVietnamese());
        String uri_icon = "mipmap/"+word.getImage();
        int ImageResource = view.getContext().getResources().getIdentifier(uri_icon, null, view.getContext().getApplicationContext().getPackageName());
        Drawable image = view.getContext().getResources().getDrawable(ImageResource);
        item.img.setImageDrawable(image);
        return view;
    }
}
