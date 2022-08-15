package rustamspl.github.io.startchat;

import android.content.Intent;
import android.net.Uri;
import android.telecom.CallRedirectionService;
import android.telecom.PhoneAccountHandle;
import android.util.Log;

public class MyCallRedirectionService extends CallRedirectionService {
    @Override
    public void onPlaceCall( Uri uri,  PhoneAccountHandle phoneAccountHandle, boolean b) {

       if(! MainActivity.isRedirectOn(this)) {
           placeCallUnmodified ();
           return;
       };
        String phoneNumber= uri.toString();
        String phoneNumber1 =  phoneNumber.substring(4).replaceAll("%2[Bb]","+").replaceAll("^8","+7")
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
