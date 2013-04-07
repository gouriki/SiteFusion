package net.gouriki.sitefusion;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {

	private WebView wvMain;
	private EditText etUrl;
	private String htmlUrl;
	private String cssUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etUrl = (EditText) findViewById(R.id.urlbar);
		htmlUrl = etUrl.getText().toString();
		etUrl.setOnEditorActionListener(new OnEditorActionListener() {
	        @Override
	        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	            if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
	        		htmlUrl = v.getText().toString();
	        		loadStart();
	                return true;
	            }
	            return false;
	        }
	    });

		wvMain = (WebView) findViewById(R.id.webview);
		wvMain.setVerticalScrollbarOverlay(true);
		wvMain.setWebViewClient(new WebViewClient());
		wvMain.addJavascriptInterface(this, "activity");
		wvMain.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				View pBarWrapper = findViewById(R.id.progress_bar_wrapper);
				ProgressBar pBar = (ProgressBar) findViewById(R.id.progress_bar);
				pBar.setProgress(newProgress);

				if (newProgress == 100) {
					pBarWrapper.setVisibility(View.GONE);
				} else {
					pBarWrapper.setVisibility(View.VISIBLE);
				}
			}
		});

		WebSettings set = wvMain.getSettings();
		set.setSupportMultipleWindows(true);
		set.setLoadsImagesAutomatically(true);
		set.setBuiltInZoomControls(true);
		set.setSupportZoom(true);
		set.setLightTouchEnabled(true);
		set.setJavaScriptEnabled(true);
		set.setUserAgentString(Util.UA);
		loadStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = new Intent(this, Preference.class);
    	intent.setAction(Intent.ACTION_VIEW);
    	startActivity(intent);
    	return true;
    }

	public void loadStart(){
		try {
			Task task = new Task(this);
			task.execute(new String[]{htmlUrl,cssUrl});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loaded(String contents) {
		wvMain.loadDataWithBaseURL(htmlUrl, contents, "text/html", null, null);
	}
}
