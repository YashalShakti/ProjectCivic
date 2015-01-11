 package yashalshakti.projectone;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.sql.SQLException;
import java.util.List;


 public class Test extends Activity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

     Button set,get,map;



     // Request code to use when launching the resolution activity
     private static final int REQUEST_RESOLVE_ERROR = 1001;
     // Unique tag for the error dialog fragment
     private static final String DIALOG_ERROR = "dialog_error";
     // Bool to track whether the app is already resolving an error
     private boolean mResolvingError = false;


     EditText setData[] = new EditText[4];
     TextView getData[] = new TextView[4];
     EditText loc;
     private FragmentManager supportFragmentManager;
    EditText sel,sel2;
     public GoogleApiClient mGoogleApiClient;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



         // Parse
         Parse.enableLocalDatastore(this);
         Parse.initialize(this, "wZDyqij57WYlPBPJpgbT3NWQLhKXmWpofCLINMOX", "BP6aEXUHiI3HchhQHnJ21NOmzMnRygVyy7ILBfhJ");
         ParsePush.subscribeInBackground("", new SaveCallback() {
             @Override
             public void done(ParseException e) {
                 if (e == null) {
                     Toast.makeText(getApplicationContext(),"Subscribed to the Yashal's Connect Service",Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(getApplicationContext(),"Error in Subscribing",Toast.LENGTH_SHORT).show();
                 }
             }
         });
         // Parse



        //Initialize
        set = (Button)findViewById(R.id.setButton);
        get = (Button)findViewById(R.id.getButton);
        setData[0]= (EditText)findViewById(R.id.set1);
        setData[1]= (EditText)findViewById(R.id.set2);
        setData[2]= (EditText)findViewById(R.id.set3);
        setData[3]= (EditText)findViewById(R.id.set4);
        getData[0]= (TextView)findViewById(R.id.get1);
        getData[1]= (TextView)findViewById(R.id.get2);
        sel= (EditText)findViewById(R.id.selection);
        sel2= (EditText)findViewById(R.id.selection2);
        loc=(EditText)findViewById(R.id.set5);
         map=(Button)findViewById(R.id.mapButton);
         //Initialize



         //OnClicks
        get.setOnClickListener(this);
        set.setOnClickListener(this);
         map.setOnClickListener(this);
         //OnClicks


        //Google Play Services
                                                                      //Intent intent = new Intent(this,GetLocation.class);
                                                                     //startActivity(intent);
         buildGoogleApiClient();

        set.requestFocus();
    }

     protected synchronized void buildGoogleApiClient() {
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                 .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                 .addApi(LocationServices.API)
                 .build();
     }

     @Override
     protected void onStart() {
         super.onStart();

         mGoogleApiClient.connect();
     }

     @Override
     protected void onStop() {
         super.onStop();
         if (mGoogleApiClient.isConnected()) {
             mGoogleApiClient.disconnect();}
     }


     @Override
     public void onConnected(Bundle bundle) {
         Toast.makeText(getApplicationContext(),"Connected to Google Play Services",Toast.LENGTH_SHORT).show();
         Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
         if (mLastLocation != null) {
             setData[2].setText(String.valueOf(mLastLocation.getLatitude()));

             loc.setText(String.valueOf(mLastLocation.getLongitude()));
             setData[3].setText("\nSpeed :"+String.valueOf(mLastLocation.getSpeed()));
             setData[3].append("\nAltitude :"+String.valueOf(mLastLocation.getAltitude()));


         }

     }

     @Override
     public void onConnectionSuspended(int i) {

     }

     @Override
     public void onConnectionFailed(ConnectionResult result) {
         Toast.makeText(getApplicationContext(),"Error in connection to Google Play Services",Toast.LENGTH_SHORT).show();
         if (mResolvingError) {
             // Already attempting to resolve an error.
             return;
         } else if (result.hasResolution()) {
             try {
                 mResolvingError = true;
                 result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
             } catch (IntentSender.SendIntentException e) {
                 // There was an error with the resolution intent. Try again.
                 mGoogleApiClient.connect();
             }
         } else {
             // Show dialog using GooglePlayServicesUtil.getErrorDialog()
             showErrorDialog(result.getErrorCode());
             mResolvingError = true;
         }
     }

     // The rest of this code is all about building the error dialog

     /* Creates a dialog for an error message */
     private void showErrorDialog(int errorCode) {
         // Create a fragment for the error dialog
         ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
         // Pass the error that should be displayed
         Bundle args = new Bundle();
         args.putInt(DIALOG_ERROR, errorCode);
         dialogFragment.setArguments(args);
         dialogFragment.show(getSupportFragmentManager(), "errordialog");
     }

     /* Called from ErrorDialogFragment when the dialog is dismissed. */
     public void onDialogDismissed() {
         mResolvingError = false;
     }

     public FragmentManager getSupportFragmentManager() {
         return supportFragmentManager;
     }

     /* A fragment to display an error dialog */
     public static class ErrorDialogFragment extends DialogFragment {
         public ErrorDialogFragment() { }

         @Override
         public Dialog onCreateDialog(Bundle savedInstanceState) {
             // Get the error code and retrieve the appropriate dialog
             int errorCode = this.getArguments().getInt(DIALOG_ERROR);
             return GooglePlayServicesUtil.getErrorDialog(errorCode,
                     this.getActivity(), REQUEST_RESOLVE_ERROR);
         }

         @Override
         public void onDismiss(DialogInterface dialog) {
             ((GetLocation)getActivity()).onDialogDismissed();
         }
     }

