package pagiisnet.pagiisnet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.Utils.Messages;

public class OnChatting extends AppCompatActivity {

    private TextView userNameDisplay, messageTime;
    private ImageView userDisplayImage;



    private FloatingActionButton fabSend;
    private TextInputLayout textInputLayout;
    private EditText textInput;
    private RecyclerView messagesRecyclerView;


    private FirebaseAuth firebaseAuth;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference databaseReference;

    private final List<Messages> messageList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    private  MessagesAdapter messagesAdapter;



    private String messageSenderId;
    private String  messageReceiverId;


    private final List<Messages>  list_chat= new ArrayList<>();
    private FirebaseListAdapter<Messages> adapter;
    private String  userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_chatting);
        messageSenderId = firebaseAuth.getCurrentUser().getUid();

        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View actionBarView = layoutInflater.inflate(R.layout.chats_custom_bar,null);
        actionBar.setCustomView(actionBarView);

        fabSend = findViewById(R.id.sendIcon);

        messageTime = findViewById(R.id.lastSeen);
        userDisplayImage = findViewById(R.id.customDP);
        userNameDisplay = findViewById(R.id.userNameDisplay);


        userNameDisplay.setText(userName);



        messagesAdapter = new MessagesAdapter(messageList);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        linearLayoutManager = new LinearLayoutManager(this);

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        messagesRecyclerView.setAdapter(messagesAdapter);




        fetchMessages();



        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {

                sendMessage();

                FirebaseDatabase.getInstance().getReference().push().setValue(new Messages(textInput.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),true));
                textInput.setText("");

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }


        FirebaseUser user = firebaseAuth.getCurrentUser(); //Firebase user Object

        databaseReference = FirebaseDatabase.getInstance().getReference();


        //displayChatMessage();

    }

    private void fetchMessages() {

        databaseReference.child("Messages").child(messageSenderId).child(messageReceiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messageList.add(messages);

                        messagesAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //Method In the new onClick Method Send Message
    private void sendMessage() {

        String messageText = textInput.getText().toString();
        textInput = findViewById(R.id.textInputField);

        if(TextUtils.isEmpty(messageText)){

            Toast.makeText(OnChatting.this, "Type In Message ...",Toast.LENGTH_SHORT).show();
        }else
        {
            String message_sender_ref = "Messages/" + messageSenderId + "/"+ messageReceiverId;

            String message_receiver_ref = "Messages/" + messageReceiverId + "/"+ messageSenderId;


            DatabaseReference user_message_key = databaseReference.child("Messsages").child(messageSenderId)
                    .child(messageReceiverId).push();


            //UserMessageKey
            String message_push_id = user_message_key.getKey();


            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("seen", false);
            messageTextBody.put("type", "text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from", messageSenderId);


            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(message_sender_ref + "/" + message_push_id, messageTextBody);

            //for Receiver
            messageBodyDetails.put(message_receiver_ref + "/" + message_push_id, messageTextBody);

            databaseReference.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {


                    if(databaseError != null)
                    {

                        Log.d("Chat_Log", databaseError.getMessage());

                    }

                    textInput.setText("");
                }
            });

        }
    }


    private void displayChatMessage() {

        DatabaseReference mf = FirebaseDatabase.getInstance().getReference();
        RecyclerView chatsListView = findViewById(R.id.messagesRecyclerView);
        //Suppose you want to retrieve "chats" in your Firebase DB:
        Query query = FirebaseDatabase.getInstance().getReference().child("chats");
//The error said the constructor expected FirebaseListOptions - here you create them:
        FirebaseListOptions<Messages> options = new FirebaseListOptions.Builder<Messages>()
                .setQuery(query,Messages.class)
                .setLayout(android.R.layout.activity_list_item)
                .build();
        //Finally you pass them to the constructor here:
        adapter = new FirebaseListAdapter<Messages>(options){

            @Override
            protected void populateView(View v,Messages model, int i) {
                // Get references to the views of message.xml
                //TextView messageText = (TextView)v.findViewById(R.id.message_text);
                //TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                // BubbleTextView bubbleTextView = (BubbleTextView)v.findViewById(R.id.sendBubble_text_message);

                //bubbleTextView.setText(model.getMessageText());//try to display the chat message on the bubble chat view

                // Set their text
                // messageText.setText(model.getMessageText());
                // Format the date before showing it
                //messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
    }
}