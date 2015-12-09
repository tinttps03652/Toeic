package han.project.toeic;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import han.project.mode.WordModel;
import han.project.util.AudioPlayer;
import han.project.util.Parser;

public class ListVocabActivity extends AppCompatActivity {
    ListView lv;
    WordAdapter adapter;
    List<WordModel> list;
    WordModel word;
    String vocabulary[] = {"xmlfile/contracts.xml", "xmlfile/marketing.xml", "xmlfile/warranties.xml", "xmlfile/business_planning.xml", "xmlfile/conferences.xml",
            "xmlfile/computers_internet.xml", "xmlfile/office_technology.xml", "xmlfile/office_procedures.xml", "xmlfile/electronics.xml", "xmlfile/correspondence.xml",
            "xmlfile/jobads_recruitment.xml","xmlfile/apply_interviewing.xml","xmlfile/hiring_traning.xml","xmlfile/salaries_benefits.xml","xmlfile/promotions_pensions_awards.xml",
            "xmlfile/shopping.xml", "xmlfile/ordering_supplies.xml","xmlfile/shipping.xml","xmlfile/invoices.xml","xmlfile/inventory.xml"
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
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
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
