package han.project.toeic;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
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

import han.project.util.Query;
import han.project.mode.WordModel;
import han.project.util.AudioPlayer;
import han.project.util.Parser;

public class PracticeActivity extends AppCompatActivity {
    String vocabulary[] = {"xmlfile/contracts.xml", "xmlfile/marketing.xml", "xmlfile/warranties.xml", "xmlfile/business_planning.xml", "xmlfile/conferences.xml",
            "xmlfile/computers_internet.xml", "xmlfile/office_technology.xml", "xmlfile/office_procedures.xml", "xmlfile/electronics.xml", "xmlfile/correspondence.xml",
            "xmlfile/job_ad_recruitment.xml","xmlfile/apply_interviewing.xml","xmlfile/hiring_traning.xml","xmlfile/salaries_benefits.xml","xmlfile/promotions_pensions_awards.xml",
            "xmlfile/shopping.xml", "xmlfile/ordering_supplies.xml","xmlfile/shipping.xml","xmlfile/invoices.xml","xmlfile/inventory.xml"
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
        setContentView(R.layout.activity_practice);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        final Query q = new Query(this);
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
            Toast.makeText(PracticeActivity.this, "This Topic Have not done yet!", Toast.LENGTH_SHORT).show();
        }
        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String audiofile = word.getAudio();
                    au = new AudioPlayer(audiofile, PracticeActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        check();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Word = word.getWord().toString();
                String An = answer.getText().toString();
                if ((next.getText() + "").trim().equals("Check")) {
                    if (An.equalsIgnoreCase(Word)) {
                        q.Longtoast("Correct!");
                        count++;
                    } else {
                        q.Longtoast("Answer: "+Word);

                    }
                    score.setText("Score: " + count + "");
                    next.setText("Continue");
                } else {
                    i++;

                    pages.setText((i + 1) + "/" + list.size());
                    showQuestion();
                    next.setText("Check");
                }
            }
        });

    }

    private void showQuestion() {
        answer.setText("");
        next.setEnabled(false);
        check();
        if (i <= list.size() - 1) {
            word = list.get(i);
            String uri_icon = "mipmap/" + word.getImage();
            int ImageResource = this.getResources().getIdentifier(uri_icon, null, this.getPackageName());
            Drawable image = this.getResources().getDrawable(ImageResource);
            img.setImageDrawable(image);
            String audiofile = word.getAudio();
            au = new AudioPlayer(audiofile, PracticeActivity.this);
            meaning.setText(word.getMeaning() + "");
            pages.setText((i + 1) + "/" + list.size());
            answer.setText("");
        } else {
            i = 0;
            Intent intent = new Intent(PracticeActivity.this, ResultActivity.class);
            intent.putExtra("score", score.getText().toString());
            startActivity(intent);
        }
        score.setText("Score: " + count + "");

    }

    private void check() {

        answer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (TextUtils.isEmpty(answer.getText() + "")) {

                    next.setEnabled(false);

                } else {
                    next.setEnabled(true);
                }
                return false;
            }
        });

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(word.getWord().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        i = 0;
       showQuestion();
    }

    public void generateData(String str) {
        try {
            list = Parser.parseWords(this.getAssets().open(str));
        } catch (Exception e) {
            Toast.makeText(this, "Error " + e, Toast.LENGTH_SHORT).show();
        }
    }
}
