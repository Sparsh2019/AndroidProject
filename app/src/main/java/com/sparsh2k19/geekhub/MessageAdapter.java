package com.sparsh2k19.geekhub;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Activity context;
    private ArrayList<Message> messages;

    public MessageAdapter(Activity context, ArrayList<Message> messages){
        super(context, R.layout.item_message, messages);
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_message, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        TextView time = convertView.findViewById(R.id.time);
        TextView message = convertView.findViewById(R.id.message);
        ImageView image = convertView.findViewById(R.id.image);
        RelativeLayout code = convertView.findViewById(R.id.code_box);

        Message currentMessage = messages.get(position);
        name.setText(currentMessage.getName());
        String dateFormat = "HH:mm dd-MM-yyyy";
        String timeStr = new SimpleDateFormat(dateFormat).format(new Date(currentMessage.getTime()));
        time.setText(timeStr);
        if(currentMessage.getUid().equals(FirebaseAuth.getInstance().getUid())){
            ((LinearLayout) convertView.findViewById(R.id.message_item)).setGravity(Gravity.END);
            message.setBackground(ContextCompat.getDrawable(context, R.drawable.user_message_bubble));
            image.setBackground(ContextCompat.getDrawable(context, R.drawable.user_message_bubble));
            code.setBackground(ContextCompat.getDrawable(context, R.drawable.user_message_bubble));
        }

        if(currentMessage.getImageUri() != null) {
            image.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
            code.setVisibility(View.GONE);
            Glide.with(context).load(currentMessage.getImageUri()).into(image);
        } else if (currentMessage.getCode() != null) {
            image.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            code.setVisibility(View.VISIBLE);

            Code codeInstant = currentMessage.getCode();
            ((TextView) convertView.findViewById(R.id.lang)).setText(codeInstant.getLang());
            ((TextView) convertView.findViewById(R.id.source)).setText(codeInstant.getSource());
            ((TextView) convertView.findViewById(R.id.filename)).setText(codeInstant.getFilename());
        } else {
            image.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            code.setVisibility(View.GONE);
            message.setText(currentMessage.getMessage());
        }
        return convertView;
    }
}
