package han.project.toeic;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
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
        String title = (String)getIntent().getStringExtra("title");
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
            list = LessonsParser.parseWords(this.getAssets().open(str));
            adapter = new WordAdapter(this, list);
            lv.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();
        }
    }

    public class AudioPlayer {

        String fileName;
        Context contex;
        MediaPlayer mp;

        //Constructor
        public AudioPlayer(String name, Context context) {
            fileName = name;
            contex = context;
            playAudio();
        }

        //Play Audio
        public void playAudio() {
            mp = new MediaPlayer();
            try {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = new MediaPlayer();
                }
                AssetFileDescriptor descriptor = contex.getAssets()
                        .openFd("audios/" + fileName);
                mp.setDataSource(descriptor.getFileDescriptor(),
                        descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mp.prepare();
                mp.setVolume(3f, 3f);

                mp.start();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Stop Audio
        public void stop() {
            mp.stop();
        }

    }



}
