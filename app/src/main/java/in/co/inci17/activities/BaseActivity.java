package in.co.inci17.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import in.co.inci17.R;
import in.co.inci17.auxiliary.CircularImageView;
import in.co.inci17.auxiliary.User;

public class BaseActivity extends AppCompatActivity {

    public ImageButton ibMenu;
    public FrameLayout frame;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public FrameLayout fragment_frame;
    public Fragment fragment = null;
    public Intent intent = null;
    public Runnable runnable;
    private ActionBarDrawerToggle drawerToggle;
    View headerView;

    private User user;

    //Flags
    boolean doubleBackToExitPressedOnce = false;
    public boolean at_home=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((user = User.getCurrentUser(this)) == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    protected void onCreateDrawer() {
        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        fragment_frame = (FrameLayout) findViewById(R.id.content_frame);

        //Menu Button
        ibMenu = (ImageButton) findViewById(R.id.ib_menu);

        //What you see on your screen
        frame = (FrameLayout) findViewById(R.id.content_frame);

        headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        navigationView.addHeaderView(headerView);

        TextView tvName = (TextView) headerView.findViewById(R.id.tv_username);
        CircularImageView imageView = (CircularImageView) headerView.findViewById(R.id.profile_image);
        tvName.setText(user.getDisplayName());
        Picasso.with(this).load(user.getImageUrl()).into(imageView);

        //Unchecking all the drawer menu items before going back to home in case the app crashes
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(!menuItem.isChecked())
                    displayView(menuItem.getItemId());
                else
                    drawerLayout.closeDrawers();

                return true;
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                frame.setTranslationX((drawerLayout.getWidth() * slideOffset) / 4);
                frame.setTranslationX((drawerLayout.getWidth() * slideOffset) / 4);
                frame.setScaleX(1-(slideOffset/20));
                frame.setScaleY(1-(slideOffset/20));
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                    runnable.run();
                    runnable = null;
                }
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(drawerToggle);
        //calling sync state is necessary or else the hamburger icon won't show up
        drawerToggle.syncState();

        if(ibMenu!=null) {
            ibMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //Function for fragment selection and commits
    public void displayView(final int viewId) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (viewId) {
            case R.id.item_home:
                fragment = null;
                intent = new Intent(this, HomeActivity.class);
                break;
            case R.id.item_schedule:
                fragment = null;
                intent = new Intent(this, ScheduleActivity.class);
                break;
            case R.id.item_bookmark:
                fragment = null;
                intent = new Intent(this, BookmarkActivity.class);
                break;

            case R.id.item_map:
                fragment = null;
                intent = new Intent(this, MapActivity.class);
                break;

            case R.id.item_invite:
                fragment = null;
                intent = new Intent(this, InviteActivity.class);
                break;

            case R.id.item_logout:
                fragment = null;
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_SHORT).show();
                navigationView.getMenu().getItem(5).setChecked(true);
                break;
            case R.id.item_faq:
                fragment = null;
                intent = new Intent(this, FAQActivity.class);
                break;
            case R.id.item_contact:
                fragment = null;
                Toast.makeText(getApplicationContext(), "Contact dialog box needs to be implemented.", Toast.LENGTH_SHORT).show();
                navigationView.getMenu().getItem(7).setChecked(true);
                break;
            case R.id.item_rate:
                fragment = null;
                Toast.makeText(getApplicationContext(), "Rate dialog box needs to be implemented.", Toast.LENGTH_SHORT).show();
                navigationView.getMenu().getItem(8).setChecked(true);
                break;
            case R.id.item_feedback:
                fragment = null;
                Toast.makeText(getApplicationContext(), "Feedback dialog box needs to be implemented.", Toast.LENGTH_SHORT).show();
                navigationView.getMenu().getItem(9).setChecked(true);
                break;
            case R.id.item_about_us:
                fragment = null;
                intent = new Intent(this, AboutUsActivity.class);
                break;

            default:
                Toast.makeText(getApplicationContext(), "Something's Wrong", Toast.LENGTH_SHORT).show();
                break;
        }

        if(intent!=null){
            runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            });
        }

        drawerLayout.closeDrawers();

        if (fragment != null) {

//            Fragment temp  = getSupportFragmentManager().findFragmentById(R.id.frame);
//
//            if(temp==homefrag) {
//                    fragmentTransaction.add(R.id.frame, fragment);
//                    fragmentTransaction.commit();
//                }
//            }
//            else
//            { if(temp!=null) {
//                    fragmentTransaction.remove(temp);
//                    fragmentTransaction.add(R.id.frame, fragment);
//                    fragmentTransaction.commit();
//                } else {
//                    fragmentTransaction.remove(temp);
//                    fragmentTransaction.commit();
//                }
//            }

        }
    }

    public void runWhenIdle(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void onBackPressed() {
        //Go to home if the drawer is closed and the we are not on the HomeFragment (at_home flag checks for the latter)
        if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            if (at_home == false) {
                at_home = true;
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                //Implementation of "Click back again to exit"
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                //To keep track of how long the user has been waiting
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
        else
            drawerLayout.closeDrawers();
    }
}