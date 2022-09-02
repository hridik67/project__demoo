package com.example.demoproject.Chats;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoproject.R;

import java.util.AbstractList;

public class mychatuserAdapter extends RecyclerView.Adapter<mychatuserAdapter.myviewHolder> {
    AbstractList<ChatUserData> chatUserData;
    private final UserListener userListener;

    public mychatuserAdapter(AbstractList<ChatUserData> chatUserData,UserListener userListener) {
        this.chatUserData = chatUserData;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_row_design,parent,false);
        return new myviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageBitmap(chatUserData.get(position).getImage());
        holder.name.setText(chatUserData.get(position).getName());
        holder.descp.setText(chatUserData.get(position).getDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListener.onUserClicked(chatUserData.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatUserData.size();
    }

    class myviewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name,descp;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.userImage);
            name=itemView.findViewById(R.id.name);
            descp=itemView.findViewById(R.id.descrip);
        }
    }
}
