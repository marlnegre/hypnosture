package com.google.hangouts.hypnosture;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.model.statistics_helper.StatisticsHelper;
import com.google.hangouts.hypnosture.util.Helpers;

import java.util.Date;
import java.util.Map;


public class AngleAdmin {

    public static Query getAngle(final AngleAdmin.GetAngleCallback callback) {

        Query angle = FirebaseDatabase.getInstance().getReference("Angle");
        angle.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get map of users in datasnapshot
                String proper = (String) dataSnapshot.child("Proper").getValue().toString();
                callback.onGet(proper);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
        return angle;
    }

    public interface GetAngleCallback {
        void onGet(String angle);
    }
}
