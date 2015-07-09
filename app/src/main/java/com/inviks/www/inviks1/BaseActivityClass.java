package com.inviks.www.inviks1;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class BaseActivityClass extends ActionBarActivity
{

    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    // drawer menu items list, protected so that we can access it in subclass
    protected ListView mDrawerList;
    protected String[] listArray = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
    //Static variable for selected item position. Which can be used in child activity to know which item is selected from the list.
    protected static int position;
    private DrawerLayout mDrawerLayout;
   //Drawer listner class for drawer open, close etc.
    private ActionBarDrawerToggle actionBarDrawerToggle;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        //actionBar.show();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/

    }
    protected DrawerLayout fullLayout;
    protected FrameLayout frameLayout;
    public void setContentView(int layoutResID)
    {
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_left_swipe, null);
        frameLayout = (FrameLayout) fullLayout.findViewById(R.id.container);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(fullLayout);
        setup();
    }
    void setup() {

        mDrawerLayout = (DrawerLayout) fullLayout.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) fullLayout.findViewById(R.id.left_drawer);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listArray));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                openActivity(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,						/* host Activity */
                mDrawerLayout, 				/* DrawerLayout object */
                R.drawable.ic_action_threebar,     /* nav drawer image to replace 'Up' caret */
                R.string.open_drawer,       /* "open drawer" description for accessibility */
                R.string.close_drawer)      /* "close drawer" description for accessibility */
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                //getSupportActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        /*if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        else if(item.getItemId()== R.id.action_cart)
        {
            /*SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String isUserLoggedIn = sharedPreferences.getString("isLoggedIn", "");
            if (null != isUserLoggedIn && isUserLoggedIn.toLowerCase().equals("yes"))
            {
                // update cart for that user
                //new UpdateCart().execute(thisMedicine.getMedicineId());
            }
            else
            {
                String cartItems = sharedPreferences.getString("cartMedicineIds", "");
                String cartQtys = sharedPreferences.getString("cartMedicineQtys", "");
                Intent intent=new Intent(this,CartView.class);
                startActivity(intent);
                //Toast.makeText(this,cartItems+" "+cartQtys , Toast.LENGTH_LONG).show();
            }*/
            Intent intent=new Intent(this,CartView.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @param position
     *
     * Launching activity when any list item is clicked.
     */
    protected void openActivity(int position) {

		mDrawerList.setItemChecked(position, true);
		//setTitle(listArray[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivityClass.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                //startActivity(new Intent(this, Item1Activity.class));
                break;
            case 1:
                //startActivity(new Intent(this, Item2Activity.class));
                break;
            case 2:
                //startActivity(new Intent(this, Item3Activity.class));
                break;
            case 3:
                //startActivity(new Intent(this, Item4Activity.class));
                break;
            case 4:
                //startActivity(new Intent(this, Item5Activity.class));
                break;

            default:
                break;
        }

        Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
    }

/*    // Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    /*// We can override onBackPressed method to toggle navigation drawer
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
        finish();
    }*/
}
