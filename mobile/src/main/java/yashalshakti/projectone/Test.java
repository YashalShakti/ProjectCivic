 package yashalshakti.projectone;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


 public class Test extends Activity implements View.OnClickListener {

     Button set,get;
     EditText setData[] = new EditText[4];
     TextView getData[] = new TextView[4];
     EditText loc;
    EditText sel,sel2;

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
         //Initialize



         //OnClicks
        get.setOnClickListener(this);
        set.setOnClickListener(this);
         //OnClicks


        //Google Play Services
         Intent intent = new Intent(this,GetLocation.class);
         startActivity(intent);

        set.requestFocus();
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
                       d.show();

                       ParseQuery<ParseObject> query = ParseQuery.getQuery("Data");
                       ParseGeoPoint userLocation = new ParseGeoPoint(Double.parseDouble(sel.getText().toString()),Double.parseDouble(sel2.getText().toString()));
                       query.whereNear("Location",userLocation);

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
                   }

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
                        setData[2].setText("");
                        setData[3].setText("");
                        loc.setText("");
                        setData[0].requestFocus();
                    }
                }
                 break;
         }

     }
 }
