package com.google.hangouts.hypnosture.model.statistics_helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.hangouts.hypnosture.model.Statistics;
import com.google.hangouts.hypnosture.util.Helpers;

import java.util.Date;
import java.util.Map;

public class StatisticsHelper {
    public static void writeNewStatistics(Integer improper_posture) {
        Statistics statistics = new Statistics(improper_posture);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Statistics").child(currentUser.getUid()).push().setValue(statistics);
    }

    public static Query getStatistics(Date currentDate, final GetStatisticsCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Date startDate = Helpers.getStartOfDay(currentDate);
        Date endDate = Helpers.getEndOfDay(currentDate);

//        System.out.println(currentDate);
//        System.out.println(startDate);
//        System.out.println(endDate);
//        System.out.println(currentUser.getUid());
        Query statistics = FirebaseDatabase.getInstance().getReference("Statistics").child(currentUser.getUid()).orderByChild("timestamp").startAt(startDate.getTime()).endAt(endDate.getTime());

        statistics.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        callback.onGet(calculateImproperPosture((Map<String,Object>) dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        return statistics;
    }

    public interface GetStatisticsCallback {
        void onGet(Long improperPosture);
    }

    private static Long calculateImproperPosture(Map<String,Object> users) {

        Long improperPosture = 0L;

        if (users != null) {
            for (Map.Entry<String, Object> entry : users.entrySet()) {
                Map singleUser = (Map) entry.getValue();
                Long posture = (Long) singleUser.get("improper_posture");
                improperPosture += posture;
            }
        }

        return improperPosture;
    }
}
