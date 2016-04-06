package ua.xenazakharova.test.vkphoto.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class PhotoDataModel implements Serializable {

		private static final long serialVersionUID = 1L;
		private int id;
	    private String text;
	    private Date date;
	    private int width;
	    private int height;
	    private String photo_75;
	    private String photo_130;
	    private String photo_604;
	     
	    public int getId() {
			return id;
	    }
		public void setId(int id) {
	        this.id = id;
	    }

		public String getText() {
	    	return text;
	    }
	    public void setText(String text) {
	        this.text = text;
	    }
	    
	    public String getDate() {
	    	
	    	Calendar calendar;
			String result = null;
			  
	 		if (date!=null){
				calendar = Calendar.getInstance();
				calendar.setTime(date);
				
				result = String.format("%ta, %tb %te, %tY", calendar, calendar, calendar, calendar);

	    	}
	    	return result;
	    }
	    public void setDate(Long date) {
	        this.date = new Date(date);
	    }
	    
	    public int getWidth() {
			return width;
	    }
		public void setWidth(int width) {
	        this.width = width;
	    }
		
		public int getHeight() {
			return height;
	    }
		public void setHeight(int height) {
	        this.height = height;
	    }
		
		public float getRatio() {
			return (float)height/width;
	    }
	    
	    public String getPhoto(int type) {
	    	switch(type){
	    		case 1: return photo_75;
	    		case 2: return photo_130;
	    		case 3: return photo_604;
	    		default: return photo_604;
	    	}
	    }
		public void setPhoto(int type, String photo) {
	        
	    	switch(type){
    		case 1:	this.photo_75 = photo;
	    		break;
	    		case 2: this.photo_130 = photo;
	    		break;
	    		case 3: this.photo_604 = photo;
	    		break;
	    		default:
	    		break;
	    	}
	    }
		
}   
