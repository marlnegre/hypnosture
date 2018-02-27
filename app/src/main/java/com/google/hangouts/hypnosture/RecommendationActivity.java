package com.google.hangouts.hypnosture;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendationActivity extends AppCompatActivity {

    String[] recom = {"Don't be a slouch", "Beware of 'Text Neck'", "Hit the hay the right way", "Stand up straight","Don't slump at your desk","Don't be a low rider"};
    int[] images = {R.drawable.slouch, R.drawable.textback, R.drawable.sleep, R.drawable.standup, R.drawable.sit, R.drawable.drive};

    ListView listOfExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__recommendations);

        Toolbar toolbar = findViewById(R.id.toolbar_recommendation);
        setSupportActionBar(toolbar);
        listOfExercises = (ListView) findViewById(R.id.listView);
        MyAdapter myAdapter = new MyAdapter(RecommendationActivity.this, recom, images);
        listOfExercises.setAdapter(myAdapter);
        listOfExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//               Intent intent = new Intent(RecommendationActivity.this, DetailActivity.class);
//               intent.putExtra("exercise", exercises[i]);
//               intent.putExtra("image", images[i]);
//               intent.putExtra("info", info[i]);
//               startActivity(intent);
                if(i == 0)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationActivity.this);
                    builder.setTitle("Don't be a slouch!");
                    builder.setMessage("It adds to the stress on your spine. That puts a strain on the bones, muscles, and joints you need to hold your backbone in place. But lousy posture isn't just bad for your back. A constant slump smashes your inside organs together, and makes it harder for your lungs and intestines to work. Over time, that’ll make it hard to digest food or get enough air when you breathe.");
                    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (i==1){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationActivity.this);
                    builder.setTitle("Beware of Text Neck!");
                    builder.setMessage("Too much texting caused you to come down with neck strains, and headaches. \n" +
                            "\n" +
                            "To help you with this problem, try to take a break every 15 minutes and hold your head back. Holding your arms while looking straight may seem awkward, but this might save your neck and spine from any strain.  ");
                    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else if (i==2){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationActivity.this);
                    builder.setTitle("Hit the hay the right way!");
                    builder.setMessage(" This is an idiomatic expression saying 'Go to bed properly'. \n" +
                            "\n" +
                            "Maintaining the curve in your lower back helps your position to be right. Try lying on your back with a pillow under your knees or on your side with your knees slighlty bent. This will help you maintain good sleeping posture.");
                    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else if (i==3){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationActivity.this);
                    builder.setTitle("Stand up straight!");
                    builder.setMessage("We need some imagination on this one. This may sound easy but it's a little way harder to maintain this posture.\n" +
                            "\n" +
                            "Visualize that a string coming from the top of your head is pulling you gently up towards the ceiling; Be sure to try to walk as if you had a book on your head try to balance everything; and Let your posture and balance rely more on your calves. \n" +
                            "\n" +
                            "These visualizations will guide your sense of proper position.");
                    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else if (i==4){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationActivity.this);
                    builder.setTitle("Don't slump at your desk!");
                    builder.setMessage("Like standing up straight, sitting down straight is also a must. Use the same good posture techniques you would use while standing. Choosing a quality chairs that provides firm support in the lower back area,  and make sure your knees are a little higher than your hips when you sit.");
                    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationActivity.this);
                    builder.setTitle("Don't be a low rider!");
                    builder.setMessage("Sure, it's cool and comfy to recline during a long drive. But it isn’t great for your posture. Instead, pull your seat close to the steering wheel. Try not to lock your legs. Bend your knees slightly. They should be at hip level or a tad above. Don't forget to put a pillow or rolled-up towel behind you for support.");
                    builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });

        setBackBtn();

    }


    public void setBackBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}