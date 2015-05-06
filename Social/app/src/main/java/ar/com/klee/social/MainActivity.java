package ar.com.klee.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;

import java.security.MessageDigest;
import java.util.Arrays;


public class MainActivity extends Activity {

    private CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        showHashKey(getApplicationContext());
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        setContentView(R.layout.publish_text);

                        Button publishButton = (Button)findViewById(R.id.publish);

                        publishButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView publishText = (TextView)findViewById(R.id.publishText);
                                final String textToPublish = publishText.getText().toString();
                                if(!textToPublish.isEmpty()) {
                                    ShareApi.share(new ShareLinkContent.Builder().setRef(textToPublish).setContentTitle("Google").setContentUrl(Uri.parse("http://google.com")).build(), null);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "ar.com.klee.social", PackageManager.GET_SIGNATURES); //Your package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.v("Exception: ", e.getMessage(), e);
        }
    }
}
