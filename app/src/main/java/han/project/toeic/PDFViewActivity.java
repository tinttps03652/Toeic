package han.project.toeic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import static java.lang.String.format;

//import com.joanzapata.pdfview.PDFView;
//import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener {
    PDFView pdf;
    private Toolbar toolbar;
    public static final String FILE[] = {"unit1_present_tenses.pdf", "unit2_past_tenses.pdf", "unit3_future_tenses.pdf",
            "unit4_v_ing.pdf", "unit5_to_infinitive.pdf", "unit6_bare_infinitive_gerund_or_infinitive.pdf", "unit7_modal_verbs.pdf",
            "unit8_sentences_elements.pdf", "unit9_passive_voice1.pdf", "unit10_passive_voice2.pdf", "unit11_comparisons.pdf",
            "unit12_conditionals.pdf", "unit13_wishes.pdf", "unit14_relative_clauses.pdf", "unit15_exclamatory_sentences.pdf",
            "unit16_reported_speech.pdf", "unit17_types_of_questions.pdf", "unit18_subjunctive_structures.pdf", "unit19_subject_verb_agreement.pdf",
            "unit20_inversion_of_verbs.pdf", "unit21_emphasis.pdf", "unit22_relationships_between_ideas.pdf", "unit23_relationships_between_ideas2.pdf",
            "unit24_relationships_between_ideas3.pdf"
    };
    String pdfName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pdf = (PDFView) findViewById(R.id.pdfview);
        int position = getIntent().getIntExtra("position", -1);
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
        pdfName = FILE[position];
        display(pdfName, true);

    }

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;
        setTitle(pdfName = assetFileName);
        pdf.fromAsset(assetFileName)
                .swipeVertical(true)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .load();
    }

    Integer pageNumber = 1;

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(format("%s %s / %s", pdfName, page, pageCount));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
