package pagiisnet.pagiisnet;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import pagiisnet.pagiisnet.Utils.Messages;
import pagiisnet.pagiisnet.R;

public class MessagesAdapter extends  RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private final List<Messages> userMessagesList;

    private FirebaseAuth mAuth;

    private DatabaseReference usersDatabaseRef;


    public MessagesAdapter(List<Messages> userMessagesList)
    {

        this.userMessagesList = userMessagesList;



    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatting_message_item,viewGroup,false);

        mAuth = FirebaseAuth.getInstance();
        return  new MessagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessagesViewHolder messagesViewHolder, int i) {

        String messageSenderId = mAuth.getCurrentUser().getUid();


        Messages messages = userMessagesList.get(i);

        String fromMessgeType = messages.getType();


        String  fromUserId = (messages.getFrom());

        usersDatabaseRef = FirebaseDatabase.getInstance().getReference().child(fromUserId);
        usersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String userName = dataSnapshot.child("userNameAsEmail").getValue().toString();
                String userImage = dataSnapshot.child("userThumbImageDp").getValue().toString();

                Picasso.with(messagesViewHolder.profileDp.getContext()).load(userImage)
                        .placeholder(R.drawable.default_display_pic).into(messagesViewHolder.profileDp);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(fromMessgeType.equals("text"))
        {

            messagesViewHolder.messagePicture.setVisibility(View.INVISIBLE);


            if(fromUserId.equals(messageSenderId))
            {
                messagesViewHolder.messageText.setBackgroundResource(R.drawable.black_text_view_background);
                messagesViewHolder.messageText.setTextColor(Color.BLACK);
                messagesViewHolder.messageText.setGravity(Gravity.RIGHT);
            }else
            {

                messagesViewHolder.messageText.setBackgroundResource(R.drawable.orrnge_text_background_view);
                messagesViewHolder.messageText.setTextColor(Color.BLACK);
                messagesViewHolder.messageText.setGravity(Gravity.LEFT);

            }

            messagesViewHolder.messageText.setText(messages.getMessage());


        }else
        {

            messagesViewHolder.messageText.setVisibility(View.INVISIBLE);
            messagesViewHolder.messageText.setPadding(0,0,0,0);


            Picasso.with(messagesViewHolder.messagePicture.getContext()).load(messages.getMessage())
                    .placeholder(R.drawable.default_display_pic).into(messagesViewHolder.messagePicture);



        }


    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public  class MessagesViewHolder extends RecyclerView.ViewHolder
    {

        public TextView messageText;

        public ImageView profileDp;

        public ImageView messagePicture;

        public MessagesViewHolder(View view )
        {

            super(view);
            messageText = view.findViewById(R.id.messageText);
            //messagePicture= (ImageView)view.findViewById(R.id.picture_message_image_view);
            profileDp = view.findViewById(R.id.profileDp);


        }


    }

}
