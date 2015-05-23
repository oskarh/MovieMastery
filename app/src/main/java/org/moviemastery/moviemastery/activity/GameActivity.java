package org.moviemastery.moviemastery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import org.moviemastery.moviemastery.R;
import org.moviemastery.moviemastery.callback.CountDownListener;
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
import org.moviemastery.moviemastery.ui.QuestionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

public class GameActivity extends Activity implements QuestionFetcherListener, CreateGameListener, CountDownListener {

    public static final int QUESTION_COUNT = 4;

    List<BootstrapButton> answerList;

    @InjectView(R.id.scoreLabel)
    TextView scoreLabel;

    @InjectView(R.id.floatingResult)
    TextView floatingResult;

    private List<FontAwesomeText> heartList;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    FontAwesomeText fiftyFiftyLifeLine;

    FontAwesomeText timeExtensionLifeLine;

    FontAwesomeText skipQuestionLifeLine;

    private BootstrapButton nextQuestionButton;

    private List<QuestionSet> questions;

    private Game game;

    private int currentQuestionIndex;

    private MyCountDownTimer myCountDownTimer;

    private Animation fadeOut = new AlphaAnimation(1, 0);

    private CountDownLatch latch = new CountDownLatch(2);

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QuestionSetFetchTask task = new QuestionSetFetchTask(ServiceBuilder.getQuestionSetApi(), this);
        task.execute();

