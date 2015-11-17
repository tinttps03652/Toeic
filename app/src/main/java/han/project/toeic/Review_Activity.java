package han.project.toeic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Review_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Bundle b = getIntent().getExtras();
        String words[] = b.getStringArray("selectedItems");

    }
}
