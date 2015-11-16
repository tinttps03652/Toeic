package han.project.toeic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Han on 16/11/2015.
 */
public class GrammarAdapter extends BaseAdapter{
    Activity ac;
    int images[];
    String title[];
    String meaning[];
    GrammarAdapter(Activity ac,int images[],String title[],String meaning[]){
        this.images = images;
        this.title = title;
        this.meaning = meaning;
        this.ac = ac;

    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class grammar_item{
        ImageView img;
        TextView title,meaning;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = ac.getLayoutInflater();
        grammar_item i;
        if(view == null){
            i = new grammar_item();
            view = inflater.inflate(R.layout.grammar_item,null);
            i.img = (ImageView)view.findViewById(R.id.grammarimage);
            i.title = (TextView)view.findViewById(R.id.textView1);
            i.meaning = (TextView)view.findViewById(R.id.textView2);
            view.setTag(i);
        }else{
            i = (grammar_item)view.getTag();
        }
        i.img.setImageResource(images[position]);
        i.title.setText(title[position].toString());
        i.meaning.setText(meaning[position].toString());
        return view;
    }
}

