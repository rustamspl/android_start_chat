package rustamspl.github.io.startchat;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final String DO_REDIRECT ="DO_REDIRECT" ;
    private Switch swRedirect;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        swRedirect=findViewById(R.id.swRedirect);
        swRedirect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences prefs = getSharedPreferences(
                        getPackageName() + "_preferences", Context.MODE_PRIVATE);
                prefs.edit().putBoolean(DO_REDIRECT,b).commit();
            }
        });
//        Button b=findViewById(R.id.button);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//            }
//        });

        if (!isRedirection()){
            roleAcquire(RoleManager.ROLE_CALL_REDIRECTION);
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DO_REDIRECT, swRedirect.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        swRedirect.setChecked(savedInstanceState.getBoolean(DO_REDIRECT,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void roleAcquire(String roleName) {
        RoleManager roleManager;
        if (roleAvailable(roleName)) {
            roleManager = getSystemService(RoleManager.class);
            Intent intent = roleManager.createRequestRoleIntent(roleName);
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(
                    this,
                    "Redirection call with role in not available",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isRedirection() {
        return isRoleHeldByApp(RoleManager.ROLE_CALL_REDIRECTION);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private  boolean isRoleHeldByApp( String roleName){
        RoleManager roleManager = getSystemService(RoleManager.class);
        return roleManager.isRoleHeld(roleName);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private  boolean roleAvailable(String roleName){
        RoleManager roleManager = getSystemService(RoleManager.class);
        return roleManager.isRoleAvailable  (roleName);
    }


}
