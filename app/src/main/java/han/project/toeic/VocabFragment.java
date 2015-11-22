package han.project.toeic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han on 18/10/2015.
 */
public class VocabFragment extends Fragment {
    ListView lv;
    Representative re = null;
    MyAdapter adapter;
    List<Representative> list;
    AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vocab_fragment, container, false);
        lv = (ListView) view.findViewById(R.id.listView);
        adapter = new MyAdapter(getActivity(), generateData());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("BA562590DEC40F4051302BC23C801F64")
                .build());
        return view;
    }

    private List<Representative> generateData() {
        list = new ArrayList<>();
        try {
            list = Parser.parse(getActivity().getAssets().open("lessons.xml"));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "error: " + e, Toast.LENGTH_SHORT).show();
        }
        return list;
    }


}
