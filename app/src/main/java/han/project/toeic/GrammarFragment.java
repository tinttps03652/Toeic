package han.project.toeic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Han on 18/10/2015.
 */
public class GrammarFragment extends Fragment {
    int images[] = {R.mipmap.present_tenses, R.mipmap.past_tenses, R.mipmap.future_tenses,
            R.mipmap.v_ing, R.mipmap.to_infinitive, R.mipmap.bare_infinitive_gerund_infinitive,
            R.mipmap.modal_verbs, R.mipmap.setence_elements, R.mipmap.passive_voice, R.mipmap.passive_voice2,
            R.mipmap.comparisons, R.mipmap.conditionals, R.mipmap.wishes, R.mipmap.relative_clauses,
            R.mipmap.exclamatory_sentences, R.mipmap.reported_speech, R.mipmap.types_of_questions,
            R.mipmap.subjunctive_structures, R.mipmap.subject_verb_agreement, R.mipmap.inversion_of_verbs,
            R.mipmap.emphasis, R.mipmap.relationships_between_ideas, R.mipmap.relationships_between_ideas2,
            R.mipmap.relationships_between_ideas3};
    String title[] = {};
    String meaning[] = {};
    GrammarRVAdapter rvAdapter;
    RecyclerView rv;
    LinearLayoutManager llm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grammar_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv1);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());
        title = getActivity().getResources().getStringArray(R.array.grammar_lessons);
        meaning = getActivity().getResources().getStringArray(R.array.grammar_meaning);
        rvAdapter = new GrammarRVAdapter(images, title, meaning);
        rv.setAdapter(rvAdapter);
        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), PDFViewActivity.class);
                i.putExtra("position", position);
                i.putExtra("title", title[position].toString());
                startActivity(i);
            }
        });
        return view;
    }

}


