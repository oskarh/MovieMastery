package org.moviemastery.moviemastery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.moviemastery.moviemastery.R;
import org.moviemastery.moviemastery.callback.GameFetcherListener;
import org.moviemastery.moviemastery.repository.Game;
import org.moviemastery.moviemastery.service.ServiceBuilder;
import org.moviemastery.moviemastery.task.GameFetcherTask;
import org.moviemastery.moviemastery.ui.GameViewHolder;

import java.util.Collection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import uk.co.ribot.easyadapter.EasyAdapter;

public class OverviewActivity extends Activity implements GameFetcherListener {

    @InjectView(R.id.previousGamesList)
    ListView previousGamesList;

    @InjectView(R.id.nameLabel)
    TextView nameLabel;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.inject(this);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String username = getIntent().getExtras().getString(LoginActivity.USERNAME);
            nameLabel.setText(username);
        }
        GameFetcherTask task = new GameFetcherTask(ServiceBuilder.getQuizUserApi(), this);
        task.execute();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @DebugLog
    @OnClick(R.id.startGameButton)
    public void startNewGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(LoginActivity.USERNAME, nameLabel.getText());
        startActivity(intent);
    }

    @DebugLog
    @OnClick(R.id.previousGamesButton)
    public void showPreviousGames(View view) {
        startActivity(new Intent(this, PreviousGamesActivity.class));
    }

    @DebugLog
    @OnClick(R.id.leaderboardButton)
    public void showLeaderboard(View view) {
        startActivity(new Intent(this, LeaderboardActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            ServiceBuilder.setAccessToken(null);
            Intent intent = new Intent(this, LoginActivity.class);
            // Clear back stack so user can't go back to overview activity by using back button after logging out
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    public void onGamesReceived(Collection<Game> games) {
        previousGamesList.setAdapter(new EasyAdapter<Game>(this, GameViewHolder.class, (java.util.List<Game>) games));
    }
}
