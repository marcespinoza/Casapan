package com.casapan.pedidos.Vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.casapan.pedidos.Adapter.TabAdapter;
import com.casapan.pedidos.R;
import com.casapan.pedidos.Util.Constants;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        adapter.addFragment(new FragmentPedidos(), "Pedidos");
        adapter.addFragment(new FragmentArticulo(), "Articulos");
        adapter.addFragment(new FragmentConfig(), "Configuración");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_pedido);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_pan);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.blue_grey_500),PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        checkSucursal();
    }

    private void checkSucursal() {
        String sucursal = Constants.getSPreferences(this).getNombreSucursal();
        if(sucursal.equals("")){
            tabLayout.getTabAt(2).select();
            Toast.makeText(this, "Ingrese nombre de sucursal", Toast.LENGTH_LONG).show();
        }else{
            tabLayout.getTabAt(0).select();
        }
    }

}