//Once the user completes the resolution provided by startResolutionForResult() or GooglePlayServicesUtil.getErrorDialog(), your activity receives the onActivityResult() callback with the RESULT_OK result code. You can then call connect() again. For example:

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == REQUEST_RESOLVE_ERROR) {
             mResolvingError = false;
             if (resultCode == RESULT_OK) {
                 // Make sure the app is not already connected or attempting to connect
                 if (!mGoogleApiClient.isConnecting() &&
                         !mGoogleApiClient.isConnected()) {
                     mGoogleApiClient.connect();
                 }
             }
         }
     }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

     @Override
     public void onClick(View v) {

         switch (v.getId())
         {
             case R.id.getButton:
                 try {

                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 boolean dataret=true;

               try {


                    dataret=true;


               }catch (Exception e)
               {
                   dataret=false;
                    Dialog d = new Dialog(this);
                   d.setTitle("Project One");
                   TextView tv=new TextView(this);
                   tv.setText("ERROR"+e.toString());
                   d.setContentView(tv);
                   d.show();

               }finally {

                   if(true)

                   {
                       Dialog d = new Dialog(this);
                       d.setTitle("Project One");
                       TextView tv=new TextView(this);
                       tv.setText("Data retrieved from the Cloud");
                       d.setContentView(tv);
                        getData[0].setText("");
                       getData[1].setText("");
                       ParseGeoPoint userLocation = new ParseGeoPoint(Double.parseDouble(sel.getText().toString()),Double.parseDouble(sel2.getText().toString()));
                       ParseQuery<ParseObject> query = ParseQuery.getQuery("Data");

                       query.whereNear("Location",userLocation);
                        query.whereWithinKilometers("Location",userLocation,3.0);
                       query.findInBackground(new FindCallback<ParseObject>() {


                           @Override
                           public void done(List<ParseObject> parseObjects, ParseException e) {
                               if (e == null) {
                                   getData[0].setText("Matches Found = " + parseObjects.size());
                                  String s="";
                                   for(ParseObject i : parseObjects)
                                   {
                                       s+=i.getString("Name")+"\n";
                                   }
                                   getData[1].setText(s);
                               } else {
                                   getData[0].setText("Error");

                               }
                           }
                       });
                       d.show(); }

               }

                 break;



             case R.id.setButton:
                 boolean work=true;
                try{

                    ParseObject setCloud = new ParseObject("Data");
                  
                    ParseGeoPoint point = new ParseGeoPoint(Double.parseDouble(setData[2].getText().toString()),Double.parseDouble(loc.getText().toString()));
                    setCloud.put("Name",setData[0].getText().toString());
                    setCloud.put("Type",setData[1].getText().toString());
                    setCloud.put("Location",point);
                    setCloud.put("Comment",setData[3].getText().toString());
                    setCloud.saveInBackground();
                }catch (Exception e)
                { work=false;}
                finally
                {
                    if(work)
                    {
                        Dialog d = new Dialog(this);
                        d.setTitle("Project One");
                        TextView tv = new TextView(this);
                        tv.setGravity(0);
                        tv.setText("Entered into the Cloud!");
                        d.setContentView(tv);
                        d.show();
                        setData[0].setText("");
                        setData[1].setText("");
                       // setData[2].setText("");
                        setData[3].setText("");
                      //  loc.setText("");
                        setData[0].requestFocus();
                    }
                }
                 break;
             case R.id.mapButton:
                 Intent intent =  new Intent(this,MapsActivity.class);
                intent.putExtra("latitude",setData[2].getText().toString());
                 intent.putExtra("longitude",loc.getText().toString());
                 startActivity(intent);
         }

     }
 }
