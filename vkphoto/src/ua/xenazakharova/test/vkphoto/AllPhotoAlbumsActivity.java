package ua.xenazakharova.test.vkphoto;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.xenazakharova.test.vkphoto.model.AlbumDataModel;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class AllPhotoAlbumsActivity extends Activity {

	GridView gridView;
	ProgressBar progressBar;
	
	int userId;
	
	AlbumsGridViewAdapter gridAdapter;

	ArrayList<AlbumDataModel> albumsList = new ArrayList<AlbumDataModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_albums_grid);
		
		showActionBar();
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		gridView = (GridView) findViewById(R.id.gridView);
		
		userId = Integer.parseInt(VKSdk.getAccessToken().userId);
		
		VKParameters params = new VKParameters();
        params.put("owner_id", userId);
        params.put("need_covers", 1);
       	//params.put("need_system", 1);
        
		VKRequest request = new VKRequest(getString(R.string.photos_getalbums), params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                
                albumsListParser(response.json, userId);
            }
            
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        }); 
	}
	
	void albumsListParser(JSONObject json, final int id_user){

		JSONObject obj;
		try {
			obj = json.getJSONObject("response");
			JSONArray items = obj.getJSONArray("items");
			
			for (int i=0;i<items.length();i++) {
					JSONObject item = (JSONObject) items.get(i);
					
					AlbumDataModel album = new AlbumDataModel();
					album.setId(item.optInt("id"));
					album.setTitle(item.optString("title"));
					album.setDesc(item.optString("description"));
					album.setThumbSrc(item.optString("thumb_src"));
					album.setSize(item.optInt("size"));
					
					albumsList.add(album);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{

			progressBar.setVisibility(View.GONE);
			
			gridAdapter = new AlbumsGridViewAdapter(this, R.layout.grid_item_layout, albumsList);
	        gridView.setAdapter(gridAdapter);
	        
	        gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

	               	Intent intent = new Intent (AllPhotoAlbumsActivity.this,PhotoAlbumActivity.class);
		  			intent.putExtra("owner_id", id_user);
		  			intent.putExtra("album_id", albumsList.get(position).getId());
		  			intent.putExtra("album_title", albumsList.get(position).getTitle());
		  			startActivity(intent);
				}
			});
		}			
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.photo_album, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			logoutVk();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void logoutVk(){

		VKSdk.logout();
		
		Intent intent = new Intent (AllPhotoAlbumsActivity.this,LoginActivity.class);
		startActivity(intent);
    	
		finish();
	}
	
	@SuppressLint("NewApi")
	void showActionBar(){
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
			
  	    View customActionBarView = getLayoutInflater().inflate(R.layout.ab_main, null, true);
		
 	    TextView actionBarTitle = (TextView) customActionBarView.findViewById(R.id.actionBarTitle);
 	    TextView actionBarInfo = (TextView) customActionBarView.findViewById(R.id.actionBarInfo);
 	    ImageButton backBtn = (ImageButton) customActionBarView.findViewById(R.id.backBtn);
 	    
 	    backBtn.setVisibility(View.GONE);
 	    actionBarInfo.setVisibility(View.GONE);
 	   
 	    actionBarTitle.setText(getString(R.string.albums));
   		
   		actionBar.setCustomView(customActionBarView);
   		actionBar.setDisplayShowCustomEnabled(true);   		
	}
	
	public class AlbumsGridViewAdapter extends ArrayAdapter {
		private Context context;
		private int layoutResourceId;
		private ArrayList<AlbumDataModel> data = new ArrayList<AlbumDataModel>();
		
		private int widthDevice;

		@SuppressWarnings("unchecked")
		public AlbumsGridViewAdapter(Context context, int layoutResourceId, ArrayList<AlbumDataModel> data) {
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
				holder.imageTitle = (TextView) row.findViewById(R.id.textTextView);
				holder.image = (ImageView) row.findViewById(R.id.imageView);
				holder.imageInfo = (TextView) row.findViewById(R.id.infoTextView);
				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			AlbumDataModel item = data.get(position);
			
			holder.imageTitle.setText(item.getTitle());
			holder.imageInfo.setText(Utils.plural(item.getSize(), getString(R.string.photos1), getString(R.string.photos2), getString(R.string.photos3)));
				
			Picasso.with(context).load(data.get(position).getThumbSrc())
				.resize((int)widthDevice/4, 0)
				.into(holder.image);
			
			return row;
		}

		 class ViewHolder {
			TextView imageTitle;
			TextView imageInfo;
			ImageView image;
		}
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        if (!VKSdk.isLoggedIn()) {
        	Intent intent = new Intent (AllPhotoAlbumsActivity.this,LoginActivity.class);
  			startActivity(intent);
        	finish();         
        }
     }
	
}
