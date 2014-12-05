package org.moviemastery.moviemastery.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import org.moviemastery.moviemastery.R;
import org.moviemastery.moviemastery.callback.CreateGameListener;
import org.moviemastery.moviemastery.callback.QuestionFetcherListener;
import org.moviemastery.moviemastery.repository.Game;
import org.moviemastery.moviemastery.repository.QuestionSet;
import org.moviemastery.moviemastery.service.QuizUserApi;
import org.moviemastery.moviemastery.service.ServiceBuilder;
import org.moviemastery.moviemastery.service.SoundPlayerService;
import org.moviemastery.moviemastery.task.CreateGameTask;
import org.moviemastery.moviemastery.task.PostGameTask;
import org.moviemastery.moviemastery.task.QuestionSetFetchTask;
import org.moviemastery.moviemastery.task.UpdateRatingTask;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;

public class GameActivity extends Activity implements QuestionFetcherListener, CreateGameListener {

    public static final int backgroundColor = 0xFFF1F1F1;

    @InjectView(R.id.movie1Label)
    TextView movie1;

    @InjectView(R.id.movie2Label)
    TextView movie2;

    @InjectView(R.id.movie3Label)
    TextView movie3;

    @InjectView(R.id.movie4Label)
    TextView movie4;

    @InjectView(R.id.explanationLabel)
    TextView explanationLabel;

    @InjectView(R.id.scoreLabel)
    TextView scoreLabel;

    @InjectView(R.id.resultLabel)
    TextView resultLabel;

    FontAwesomeText heart1;

    FontAwesomeText heart2;

    FontAwesomeText heart3;

    FontAwesomeText likeButton;

    BootstrapButton nextQuestionButton;

    FontAwesomeText dislikeButton;

    private List<QuestionSet> questions;

    private Game game;

    private int currentQuestionIndex;

    private boolean isQuestionAnswered = false;

