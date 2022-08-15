package rustamspl.github.io.startchat;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final String DO_REDIRECT ="DO_REDIRECT" ;
    private Switch swRedirect;

    public static SharedPreferences getPrefs(Context ctx){
        return  ctx.getSharedPreferences(
                ctx.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }
    public static  boolean isRedirectOn(Context ctx){
       return getPrefs(ctx).getBoolean(DO_REDIRECT,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx=this;
        setContentView(R.layout.activity_main);
        swRedirect=findViewById(R.id.swRedirect);
        swRedirect.setChecked(isRedirectOn(ctx));
        swRedirect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                getPrefs(ctx).edit().putBoolean(DO_REDIRECT,b).commit();
            }
        });

        if (!isRedirection()){
            roleAcquire(RoleManager.ROLE_CALL_REDIRECTION);
        }


    }

//    @Override
//    protected void onSaveInstanceState( Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(DO_REDIRECT, swRedirect.isChecked());
//    }
//
//    @Override
//    protected void onRestoreInstanceState( Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        swRedirect.setChecked(savedInstanceState.getBoolean(DO_REDIRECT,false));
//    }

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
