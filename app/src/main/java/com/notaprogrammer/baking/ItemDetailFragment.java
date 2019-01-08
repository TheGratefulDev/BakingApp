package com.notaprogrammer.baking;

import android.app.Activity;
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

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.notaprogrammer.baking.model.Recipe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_SELECTED_ITEM = "ARG_SELECTED_ITEM";

    Recipe.Step step;

    public ItemDetailFragment() { }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_SELECTED_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            step = Recipe.Step.parseJSON(getArguments().getString(ARG_SELECTED_ITEM));

            Activity activity = getActivity();
            if(activity instanceof  ItemDetailActivity){
                ItemDetailActivity itemDetailActivity = (ItemDetailActivity) getActivity();
                ActionBar actionBar = null;
                if (itemDetailActivity != null) {
                    actionBar = itemDetailActivity.getSupportActionBar();
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

        initalizePlayer(step);

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


    private void initalizePlayer(Recipe.Step step) {

        if(TextUtils.isEmpty(step.getVideoUrl())){
            playerView.setVisibility(View.GONE);
        }else{
            if(exoPlayer == null){

                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                exoPlayer = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(this.getActivity()).getApplicationContext(), trackSelector, loadControl);
                playerView.setPlayer(exoPlayer);

                String userAgent = Util.getUserAgent(this.getActivity().getApplicationContext(), this.getActivity().getPackageName());

                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(step.getVideoUrl()), new DefaultDataSourceFactory(this.getActivity().getApplicationContext(), userAgent), new DefaultExtractorsFactory(), null, null);

                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);

            }
            playerView.setVisibility(View.VISIBLE);
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
