package com.inviks.www.inviks1;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.inviks.Helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class BaseActivityClass extends ActionBarActivity
{
    LinkedHashMap<String,List<String>> mainDrawerMap;
    List<String> headerList;
    ExpandableListView view;
    CustomExpandableListAdapter adapter;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    // drawer menu items list, protected so that we can access it in subclass
    protected ExpandableListView drawerList;
    // after login, options will be-My Orders,Edit Profile,Sign Out

    //Static variable for selected item position. Which can be used in child activity to know which item is selected from the list.
    protected static int position;
    private DrawerLayout drawerLayout;
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
        //setupExpandableListView();
        setupListView();
        setupDrawers();
    }
    void setupListView()
    {
        mainDrawerMap = getInfo();
        headerList=new ArrayList<String>(mainDrawerMap.keySet());
        drawerLayout = (DrawerLayout) fullLayout.findViewById(R.id.drawer_layout);
        drawerList = (ExpandableListView) fullLayout.findViewById(R.id.expandableListViewDrawer);
        adapter = new CustomExpandableListAdapter(this, mainDrawerMap, headerList);
        drawerList.setAdapter(adapter);
        drawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                // The 'Me' group has 'My Profile','My Orders' and 'Sign Out'
                if(groupPosition==0 && childPosition==2)
                {
                    // user clicked on sign out button
                    Toast.makeText(getBaseContext(), "You have been signed out", Toast.LENGTH_SHORT).show();
                    mainDrawerMap.put("Login", null);
                    mainDrawerMap.remove("Me");
                    headerList = new ArrayList<String>(mainDrawerMap.keySet());
                    adapter = new CustomExpandableListAdapter(getBaseContext(), mainDrawerMap, headerList);
                    drawerList.setAdapter(adapter);
                    signout();
                }
                return false;
            }
        });
        drawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                //drawerList.setItemChecked(groupPosition, true);
                //setTitle(listArray[position]);
                //drawerLayout.closeDrawer(drawerList);

                // check for first item, it can be login or Me>Edit profile,view order,sign out
                String groupName=headerList.get(groupPosition);
                groupName=groupName.toLowerCase();
                switch(groupName)// means its login
                {
                    case "login":
                        startActivityForResult(new Intent(getBaseContext(), LoginMain.class), 1);
                        break;
                    case "me":
                        return false;
                    case "home"://home
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        finish();
                        break;
                    case "change location"://change location
                        FragmentManager manager=getFragmentManager();
                        ChangeLocationDialog myDialog=new ChangeLocationDialog();
                        myDialog.show(manager, "Change location");
                        //startActivity(new Intent(this, Item3Activity.class));
                        break;
                    case "share"://share
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/html");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.shareSubject));
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.shareBody));
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        //startActivity(new Intent(this, Item4Activity.class));
                        break;
                    case "rate us"://rate us
                        // need to test this
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try
                        {
                            startActivity(goToMarket);
                        }
                        catch (ActivityNotFoundException e)
                        {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;
                    case "feedback"://feedback
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("mailto:"));
                        String []to={getString(R.string.inviksMailId)};
                        intent.putExtra(Intent.EXTRA_EMAIL,to);
                        intent.setType("message/rfc822");
                        Intent chooser=Intent.createChooser(intent, "Send email");
                        startActivity(chooser);
                        break;
                    case "about"://about
                        Uri aboutUrl = Uri.parse(getString(R.string.aboutUrl));
                        Intent aboutIntent = new Intent(Intent.ACTION_VIEW, aboutUrl);
                        startActivity(aboutIntent);
                        break;
                    case "help"://help
                        startActivity(new Intent(getBaseContext(), HelpActivity.class));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
    void signout()
    {
        Helper.removeSharedPref(this,getString(R.string.loggedInUser_sharedPref_string));
        Helper.removeSharedPref(this,getString(R.string.isLoggedIn_sharedPref_string));
    }
    void setupDrawers()
    {
        // enable ActionBar app icon to behave as action to toggle nav drawer
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,						/* host Activity */
                drawerLayout, 				/* DrawerLayout object */
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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
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
            Intent intent=new Intent(this,CartView.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public LinkedHashMap<String,List<String>> getInfo()
    {
        LinkedHashMap<String,List<String>> details=new LinkedHashMap<String,List<String>>();

        List<String> login=new ArrayList<String>();
        String[] listArray = { "Home", "Check pincode", "Share", "Rate us","Feedback","About","Help" };
        login.add("Edit Profile");
        login.add("My Orders");
        login.add("Sign Out");
        details.put("Me", login);
        for(int i=0;i<listArray.length;i++)
        {
            details.put(listArray[i],null);
        }

        return details;
    }

/*    // Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    /*// We can override onBackPressed method to toggle navigation drawer
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(drawerList)){
            drawerLayout.closeDrawer(drawerList);
        }
        else {
            drawerLayout.openDrawer(drawerList);
        }
        finish();
    }*/
}
