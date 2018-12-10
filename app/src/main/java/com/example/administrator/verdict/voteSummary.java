package com.example.administrator.verdict;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class voteSummary extends AppCompatActivity {

    TextView q,v1,v2,v3,v4;
    String question,userid,o1,o2,o3,o4,vt1,vt2,vt3,vt4;
    FirebaseAuth firebaseAuth;
    HashMap hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_summary);
        q=findViewById(R.id.questiontxt);
        v1=findViewById(R.id.vote1txt);
        v2=findViewById(R.id.vote2txt);
        v3=findViewById(R.id.vote3txt);
        v4=findViewById(R.id.vote4txt);



        question= getIntent().getExtras().getString("Question");
        q.setVisibility(View.VISIBLE);
        q.setText(question);

        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();

        getOptions();


    }
    public void getOptions(){
        final DatabaseReference quesAndOptions= FirebaseDatabase.getInstance().getReference("all questions");
        quesAndOptions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    hashMap= (HashMap) dataSnapshot1.getValue();
                    assert hashMap != null;
                    String a= (String) hashMap.get("Question");
                    if(question.equals(a)){
                        o1=hashMap.get("Option 1").toString();
                        o2=hashMap.get("Option 2").toString();
                        o3=hashMap.get("Option 3").toString();
                        o4=hashMap.get("Option 4").toString();
                        setVotes();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setVotes(){
        final DatabaseReference votesCount=FirebaseDatabase.getInstance().getReference("all Votes").child(question);



        votesCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vt1 = dataSnapshot.child("Option 1").getValue().toString();
                vt2 = dataSnapshot.child("Option 2").getValue().toString();
                vt3 = dataSnapshot.child("Option 3").getValue().toString();
                vt4 = dataSnapshot.child("Option 4").getValue().toString();

                if ((!o1.equals(null)) && (!o1.equals(""))) {
                    v1.setVisibility(View.VISIBLE);
                    v1.setText(o1 + "  \nTotal Votes:   " + vt1);
                }
                if ((!o2.equals(null)) && (!o2.equals(""))) {
                    v2.setVisibility(View.VISIBLE);
                    v2.setText(o2 + "  \nTotal Votes:   " + vt2);
                }
                if ((!o3.equals(null)) && (!o3.equals(""))) {
                    v3.setVisibility(View.VISIBLE);
                    v3.setText(o3 + "  \nTotal Votes:   " + vt3);
                }
                if ((!o4.equals(null)) && (!o4.equals(""))) {
                    v4.setVisibility(View.VISIBLE);
                    v4.setText(o4 + "  \nTotal Votes:   " + vt4);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
