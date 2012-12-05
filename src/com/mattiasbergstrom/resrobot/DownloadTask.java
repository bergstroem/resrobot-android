package com.mattiasbergstrom.resrobot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<URL, Integer, String> {
	
	public interface DownloadCompleteCallback{
		public void downloadComplete(String result);
	}
	
	private DownloadCompleteCallback downloadCompleteCallback;

	public void setDownloadCompleteCallback(
			DownloadCompleteCallback downloadCompleteCallback) {
		this.downloadCompleteCallback = downloadCompleteCallback;
	}

	@Override
	protected String doInBackground(URL... urls) {
		HttpURLConnection urlConnection = null;
		String result = "";
		try {
			urlConnection = (HttpURLConnection) urls[0].openConnection();
			
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			//readStream(in);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line = inReader.readLine()) != null) {
				result += line;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}
		
		return result;
	}
	
	// This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(String result) {
    	downloadCompleteCallback.downloadComplete(result);
        //showNotification("Downloaded " + result + " bytes");
    }
}
