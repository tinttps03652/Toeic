package han.project.toeic;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Review_Activity extends AppCompatActivity {
    String vocabulary[] = {"contracts.xml", "marketing.xml", "warranties.xml", "business_planning.xml", "conferences.xml",
            "computers_internet.xml", "office_technology.xml", "office_procedures.xml", "electronics.xml", "correspondence.xml"


    };
    ListView lv;
    WordAdapter adapter;
    List<WordModel> list;
    WordModel word;
    int pos;
    int index;
    AudioPlayer au;
    TextView score, pages, titlePractice;
    Button next;
    EditText answer;
    ImageView img;
    ImageButton playAudio;
    String title;
    int i = 0;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        score = (TextView) findViewById(R.id.tvScore);
        pages = (TextView) findViewById(R.id.page);
        titlePractice = (TextView) findViewById(R.id.textView5);
        next = (Button) findViewById(R.id.btnNext);
        img = (ImageView) findViewById(R.id.imageView4);
        playAudio = (ImageButton) findViewById(R.id.imageButton);
        answer = (EditText) findViewById(R.id.editText);
        index = getIntent().getIntExtra("index", -1);
        title = getIntent().getStringExtra("title");
        list = new ArrayList<>();
        try {
            String pos = vocabulary[index];
            generateData(pos);
            showQuestion();
        } catch (Exception e) {
            Toast.makeText(Review_Activity.this, "This Subject Have not done yet!", Toast.LENGTH_SHORT).show();
        }

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String audiofile = word.getAudio();
                au = new AudioPlayer(audiofile, Review_Activity.this);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Word = word.getWord().toString();
                String An = answer.getText().toString();
                if (An.equalsIgnoreCase(Word)) {
                    count++;
                    i++;
                    pages.setText((i + 1) + "/" + 12);
                    if (i == 12) {
                        Toast.makeText(Review_Activity.this, "Congratulations You have finished the lesson", Toast.LENGTH_SHORT).show();
                        i = 0;
                    }
                } else {
                    Toast.makeText(Review_Activity.this, "Try Again !", Toast.LENGTH_SHORT).show();
                }

                showQuestion();
            }
        });

    }

    private void showQuestion() {
        word = list.get(i);
        titlePractice.setText("Practice " + title);
        String uri_icon = "mipmap/" + word.getImage();
        int ImageResource = this.getResources().getIdentifier(uri_icon, null, this.getPackageName());
        Drawable image = this.getResources().getDrawable(ImageResource);
        img.setImageDrawable(image);
        String audiofile = word.getAudio();
        au = new AudioPlayer(audiofile, Review_Activity.this);
        score.setText("Score: " + count + "");
        pages.setText((i + 1) + "/" + 12);
        answer.setText("");
    }

    public void generateData(String str) {
        try {
            list = LessonsParser.parseWords(this.getAssets().open(str));
        } catch (Exception e) {
            Toast.makeText(this, "Error "+e, Toast.LENGTH_SHORT).show();
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
