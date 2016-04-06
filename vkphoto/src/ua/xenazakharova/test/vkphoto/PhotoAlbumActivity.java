package ua.xenazakharova.test.vkphoto;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.xenazakharova.test.vkphoto.model.PhotoDataModel;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class PhotoAlbumActivity extends Activity{
	GridView gridView;
	PhotosGridViewAdapter gridAdapter;
	ProgressBar progressBar;
	
	int idOwner, idAlbum;
	String titleAlbum;
	int countAlbum;
	ArrayList<PhotoDataModel> photosList = new ArrayList<PhotoDataModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_photos);
		
		Intent intent = getIntent();
		idOwner = intent.getIntExtra("owner_id",0);
		idAlbum = intent.getIntExtra("album_id",0);
		titleAlbum = intent.getStringExtra("album_title");
		
		showActionBar(titleAlbum, countAlbum);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		gridView = (GridView) findViewById(R.id.gridView);
		
		VKParameters params = new VKParameters();
        params.put("owner_id", idOwner);
        params.put("album_id", idAlbum);
    	
        VKRequest request = new VKRequest(getString(R.string.photos_get), params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                  photosListParser(response.json);
            }            
            
         });

	}
	
	void photosListParser(final JSONObject json){

    	try {
    		JSONObject obj = json.getJSONObject("response");
			
			JSONArray items = obj.getJSONArray("items");
			
			for (int i=0;i<items.length();i++) {
					JSONObject item = (JSONObject) items.get(i);
					
					PhotoDataModel album = new PhotoDataModel();
					album.setId(item.optInt("id"));
					album.setText(item.optString("text"));
					album.setDate(item.optLong("date"));
					album.setWidth(item.optInt("width"));
					album.setHeight(item.optInt("height"));
					album.setPhoto(1,item.optString("photo_75"));
					album.setPhoto(2,item.optString("photo_130"));
					album.setPhoto(3,item.optString("photo_604"));
					
					photosList.add(album);
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			progressBar.setVisibility(View.GONE);
			
			if(photosList.size()>0)
				showActionBar(titleAlbum, photosList.size());
			
			gridAdapter = new PhotosGridViewAdapter(PhotoAlbumActivity.this, R.layout.grid_item_image, photosList);
  	        gridView.setAdapter(gridAdapter);
  	        
  	        gridView.setOnItemClickListener(new OnItemClickListener() {
  				public void onItemClick(AdapterView<?> parent, View v,
  					int position, long id) {
  					
  					Intent intent = new Intent (PhotoAlbumActivity.this, PhotosPagerView.class);
  		  			intent.putExtra("position", position);
  		  			intent.putExtra("album_title", titleAlbum);
  		  			
  		  			Bundle bundleObject = new Bundle();
	  		  		bundleObject.putSerializable("photos_list", photosList);
	  		  		                 
	  		  		intent.putExtras(bundleObject);
	  		  		startActivity(intent);  	               	
  				}
  			});		
			
		}		
	}
	
	@SuppressLint("NewApi")
	void showActionBar(String abTitle, int abInfo){
			
		ActionBar actionBar = getActionBar();
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
  	    View customActionBarView = getLayoutInflater().inflate(R.layout.ab_main, null, true);
		
 	    TextView actionBarTitle = (TextView) customActionBarView.findViewById(R.id.actionBarTitle);
 	    TextView actionBarInfo = (TextView) customActionBarView.findViewById(R.id.actionBarInfo);
  	    ImageButton backBtn = (ImageButton) customActionBarView.findViewById(R.id.backBtn);
 	   
 	    backBtn.setOnClickListener(new OnClickListener(){
           public void onClick(View arg)
           {
               finish();
           }
 	    });
 	    
   		actionBarTitle.setText(abTitle);
   		actionBarInfo.setText(Utils.plural(abInfo, getString(R.string.photos1), getString(R.string.photos2), getString(R.string.photos3)));
   		
   		actionBar.setCustomView(customActionBarView);
   		actionBar.setDisplayShowCustomEnabled(true);   		
	}
	
	public class PhotosGridViewAdapter extends ArrayAdapter {
		private Context context;
		private int layoutResourceId;
		private ArrayList<PhotoDataModel> data = new ArrayList<PhotoDataModel>();
		
		private int widthDevice;

		@SuppressWarnings("unchecked")
		public PhotosGridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			
			Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
			this.widthDevice  = mDisplay.getWidth();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			ViewHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) row.findViewById(R.id.image);
				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			PhotoDataModel item = data.get(position);
			
			Picasso.with(context).load(data.get(position).getPhoto(3))
				.resize((int)widthDevice/3, 0)
				.into(holder.image);
			return row;
		}

		 class ViewHolder {
			ImageView image;
		}
	}
	

}
