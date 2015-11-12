package han.project.toeic;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListVocabActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    ListView lv;
    WordAdapter adapter;
    List<WordModel> list;
    private TextToSpeech tts;
    WordModel word;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vocab);
        tts = new TextToSpeech(this, this);
        lv = (ListView)findViewById(R.id.listView2);
        list = new ArrayList<WordModel>();
        int index = (int)getIntent().getIntExtra("index",-1);
        switch (index){
            case 0:
                generateContracts();
                break;
            case 1:
                generateMarketing();
                break;
            case 2:
                generateWarranties();
                break;
            case 3:
                generateBusiness_Planning();
                break;
            case 4:
                generateConferences();
                break;
            case 5:
                generateComputers_internet();
                break;
            case 6:
                generateOfficeTech();
                break;
            case 7:
                generateOfficeProcedures();
                break;
            case 8:
                generateElectronics();
                break;
            case 9:
                generateCorrespondence();
                break;

        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word = list.get(position);
                tts.speak(word.getWord().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    public void generateContracts(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("contracts.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateMarketing(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("marketing.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateWarranties(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("warranties.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateBusiness_Planning(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("business_planning.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateConferences(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("conferences.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateOfficeTech(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("office_technology.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateCorrespondence(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("correspondence.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateComputers_internet(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("computers_internet.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateOfficeProcedures(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("office_procedures.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public void generateElectronics(){
        try {
            list = LessonsParser.parseWords(this.getAssets().open("electronics.xml"));
            adapter = new WordAdapter(this,list);
            lv.setAdapter(adapter);
        }catch(Exception e){
            Toast.makeText(this,"Error ",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

}
