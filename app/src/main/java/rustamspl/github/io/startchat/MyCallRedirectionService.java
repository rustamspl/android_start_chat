package rustamspl.github.io.startchat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.telecom.CallRedirectionService;
import android.telecom.PhoneAccountHandle;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class MyCallRedirectionService extends CallRedirectionService {
    @Override
    public void onPlaceCall(@NonNull Uri uri, @NonNull PhoneAccountHandle phoneAccountHandle, boolean b) {
        SharedPreferences prefs = getSharedPreferences(
                getPackageName() + "_preferences", Context.MODE_PRIVATE);
       if(! prefs.getBoolean(MainActivity.DO_REDIRECT,false)) {
           placeCallUnmodified ();
           return;
       };

        String phoneNumber = uri.toString().substring(4);
        String phoneNumber1 =  phoneNumber.replaceAll("^8","+7")
                .replaceAll("[^+0-9]","");
        Log.d("intent received","Received phone number : "+phoneNumber + " ; "+phoneNumber1);

        String url = "https://wa.me/"+phoneNumber1;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        cancelCall();
    }
}
