package org.moviemastery.moviemastery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.moviemastery.moviemastery.R;
import org.moviemastery.moviemastery.UnsafeHttpsClient;
import org.moviemastery.moviemastery.callback.LoginListener;
import org.moviemastery.moviemastery.service.QuizUserApi;
import org.moviemastery.moviemastery.service.ServiceBuilder;
import org.moviemastery.moviemastery.task.LoginTask;

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

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
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
                Toast.makeText(LoginActivity.this, "Failed to login, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
