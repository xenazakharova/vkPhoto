package ua.xenazakharova.test.vkphoto;

import java.util.ArrayList;

import ua.xenazakharova.test.vkphoto.model.PhotoDataModel;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotosPagerView extends FragmentActivity {
	
    String LOG_TAG = "PhotosPagerViewLog";

    ViewPager phPager;
	RelativeLayout infoRelativeLayout;
	TextView photoTitle;
	
	private PagerAdapter phPagerAdapter;

    int currentPosition;
    String titleAlbum, infoAlbum;
	
	ArrayList<PhotoDataModel> photosList = new ArrayList<PhotoDataModel>();

	@SuppressWarnings({ "unchecked", "deprecation" })
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
	    setContentView(R.layout.activity_photos_pager);
        
        Intent in = getIntent();         
        currentPosition = in.getIntExtra("position",0);
        titleAlbum = in.getStringExtra("album_title");
  
        try{
            Bundle bundleObject = getIntent().getExtras();
                     
            photosList = (ArrayList<PhotoDataModel>) bundleObject.getSerializable("photos_list");
            infoAlbum = (currentPosition + 1) +" / "+photosList.size();
            
            showActionBar(titleAlbum, infoAlbum);
                     
        } catch(Exception e){
            e.printStackTrace();
        }
    
        infoRelativeLayout = (RelativeLayout) findViewById(R.id.infoRelativeLayout);
        photoTitle = (TextView) findViewById(R.id.photoTitle);
        
        photoTitle.setText(photosList.get(currentPosition).getText());
    
	 	phPager = (ViewPager) findViewById(R.id.photoPager);
	 	phPagerAdapter = new PhotosPagerViewAdapter(getSupportFragmentManager());
        phPager.setAdapter(phPagerAdapter);
		phPagerAdapter.notifyDataSetChanged();
		phPager.setCurrentItem(currentPosition);
		
        phPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				photoTitle.setText(photosList.get(position).getText());
        		
        		infoAlbum = (position +1) +" / "+photosList.size();
        		
        		showActionBar(titleAlbum, infoAlbum);			
        	}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub				
			}
		});         
    }
	
	public void toggleActionBar(){
		if(getActionBar().isShowing()){
	  		getActionBar().hide();
	  		infoRelativeLayout.setVisibility(View.GONE);
	  	}else{
	  	  	getActionBar().show();
	  	  	infoRelativeLayout.setVisibility(View.VISIBLE);
	  	}
	}
	
	@SuppressLint("NewApi")
	void showActionBar(String abTitle, String abInfo){
			
		ActionBar actionBar = getActionBar();
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
  	    View customActionBarView = getLayoutInflater().inflate(R.layout.ab_photo, null, true);
		
 	    TextView actionBarTitle = (TextView) customActionBarView.findViewById(R.id.actionBarTitle);
 	    TextView actionBarInfo = (TextView) customActionBarView.findViewById(R.id.actionBarInfo);
 	    ImageButton backBtn = (ImageButton) customActionBarView.findViewById(R.id.backBtn);
 	   
 	    backBtn.setOnClickListener(new OnClickListener(){
 	    	public void onClick(View arg){
	           finish();
 	    	}
 	    });
 	    
   		actionBarTitle.setText(abTitle);
   		actionBarInfo.setText(abInfo);
   		
   		actionBar.setCustomView(customActionBarView);
   		actionBar.setDisplayShowCustomEnabled(true);
   		
	}
	    
    public class PhotosPagerViewAdapter extends FragmentStatePagerAdapter {
    	
    	public PhotosPagerViewAdapter(FragmentManager fm){
    	    super(fm);
    	  }
    	  @Override 
    	  public Fragment getItem(int position) {
    		  return PhotoItemFragment.newInstance(photosList.get(position).getPhoto(3));    		  
    	  }
  
    	  @Override
    	  public int getCount() {
    	    return photosList.size();
    	  }	   
    }

}
