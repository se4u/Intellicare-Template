package edu.northwestern.cbits.intellicare.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.northwestern.cbits.ic_template.R;

public class StarRatingView extends RelativeLayout 
{
	public interface OnRatingChangeListener
	{
		public void onRatingChanged(View view, int rating);
	}

	private int _rating = 0;
	private OnRatingChangeListener _listener = null;
	
	public void setOnRatingChangeListener(OnRatingChangeListener listener)
	{
		this._listener = listener;
	}
	
	public StarRatingView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       inflater.inflate(R.layout.layout_star_rating_view, this);
       
       final ImageView one = (ImageView) this.findViewById(R.id.one_star);
       final ImageView two = (ImageView) this.findViewById(R.id.two_star);
       final ImageView three = (ImageView) this.findViewById(R.id.three_star);
       final ImageView four = (ImageView) this.findViewById(R.id.four_star);
       final ImageView five = (ImageView) this.findViewById(R.id.five_star);
       
       final StarRatingView me = this;
       
       OnClickListener listener = new OnClickListener()
       {
			public void onClick(View view) 
			{
				five.setImageResource(R.drawable.ic_action_star_0);
				four.setImageResource(R.drawable.ic_action_star_0);
				three.setImageResource(R.drawable.ic_action_star_0);
				two.setImageResource(R.drawable.ic_action_star_0);
				one.setImageResource(R.drawable.ic_action_star_0);

				switch (view.getId())
				{
					case R.id.five_star:
						five.setImageResource(R.drawable.ic_action_star_10);
					case R.id.four_star:
						four.setImageResource(R.drawable.ic_action_star_10);
					case R.id.three_star:
						three.setImageResource(R.drawable.ic_action_star_10);
					case R.id.two_star:
						two.setImageResource(R.drawable.ic_action_star_10);
					case R.id.one_star:
						one.setImageResource(R.drawable.ic_action_star_10);
				}

				switch (view.getId())
				{
					case R.id.five_star:
						me._rating = 5;
						break;
					case R.id.four_star:
						me._rating = 4;
						break;
					case R.id.three_star:
						me._rating = 3;
						break;
					case R.id.two_star:
						me._rating = 2;
						break;
					case R.id.one_star:
						me._rating = 1;
						break;
				}
				
				if (me._listener != null)
					me._listener.onRatingChanged(view, me._rating);
			}
       };
       
       one.setOnClickListener(listener);
       two.setOnClickListener(listener);
       three.setOnClickListener(listener);
       four.setOnClickListener(listener);
       five.setOnClickListener(listener);
	}
	
	public int getRating()
	{
		return this._rating;
	}
}
