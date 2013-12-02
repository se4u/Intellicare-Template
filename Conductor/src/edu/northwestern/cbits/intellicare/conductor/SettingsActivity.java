package edu.northwestern.cbits.intellicare.conductor;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity 
{
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setTitle(R.string.title_settings);
		
		this.addPreferencesFromResource(R.layout.activity_settings);
	}
}
