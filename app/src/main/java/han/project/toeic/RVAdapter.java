package han.project.toeic;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import han.project.mode.Representative;

/**
 * Created by Han on 05/11/2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ReprentativeViewHolder> {
    List<Representative> list;
    LayoutInflater inflater;
    View v;

    RVAdapter(List<Representative> list) {
        this.list = list;

    }

    @Override
    public ReprentativeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.cardview_lesson_layout, parent, false);
        return new ReprentativeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReprentativeViewHolder holder, int position) {
        Representative re = list.get(position);
        holder.title.setText(re.getTitle());
        holder.meaning.setText(re.getMeaning());
        String uri_icon = "mipmap/" + re.getImage();
        int ImageResource = v.getContext().getResources().getIdentifier(uri_icon, null, v.getContext().getApplicationContext().getPackageName());
        Drawable image = v.getContext().getResources().getDrawable(ImageResource);
        holder.img.setImageDrawable(image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public final static class ReprentativeViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView meaning;
        ImageView img;

        ReprentativeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.textView1);
            meaning = (TextView) itemView.findViewById(R.id.textView2);
            img = (ImageView) itemView.findViewById(R.id.imageView1);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

