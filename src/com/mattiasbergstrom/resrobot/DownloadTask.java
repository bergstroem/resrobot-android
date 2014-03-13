package com.mattiasbergstrom.resrobot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<URL, Void, Void> {
	
	public interface DownloadCompleteCallback{
		public void downloadComplete(String result);
	}
	
	private DownloadCompleteCallback downloadCompleteCallback;

	public void setDownloadCompleteCallback(
			DownloadCompleteCallback downloadCompleteCallback) {
		this.downloadCompleteCallback = downloadCompleteCallback;
	}

	@Override
	protected Void doInBackground(URL... urls) {
		HttpURLConnection urlConnection = null;
		String result = "";
		try {
			urlConnection = (HttpURLConnection) urls[0].openConnection();
			
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
			BufferedReader inReader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
			String line = null;
			while((line = inReader.readLine()) != null) {
				result += line;
			}
			
		} catch (IOException e) {
			result = null;
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}
		
    	downloadCompleteCallback.downloadComplete(result);
		
		return null;
	}

    // This is called when doInBackground() is finished
    protected void onPostExecute(Void result) {
        //showNotification("Downloaded " + result + " bytes");
    }
}
