package com.google.hangouts.hypnosture.model.snore_helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.model.Snore;
import com.google.hangouts.hypnosture.model.Statistics;
import com.google.hangouts.hypnosture.model.statistics_helper.StatisticsHelper;
import com.google.hangouts.hypnosture.util.Helpers;

import java.util.Date;
import java.util.Map;

/**
 * Created by neil on 3/13/18.
 */

public class SnoreHelper {
    public static void writeNewSnore(Integer snore_detected) {
        Snore snore = new Snore(snore_detected);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Snore").child(currentUser.getUid()).push().setValue(snore);
    }

    public static Query getSnore(Date currentDate, final SnoreHelper.GetSnoreCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Date startDate = Helpers.getStartOfDay(currentDate);
        Date endDate = Helpers.getEndOfDay(currentDate);

        System.out.println(currentDate);
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(currentUser.getUid());
        Query statistics = FirebaseDatabase.getInstance().getReference("Snore").child(currentUser.getUid()).orderByChild("timestamp").startAt(startDate.getTime()).endAt(endDate.getTime());

        statistics.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        callback.onGet(calculateSnoreDetected((Map<String,Object>) dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        return statistics;
    }

    public interface GetSnoreCallback {
        void onGet(Long snoreDetected);
    }

    private static Long calculateSnoreDetected(Map<String,Object> users) {

        Long snoreDetected = 0L;

        if (users != null) {
            for (Map.Entry<String, Object> entry : users.entrySet()) {
                Map singleUser = (Map) entry.getValue();
                Long snore = (Long) singleUser.get("snore_detected");
                snoreDetected += snore;
            }
        }

        return snoreDetected;
    }

}
