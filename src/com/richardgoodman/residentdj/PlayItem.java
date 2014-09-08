package com.richardgoodman.residentdj;

public class PlayItem
{
	//Variable Initialisation
    public String id, source, title, thumb;

    //Constructor Method
    public PlayItem(String ID, String Source, String Title, String Thumb)
    {
        this.id = ID;
        this.source = Source;
        this.title = Title;
        this.thumb = Thumb;    
    }
    
    public String getID()
    {
        return id;
    }
    
    public String getSource()
    {
    	return source;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String getThumb()
    {
        return thumb;
    }
}