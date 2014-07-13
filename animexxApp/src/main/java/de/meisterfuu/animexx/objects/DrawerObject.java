package de.meisterfuu.animexx.objects;


public class DrawerObject {
	
	String title;
	String code;
	String subtitle;
	int iconId;
	
	public DrawerObject(){
		
	}

	
	public String getTitle() {
		return title;
	}

	
	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getSubtitle() {
		return subtitle;
	}

	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	
	public int getIconId() {
		return iconId;
	}

	
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String pCode) {
		code = pCode;
	}

}
