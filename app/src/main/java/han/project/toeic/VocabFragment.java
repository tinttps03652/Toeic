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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import han.project.mode.Representative;
import han.project.util.Parser;

/**
 * Created by Han on 18/11/2015.
 */
public class VocabFragment extends Fragment {
    Representative re = null;
    List<Representative> list;
    AdView mAdView;
    EditText edt;
    RecyclerView rv;
    LinearLayoutManager llm;
    RVAdapter rvAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vocab_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        list = new ArrayList<>();
        try {
            list = Parser.parse(getActivity().getAssets().open("lessons.xml"));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "error: " + e, Toast.LENGTH_SHORT).show();
        }
        rvAdapter = new RVAdapter(list);
        rv.setAdapter(rvAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                int index = position;
                Intent i = new Intent(getActivity(), ListVocabActivity.class);
                Representative obj = list.get(position);
                String title = obj.getTitle();
                Bundle b = new Bundle();
                b.putInt("index", index);
                b.putString("title", title);
                i.putExtras(b);
                startActivity(i);
            }
        });

        mAdView = (AdView) view.findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("BA562590DEC40F4051302BC23C801F64")
                .build());

        return view;
    }


}
