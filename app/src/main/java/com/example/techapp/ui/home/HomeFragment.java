package com.example.techapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.techapp.Adapters.SectionPagerAdapter;
import com.example.techapp.AddproductActivity;
import com.example.techapp.R;
import com.example.techapp.ui.home.drones.DroneFragment;
import com.example.techapp.ui.home.laptops.LaptopFragment;
import com.example.techapp.ui.home.phones.PhoneFragment;
import com.example.techapp.ui.home.wearables.WearableFragment;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment{

    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;
    Button addproduct;


    public HomeFragment() {

    }

    public static HomeFragment getInstance()    {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = myFragment.findViewById(R.id.view_pager);
        tabLayout = myFragment.findViewById(R.id.tab_layout);
        addproduct=myFragment.findViewById(R.id.addproduct);

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(myFragment.getContext(), AddproductActivity.class);
                startActivity(i);
            }
        });

        return myFragment;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewpager) {
        SectionPagerAdapter adapter=new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new WearableFragment(),"Wearable");
        adapter.addFragment(new LaptopFragment(),"Laptops");
        adapter.addFragment(new PhoneFragment(),"Phones");
        adapter.addFragment(new DroneFragment(),"Drones");

        viewPager.setAdapter(adapter);

    }

}