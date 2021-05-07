package rustamspl.github.io.startchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        if(intent.getData()!=null){
            Log.d("intent received",intent.getData().toString());
            String phoneNumber = intent.getData().toString(); //contains tel:phone_no
            phoneNumber = phoneNumber.substring(4);
            Log.d("intent received","Received phone number : "+phoneNumber);

            String url = "https://wa.me/"+phoneNumber;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

            /// do what you like here
        }else{
            Log.d("intent received","null intent received");
        }
    }
}
