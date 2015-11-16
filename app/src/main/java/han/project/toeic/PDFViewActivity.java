package han.project.toeic;

import android.support.v7.app.AppCompatActivity;

import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener {
   /* PDFView pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        pdf = (PDFView)findViewById(R.id.pdfview);
        int position = getIntent().getIntExtra("position",-1);
        pdfName = FILE[position];
        display(pdfName,true);

    }

    public static final String FILE[] = {"present_tenses.pdf","grammar2.pdf"};
    String pdfName ;
    Integer pageNumber = 1;

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;
        setTitle(pdfName = assetFileName);
        pdf.fromAsset(assetFileName)
                .swipeVertical(true)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .load();
    }
*/
    @Override
    public void onPageChanged(int page, int pageCount) {
       // pageNumber = page;
        //setTitle(format("%s %s / %s", pdfName, page, pageCount));

    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

   /* private boolean displaying(String fileName) {
        return fileName.equals(pdfName);
    }*/
}
