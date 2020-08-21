package com.example.tracking_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {


    public static String Userid="userid";
    create_user addcircle;
    ArrayList<create_user> namelist;
    Context context;

    public MembersAdapter(ArrayList<create_user> nameList, Context applicationContext) {
    this.namelist=nameList;
    this.context=applicationContext;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);
       MembersViewHolder membersViewHolder=new MembersViewHolder(view,context,namelist);
       return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {

        addcircle = namelist.get(position);
        final String username=addcircle.firstname+" "+addcircle.lstname;
        final String Uid=addcircle.userid;
        holder.name.setText(addcircle.firstname+" "+addcircle.lstname);
        Picasso.get().load(addcircle.imageurl).placeholder(R.drawable.defaultimage).into(holder.circleImageView);

        if(addcircle.isharing.equals("false")){
           holder.i1.setImageResource(R.drawable.redbutton);
           holder.v.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(context, username+"  not share location", Toast.LENGTH_SHORT).show();
               }
           });
                   }
        else if(addcircle.isharing.equals("true")){
           holder.i1.setImageResource(R.drawable.gree);
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Tag",username);
                    Intent intent=new Intent(context,UserLocation.class);
                    intent.putExtra(Userid,Uid);
                    context.startActivity(intent);

                }
            });

        }




    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }


    public static class MembersViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView i1;
        CircleImageView circleImageView;
        View v;
        Context c;
        ArrayList<create_user> nameArraylist;
        FirebaseAuth auth;
        FirebaseUser user;


        public MembersViewHolder(@NonNull View itemView,  Context c, ArrayList<create_user> nameArraylist) {
            super(itemView);
            this.c = c;
            v=itemView;
            this.nameArraylist = nameArraylist;

            auth=FirebaseAuth.getInstance();
            user=auth.getCurrentUser();
            name=itemView.findViewById(R.id.item_title);
            i1=v.findViewById(R.id.onlinebutton);
            circleImageView=itemView.findViewById(R.id.imageprofile);
        }
    }
}
