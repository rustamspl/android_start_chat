package rustamspl.github.io.startchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.telecom.CallRedirectionService;
import android.telecom.PhoneAccountHandle;
import android.util.Log;
import android.view.WindowManager;

public class MyCallRedirectionService extends CallRedirectionService  implements DialogInterface.OnClickListener{
    private String phoneNumber;
    private int timeout;

    @Override
    public void onRedirectionTimeout() {
        cancelCall();
    }

    @Override
    public void onPlaceCall( Uri uri,  PhoneAccountHandle phoneAccountHandle, boolean allowInteractiveResponse) {

       if(! (allowInteractiveResponse &&MainActivity.isAppEnabled(this))) {
           placeCallUnmodified ();
           return;
       };

        phoneNumber= uri.toString();
         timeout=4;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dlg = builder.setMessage(getDlgMsg(timeout)).setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this).create();
        dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        dlg.show();
        MyCallRedirectionService ctx = this;
        new CountDownTimer(timeout*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(timeout>0) timeout--;
               dlg.setMessage(getDlgMsg(timeout));
            }

            public void onFinish() {
            }
        }.start();

    }

    private String getDlgMsg(int timeout) {
        String s=getString(R.string.open_in_ws);
        if(timeout>0){
            s+=" (" + timeout +")";
        }else {
            s+=" "+getString(R.string.if_no);
        }
       return  s;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                String phoneNumber1 =  phoneNumber.substring(4).replaceAll("%2[Bb]","+").replaceAll("^8","+7")
                        .replaceAll("[^+0-9]","");
                Log.d("intent received","Received phone number : "+phoneNumber + " ; "+phoneNumber1);

                String url = "https://wa.me/"+phoneNumber1;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                cancelCall();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                    placeCallUnmodified ();

                break;
        }
    }
}
