package com.richardgoodman.residentdj;

//Import Statements
import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Search {

	//Variable Initialisation
    private static final long NUMBER_OF_VIDEOS_RETURNED = 50;
    HttpTransport HTTP_TRANSPORT;
    JsonFactory JSON_FACTORY;
    Iterator<SearchResult> iteratorSearchResults;
    PlayItem playItem;
    List<PlayItem> listOfPlayItems;
    String results;

    /**
     * Defines a global instance of a YouTube object, which will be used
     * to make YouTube Data API requests.
     */
    public static YouTube youtube;
    
    public Search() {
    	HTTP_TRANSPORT = new NetHttpTransport();
    	JSON_FACTORY = new JacksonFactory();
    	listOfPlayItems = new ArrayList<PlayItem>();
    }

    /**
     * Initialises a YouTube object to search for videos on YouTube. Then
     * display the name and thumb nail image of each video in the result set.
     */
    public List<PlayItem> main(String searchTerm) 
    {
    	try 
    	{
	        // This object is used to make YouTube Data API requests. The last
	        // argument is required, but since we don't need anything
	        // initialised when the HttpRequest is initialised, we override
	        // the interface and provide a no operation function.
    		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();
	
	        String queryTerm = searchTerm;
	
	        // Defines the API request for retrieving search results.
	        YouTube.Search.List search = youtube.search().list("id,snippet");
	
	        // Sets the developer key from the non-authenticated requests.
	        String apiKey = DeveloperKey.DEVELOPER_KEY;
	        search.setKey(apiKey);
	        search.setQ(queryTerm);
	
	        // Restrict the search results to only include videos.
	        search.setType("video");
	
	        // To increase efficiency, only retrieve the fields that the application uses.
	        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	        search.setSafeSearch("none");
	        search.setVideoSyndicated("true");
	        search.setVideoEmbeddable("true");
	        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
	
	        SearchListResponse searchResponse = search.execute();
	        List<SearchResult> searchResultList = searchResponse.getItems();
	        
	        if (searchResultList != null) 
	        {	            
	            iteratorSearchResults = searchResultList.iterator();
	
	            while (iteratorSearchResults.hasNext()) 
	            {
	                SearchResult singleVideo = iteratorSearchResults.next();
	                ResourceId rId = singleVideo.getId();
	
	                // Confirm that the result represents a video. Otherwise, the
	                // item will not contain a video ID.
	                if (rId.getKind().equals("youtube#video")) 
	                {
	                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();	                    
	                    playItem = new PlayItem(rId.getVideoId(), "YouTube", singleVideo.getSnippet().getTitle(), thumbnail.getUrl());
	                    listOfPlayItems.add(playItem);
	                }
	            }
	        }
    	}
    	catch (GoogleJsonResponseException e) 
    	{
    	      Log.d("JSON Response Exception", "There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
    	} 
    	catch (IOException e) 
    	{
    	      Log.d("IO Exception", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
    	} 
    	catch (Throwable t) 
    	{
    	      Log.d("Throwable Error", t.toString());
    	}
        
        return listOfPlayItems;
    }
}