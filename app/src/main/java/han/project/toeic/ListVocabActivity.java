package han.project.toeic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListVocabActivity extends AppCompatActivity {
    ListView lv;
    WordAdapter adapter;
    List<WordModel> list;
    WordModel word;
    String vocabulary[] = {"contracts.xml", "marketing.xml", "warranties.xml", "business_planning.xml", "conferences.xml",
            "computers_internet.xml", "office_technology.xml", "office_procedures.xml", "electronics.xml", "correspondence.xml"


    };
    String pos;
    AudioPlayer au;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vocab);
        lv = (ListView) findViewById(R.id.listView2);
        list = new ArrayList<>();
        int index = (int) getIntent().getIntExtra("index", -1);
        String title = (String) getIntent().getStringExtra("title");
        try {
            pos = vocabulary[index];
            generateData(pos);
        } catch (Exception e) {
            Toast.makeText(ListVocabActivity.this, "This Subject Have not done yet!", Toast.LENGTH_SHORT).show();
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word = list.get(position);
                String audiofile = word.getAudio();
                au = new AudioPlayer(audiofile, ListVocabActivity.this);

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!

        super.onDestroy();
    }

    public void generateData(String str) {
        try {
            list = Parser.parseWords(this.getAssets().open(str));
            adapter = new WordAdapter(this, list);
            lv.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();
        }
    }


}
