package edu.northwestern.cbits.intellicare.messages;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import edu.northwestern.cbits.intellicare.SequentialPageActivity;

public class HelpActivity extends SequentialPageActivity 
{
	public static final String HELP_COMPLETED = "HELP_COMPLETED";

	public int pagesSequence() 
	{
		return R.array.help_urls;
	}

	public int titlesSequence() 
	{
		return R.array.help_titles;
	}

	public int subtitlesSequence() 
	{
		return R.array.help_subtitles;
	}
	
	public void onSequenceComplete()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = prefs.edit();
		
		e.putBoolean(HelpActivity.HELP_COMPLETED, true);
		
		e.commit();
	}
}
