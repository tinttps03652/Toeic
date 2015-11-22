package han.project.toeic;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView score, pages, meaning;
    Button next;
    EditText answer;
    ImageView img;
    ImageButton playAudio;
    String title;
    int i = 0;
    int count = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        score = (TextView) findViewById(R.id.tvScore);
        pages = (TextView) findViewById(R.id.page);
        next = (Button) findViewById(R.id.btnNext);
        img = (ImageView) findViewById(R.id.imageView4);
        meaning = (TextView) findViewById(R.id.meaning);
        playAudio = (ImageButton) findViewById(R.id.imageButton);
        answer = (EditText) findViewById(R.id.editText);
        index = getIntent().getIntExtra("index", -1);
        title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
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
                if (An.trim().equalsIgnoreCase("")) {
                    Toast.makeText(Review_Activity.this, "Enter Your Answer!", Toast.LENGTH_SHORT).show();
                } else {
                    if (An.equalsIgnoreCase(Word)) {
                        count++;
                        i++;
                        pages.setText((i + 1) + "/" + 12);
                        if (i == 12) {
                            next.setVisibility(View.GONE);
                            i = 0;
                            Intent i = new Intent(Review_Activity.this, ResultActivity.class);
                            i.putExtra("score", score.getText().toString());
                            startActivity(i);
                        }
                    } else {
                        i++;
                    }
                }

                showQuestion();
            }
        });

    }

    private void showQuestion() {
        word = list.get(i);
        String uri_icon = "mipmap/" + word.getImage();
        int ImageResource = this.getResources().getIdentifier(uri_icon, null, this.getPackageName());
        Drawable image = this.getResources().getDrawable(ImageResource);
        img.setImageDrawable(image);
        String audiofile = word.getAudio();
        au = new AudioPlayer(audiofile, Review_Activity.this);
        meaning.setText(word.getMeaning() + "");
        score.setText("Score: " + count + "");
        pages.setText((i + 1) + "/" + 12);
        answer.setText("");
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(word.getWord().toString());

    }

    public void generateData(String str) {
        try {
            list = Parser.parseWords(this.getAssets().open(str));
        } catch (Exception e) {
            Toast.makeText(this, "Error " + e, Toast.LENGTH_SHORT).show();
        }
    }


}
