package com.example.tracking_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinedAdapter  extends RecyclerView.Adapter<JoinedAdapter.MembersViewHolder>{

    public static String USER_ID="user_id";
    ArrayList<create_user> namelist;
    Context context;
    create_user addcircle;

    public JoinedAdapter(ArrayList<create_user> namelist, Context context) {
        this.namelist = namelist;
        this.context = context;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.joinedcircle,parent,false);
        JoinedAdapter.MembersViewHolder membersViewHolder=new JoinedAdapter.MembersViewHolder(view,context,namelist);
        return membersViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        addcircle = namelist.get(position);
        final String Userid=addcircle.userid;
        holder.name.setText(addcircle.firstname+" "+addcircle.lstname);
        Picasso.get().load(addcircle.imageurl).placeholder(R.drawable.defaultimage).into(holder.circleImageView);
       holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,joinedMember.class);
                intent.putExtra(USER_ID,Userid);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircleImageView circleImageView;
        View v;
        Context c;
        ArrayList<create_user> nameArraylist;
        FirebaseAuth auth;
        FirebaseUser user;


        public MembersViewHolder(@NonNull View itemView, Context c, ArrayList<create_user> nameArraylist) {
            super(itemView);
            this.c = c;
            v=itemView;
            this.nameArraylist = nameArraylist;

            auth=FirebaseAuth.getInstance();
            user=auth.getCurrentUser();
            name=itemView.findViewById(R.id.item_title);
            circleImageView=itemView.findViewById(R.id.imageprofile);

        }
    }

}
