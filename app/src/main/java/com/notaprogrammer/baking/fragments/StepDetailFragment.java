package com.notaprogrammer.baking.fragments;

import android.app.Activity;
import android.content.Context;
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

import com.google.android.exoplayer2.C;
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
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.activities.StepDetailActivity;
import com.notaprogrammer.baking.model.Recipe;
import com.notaprogrammer.baking.utils.NetworkUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.notaprogrammer.baking.utils.StringUtils.getUTF8DecodedDescription;

public class StepDetailFragment extends Fragment {

    public static final String ARG_SELECTED_ITEM = "ARG_SELECTED_ITEM";

    @BindView(R.id.exo_player_view) PlayerView playerView;
    @BindView(R.id.tv_step_description) TextView textViewDescription;

    ExoPlayer exoPlayer;
    Recipe.Step step;

    public StepDetailFragment() { }

    public static Bundle stepDetailBundle(Recipe.Step step){
        Bundle arguments = new Bundle();
        arguments.putString(ARG_SELECTED_ITEM, step.toJsonString());
        return arguments;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey( ARG_SELECTED_ITEM )) {

            step = Recipe.Step.parseJSON(getArguments().getString( ARG_SELECTED_ITEM ));

            Activity activity = getActivity();

            //update action bar if the parent activity is StepDetailActivity
            if(activity instanceof StepDetailActivity){

                StepDetailActivity stepDetailActivity = (StepDetailActivity) getActivity();

                ActionBar actionBar = null;
                if (stepDetailActivity != null) {
                    actionBar = stepDetailActivity.getSupportActionBar();
                }

                if (actionBar != null) {
                    actionBar.setTitle(step.getShortDescription());
                }

            }

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (step != null) {
            String description = getUTF8DecodedDescription(step.getDescription());
            updateDescriptionTextView(description);
        }

        return rootView;
    }

    private void updateDescriptionTextView(String decodedDescription){
        textViewDescription.setText(decodedDescription);
    }

    private void initializePlayer(Context context, Recipe.Step step) {

        if(TextUtils.isEmpty( step.getVideoUrl() )){

            playerView.setVisibility(View.GONE);

        }else{

            if(exoPlayer == null && NetworkUtils.isNetworkAvailable(context)){
                loadVideoToExoPlayerView(context, step);
            }
        }
    }

    private void loadVideoToExoPlayerView(Context context, Recipe.Step step){
        Uri mediaUri = Uri.parse(step.getVideoUrl());

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory());

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);


        playerView.setPlayer(exoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

        if (position != C.TIME_UNSET){
            exoPlayer.seekTo(position);
        }

        playerView.setVisibility(View.VISIBLE);
    }

    private void releasePlayer(){
        if(exoPlayer!=null){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initializePlayer(Objects.requireNonNull(this.getActivity()).getApplicationContext(),  step);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer(Objects.requireNonNull(this.getActivity()).getApplicationContext(),  step);
        }
    }

    long position = 0;


    @Override
    public void onPause() {
        super.onPause();

        if(exoPlayer!=null){
            position = exoPlayer.getCurrentPosition();
        }

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
