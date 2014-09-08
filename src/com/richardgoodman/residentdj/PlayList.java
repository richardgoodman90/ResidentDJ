package com.richardgoodman.residentdj;

//Import Statements
import java.util.ArrayList;
import java.util.List;

public class PlayList {
	
	//Variable Initialisation
	List<PlayItem> list;
	PlayItem pi;
    String id, source, title, thumb;
	
    //Constructor Method
	public PlayList() {
		list = new ArrayList<PlayItem>();
	}
	
	public boolean addToPlayList(String ID, String Source, String Title, String Thumb) {
		try {
			pi = new PlayItem(ID, Source, Title, Thumb);
			list.add(pi);
			pi = null;
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public boolean removeFromPlayList() {
		if (getCount() != 0) {
			list.remove(0);
			return true;
		}
		return false;
	}
	
	public boolean removeFromPlayListAtLocation(int location) {
		if (getCount() != 0 && getCount() > location) {
			list.remove(location);
			return true;
		}
		return false;
	}
	
	public int getCount() {
		return list.size();
	}
	
	public String getVideoID() {
		return list.get(0).id;
	}
	
	public List<PlayItem> returnPlaylist() {
		return list;
	}
	
	public List<String> returnVideoIds() {
		List<String> temp = new ArrayList<String>();
		for(int x=0; x<list.size(); x++) {
			temp.add(list.get(x).id);
		}
		
		return temp;
	}
	
	public List<String> returnVideoTitles() {
		List<String> temp = new ArrayList<String>();
		for(int x=0; x<list.size(); x++) {
			temp.add(list.get(x).title);
		}
		
		return temp;
	}
}
