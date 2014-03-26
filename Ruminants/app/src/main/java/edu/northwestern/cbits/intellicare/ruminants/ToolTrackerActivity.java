package edu.northwestern.cbits.intellicare.ruminants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gwen on 3/20/14.
 */
public class ToolTrackerActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_tracker);
    }


    public static class toolLog {
        public String use;
        public String prompt;
        public int icon;
        public Intent launchIntent;

        public toolLog(String use, String prompt, int icon, Intent launchIntent) {
            this.use = use;
            this.prompt = prompt;
            this.icon = icon;
            this.launchIntent = launchIntent;
        }
    }

    public static String practicePrompt(Context context) {

        String worryPracticePrompt = context.getResources().getStringArray(R.array.practice_prompt)[0];

        return worryPracticePrompt;
    }

    public static String wizardOnePrompt(Context context) {

        String logPrompt = context.getResources().getStringArray(R.array.log_prompt)[0];

        return logPrompt;
    }

    public static String didacticPrompt(Context context) {

        String didacticToolPrompt = context.getResources().getStringArray(R.array.didactic_prompt)[0];

        return didacticToolPrompt;
    }


    protected void onResume() {
        super.onResume();

        final ToolTrackerActivity me = this;

        ArrayList<toolLog> toolLogs = new ArrayList<toolLog>();

        toolLogs.add(new toolLog(this.getString(R.string.wpt_use) + " " + RuminantsContentProvider.WPT_COUNT + " times.", practicePrompt(this), R.drawable.clock_checklist_dark, new Intent(this, WorryPracticeActivity.class)));
        toolLogs.add(new toolLog(this.getResources().getString(R.string.worry_log_use) + " " + RuminantsContentProvider.LOG_COUNT + " times.", wizardOnePrompt(this), R.drawable.clock_checklist_dark, new Intent(this, RuminationLogActivity.class)));
        toolLogs.add(new toolLog(this.getString(R.string.didactic_content_use) + " " + RuminantsContentProvider.DIDACTIC_COUNT + " times.", didacticPrompt(this), R.drawable.clock_checklist_dark, new Intent(this, DidacticActivity.class)));

        ListView toolList = (ListView) this.findViewById(R.id.tool_use_log);

        final ArrayAdapter<toolLog> adapter = new ArrayAdapter<toolLog>(this, R.layout.row_use_log, toolLogs) {
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    convertView = inflater.inflate(R.layout.row_use_log, parent, false);
                }

                TextView use = (TextView) convertView.findViewById(R.id.tool_use);
                TextView prompt = (TextView) convertView.findViewById(R.id.tool_prompt);

                final toolLog t = this.getItem(position);

                use.setText(t.use);
                prompt.setText(t.prompt);

                ImageView icon = (ImageView) convertView.findViewById(R.id.tool_icon);

                if (t.icon != 0) {
                    icon.setImageDrawable(me.getResources().getDrawable(t.icon));

                    icon.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (t.launchIntent != null)
                                me.startActivity(t.launchIntent);
                        }
                    });
                }
                return convertView;
            }
        };

        toolList.setAdapter(adapter);
    }

    protected void onPause()
    {
        super.onPause();
    }

}
