package han.project.toeic;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        TextView tabIntro = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabIntro.setText("Vocabulary");
        tabIntro.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_tab_white_24dp,0,0);
        tabLayout.getTabAt(0).setCustomView(tabIntro);

        TextView tabVocab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabVocab.setText("Video");
        tabVocab.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_video_library_white_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabVocab);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
