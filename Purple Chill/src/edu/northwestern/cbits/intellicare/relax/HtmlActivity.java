package edu.northwestern.cbits.intellicare.relax;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import edu.northwestern.cbits.intellicare.ConsentedActivity;
import edu.northwestern.cbits.intellicare.logging.LogManager;

public class HtmlActivity extends ConsentedActivity 
{
	protected static final String FILENAME = "filename";
	
	private static final String PMR_HTML = "pmr_tool.html";
	private static final String DOT_HTML = "dot_tool.html";
	
	private int _titleId = -1;
	private int _subtitleId = -1;
	private int _messageId = -1;
	private long _length = 0;

    private Thread _timerThread = null;

	private String _filename = null;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_html_content);
		
		this._filename  = this.getIntent().getStringExtra(HtmlActivity.FILENAME);
		
		if (PMR_HTML.equals(this._filename))
		{
			this._titleId  = R.string.title_pmr_exercise;
			this._subtitleId = R.string.subtitle_pmr_exercise;
			this._messageId = R.string.message_pmr_exercise;
			this._length = 600000;
		}
		else if (DOT_HTML.equals(this._filename))
		{
			this._titleId  = R.string.title_dot_exercise;
			this._subtitleId = R.string.subtitle_dot_exercise;
			this._messageId = R.string.message_dot_exercise;
			this._length = 100000;
		}
		
		if (this._titleId != -1)
		{
			this.getSupportActionBar().setTitle(this._titleId);
			this.getSupportActionBar().setSubtitle(this._subtitleId);
		}
		else
			this.finish();
	}
	
	public void onResume()
	{
		super.onResume();
		
		final HtmlActivity me = this;

        AudioFileManager audio = AudioFileManager.getInstance(this);

        if (audio.isPlaying() == true){
                audio.pause();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle(R.string.title_instructions);
		
		builder.setMessage(this._messageId);
		
		builder.setPositiveButton(R.string.action_continue, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				WebView webView = (WebView) me.findViewById(R.id.web_view);

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
				
				webView.loadUrl("file:///android_asset/www/" + me._filename);
				
				Runnable r = new Runnable()
				{
					public void run() 
					{
						try 
						{
							Thread.sleep(me._length);

                            me.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    me.fetchStress();
                                }
                            });
						}
						catch (InterruptedException e) 
						{

						}

                        me._timerThread = null;
					}
				};
				
				me._timerThread = new Thread(r);
                me._timerThread.start();
			}
		});
		
		builder.create().show();
	}

    protected void onPause()
    {
        super.onPause();

        if (this._timerThread != null)
            this._timerThread.interrupt();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.getMenuInflater().inflate(R.menu.menu_player, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_track_info)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder = builder.setTitle(R.string.title_instructions);
            builder = builder.setMessage(this._messageId);

            builder.create().show();
        }
        else if (item.getItemId() == android.R.id.home)
        {
            if (this.isTaskRoot())
            {
                Intent intent = new Intent(this, IndexActivity.class);
                this.startActivity(intent);
            }

            this.finish();
        }

        return true;
    }
	
	private void fetchStress()
	{
		final HtmlActivity me = this;

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View body = inflater.inflate(R.layout.view_stress_rating, null);
        
        final TextView ratingNumber = (TextView) body.findViewById(R.id.rating_number);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder = builder.setTitle(R.string.title_rate_stress);
		builder = builder.setPositiveButton(R.string.button_continue, new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1) 
			{
				try
				{
					int stressLevel = Integer.parseInt(ratingNumber.getText().toString());
					
            		HashMap<String,Object> payload = new HashMap<String, Object>();
            		payload.put(GroupActivity.STRESS_RATING, stressLevel);
            		payload.put(GroupActivity.TRACK_END, true);
            		LogManager.getInstance(me).log("rated_stress", payload);
            		
            		me.finish();
				}
				catch (NumberFormatException e)
				{
					Toast.makeText(me, R.string.toast_rate_stress, Toast.LENGTH_LONG).show();
					
					me.fetchStress();
				}
			}
		});

        final SeekBar ratingBar = (SeekBar) body.findViewById(R.id.stress_rating);
        
        ratingBar.setMax(9);
        
        ratingBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
                {
                    progress += 1;
                    
                    ratingNumber.setText("" + progress);
                }

                public void onStartTrackingTouch(SeekBar seekBar) 
                {

                }

                public void onStopTrackingTouch(SeekBar seekBar) 
                {

                }
        });

        builder = builder.setView(body);
		
		builder.create().show();
	}
}
