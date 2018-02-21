package com.google.hangouts.hypnosture;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ScreenShot extends AppCompatActivity implements  View.OnClickListener {

    Button fullPageScreenshot, customPageScreenshot;
    LinearLayout rootContent;
    ImageView imageView;
    TextView hiddenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
        findViews();
        implementClickEvents();
    }

    /*  Find all views Ids  */
    private void findViews() {
        fullPageScreenshot = (Button) findViewById(R.id.full_page_screenshot);
        customPageScreenshot = (Button) findViewById(R.id.custom_page_screenshot);

        rootContent = (LinearLayout) findViewById(R.id.root_content);

        imageView = (ImageView) findViewById(R.id.image_view);

        hiddenText = (TextView) findViewById(R.id.hidden_text);
    }

    /*  Implement Click events over Buttons */
    private void implementClickEvents() {
        fullPageScreenshot.setOnClickListener(this);
        customPageScreenshot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.full_page_screenshot:
                takeScreenshot(ScreenshotType.FULL);
                break;
            case R.id.custom_page_screenshot:
                takeScreenshot(ScreenshotType.CUSTOM);
                break;
        }
    }

    /*  Method which will take screenshot on Basis of Screenshot Type ENUM  */
    public void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        switch (screenshotType) {
            case FULL:
                //If Screenshot type is FULL take full page screenshot i.e our root content.
                b = com.google.hangouts.hypnosture.ScreenshotUtils.getScreenShot(rootContent);
                break;
            case CUSTOM:
                //If Screenshot type is CUSTOM

                fullPageScreenshot.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
                hiddenText.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of hidden text

                b = com.google.hangouts.hypnosture.ScreenshotUtils.getScreenShot(rootContent);

                //After taking screenshot reset the button and view again
                fullPageScreenshot.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of first button again
                hiddenText.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of hidden text

                //NOTE:  You need to use visibility INVISIBLE instead of GONE to remove the view from frame else it wont consider the view in frame and you will not get screenshot as you required.
                break;
        }

        //If bitmap is not null
        if (b != null) {
            //showScreenShotImage(b);//show bitmap over imageview

            sendNotification();

            File saveFile = com.google.hangouts.hypnosture.ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            File file = com.google.hangouts.hypnosture.ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);//save the screenshot to selected path
            shareScreenshot(file);//finally share screenshot
        } else
            //If bitmap is null show toast message
            Toast.makeText(this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();

    }

    /*  Show screenshot Bitmap */
    private void showScreenShotImage(Bitmap b) {
        imageView.setImageBitmap(b);
    }

    private void sendNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Hey...")
                        .setContentText("You have an improper posture!");

        int notificationId = 101;

        NotificationManager notifyMgr =
                (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);

        notifyMgr.notify(notificationId, builder.build());
    }

    /*  Share Screenshot  */
    public void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharing_text));
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }
}