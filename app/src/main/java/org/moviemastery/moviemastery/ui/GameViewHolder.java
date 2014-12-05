package org.moviemastery.moviemastery.ui;

import android.view.View;
import android.widget.TextView;

import org.moviemastery.moviemastery.R;
import org.moviemastery.moviemastery.repository.Game;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

@LayoutId(R.layout.game_layout)
public class GameViewHolder extends ItemViewHolder<Game> {

    @ViewId(R.id.game_id)
    TextView gameId;

    @ViewId(R.id.game_score)
    TextView gameScore;

    public GameViewHolder(View view) {
        super(view);
    }

    @Override
    public void onSetValues(Game game, PositionInfo positionInfo) {
        gameId.setText("Game id: " + game.getId());
        gameScore.setText("Score: " + game.getScore());
    }
}
