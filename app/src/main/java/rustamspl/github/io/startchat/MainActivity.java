package rustamspl.github.io.startchat;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final String APP_ENABLED ="APP_ENABLED" ;
    private Switch swRedirect;

    public static SharedPreferences getPrefs(Context ctx){
        return  ctx.getSharedPreferences(
                ctx.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }
    public static  boolean isAppEnabled(Context ctx){
       return getPrefs(ctx).getBoolean(APP_ENABLED,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx=this;
        setContentView(R.layout.activity_main);
        swRedirect=findViewById(R.id.swRedirect);
        swRedirect.setChecked(isAppEnabled(ctx));
        swRedirect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                getPrefs(ctx).edit().putBoolean(APP_ENABLED,b).commit();
            }
        });


        checkPermission();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        checkPermission();
    }

    public void checkPermission() {
        if (!isRedirection()){
            roleAcquire(RoleManager.ROLE_CALL_REDIRECTION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 2);
            }
        }
    }



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

    private boolean isRedirection() {
        return isRoleHeldByApp(RoleManager.ROLE_CALL_REDIRECTION);
    }

    private  boolean isRoleHeldByApp( String roleName){
        RoleManager roleManager = getSystemService(RoleManager.class);
        return roleManager.isRoleHeld(roleName);
    }


    private  boolean roleAvailable(String roleName){
        RoleManager roleManager = getSystemService(RoleManager.class);
        return roleManager.isRoleAvailable  (roleName);
    }


}
