package com.example.deepak.uploadimagetoserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Matrix;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class MainActivity extends Activity {



    Display dp = new Display();
    String gallery = dp.gallery_name;

    public static String URL = "https://www.scholarshield.in/Classes/m1/images/uploadimage.php";
//    private static final String IMAGE_CAPTURE_FOLDER = "Classes/m1/images";
    private static final String IMAGE_CAPTURE_FOLDER = "DCIM/Camera";
    private static final int CAMERA_PIC_REQUEST = 1111;
    private Button btnCamera;
    private static File file;
    private static Uri _imagefileUri;
    private TextView resultText;
    private static String _bytes64Sting, _imageFileName;
    int count1 = 0;
    int count2 = 0;
    int flag = 0;

    //This imagepath should be stored one by one in this replacable variable stored_iamge_path and then shd go in the arraylist
    String stored_image_uri_path;


    ArrayList<String> list_Unsent_Images=new ArrayList<String>();//Creating arraylist.
    // list.add("Ravi");//Adding object in arraylist.
    //list.add("Vijay");

    public boolean isOnline() {
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
    }


      // This , I have to create the onclick bundle in this class which will launch after the button click

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //code for toast of Internet Connectivity
      //  new AsyncHttpTask().execute("www.google.com");


        isOnline();// to check the internet.


        // code to play the sound
        final MediaPlayer mp = MediaPlayer.create(this,R.raw.cb_sml);



        _imageFileName = String.valueOf(System.currentTimeMillis());
        btnCamera = (Button) findViewById(R.id.button);//same for camera open and play sound track
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
                mp.start();
            }
        });
    }



    // got the imagefileuri stored locally in the global variable stored_image_uri_path

    public void return_stored_image_uri(String str){

        stored_image_uri_path = str;

    }



    private void captureImage() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        _imagefileUri = Uri.fromFile(getFile());// we created uri of the image file here.

        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imagefileUri);//to store the image captured in camera
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                try {
                    uploadImage(_imagefileUri.getPath());//got path of the image using this imagefileuri.getpath();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //I need to store the imagepath only when the uploading fails.
                return_stored_image_uri(_imagefileUri.getPath());

            //    return_stored_image_uri(_imagefileUri.getPath());//for storing the image path locally per new image click

              //  if(flag == 0){//i need to know before hand that image uploading has failed
              //      resend_images(_imagefileUri.getPath());
              //  }
            }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT).show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();

        }
    }







    // here is the method to upload the image using its picture path
    //what I do here is format the image and make it ready to use the uploadtoServer class and upload it

       private void uploadImage(String picturePath) throws IOException {
        Bitmap bm = BitmapFactory.decodeFile(picturePath);//created a bitmap of the image I received

        ExifInterface ei = new ExifInterface(picturePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        // I got the orientation of the image here

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        android.graphics.Matrix m = new android.graphics.Matrix();
        m.postRotate(rotationAngle);
        Bitmap rotatedImg = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);




        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        rotatedImg.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] byteArray = bao.toByteArray();
        _bytes64Sting = Base64.encodeBytes(byteArray);
        RequestPackage rp = new RequestPackage();
        rp.setMethod("POST");//using the post method to post the data
        rp.setUri(URL);
        rp.setSingleParam("base64", _bytes64Sting);//adding up all the parameters to the hashmap in RequestPackage java class
        rp.setSingleParam("ImageName", _imageFileName + ".jpg");
        rp.setSingleParam("Gallery", gallery);

        // Upload image to server
        new uploadToServer().execute(rp);
        // here the upload to server activity finishes, I can keep a flag here
        //count1++;  *** no need to count here as counting in the onpostexecute method
    }

    // To resend the image logic here
   // private void resend_images(String picturePath)//need to give some variable in the parameter field here





    public class uploadToServer extends AsyncTask<RequestPackage, Void, String> {

        // Async task is used to do tasks in background

//        private ProgressDialog pd = new ProgressDialog(MainActivity.this);


        //This method gets executed before the starting of the background activity(image getting uploaded to server)
        protected void onPreExecute() {
            super.onPreExecute();
            resultText = (TextView) findViewById(R.id.textView);
            resultText.setText("New file "+_imageFileName+".jpg created\n");


           // pd.setMessage("Image uploading!, please wait..");
            //pd.setCancelable(false);//cant cancel image upload
            //pd.show();
        }


        //This process occours in the background ,this is the heavy task(uploading the image onto the server) running at background.
        @Override
        protected String doInBackground(RequestPackage... params) {

            String content = MyHttpURLConnection.getData(params[0]);
            //resultText = (TextView) findViewById(R.id.textView);
            //resultText.setText("New file "+_imageFileName+".jpg created\n");


//            pd.setMessage("Image uploading!, please wait..");
//            pd.setCancelable(false);//cant cancel image upload
//            pd.show();
            return content;

        }

        @Override
       /* protected void onProgressUpdate(Void... values) {
            //super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(),
                    "Image is  being uploaded in the background till the time you may take another picture", Toast.LENGTH_SHORT)
                    .show();
        }*/

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();
            resultText.setText(result);
            count1++;
            if(result != null && !result.trim().isEmpty()){
                Toast.makeText(getApplicationContext(),
                        "Finished Uploading Image number : " +  count1, Toast.LENGTH_SHORT)
                        .show();
             //flag 0 means successfull uploading
            }
            else{
                //flag = 1;//flag = 1 means that image has failed uploading
                //to push the imagepath to the arraylist
                list_Unsent_Images.add(stored_image_uri_path);



            }
             // increase the value of count1 , which shows the num of images successfully uploaded on server
           /* Toast.makeText(getApplicationContext(),
                    "Upload to server finished", Toast.LENGTH_SHORT)
                    .show();*/
            if(flag == 0){

            }
            else{
                // resend the image and again clear the flag to 0.

               //Building up the notification for it.
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.nf)
                                .setContentTitle("Image Sending Notification")
                                .setContentText("Failed Uploading Image number : " +  count1);

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//                resultIntent.setAction("android.intent.action.MAIN"); trying without this
//                resultIntent.addCategory("android.intent.category.LAUNCHER");

                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                getApplicationContext(),
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);

                // Sets an ID for the notification
                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

                //Reseting the flag now as work of sending the notification is done by now
                flag = 0;
                //now to call the method on notification click


                BroadcastReceiver call_method = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action_name = intent.getAction();
                        if (action_name.equals("call_method")) {
                            // call your method here and do what ever you want.

                            try {
                                uploadImage(stored_image_uri_path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    };
                };
                registerReceiver(call_method, new IntentFilter("call_method"));


            }

           // flag = flag + 1; // flag non zero means image sent to server
        }
    }

    private File getFile() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        file = new File(filepath, IMAGE_CAPTURE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        return new File(file + File.separator + _imageFileName
                + ".jpg");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
