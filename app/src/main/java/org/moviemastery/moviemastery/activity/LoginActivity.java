package org.moviemastery.moviemastery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.moviemastery.moviemastery.R;
import org.moviemastery.moviemastery.UnsafeHttpsClient;
import org.moviemastery.moviemastery.callback.LoginListener;
import org.moviemastery.moviemastery.service.QuizUserApi;
import org.moviemastery.moviemastery.service.ServiceBuilder;
import org.moviemastery.moviemastery.task.LoginTask;
import org.moviemastery.moviemastery.task.ServletPostAsyncTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import retrofit.client.ApacheClient;


public class LoginActivity extends Activity implements LoginListener{

    public static final String USERNAME = "username";

    private final String CLIENT_ID = "mobile";

    @InjectView(R.id.usernameText)
    EditText usernameText;

    @InjectView(R.id.passwordText)
    EditText passwordText;

    @InjectView(R.id.movieMasteryLabel)
    ImageView logo;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        new ServletPostAsyncTask().execute(new Pair<Context, String>(this, "Flaming-Goose"));
        animateLogo();
    }

    private void animateLogo() {
        ScaleAnimation animation = new ScaleAnimation(1.15f, 1, 1.15f, 1, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
        animation.setDuration(1700);
        logo.startAnimation(animation);
    }

    @DebugLog
    @OnClick(R.id.loginButton)
    public void loginButtonClicked() {
        LoginTask loginTask = new LoginTask(new ApacheClient(UnsafeHttpsClient.createHttpClient()),
                ServiceBuilder.URL + QuizUserApi.TOKEN_PATH, usernameText.getText().toString(), passwordText.getText().toString(), CLIENT_ID, LoginActivity.this);
        loginTask.execute();
    }

    @DebugLog
    @Override
    public void onSuccess(String accessToken) {
        ServiceBuilder.setAccessToken(accessToken);
        Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
        intent.putExtra(USERNAME, usernameText.getText().toString());
        startActivity(intent);
    }

    @DebugLog
    @Override
    public void onError(Exception exception) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(LoginActivity.this, getString(R.string.faile_login_message), Toast.LENGTH_LONG).show();
            }
        });
    }
}
