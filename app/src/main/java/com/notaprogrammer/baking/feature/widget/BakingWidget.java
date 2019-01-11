package com.notaprogrammer.baking.feature.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.notaprogrammer.baking.Constant;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.activities.RecipeListActivity;
import com.notaprogrammer.baking.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String widgetRecipe = sharedPreferences.getString(Constant.RECIPE_WIDGET, null);

        Recipe recipe = Recipe.parseJsonObject(widgetRecipe);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, RecipeListActivity.class), 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        // Widgets allow click handlers to only launch pending intents
        if (recipe != null) {
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

            views.setTextViewText(R.id.appwidget_text, recipe.getName());
            // Initialize the list view
            Intent intent = new Intent(context, BakingWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            // Bind the remote adapter
            views.setRemoteAdapter(R.id.appwidget_list, intent);


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_list);

        }else{
            views.setTextViewText(R.id.appwidget_text, context.getString(R.string.widget_default_text));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

