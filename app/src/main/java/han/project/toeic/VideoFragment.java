package han.project.toeic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Han on 18/10/2015.
 */
public class VideoFragment extends Fragment {
    ListView lv;
    int images[] = {R.mipmap.toeic_trick,R.mipmap.vocabulary_600,R.mipmap.listening_600,R.mipmap.reading_toeic,
            R.mipmap.practice_600,R.mipmap.toeic_economy,  R.mipmap.toeic_grammar,R.mipmap.toeic_fulltest,
            R.mipmap.toeic_fulltest, R.mipmap.toeic_analyst,R.mipmap.longman_toeic,R.mipmap.toeic_economy_everyday,
            R.mipmap.toeic_vtv2,R.mipmap.stater_toeic};
    String title[] = null;
    Adapter adapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        lv = (ListView)view.findViewById(R.id.list_youtube);
        adapter = new Adapter();
        lv.setAdapter(adapter);
        title = getActivity().getResources().getStringArray(R.array.video_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),YoutubeVideoList.class);
                int pos = position;
                i.putExtra("position",pos);
                startActivity(i);
            }
        });
        return view;
    }
    public class Adapter extends BaseAdapter{

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
        public class youtube_item{
            ImageView img;
            TextView title;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater  = getActivity().getLayoutInflater();
            youtube_item i;
            if(view == null){
                i = new youtube_item();
                view = inflater.inflate(R.layout.youtube_item,null);
                i.img = (ImageView)view.findViewById(R.id.img_youtube);
                i.title = (TextView)view.findViewById(R.id.tv_title);
                view.setTag(i);
            }else{
                i = (youtube_item)view.getTag();
            }
            i.img.setImageResource(images[position]);
            i.title.setText(title[position]+"");
            return view;
        }
    }
}
