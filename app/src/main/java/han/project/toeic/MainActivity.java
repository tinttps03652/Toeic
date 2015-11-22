package han.project.toeic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new VocabFragment(), "Vocabulary");
        adapter.addFragment(new VideoFragment(), "Video");
        adapter.addFragment(new GrammarFragment(),"Grammar");
        adapter.addFragment(new PracticeFragment(), "Practice");
        viewPager.setAdapter(adapter);
    }
    private  void setupTabIcons(){
        TextView tabVocab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabVocab.setText("Vocab");
        tabVocab.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_tab_white_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabVocab);

        TextView tabVideo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabVideo.setText("Video");
        tabVideo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_video_library_white_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabVideo);

        TextView tabGrammar = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabGrammar.setText("Grammar");
        tabGrammar.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_library_books_white_24dp,0,0);
        tabLayout.getTabAt(2).setCustomView(tabGrammar);

        TextView tabPractice = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabPractice.setText("Practice");
        tabPractice.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_assignment_white_24dp,0,0);
        tabLayout.getTabAt(3).setCustomView(tabPractice);

    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Hello User");
        alert.setMessage("Developers: Group 3 \n" +
                "Email: hanthienduc.96@gmail.com \n" +
                "Phone: 0943 414 425 \n" +
                "We want to make user have a good exprience so if you have any problem please contact with us through email above");
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_info:
                showDialog();
                break;
            case R.id.action_share:
                shareIt();
                break;
            case R.id.action_feedback:
                feedback();
                break;
            case R.id.action_login:
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private void shareIt(){
        try {
            Intent shareingIntent = new Intent(Intent.ACTION_SEND);
            shareingIntent.setType("text/plain");
            shareingIntent.putExtra(Intent.EXTRA_SUBJECT, "Toeic");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=hohieu.wordtoeic \n\n";
            shareingIntent.putExtra(Intent.EXTRA_TEXT,sAux);
            startActivity(Intent.createChooser(shareingIntent,"Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void feedback(){
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
        intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
        intent.setData(Uri.parse("mailto:default@hanthienduc.96@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }
}
