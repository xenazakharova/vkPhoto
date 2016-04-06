package ua.xenazakharova.test.vkphoto.model;

public class AlbumDataModel{
	 private int id;
     private String title;
     private String description;
     private String thumb_src;
     private int size;
     
    public int getId() {
		return id;
    }
	public void setId(int id) {
        this.id = id;
    }

	public String getTitle() {
    	return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDesc() {
    	return description;
    }
    public void setDesc(String description) {
        this.description = description;
    }
    
    public String getThumbSrc() {
		return thumb_src;
    }
	public void setThumbSrc(String thumb_src) {
        this.thumb_src = thumb_src;
    }
	
	public int getSize() {
			return size;
    }
	public void setSize(int size) {
        this.size = size;
    }
}   
