package edu.northwestern.cbits.intellicare.aspire;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import edu.northwestern.cbits.intellicare.ConsentedActivity;

public class EditActivity extends ConsentedActivity 
{
	protected static final String CARD_ID = "card_id";

	private long _cardId = -1;
	private String _cardName = null;
	private String _cardDescription = null;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_edit);
		
		this._cardId  = this.getIntent().getLongExtra(EditActivity.CARD_ID, -1);
		
		if (this._cardId != -1)
		{
			String where = AspireContentProvider.ID + " = ?";
			String[] args = { "" + this._cardId };
			
			Cursor c = this.getContentResolver().query(AspireContentProvider.ASPIRE_CARD_URI, null, where, args, null);
			
			if (c.moveToNext())
			{
				this._cardName = c.getString(c.getColumnIndex(AspireContentProvider.CARD_NAME));
				this._cardDescription = c.getString(c.getColumnIndex(AspireContentProvider.CARD_DESCRIPTION));

				this.getSupportActionBar().setTitle(this._cardName);
				
				TextView description = (TextView) this.findViewById(R.id.card_description);
				description.setText(this.getString(R.string.description_path, this._cardDescription));
			}
		}
		else
			this.finish();
		
		this.getSupportActionBar().setSubtitle(R.string.subtitle_editor);
	}
	
	protected void onResume()
	{
		super.onResume();
		
		this.refreshList();
	}
	
	private void refreshList() 
	{
		final EditActivity me = this;

		String where = AspireContentProvider.PATH_CARD_ID + " = ?";
		String[] args = { "" + this._cardId };
		
		String[] from = { AspireContentProvider.PATH_PATH };
		int[] to = { android.R.id.text1 };
		
		Cursor c = this.getContentResolver().query(AspireContentProvider.ASPIRE_PATH_URI, null, where, args, null);
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, from, to, 0);	

		ListView list = (ListView) this.findViewById(R.id.list_view);
		
		list.setAdapter(adapter);
		
		list.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, final long id) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(me);
				builder.setTitle(R.string.title_delete_task);
				builder.setMessage(R.string.message_delete_task);
				
				builder.setPositiveButton(R.string.action_delete, new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1) 
					{
						String where = AspireContentProvider.ID + " = ?";
						String[] args = { "" + id };
						
						me.getContentResolver().delete(AspireContentProvider.ASPIRE_PATH_URI, where, args);
								
						
						me.refreshList();
					}
				});
				
				builder.setNegativeButton(R.string.action_cancel, new OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which) 
					{

					}
				});
				
				builder.create().show();
				
				return true;
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) 
	{
		this.getMenuInflater().inflate(R.menu.menu_edit, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		int itemId = item.getItemId();
		
		switch (itemId)
		{
			case R.id.action_add_path:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				
				builder.setTitle(R.string.title_new_task);
				
				LayoutInflater inflater = LayoutInflater.from(this);
				View view = inflater.inflate(R.layout.view_add_task, null, false);
				
				builder.setView(view);
				builder.setCancelable(false);
				
				final EditActivity me = this;
				
				final EditText taskField = (EditText) view.findViewById(R.id.field_new_task);
				
				builder.setPositiveButton(R.string.action_add_task, new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						ContentValues values = new ContentValues();
						values.put(AspireContentProvider.PATH_CARD_ID, me._cardId);
						values.put(AspireContentProvider.PATH_PATH, taskField.getText().toString().trim());
						
						me.getContentResolver().insert(AspireContentProvider.ASPIRE_PATH_URI, values);
						
						me.refreshList();
					}
				});
				
				builder.create().show();

				break;
			case R.id.action_close:
				this.finish();

				break;
		}
		
		return true;
	}
}
