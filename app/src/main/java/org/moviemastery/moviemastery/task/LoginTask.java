package org.moviemastery.moviemastery.task;

import android.os.AsyncTask;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.moviemastery.moviemastery.callback.LoginListener;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.FormUrlEncodedTypedOutput;


public class LoginTask extends AsyncTask<Void, Void, String> {

    private Client client;
    private String tokenIssuingEndpoint;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String accessToken;

    private LoginListener loginListener;

    public LoginTask(Client client, String tokenIssuingEndpoint, String username,
                     String password, String clientId, LoginListener loginListener) {
        this.client = client;
        this.tokenIssuingEndpoint = tokenIssuingEndpoint;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = "";
        this.loginListener = loginListener;
    }

    @DebugLog
    @Override
    protected String doInBackground(Void... voids) {
        try {
            FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
            to.addField("username", username);
            to.addField("password", password);

            // Add the client ID and client secret to the body of the request.
            to.addField("client_id", clientId);
            to.addField("client_secret", clientSecret);

            // Indicate that we're using the OAuth Password Grant Flow
            // by adding grant_type=password to the body
            to.addField("grant_type", "password");

            String base64Auth = BaseEncoding.base64().encode(new String(clientId + ":" + clientSecret).getBytes());
            // Add the basic authorization header
            List<Header> headers = new ArrayList<Header>();
            headers.add(new Header("Authorization", "Basic " + base64Auth));

            // Create the actual password grant request using the data above
            Request req = new Request("POST", tokenIssuingEndpoint, headers, to);

            // Request the password grant.
            Response resp = client.execute(req);

            // Make sure the server responded with 200 OK
            if (resp.getStatus() < 200 || resp.getStatus() > 299) {
                // If not, we probably have bad credentials
                throw new Exception("Login failure: "
                        + resp.getStatus() + " - " + resp.getReason());
            } else {
                // Extract the string body from the response

                String body = IOUtils.toString(resp.getBody().in());

                // Extract the access_token (bearer token) from the response so that we
                // can add it to future requests.
                accessToken = new Gson().fromJson(body, JsonObject.class).get("access_token").getAsString();

            }
        } catch (Exception e) {
            loginListener.onError(e);
        }
        return accessToken;
    }

    @Override
    protected void onPostExecute(String accessToken) {
        if (accessToken != null) {
            loginListener.onSuccess(accessToken);
        }
    }
}