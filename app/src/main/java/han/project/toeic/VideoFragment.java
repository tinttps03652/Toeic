package han.project.toeic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Han on 18/10/2015.
 */
public class VideoFragment extends Fragment {
    int images[] = {R.mipmap.toeic_trick, R.mipmap.vocabulary_600, R.mipmap.listening_600, R.mipmap.reading_toeic,
            R.mipmap.practice_600, R.mipmap.toeic_economy, R.mipmap.toeic_grammar, R.mipmap.toeic_fulltest,
            R.mipmap.toeic_fulltest, R.mipmap.toeic_analyst, R.mipmap.longman_toeic, R.mipmap.toeic_economy_everyday,
            R.mipmap.toeic_vtv2, R.mipmap.stater_toeic};
    String title[] = null;
    String meaning[] = null;
    MyAdapter Adapter;
    RecyclerView rv;
    LinearLayoutManager llm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv2);
        title = getActivity().getResources().getStringArray(R.array.video_list);
        meaning = getActivity().getResources().getStringArray(R.array.video_list_meaning);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        Adapter = new MyAdapter();
        rv.setAdapter(Adapter);
        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), YoutubeVideoList.class);
                int pos = position;
                i.putExtra("position", pos);
                startActivity(i);
            }
        });
        return view;

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VideoViewHolder> {
        LayoutInflater inflater;
        View view;

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.cardview_video_layout, parent, false);
            return new VideoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            holder.img.setImageResource(images[position]);
            holder.title.setText(title[position]);
            holder.meaning.setText(meaning[position]);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }

        public final class VideoViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            ImageView img;
            TextView title,meaning;

            public VideoViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv2);
                img = (ImageView) itemView.findViewById(R.id.img_youtube);
                title = (TextView) itemView.findViewById(R.id.tv_title);
                meaning = (TextView) itemView.findViewById(R.id.tv_meaning);
            }
        }
    }


}
