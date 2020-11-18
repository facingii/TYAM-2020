package mx.uv.fiee.iinf.tyam.swinfoapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.LinkedList;

import mx.uv.fiee.iinf.tyam.swinfoapp.fragments.PeopleFragment;
import mx.uv.fiee.iinf.tyam.swinfoapp.fragments.PlanetsFragment;
import mx.uv.fiee.iinf.tyam.swinfoapp.fragments.VehicleFragment;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private int prevTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        if (getSupportActionBar () != null) {
            getSupportActionBar ().setTitle (Utils.TITLE);
        }

        ViewPager2 viewPager = findViewById (R.id.myViewPager);

        MyViewPagerAdapter mvpa = new MyViewPagerAdapter (getSupportFragmentManager (), getLifecycle ());
        mvpa.addFragment (new PlanetsFragment ());
        mvpa.addFragment (new PeopleFragment  ());
        mvpa.addFragment (new VehicleFragment ());

        viewPager.setAdapter (mvpa);

       tabLayout = findViewById (R.id.tabLayout);
       tabLayout.addOnTabSelectedListener (new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected (TabLayout.Tab tab) {
               prevTab = tab.getPosition ();
           }

           @Override
           public void onTabUnselected (TabLayout.Tab tab) {
           }

           @Override
           public void onTabReselected (TabLayout.Tab tab) {
           }
       });

       new TabLayoutMediator (tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText (Utils.TAB_PLANETS);
            }

            if (position == 1) {
                tab.setText (Utils.TAB_PEOPLE);
            }

            if (position == 2) {
                tab.setText (Utils.TAB_VEHICLES);
            }

            viewPager.setCurrentItem (tab.getPosition (), true);
        }).attach ();

    }

    @Override
    public void onBackPressed () {
        if (prevTab == 0) {
            super.onBackPressed ();
        } else {
            tabLayout.selectTab (tabLayout.getTabAt (prevTab - 1));
        }
    }
}

class MyViewPagerAdapter extends FragmentStateAdapter {
    private final LinkedList<Fragment> fragments = new LinkedList<> ();

    public MyViewPagerAdapter (@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super (fragmentManager, lifecycle);
    }

    public void addFragment (Fragment item) {
        fragments.add (item);
    }

    @NonNull
    @Override
    public Fragment createFragment (int position) {
        return fragments.get (position);
    }

    @Override
    public int getItemCount () {
        return fragments.size ();
    }
}