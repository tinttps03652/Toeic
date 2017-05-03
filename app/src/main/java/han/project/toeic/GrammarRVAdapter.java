package han.project.toeic;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Han on 30/11/2015.
 */
public class GrammarRVAdapter extends RecyclerView.Adapter<GrammarRVAdapter.GrammarViewHolder>{
    public int images[];
    public String title[];
    public String meaning[];
    View v;
    LayoutInflater inflater;
    public GrammarRVAdapter(int images[],String title[],String meaning[]){
        this.images = images;
        this.title = title;
        this.meaning = meaning;
    }
    @Override
    public GrammarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.cardview_grammar_layout,parent,false);
        return new GrammarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GrammarViewHolder holder, int position) {
        holder.image.setImageResource(images[position]);
        holder.title.setText(title[position]);
        holder.meaning.setText(meaning[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static final class GrammarViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView image;
        TextView title;
        TextView meaning;
        public GrammarViewHolder(View itemView) {
            super(itemView);
            cv  = (CardView)itemView.findViewById(R.id.cv1);
            image = (ImageView)itemView.findViewById(R.id.grammarimage);
            title = (TextView)itemView.findViewById(R.id.tvTitle);
            meaning = (TextView)itemView.findViewById(R.id.tvMeaning);
        }
    }
}
