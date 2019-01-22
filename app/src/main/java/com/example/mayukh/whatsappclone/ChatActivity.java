package com.example.mayukh.whatsappclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewChat;
    private Button btnSendMessage;
    private String selectedUser = "";
    private ArrayList<String> chatList;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //inorder to receive the selected username send by intent
        selectedUser = getIntent().getStringExtra("selectedUser");
        FancyToast.makeText(ChatActivity.this,"Chat with "+selectedUser+" is enabled for the current user "+ParseUser.getCurrentUser().getUsername()
        ,FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();

        listViewChat = findViewById(R.id.listViewChat);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(ChatActivity.this);
        chatList = new ArrayList<>();
        adapter = new ArrayAdapter(ChatActivity.this,android.R.layout.simple_list_item_1,chatList);
        listViewChat.setAdapter(adapter);
        //receiving messages and updating the chat list
        try {
            ParseQuery<ParseObject> firstUserQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserQuery = ParseQuery.getQuery("Chat");

            firstUserQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserQuery.whereEqualTo("waReceiver", selectedUser);

            secondUserQuery.whereEqualTo("waSender", selectedUser);
            secondUserQuery.whereEqualTo("waReceiver", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserQuery);
            allQueries.add(secondUserQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String waMessage = chatObject.get("waMessage") + "";
                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + " : " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(selectedUser)) {
                                waMessage = selectedUser + " : " + waMessage;
                            }
                            chatList.add(waMessage);

                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //sending messages
    @Override
    public void onClick(View v) {
        final EditText edtSendMessage = findViewById(R.id.edtSendMessage);
        //creating a new parse object to hold the information of chatting
        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender",ParseUser.getCurrentUser().getUsername());
        chat.put("waReceiver",selectedUser);
        chat.put("waMessage",edtSendMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    //update chat list view and notify adapter about the update
                    FancyToast.makeText(ChatActivity.this,"message sent to "+selectedUser,FancyToast.LENGTH_SHORT,
                            FancyToast.INFO,true).show();
                    chatList.add(ParseUser.getCurrentUser().getUsername() + " : "+edtSendMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    //resetting the edit text
                    edtSendMessage.setText("");
                }
            }
        });

    }


}
