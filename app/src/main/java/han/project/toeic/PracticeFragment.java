package han.project.toeic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Han on 18/10/2015.
 */
public class PracticeFragment extends Fragment implements View.OnClickListener{
    ListView lv;
    ArrayAdapter<String> adapter;
    String practice[] ={};
    Button start;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.practice_fragment,container,false);
        lv = (ListView)view.findViewById(R.id.listView4);
        start = (Button)view.findViewById(R.id.button2);
        practice = getActivity().getResources().getStringArray(R.array.lessons_practice);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_multiple_choice,practice);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);
        start.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }

        String[] outputStrArr = new String[selectedItems.size()];

        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
        }

        //Intent intent = new Intent(getActivity(),
              //  ResultActivity.class);

        // Create a bundle object
        Bundle b = new Bundle();
        b.putStringArray("selectedItems", outputStrArr);

        // Add the bundle to the intent.
       // intent.putExtras(b);

        // start the ResultActivity
       // startActivity(intent);


    }
}