        if (game == null) {
            CreateGameTask gameTask = new CreateGameTask(ServiceBuilder.getGameServiceApi(), this);
            gameTask.execute();
        }
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        nextQuestionButton = (BootstrapButton) findViewById(R.id.nextQuestionButton);
        currentQuestionIndex = -1;
        heartList = new ArrayList<FontAwesomeText>();
        heartList.add((FontAwesomeText) findViewById(R.id.heart1));
        heartList.add((FontAwesomeText) findViewById(R.id.heart2));
        heartList.add((FontAwesomeText) findViewById(R.id.heart3));
        fadeOut.setDuration(800);
        fadeOut.setFillAfter(true);
        answerList = new ArrayList<BootstrapButton>(QUESTION_COUNT);
        answerList.add((BootstrapButton) findViewById(R.id.alternative1Button));
        answerList.add((BootstrapButton) findViewById(R.id.alternative2Button));
        answerList.add((BootstrapButton) findViewById(R.id.alternative3Button));
        answerList.add((BootstrapButton) findViewById(R.id.alternative4Button));
        fiftyFiftyLifeLine = (FontAwesomeText) findViewById(R.id.fiftyFiftyLifeLine);
        timeExtensionLifeLine = (FontAwesomeText) findViewById(R.id.timeExtensionLifeLine);
        skipQuestionLifeLine = (FontAwesomeText) findViewById(R.id.skipQuestionLifeLine);
        myCountDownTimer = new MyCountDownTimer(this);
    }

    @DebugLog
    @OnClick({R.id.alternative1Button, R.id.alternative2Button, R.id.alternative3Button, R.id.alternative4Button})
    void handleLabelClick(BootstrapButton view) {
        // ignore buttonclick if the question has already been answered
        inactivateButtons();
        myCountDownTimer.cancel();
        questions.get(currentQuestionIndex).setUserAnswer(answerList.indexOf(view));
        String selectedButtonType;
        int selectedIndex = answerList.indexOf(view);
        if(isCorrectAnswer(selectedIndex)) {
            playFloatingAnimation(QuestionResult.CORRECT);
            selectedButtonType = "success";
            scoreLabel.setText(String.valueOf(game.increaseScore()));
            playSound(SoundPlayerService.CHEERING);
        } else {
            selectedButtonType = "danger";
            updateGameState();
        }
        nextQuestionButton.setVisibility(View.VISIBLE);
        hideViews(progressBar);
        view.setBootstrapType(selectedButtonType);
//        ((BootstrapButton) view).setBootstrapType(selectedButtonType);
    }

    private void playFloatingAnimation(QuestionResult result) {
        switch (result) {
            case CORRECT:
                floatingResult.setTextColor(getResources().getColor(R.color.green));
                floatingResult.setText(R.string.correct_answer);
                playFloatingAnimation();
                break;
            case TIME_UP:
                floatingResult.setTextColor(getResources().getColor(R.color.red));
                floatingResult.setText(R.string.time_up_answer);
                playFloatingAnimation();
                break;
        }
    }

    private void playFloatingAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(1500);
        floatingResult.startAnimation(animation);
    }

    private void updateGameState() {
        answerList.get(questions.get(currentQuestionIndex).getCorrectAnswerIndex()).setBootstrapType("success");
        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_out);
        FontAwesomeText heartToLose = heartList.get(game.increaseIncorrectAnswer());
        heartToLose.startAnimation(blinkAnimation);

        if (game.isGameOver()) {
            Log.w("TAG", "Game finished " + game);
            QuizUserApi userApi = ServiceBuilder.getQuizUserApi();
            PostGameTask gameTask = new PostGameTask(userApi, game);
            gameTask.execute();
            nextQuestionButton.setText(getString(R.string.main_menu));
            playSound(SoundPlayerService.GAME_OVER);
        } else {
            playSound(SoundPlayerService.AW);
        }
    }

    private void inactivateButtons() {
        for (BootstrapButton button : answerList) {
            button.setClickable(false);
        }
        fiftyFiftyLifeLine.setClickable(false);
        timeExtensionLifeLine.setClickable(false);
        skipQuestionLifeLine.setClickable(false);
    }

    private void activateButtons() {
        for (BootstrapButton button : answerList) {
            button.setClickable(true);
            button.setVisibility(View.VISIBLE);
        }
        fiftyFiftyLifeLine.setClickable(true);
        timeExtensionLifeLine.setClickable(true);
        skipQuestionLifeLine.setClickable(true);
    }

    private boolean isCorrectAnswer(int index) {
        return questions.get(currentQuestionIndex).getCorrectAnswerIndex() == index;
    }

    private void playSound(int soundType) {
        Intent soundIntent = new Intent(this, SoundPlayerService.class);
        soundIntent.putExtra(SoundPlayerService.SOUND, soundType);
        startService(soundIntent);
    }

    @DebugLog
    private void updateQuestion() {
        currentQuestionIndex++;
        activateButtons();
        game.addQuestion(questions.get(currentQuestionIndex));
        hideViews(nextQuestionButton);
        progressBar.setVisibility(View.VISIBLE);
        updateQuestionButtons();
        progressBar.setProgress(100);
        myCountDownTimer.reset();
    }

    private void updateQuestionButtons() {
//        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        QuestionSet currentQuestion = this.questions.get(currentQuestionIndex);
        for (int i = 0; i < answerList.size(); i++) {
            BootstrapButton answerView = answerList.get(i);
            answerView.setText(currentQuestion.getAlternatives().get(i));
            answerView.setBootstrapType("primary");
        }
    }

    @DebugLog
    private void hideViews(View... views) {
        for(View view : views) {
            view.setVisibility(View.INVISIBLE);
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
        for (BootstrapButton answer : answerList) {
            answer.setEnabled(true);
        }
        latch.countDown();
    }

    @DebugLog
    @OnClick(R.id.fiftyFiftyLifeLine)
    public void fiftyFiftyLifeLineClicked(View view) {
        List<Integer> questionAlts = getIncorrectQuestionsShuffled();
        answerList.get(questionAlts.get(0)).setVisibility(View.INVISIBLE);
        answerList.get(questionAlts.get(0)).setClickable(false);
        answerList.get(questionAlts.get(1)).setVisibility(View.INVISIBLE);
        answerList.get(questionAlts.get(1)).setClickable(false);
        fiftyFiftyLifeLine.setVisibility(View.INVISIBLE);
    }

    private List<Integer> getIncorrectQuestionsShuffled() {
        List<Integer> questionAlts = new ArrayList<Integer>(QUESTION_COUNT);
        for (int i = 0; i < QUESTION_COUNT; i++) {
            questionAlts.add(i);
        }
        questionAlts.remove(questions.get(currentQuestionIndex).getCorrectAnswerIndex());
        Collections.shuffle(questionAlts);
        return questionAlts;
    }

    @DebugLog
    @OnClick(R.id.timeExtensionLifeLine)
    public void timeExtensionLifeLineClicked(View view) {
        timeExtensionLifeLine.setVisibility(View.INVISIBLE);
        myCountDownTimer.reset();
    }

    @DebugLog
    @OnClick(R.id.skipQuestionLifeLine)
    public void skipQuestionLifeLineClicked(View view) {
        skipQuestionLifeLine.setVisibility(View.INVISIBLE);
        questions.get(currentQuestionIndex).setUserAnswer(QuestionSet.SKIPPED_QUESTION);
        updateQuestion();
    }

    @DebugLog
    @OnClick(R.id.nextQuestionButton)
    public void nextQuestionClicked(View view) {
        if(!game.isGameOver()) {
            updateQuestion();
        } else {
            finish();
//            Intent intent = new Intent(this, OverviewActivity.class);
//            Intent startedIntent = getIntent();
//            if (startedIntent != null && startedIntent.getExtras() != null) {
//                intent.putExtra(LoginActivity.USERNAME, startedIntent.getExtras().getString(LoginActivity.USERNAME));
//            }
//            startActivity(intent);
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
    public void onTick(int percentLeft) {
        progressBar.setProgress(percentLeft);
    }

    @Override
    public void onFinished() {
        playFloatingAnimation(QuestionResult.TIME_UP);
        progressBar.setVisibility(View.INVISIBLE);
        questions.get(currentQuestionIndex).setUserAnswer(QuestionSet.TIMED_OUT);
        inactivateButtons();
        updateGameState();
        nextQuestionButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // pause myCountdownTimer here
    }
}
