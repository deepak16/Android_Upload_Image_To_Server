package com.example.deepak.uploadimagetoserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by deepak on 1/3/17.
 */
public class Display extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static Button login_btn;


    /*private static EditText password;
    private static TextView attempts;
    private static Button login_btn;
    int attempt_counter = 5;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
       // isOnline();
        LoginButton();

    }
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
////        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        return activeNetworkInfo.isConnected();
//
//    }

   /* public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){

            //We are giving the toast here
           // Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("NO INTERNET CONNECTION!")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        return true;
    }*/

    public  void LoginButton() {
        username = (EditText)findViewById(R.id.username_value);
        password = (EditText)findViewById(R.id.password_value);

        //login_btn = (Button)findViewById(R.id.button_login);
        login_btn = (Button)findViewById(R.id.Bdisplay);

        //attempts.setText(Integer.toString(attempt_counter));





        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.getText().toString().equals("pathadi") &&
                                password.getText().toString().equals("1234")) {
                            gallery_name = "pathadi";


                            Toast.makeText(Display.this, "User and Password is correct",
                                    Toast.LENGTH_SHORT).show();
                            //isOnline();




                            //Intent intent = new Intent("com.example.deepak.uploadimagetoserver.MainActivity");
                            Intent intent = new Intent(Display.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else if(username.getText().toString().equals("wadala") &&
                                password.getText().toString().equals("1234")) {
                            gallery_name = "wadala";
                           // isOnline();
                           Toast.makeText(Display.this, "User and Password is correct",
                                    Toast.LENGTH_SHORT).show();
                          //  isOnline();
                            //Intent intent = new Intent("com.example.deepak.uploadimagetoserver.MainActivity");
                            Intent intent = new Intent(Display.this,MainActivity.class);
                            startActivity(intent);
                        }

                        else if(username.getText().toString().equals("mukthidham") &&
                                password.getText().toString().equals("1234")) {
                            gallery_name = "mukthidham";

                          Toast.makeText(Display.this, "User and Password is correct",
                                    Toast.LENGTH_SHORT).show();
                           // isOnline();




                            //Intent intent = new Intent("com.example.deepak.uploadimagetoserver.MainActivity");
                            Intent intent = new Intent(Display.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else if(username.getText().toString().equals("NMC19") &&
                                password.getText().toString().equals("1234")) {
                            gallery_name = "NMC19";

                            Toast.makeText(Display.this, "User and Password is correct",
                                    Toast.LENGTH_SHORT).show();
                            // isOnline();




                            //Intent intent = new Intent("com.example.deepak.uploadimagetoserver.MainActivity");
                            Intent intent = new Intent(Display.this,MainActivity.class);
                            startActivity(intent);
                        }

                        else if(username.getText().toString().equals("mukthidham2") &&
                                password.getText().toString().equals("1234")) {
                            gallery_name = "mukthidham2";

                            Toast.makeText(Display.this, "User and Password is correct",
                                    Toast.LENGTH_SHORT).show();
                            // isOnline();




                            //Intent intent = new Intent("com.example.deepak.uploadimagetoserver.MainActivity");
                            Intent intent = new Intent(Display.this,MainActivity.class);
                            startActivity(intent);
                        }

                        else {
                            Toast.makeText(Display.this, "User and Password is not correct",
                                    Toast.LENGTH_SHORT).show();
                            //trying to push this message as a notification in the tray

                            // Step 1 : Create a notification Builder

                        }

                    }
                }
        );
    }



  /*  public  void onButtonClick(View v){

        if(v.getId() == R.id.Bdisplay){

            Intent i = new Intent(Display.this,MainActivity.class);
            startActivity(i);

        }

    }*/
  public static String gallery_name;

}
