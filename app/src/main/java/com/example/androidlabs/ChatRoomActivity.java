package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "ChatRoomActivity";

    public static final String MSG_SELECTED = "MSG";
    public static final String MSGDB_ID = "ID";
    public static final String MSG_POSITION = "POSITION";
    public static final String SENT = "SENT";
    public static final String RECEIVED = "RECEIVED";
    public static final int EMPTY_ACTIVITY = 345;

    DatabaseHelper myDb;
    private ListView chatListView;
    private EditText chatText;
    private Button btnSend, btnReceive;
    private String typedMessage;
    long matchId;
    int savedPosition;



    private ArrayList<ChatMessage> mMessageArrayList = new ArrayList<ChatMessage>();
    ChatListAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_activity);

        myDb = new DatabaseHelper(this);


        btnSend = (Button) findViewById(R.id.BtnSend);
        btnReceive = (Button) findViewById(R.id.BtnReceive);
        chatText = (EditText) findViewById(R.id.chatText);
        chatListView = (ListView) findViewById(R.id.chatListView);
        boolean isTablet= findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        chatAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatAdapter);

        //viewAll();

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            Toast.makeText(ChatRoomActivity.this,"No data found",Toast.LENGTH_LONG).show();
        }

        while(res.moveToNext()) {
            if(res.getInt(2)==1) {
                ChatMessage chatMessage = new ChatMessage(true, false, res.getString(1), res.getLong(0));
                //Log.d("message: ",res.getString(1));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }
            else if (res.getInt(2)==0) {
                ChatMessage chatMessage = new ChatMessage(false, true, res.getString(1), res.getLong(0));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }
        };

        //printCursor(res);

        //FRAGMENT block for Lab8
        chatListView.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            savedPosition = position;
            dataToPass.putString(MSG_SELECTED, mMessageArrayList.get(position).getMessage());
            dataToPass.putInt(MSG_POSITION, position);

            Cursor res2 = myDb.getAllData();
            if(res2.getCount() != 0){
            res2.moveToFirst();
           do {
                if (res2.getString(1).equals(mMessageArrayList.get(position).getMessage()))
                    matchId = res2.getLong(0);
            } while(res2.moveToNext());}

            dataToPass.putLong(MSGDB_ID, matchId);
            if(mMessageArrayList.get(position).isLeft())
                dataToPass.putString(RECEIVED,"Received");
            else
                dataToPass.putString(SENT,"Sent");

            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("Back") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });


    }

    /*
    public void viewAll() {

        Cursor res = myDb.getAllData();
            if(res.getCount() == 0) {
                Toast.makeText(ChatRoomActivity.this,"No data found",Toast.LENGTH_LONG).show();
            }

        res.moveToFirst();
        do {
            if(res.getInt(3)==1) {
                ChatMessage chatMessage = new ChatMessage(true, false, res.getString(2), res.getLong(1));
                Log.d("message: ",res.getString(2));
                //Log.d("id: ",toString(res.getLong(1)));
                mMessageArrayList.add(chatMessage);
                ChatListAdapter chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }
            else if (res.getInt(3)==0) {
                ChatMessage chatMessage = new ChatMessage(false, true, res.getString(2), res.getLong(1));
                mMessageArrayList.add(chatMessage);
                ChatListAdapter chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }

        } while(res.moveToNext());
    }
*/

    public void sendChatMessage(View args0) {
        Cursor res = myDb.getAllData();
        long id = res.getCount()+1;
        typedMessage = chatText.getText().toString();
        //insert sent message to database
        boolean isInserted = myDb.insertData(id,typedMessage,1,0);
        if(isInserted)
            Toast.makeText(ChatRoomActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(ChatRoomActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();

        //insert sent message to arraylist
        ChatMessage chatMessage = new ChatMessage(true,false,typedMessage,id);
        mMessageArrayList.add(chatMessage);
        ChatListAdapter chatAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatAdapter);
        chatText.setText("");
        chatAdapter.notifyDataSetChanged();
    }


    public void receiveChatMessage(View args0) {
        Cursor res = myDb.getAllData();
        long id = res.getCount()+1;
        typedMessage = chatText.getText().toString();

        //insert received message to database
        boolean isInserted = myDb.insertData(id,typedMessage,0,1);
        if(isInserted)
            Toast.makeText(ChatRoomActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(ChatRoomActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();

        //insert received message to arraylist
        ChatMessage chatMessage = new ChatMessage(false,true,typedMessage,id);
        mMessageArrayList.add(chatMessage);
        ChatListAdapter chatAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatAdapter);
        chatText.setText("");
        chatAdapter.notifyDataSetChanged();
    }


    private class ChatListAdapter extends BaseAdapter {

        private static final String TAG = "ChatListAdapter";
        private Context mContext;
        private String mMessage;
        private int mResource;

        public int getCount() {
            return mMessageArrayList.size();
        } //This function tells how many objects to show

        public Object getItem(int position) {
            return mMessageArrayList.get(position);
        }  //This returns the string at position p

        public long getItemId(int p) {
            return p;
        } //This returns the database id of the item at position p

        public View getView(int p, View customView, ViewGroup parent) {
            View thisRow = customView;

            if (mMessageArrayList.get(p).isRight()) {
                thisRow = getLayoutInflater().inflate(R.layout.sent_message, null);
                TextView sendTextView = (TextView) thisRow.findViewById(R.id.msgSent);
                sendTextView.setText(mMessageArrayList.get(p).getMessage());
            } else if (mMessageArrayList.get(p).isLeft()) {
                thisRow = getLayoutInflater().inflate(R.layout.received_message, null);
                TextView receiveTextView = (TextView) thisRow.findViewById(R.id.msgReceived);
                receiveTextView.setText(mMessageArrayList.get(p).getMessage());
            }
            return thisRow;

        }
    }

    public void printCursor( Cursor c){
        String strDatabaseVersion = "Database version number: " + myDb.DATABASE_VERSION;
        String strNumberOfColumns = "Number of columns = " + c.getColumnCount();
        String strNumberOfResults = "Number of results = " + c.getCount();
        String strColumnNames = "Name of the columns: "
                + c.getColumnName(0) +", "
                + c.getColumnName(1) + ", "
                + c.getColumnName(2) + ", "
                + c.getColumnName(3);
        StringBuffer buffer = new StringBuffer();
        c.moveToFirst();
        do{
            buffer.append("Id: " + c.getString(0) + " ");
            buffer.append("Message: " + c.getString(1) + " ");
            buffer.append("Issent: " + c.getString(2) + " ");
            buffer.append("Isreceived: " + c.getString(3) + "\n");
        }while (c.moveToNext());

        Log.i(TAG, strDatabaseVersion + "\n" + strNumberOfColumns + "\n" + strColumnNames
                + "\n" + strNumberOfResults + "\n" + buffer.toString());

    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(MSGDB_ID, 0);
                deleteMessageId((int)id);
            }
        }
    }

    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        mMessageArrayList.remove(mMessageArrayList.get(savedPosition).getId());
        myDb.deleteData(Long.toString(matchId));
        chatAdapter.notifyDataSetChanged();

        //Refreshing data in chat view and mMessageArrayList by loading from database
        Cursor res = myDb.getAllData();
        mMessageArrayList.clear();
        while(res.moveToNext()) {
            if(res.getInt(2)==1) {
                ChatMessage chatMessage = new ChatMessage(true, false, res.getString(1), res.getLong(0));
                //Log.d("message: ",res.getString(1));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }
            else if (res.getInt(2)==0) {
                ChatMessage chatMessage = new ChatMessage(false, true, res.getString(1), res.getLong(0));
                mMessageArrayList.add(chatMessage);
                chatAdapter = new ChatListAdapter();
                chatListView.setAdapter(chatAdapter);
            }
        };
    }

}
