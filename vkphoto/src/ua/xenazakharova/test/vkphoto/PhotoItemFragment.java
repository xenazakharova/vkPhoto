package ua.xenazakharova.test.vkphoto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoItemFragment extends Fragment {
	 public static final String URL_PHOTO = "URL_PHOTO";
	 int widthDevice;

	 public static final PhotoItemFragment newInstance(String urlPhoto){
		 PhotoItemFragment f = new PhotoItemFragment();
		 Bundle bdl = new Bundle();
		 bdl.putString(URL_PHOTO, urlPhoto);
		 f.setArguments(bdl);	   
	   return f;
	 }
	 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 
		 final View rootView = inflater.inflate(R.layout.fragment_photo_item, container, false);	
		 
		 Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();
		 this.widthDevice  = mDisplay.getWidth();
		 
		 ImageView photoImage = (ImageView) rootView.findViewById(R.id.photoImage);
		 photoImage.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	((PhotosPagerView)getActivity()).toggleActionBar();
		        }
		    });
		 
		 String urlPhoto = getArguments().getString(URL_PHOTO);
		 
		 Picasso.with(rootView.getContext()).load(urlPhoto.toString())
			 .resize(widthDevice, 0)
			 .into(photoImage);
		 
		return rootView;	  
	 }
}


