package edu.northwestern.cbits.intellicare.aspire;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.SSLException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import edu.northwestern.cbits.intellicare.logging.LogManager;

public class AspireContentProvider extends ContentProvider 
{
	private static final int CARDS = 1;
	private static final int PATHS = 2;
	private static final int TASKS = 3;
	
    private static final String AUTHORITY = "edu.northwestern.cbits.intellicare.aspire";

    private static final String CARD_TABLE = "cards";
    private static final String PATH_TABLE = "paths";
    private static final String TASK_TABLE = "tasks";

	protected static final Uri ASPIRE_CARD_URI = Uri.parse("content://" + AUTHORITY + "/" + CARD_TABLE);
	protected static final Uri ASPIRE_PATH_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_TABLE);
	protected static final Uri ASPIRE_TASK_URI = Uri.parse("content://" + AUTHORITY + "/" + TASK_TABLE);

	protected static final String CARD_NAME = "name";
	protected static final String CARD_DESCRIPTION = "description";
	protected static final String CARD_IMAGE = "card_image";
    protected static final String CARD_ENABLED = "enabled";

	protected static final String PATH_CARD_ID = "card_id";
	protected static final String PATH_PATH = "path";

	protected static final String TASK_PATH_ID = "path_id";
	protected static final String TASK_YEAR = "year";
	protected static final String TASK_MONTH = "month";
	protected static final String TASK_DAY = "day";

	private static final int DATABASE_VERSION = 5;
	public static final String ID = "_id";

	private static HashMap<String, String> _cachedHashes = new HashMap<String, String>();

    private UriMatcher _matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private SQLiteDatabase _db = null;
	
    public AspireContentProvider()
    {
    	super();
    	
        this._matcher.addURI(AUTHORITY, CARD_TABLE, CARDS);
        this._matcher.addURI(AUTHORITY, PATH_TABLE, PATHS);
        this._matcher.addURI(AUTHORITY, TASK_TABLE, TASKS);
    }

    public boolean onCreate() 
	{
        final Context context = this.getContext().getApplicationContext();

        SQLiteOpenHelper helper = new SQLiteOpenHelper(context, "aspire.db", null, AspireContentProvider.DATABASE_VERSION)
        {
            public void onCreate(SQLiteDatabase db)
            {

                db.execSQL(context.getString(R.string.db_create_cards_table));

	            db.execSQL(context.getString(R.string.db_insert_card_01));
	            db.execSQL(context.getString(R.string.db_insert_card_02));
	            db.execSQL(context.getString(R.string.db_insert_card_03));
	            db.execSQL(context.getString(R.string.db_insert_card_04));
	            db.execSQL(context.getString(R.string.db_insert_card_05));
	            db.execSQL(context.getString(R.string.db_insert_card_06));
	            db.execSQL(context.getString(R.string.db_insert_card_07));
	            db.execSQL(context.getString(R.string.db_insert_card_08));
	            db.execSQL(context.getString(R.string.db_insert_card_09));
	            db.execSQL(context.getString(R.string.db_insert_card_10));
	            db.execSQL(context.getString(R.string.db_insert_card_11));
	            db.execSQL(context.getString(R.string.db_insert_card_12));
	            db.execSQL(context.getString(R.string.db_insert_card_13));
	            db.execSQL(context.getString(R.string.db_insert_card_14));
	            db.execSQL(context.getString(R.string.db_insert_card_15));
	            db.execSQL(context.getString(R.string.db_insert_card_16));
	            db.execSQL(context.getString(R.string.db_insert_card_17));
	            db.execSQL(context.getString(R.string.db_insert_card_18));
	            db.execSQL(context.getString(R.string.db_insert_card_19));
	            db.execSQL(context.getString(R.string.db_insert_card_20));


	            this.onUpgrade(db, 0, AspireContentProvider.DATABASE_VERSION);
            }

            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
            {
            	switch (oldVersion)
            	{
	                case 0:
	                	
	                case 1:
	    	            db.execSQL(context.getString(R.string.db_create_paths_table));
                        db.execSQL(context.getString(R.string.db_insert_compassion_path_01));
                        db.execSQL(context.getString(R.string.db_insert_compassion_path_02));
                        db.execSQL(context.getString(R.string.db_insert_compassion_path_03));
                        db.execSQL(context.getString(R.string.db_insert_contribution_path_01));
                        db.execSQL(context.getString(R.string.db_insert_contribution_path_02));
                        db.execSQL(context.getString(R.string.db_insert_contribution_path_03));
                        db.execSQL(context.getString(R.string.db_insert_duty_path_01));
                        db.execSQL(context.getString(R.string.db_insert_duty_path_02));
                        db.execSQL(context.getString(R.string.db_insert_duty_path_03));
                        db.execSQL(context.getString(R.string.db_insert_family_path_01));
                        db.execSQL(context.getString(R.string.db_insert_family_path_02));
                        db.execSQL(context.getString(R.string.db_insert_family_path_03));
                        db.execSQL(context.getString(R.string.db_insert_fitness_path_01));
                        db.execSQL(context.getString(R.string.db_insert_fitness_path_02));
                        db.execSQL(context.getString(R.string.db_insert_fitness_path_03));
                        db.execSQL(context.getString(R.string.db_insert_friendship_path_01));
                        db.execSQL(context.getString(R.string.db_insert_friendship_path_02));
                        db.execSQL(context.getString(R.string.db_insert_friendship_path_03));
                        db.execSQL(context.getString(R.string.db_insert_fun_path_01));
                        db.execSQL(context.getString(R.string.db_insert_fun_path_02));
                        db.execSQL(context.getString(R.string.db_insert_fun_path_03));
                        db.execSQL(context.getString(R.string.db_insert_generosity_path_01));
                        db.execSQL(context.getString(R.string.db_insert_generosity_path_02));
                        db.execSQL(context.getString(R.string.db_insert_generosity_path_03));
                        db.execSQL(context.getString(R.string.db_insert_growth_path_01));
                        db.execSQL(context.getString(R.string.db_insert_growth_path_02));
                        db.execSQL(context.getString(R.string.db_insert_growth_path_03));
                        db.execSQL(context.getString(R.string.db_insert_helpfulness_path_01));
                        db.execSQL(context.getString(R.string.db_insert_helpfulness_path_02));
                        db.execSQL(context.getString(R.string.db_insert_helpfulness_path_03));
                        db.execSQL(context.getString(R.string.db_insert_hope_path_01));
                        db.execSQL(context.getString(R.string.db_insert_hope_path_02));
                        db.execSQL(context.getString(R.string.db_insert_hope_path_03));
                        db.execSQL(context.getString(R.string.db_insert_humor_path_01));
                        db.execSQL(context.getString(R.string.db_insert_humor_path_02));
                        db.execSQL(context.getString(R.string.db_insert_humor_path_03));
                        db.execSQL(context.getString(R.string.db_insert_inner_peace_path_01));
                        db.execSQL(context.getString(R.string.db_insert_inner_peace_path_02));
                        db.execSQL(context.getString(R.string.db_insert_inner_peace_path_03));
                        db.execSQL(context.getString(R.string.db_insert_mastery_path_01));
                        db.execSQL(context.getString(R.string.db_insert_mastery_path_02));
                        db.execSQL(context.getString(R.string.db_insert_mastery_path_03));
                        db.execSQL(context.getString(R.string.db_insert_mindfulness_path_01));
                        db.execSQL(context.getString(R.string.db_insert_mindfulness_path_02));
                        db.execSQL(context.getString(R.string.db_insert_mindfulness_path_03));
                        db.execSQL(context.getString(R.string.db_insert_purpose_path_01));
                        db.execSQL(context.getString(R.string.db_insert_purpose_path_02));
                        db.execSQL(context.getString(R.string.db_insert_purpose_path_03));
                        db.execSQL(context.getString(R.string.db_insert_responsibility_path_01));
                        db.execSQL(context.getString(R.string.db_insert_responsibility_path_02));
                        db.execSQL(context.getString(R.string.db_insert_responsibility_path_03));
                        db.execSQL(context.getString(R.string.db_insert_self_acceptance_path_01));
                        db.execSQL(context.getString(R.string.db_insert_self_acceptance_path_02));
                        db.execSQL(context.getString(R.string.db_insert_self_acceptance_path_03));
                        db.execSQL(context.getString(R.string.db_insert_virtue_path_01));
                        db.execSQL(context.getString(R.string.db_insert_virtue_path_02));
                        db.execSQL(context.getString(R.string.db_insert_virtue_path_03));
                        db.execSQL(context.getString(R.string.db_insert_tradition_path_01));
                        db.execSQL(context.getString(R.string.db_insert_tradition_path_02));
                        db.execSQL(context.getString(R.string.db_insert_tradition_path_03));

	                case 2:
	    	            db.execSQL(context.getString(R.string.db_create_tasks_table));
	                case 3:
	    	            db.execSQL(context.getString(R.string.db_update_cards_add_card_image));
	                case 4:
	                	Cursor c = db.query(AspireContentProvider.CARD_TABLE, null, null, null, null, null, null);
	                	
	                	if (c.getColumnIndex(AspireContentProvider.CARD_ENABLED) == -1)
	                	{
	                		
		    	            db.execSQL(context.getString(R.string.db_update_cards_add_card_enabled));
	                	}
	                	
	                	c.close();
	                	
	                default:
                        break;
            	}
            }
        };
        
        this._db  = helper.getWritableDatabase();

        return true;
	}

    public int delete(Uri uri, String where, String[] whereArgs) 
	{
        switch(this._matcher.match(uri))
        {
	        case AspireContentProvider.CARDS:
	            return this._db.delete(AspireContentProvider.CARD_TABLE, where, whereArgs);
	        case AspireContentProvider.PATHS:
	            return this._db.delete(AspireContentProvider.PATH_TABLE, where, whereArgs);
	        case AspireContentProvider.TASKS:
	            return this._db.delete(AspireContentProvider.TASK_TABLE, where, whereArgs);
        }
        
        return 0;
	}

	@Override
	public String getType(Uri arg0) 
	{
		return null;
	}

	public Uri insert(Uri uri, ContentValues values) 
	{
		long newId = -1;

		switch(this._matcher.match(uri))
        {
	        case AspireContentProvider.CARDS:
	        	newId = this._db.insert(AspireContentProvider.CARD_TABLE, null, values);
	        	break;
	        case AspireContentProvider.PATHS:
	        	newId = this._db.insert(AspireContentProvider.PATH_TABLE, null, values);
	        	break;
	        case AspireContentProvider.TASKS:
	        	newId = this._db.insert(AspireContentProvider.TASK_TABLE, null, values);
	        	break;

        }
		
		if (newId != -1)
			return Uri.withAppendedPath(uri, "" + newId);
		
		return null;
	}	

	public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String orderBy) 
	{
		switch(this._matcher.match(uri))
        {
	        case AspireContentProvider.CARDS:
	        	return this._db.query(AspireContentProvider.CARD_TABLE, columns, selection, selectionArgs, null, null, orderBy);
	        case AspireContentProvider.PATHS:
	        	return this._db.query(AspireContentProvider.PATH_TABLE, columns, selection, selectionArgs, null, null, orderBy);
	        case AspireContentProvider.TASKS:
	        	return this._db.query(AspireContentProvider.TASK_TABLE, columns, selection, selectionArgs, null, null, orderBy);

        }
		
		return null;
	}

	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 
	{
		switch(this._matcher.match(uri))
        {
	        case AspireContentProvider.CARDS:
	        	return this._db.update(AspireContentProvider.CARD_TABLE, values, selection, selectionArgs);
	        case AspireContentProvider.PATHS:
	        	return this._db.update(AspireContentProvider.PATH_TABLE, values, selection, selectionArgs);
	        case AspireContentProvider.TASKS:
	        	return this._db.update(AspireContentProvider.TASK_TABLE, values, selection, selectionArgs);

        }
		
		return 0;
	}

	private static String createHash(Context context, String string)
	{
		if (string == null)
			return null;

		String hash = AspireContentProvider._cachedHashes.get(string);

		if (hash != null)
			return hash;

		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(string.getBytes("UTF-8"));

			hash = (new BigInteger(1, digest)).toString(16);

			while (hash.length() < 32)
			{
				hash = "0" + hash;
			}
		}
		catch (NoSuchAlgorithmException e)
		{
			LogManager.getInstance(context).logException(e);
		}
		catch (UnsupportedEncodingException e)
		{
			LogManager.getInstance(context).logException(e);
		}

		AspireContentProvider._cachedHashes.put(string, hash);

		return hash;
	}

	private static File fetchCacheDir(Context context)
	{
		File cache = context.getExternalCacheDir();

		if (cache.exists() == false)
			cache.mkdirs();

		return cache;
	}

	public static Uri fetchResizedImage(final Context context, final Uri uri, final int width, final int height) 
	{
		if (uri == null)
			return null;

		Uri resized = Uri.parse(uri.toString() + "/" + height + "-" + width);

		String hashString = AspireContentProvider.createHash(context, resized.toString());

		final File cachedFile = new File(AspireContentProvider.fetchCacheDir(context), hashString);

		if (cachedFile.exists())
		{
			cachedFile.setLastModified(System.currentTimeMillis());

			return Uri.fromFile(cachedFile);
		}
		else
		{
			Uri cached = AspireContentProvider.fetchCachedUri(context, uri);

			if (cached != null)
			{
		        BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				opts.inInputShareable = true;
				opts.inPurgeable = true;

				BitmapFactory.decodeFile(cached.getPath(), opts);

				int scale = 1;

				int thisWidth = width;
				int thisHeight = height;

				while ((thisWidth > 0 && thisHeight > 0) && (opts.outWidth > thisWidth || opts.outHeight > thisHeight))
				{
					scale *= 2;

					thisWidth = thisWidth * 2;
					thisHeight = thisHeight * 2;
				}

				if (scale < 1)
					scale = 1;

				opts = new BitmapFactory.Options();		
				opts.inDither = false;
				opts.inPurgeable = true;
				opts.inInputShareable = true;
				opts.inSampleSize = scale;

				try
				{
					Bitmap bitmap = BitmapFactory.decodeFile(cached.getPath(), opts);

					if (bitmap != null)
					{
						cachedFile.createNewFile();

						FileOutputStream fout = new FileOutputStream(cachedFile);

						bitmap.compress(CompressFormat.PNG, 100, fout);

						fout.close();

						bitmap.recycle();
						
						return Uri.fromFile(cachedFile);
					}
					else
					{
						File f = new File(cached.getPath());

						if (f.exists())
							f.delete();
					}
				}
				catch (OutOfMemoryError e)
				{
					LogManager.getInstance(context).logException(e);
				}
				catch (FileNotFoundException e) 
				{
					LogManager.getInstance(context).logException(e);
				} 
				catch (IOException e) 
				{
					LogManager.getInstance(context).logException(e);
				}
			}
		}

		return null;
	}

	public static Uri fetchCachedUri(final Context context, final Uri uri) 
	{
		if ("file".equals(uri.getScheme().toLowerCase(Locale.ENGLISH)))
			return uri;

		if (uri == null || uri.getScheme() == null)
			return null;

		Uri.Builder builder = uri.buildUpon();

		if ("https".equals(uri.getScheme().toLowerCase(Locale.getDefault())) && "freshcomics.us".equals(uri.getHost()))
			builder.scheme("http");

		final Uri fetchUri = builder.build();

		final String hashString = AspireContentProvider.createHash(context, fetchUri.toString());

		final File cacheDir = AspireContentProvider.fetchCacheDir(context);
		final File cachedFile = new File(cacheDir, hashString);

		if (cachedFile.exists() && cachedFile.length() > 0)
		{
			cachedFile.setLastModified(System.currentTimeMillis());
			return Uri.fromFile(cachedFile);
		}

		File tempFile = null;

		try 
		{
			InputStream in = context.getContentResolver().openInputStream(fetchUri);

			tempFile = File.createTempFile(hashString, "tmp", cacheDir);

			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));

			byte[] buffer = new byte[8192];
			int read = 0;

			while ((read = in.read(buffer, 0, buffer.length)) != -1)
			{
				out.write(buffer, 0, read);
			}

			out.flush();
			out.close();

			tempFile.renameTo(cachedFile);
		} 
		catch (SocketTimeoutException e)
		{
			LogManager.getInstance(context).logException(e);
		}
		catch (SocketException e)
		{
			LogManager.getInstance(context).logException(e);
		}
		catch (SSLException e)
		{
			LogManager.getInstance(context).logException(e);
		}
		catch (UnknownHostException e)
		{
			LogManager.getInstance(context).logException(e);
		}
		catch (EOFException e)
		{
			LogManager.getInstance(context).logException(e);
		}
		catch (FileNotFoundException e) 
		{
			LogManager.getInstance(context).logException(e);
		}
		catch (IOException e) 
		{
			LogManager.getInstance(context).logException(e);
		} 
		finally
		{
			if (tempFile != null && tempFile.exists())
				tempFile.delete();
		}

		if (cachedFile.exists() && cachedFile.length() > 0)
		{
			cachedFile.setLastModified(System.currentTimeMillis());
			return Uri.fromFile(cachedFile);
		}

		return null;
	}
}
