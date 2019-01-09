package com.notaprogrammer.baking;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.notaprogrammer.baking.model.Recipe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

public class DetailFragment extends Fragment {

    public static final String ARG_SELECTED_ITEM = "ARG_SELECTED_ITEM";

    Recipe.Step step;

    public DetailFragment() { }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_SELECTED_ITEM)) {

            step = Recipe.Step.parseJSON(getArguments().getString(ARG_SELECTED_ITEM));

            Activity activity = getActivity();
            if(activity instanceof DetailActivity){
                DetailActivity detailActivity = (DetailActivity) getActivity();
                ActionBar actionBar = null;
                if (detailActivity != null) {
                    actionBar = detailActivity.getSupportActionBar();
                }

                if (actionBar != null) {
                    actionBar.setTitle(step.getShortDescription());
                }
            }

        }
    }

    PlayerView playerView;
    ExoPlayer exoPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        playerView = rootView.findViewById(R.id.exo_player);
        playerView.setVisibility(View.GONE);

        initializePlayer(Objects.requireNonNull(this.getActivity()).getApplicationContext(),  step);

        if (step != null) {

            String word = "";
            try {
                 word = URLDecoder.decode(step.getDescription(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            ((TextView) rootView.findViewById(R.id.tv_item_detail)).setText(word);
        }

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }

    }


    private void initializePlayer(Context context, Recipe.Step step) {

        if(TextUtils.isEmpty(step.getVideoUrl())){

            playerView.setVisibility(View.GONE);

        }else{

            if(exoPlayer == null){
                Uri mediaUri = Uri.parse(step.getVideoUrl());

                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory());

                exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

                playerView.setPlayer(exoPlayer);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);

                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
                playerView.setVisibility(View.VISIBLE);
            }

        }
    }

    private void releasePlayer(){
        if(exoPlayer!=null){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }
}