    private Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);

    private CountDownLatch latch = new CountDownLatch(2);

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        nextQuestionButton = (BootstrapButton) findViewById(R.id.nextQuestionButton);
        likeButton = (FontAwesomeText) findViewById(R.id.likeButton);
        dislikeButton = (FontAwesomeText) findViewById(R.id.dislikeButton);
        heart1 = (FontAwesomeText) findViewById(R.id.heart1);
        heart2 = (FontAwesomeText) findViewById(R.id.heart2);
        heart3 = (FontAwesomeText) findViewById(R.id.heart3);
        fadeOut.setDuration(1600);
        QuestionSetFetchTask task = new QuestionSetFetchTask(ServiceBuilder.getQuestionSetApi(), this);
        task.execute();

        if (game == null) {
            CreateGameTask gameTask = new CreateGameTask(ServiceBuilder.getGameServiceApi(), this);
            gameTask.execute();
        }
    }

    @DebugLog
    @OnClick(R.id.movie1Label)
    public void movie1Click(View view) {
        handleLabelClick(view);
    }

    @DebugLog
    @OnClick(R.id.movie2Label)
    public void movie2Click(View view) {
        handleLabelClick(view);
    }

    @DebugLog
    @OnClick(R.id.movie3Label)
    public void movie3Click(View view) {
        handleLabelClick(view);
    }

    @DebugLog
    @OnClick(R.id.movie4Label)
    public void movie4Click(View view) {
        handleLabelClick(view);
    }

    private void handleLabelClick(View view) {
        // ignore buttonclick if the question has already been answered
        if(isQuestionAnswered) {
            return;
        }
        isQuestionAnswered = true;

        int selectedTextColor;
        int selectedIndex = getAnswerIndex(view);
        if(isCorrectAnswer(selectedIndex)) {
            resultLabel.setText("Correct!");
            selectedTextColor = Color.GREEN;
            scoreLabel.setText(String.valueOf(game.increaseScore()));
            playSound(SoundPlayerService.CHEERING);
        } else {
            resultLabel.setText("Incorrect!");
            selectedTextColor = Color.RED;
            getAnswerIndexToView(questions.get(currentQuestionIndex).getCorrectAnswerIndex()).setBackgroundColor(Color.GREEN);
            updateIncorrectAnswers(game.increaseIncorrectAnswer());
            if (game.isGameOver()) {
                QuizUserApi userApi = ServiceBuilder.getQuizUserApi();
                PostGameTask gameTask = new PostGameTask(userApi, game);
                gameTask.execute();
                nextQuestionButton.setText("Main Menu");
                playSound(SoundPlayerService.GAME_OVER);
            } else {
                playSound(SoundPlayerService.AW);
            }

        }
        explanationLabel.setText(questions.get(currentQuestionIndex).getExplanation());
        showViews(likeButton, dislikeButton, resultLabel, explanationLabel, nextQuestionButton);
        resultLabel.setTextSize(22);
        resultLabel.setTypeface(null, Typeface.BOLD);
        view.setBackgroundColor(selectedTextColor);
        likeButton.setEnabled(true);
        dislikeButton.setEnabled(true);
    }

    private boolean isCorrectAnswer(int index) {
        return questions.get(currentQuestionIndex).getCorrectAnswerIndex() == index;
    }

    private void playSound(int soundType) {
        Intent soundIntent = new Intent(this, SoundPlayerService.class);
        soundIntent.putExtra(SoundPlayerService.SOUND, soundType);
        startService(soundIntent);
    }

    private void updateIncorrectAnswers(int incorrectAnswerIndex) {
        switch (incorrectAnswerIndex) {
            case 1:
                startFlashing(heart3, 3);
                break;
            case 2:
                startFlashing(heart2, 3);
                break;
            case 3:
                startFlashing(heart1, 3);
                break;
        }
    }

    @DebugLog
    private void updateQuestion() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1600);
        QuestionSet currentQuestion = this.questions.get(currentQuestionIndex);
        isQuestionAnswered = false;
        game.addQuestion(questions.get(currentQuestionIndex));
        hideViews(likeButton, dislikeButton, resultLabel, explanationLabel, nextQuestionButton);
        movie1.setText(currentQuestion.getAlternatives().get(0));
        movie1.setBackgroundColor(backgroundColor);
        movie1.startAnimation(fadeIn);
        movie2.setText(currentQuestion.getAlternatives().get(1));
        movie2.setBackgroundColor(backgroundColor);
        movie2.startAnimation(fadeIn);
        movie3.setText(currentQuestion.getAlternatives().get(2));
        movie3.setBackgroundColor(backgroundColor);
        movie3.startAnimation(fadeIn);
        movie4.setText(currentQuestion.getAlternatives().get(3));
        movie4.setBackgroundColor(backgroundColor);
        movie4.startAnimation(fadeIn);
    }

    @DebugLog
    private void hideViews(View... views) {
        for(View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @DebugLog
    private void showViews(View... views) {
        for(View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    public void onQuestionsReceived(Collection<QuestionSet> questions) {
        currentQuestionIndex = 0;
        this.questions = (List<QuestionSet>) questions;
        movie1.setEnabled(true);
        movie2.setEnabled(true);
        movie3.setEnabled(true);
        movie4.setEnabled(true);
        latch.countDown();
    }

    @DebugLog
    @OnClick(R.id.likeButton)
    public void likeButtonClicked(View view) {
        Crouton.makeText(this, "Question set liked", Style.CONFIRM).show();
        questions.get(currentQuestionIndex).increaseRating();
        likeButton.setEnabled(false);
        dislikeButton.setEnabled(false);
        new UpdateRatingTask(ServiceBuilder.getQuestionSetTemplateApi(), questions.get(currentQuestionIndex).getQuestionSetTemplateId(), true).execute();
    }

    @DebugLog
    @OnClick(R.id.dislikeButton)
    public void dislikeButtonClicked(View view) {
        Crouton.makeText(this, "Question set disliked", Style.CONFIRM).show();
        questions.get(currentQuestionIndex).decreaseRating();
        likeButton.setEnabled(false);
        dislikeButton.setEnabled(false);
        new UpdateRatingTask(ServiceBuilder.getQuestionSetTemplateApi(), questions.get(currentQuestionIndex).getQuestionSetTemplateId(), false).execute();
    }

    @DebugLog
    @OnClick(R.id.nextQuestionButton)
    public void nextQuestionClicked(View view) {
        if(!game.isGameOver()) {
            currentQuestionIndex++;
            updateQuestion();
        } else {
            Intent intent = new Intent(this, OverviewActivity.class);
            Intent startedIntent = getIntent();
            if (startedIntent != null && startedIntent.getExtras() != null) {
                intent.putExtra(LoginActivity.USERNAME, startedIntent.getExtras().getString(LoginActivity.USERNAME));
            }
            startActivity(intent);
        }
    }

    private int getAnswerIndex(View view) {
        if(view == movie1) {
            return 0;
        } else if(view == movie2) {
            return 1;
        } else if(view == movie3) {
            return 2;
        } else if(view == movie4) {
            return 3;
        } else {
            return -1;
        }
    }

    private View getAnswerIndexToView(int index) {
        switch(index) {
            case 0:
                return movie1;
            case 1:
                return movie2;
            case 2:
                return movie3;
            case 3:
                return movie4;
            default:
                return null;
        }
    }

    @DebugLog
    @Override
    public void onGameReceived(Game game) {
        this.game = game;
        latch.countDown();
        try {
            // wait for questions to be downloaded before calling updateQuestion
            latch.await();
            updateQuestion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    @DebugLog
    public void startFlashing(final View view, int repeatCount)
    {
        final Animation fadeIn = new AlphaAnimation(0, 1);

        //set up extra variables
        fadeIn.setDuration(200);
        fadeIn.setRepeatMode(Animation.REVERSE);

        //default repeat count is 0, however if user wants, set it up to be infinite
        fadeIn.setRepeatCount(repeatCount);

        fadeIn.setStartOffset(200);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(200);
        fadeOut.setStartOffset(200);

        //set the new animation to a final animation
        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(fadeOut);
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fadeIn);
    }
}
