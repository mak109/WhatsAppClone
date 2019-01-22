package com.example.mayukh.whatsappclone;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity {

    private ListView listViewUsers;
    private SwipeRefreshLayout swipeRefreshLayout;
//    private ArrayList<String> usersList;
//    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
         final ArrayList<String> usersList = new ArrayList<>();
         final ArrayAdapter arrayAdapter = new ArrayAdapter(Users.this, android.R.layout.simple_list_item_1, usersList);
        listViewUsers = findViewById(R.id.listViewUsers);
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Users.this,ChatActivity.class);
                //send the selected user's name
                intent.putExtra("selectedUser",usersList.get(position));
                startActivity(intent);
            }
        });
        //Setting the refresh layout and its listener
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            //this method is invoked every time we pull to refresh
            public void onRefresh() {
                try {
                    /**
                     * Creating a query not containing the users already in the users list and
                     * excluding the current user
                     */
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", usersList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(objects.size() > 0){
                                if(e == null){
                                    for(ParseUser user: objects){
                                        usersList.add(user.getUsername());
                                    }
                                    //this is to inform the adapter that data has been updated
                                    arrayAdapter.notifyDataSetChanged();
                                    //to stop the refreshing
                                    if(swipeRefreshLayout.isRefreshing())
                                        swipeRefreshLayout.setRefreshing(false);

                                }
                                else
                                    FancyToast.makeText(Users.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                            }
                            else
                            { //no new users so we need to stop refreshing
                                if(swipeRefreshLayout.isRefreshing())
                                    swipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    });
                }catch (Exception e){e.printStackTrace();}
            }
        });
        try {
            ParseQuery<ParseUser> users = ParseUser.getQuery();
            users.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            users.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        for (ParseUser user : objects) {
                            usersList.add(user.getUsername());
                        }
                        listViewUsers.setAdapter(arrayAdapter);
                    } else
                        FancyToast.makeText(Users.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logOutUserItem) {
            FancyToast.makeText(Users.this,ParseUser.getCurrentUser().getUsername()+" is logged out",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Intent intent = new Intent(Users.this, SignUp.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        FancyToast.makeText(Users.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }



}
