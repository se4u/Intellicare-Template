package edu.northwestern.cbits.intellicare.aspire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import edu.northwestern.cbits.intellicare.ConsentedActivity;
import edu.northwestern.cbits.intellicare.logging.LogManager;

public class GraphActivity extends ConsentedActivity {
    public static final Uri URI = Uri.parse("intellicare://aspire/graph");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_graph);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void onResume() {
        super.onResume();

        WebView graphView = (WebView) this.findViewById(R.id.graph_web_view);
        graphView.getSettings().setJavaScriptEnabled(true);

        graphView.loadDataWithBaseURL("file:///android_asset/", GraphActivity.generateGraph(this), "text/html", null, null);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(R.string.title_graph);

        try {
            JSONObject data = GraphActivity.graphValues(this);

            Log.e("Aspire ", data.toString(2));

            if (data.length() == 1)
                actionBar.setSubtitle(R.string.subtitle_graph_single);
            else
                actionBar.setSubtitle(this.getString(R.string.subtitle_graph, data.length()));
        } catch (JSONException e) {
            LogManager.getInstance(this).logException(e);
        }
    }
	
	private static String generateGraph(Context context) 
	{
	    StringBuilder buffer = new StringBuilder();
	    
		try 
		{
		    InputStream html = context.getAssets().open("home_graph.html");

		    BufferedReader in = new BufferedReader(new InputStreamReader(html));

		    String str = null;

		    while ((str = in.readLine()) != null) 
		    {
		    	buffer.append(str);
		    	buffer.append(System.getProperty("line.separator"));
		    }

		    in.close();
		} 
		catch (IOException e) 
		{
			LogManager.getInstance(context).logException(e);
		}

		String graphString = buffer.toString();

		
		try 
		{
			JSONObject graphValues = GraphActivity.graphValues(context);

			graphString = graphString.replaceAll("VALUES_JSON", graphValues.toString());


		}
		catch (JSONException e) 
		{
			LogManager.getInstance(context).logException(e);
		}

/*		try 
		{
			graphString = graphString.replaceAll("VALUES_JSON", graphValues.toString());

			FileUtils.writeStringToFile(new File(Environment.getExternalStorageDirectory(), "graph.html"), graphString);
		} 
		catch (IOException e) 
		{
			LogManager.getInstance(context).logException(e);
		} 
		catch (JSONException e) 
		{
			LogManager.getInstance(context).logException(e);
		}
*/
		
		return graphString;
	}
	
	private static JSONObject graphValues(Context context) throws JSONException
	{
		ArrayList<String> cards = new ArrayList<String>();
		ArrayList<Long> cardIds = new ArrayList<Long>();
		
		Cursor c = context.getContentResolver().query(AspireContentProvider.ASPIRE_PATH_URI, null, null, null, null);
		
		while (c.moveToNext())
		{
			String where = AspireContentProvider.ID + " = ? AND " + AspireContentProvider.CARD_ENABLED + " != 0";
			String[] args = {"" + c.getLong(c.getColumnIndex(AspireContentProvider.PATH_CARD_ID)) };
			
			Cursor cardCursor = context.getContentResolver().query(AspireContentProvider.ASPIRE_CARD_URI, null, where, args, null);
			
			if (cardCursor.moveToNext())
			{
				String cardName = cardCursor.getString(cardCursor.getColumnIndex(AspireContentProvider.CARD_NAME));
				
				if (cards.contains(cardName) == false)
				{
					cards.add(cardName);
					cardIds.add(cardCursor.getLong(cardCursor.getColumnIndex(AspireContentProvider.ID)));
				}
			}
			
			cardCursor.close();
		}
		
		c.close();
		
		String[] colorWheel = { "#2cb1e1", "#c182e0", "#92c500", "#ffb61c", "#ff5f5f" };

		long day = 1000 * 60 * 60 * 24;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		long start = calendar.getTimeInMillis() - (day * 6);

        JSONArray data = new JSONArray();
        JSONArray colors = new JSONArray();
        JSONArray names = new JSONArray();

		for (int j = 0; j < cards.size(); j++)
		{
            colors.put(colorWheel[j % colorWheel.length]);
            names.put(cards.get(j));

			long cardId = cardIds.get(j);

            int count = 0;
			
			for (int i = 0; i < 7; i++)
			{
				long cellStart = start + (day * i);
				long cellEnd = start + (day * (i + 1));
				
				long cellMid = (cellStart + cellEnd) / 2;
				
				Calendar thisCal = Calendar.getInstance();
				thisCal.setTimeInMillis(cellMid);

				
				String pathSelect = AspireContentProvider.PATH_CARD_ID + " = ?";
				String[] pathArgs = { "" + cardId };
				
				Cursor pathCursor = context.getContentResolver().query(AspireContentProvider.ASPIRE_PATH_URI, null, pathSelect, pathArgs, null);
				
				while (pathCursor.moveToNext())
				{
					String taskSelect = AspireContentProvider.TASK_PATH_ID + " = ? AND " + AspireContentProvider.TASK_YEAR + " = ? AND " + 
										AspireContentProvider.TASK_MONTH + " = ? AND " + AspireContentProvider.TASK_DAY + " = ?";
					
					String[] taskArgs = { "" + pathCursor.getLong(pathCursor.getColumnIndex(AspireContentProvider.ID)), "" + thisCal.get(Calendar.YEAR), 
										  "" + thisCal.get(Calendar.MONTH), "" + thisCal.get(Calendar.DAY_OF_MONTH) };
					
					Cursor taskCursor = context.getContentResolver().query(AspireContentProvider.ASPIRE_TASK_URI, null, taskSelect, taskArgs, null);
					
					count += taskCursor.getCount();
					
					taskCursor.close();
				}

				pathCursor.close();
			}
        data.put(count);
		}

        JSONObject dataObj = new JSONObject();
        dataObj.put("data", data);
        dataObj.put("colors", colors);
        dataObj.put("names", names);

		return dataObj;
	}

	public boolean onCreateOptionsMenu(Menu menu) 
	{
		this.getMenuInflater().inflate(R.menu.menu_graph, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		int itemId = item.getItemId();
		
		switch (itemId)
		{
			case R.id.action_close:
				this.finish();
				break;
		}
		
		return true;
	}
}
