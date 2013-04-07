package net.gouriki.sitefusion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;

public class Task extends AsyncTask<String, Integer, String[]> {

	private Activity parent = null;

	public Task(Activity activity) {
		parent = activity;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected String[] doInBackground(String... args) {
		String ret[] = new String[2];
		try {
			ret[0] = getHtml(args[0]);
			ret[1] = getCSS(args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}

	private String getHtml(String url) throws Exception {
		List<String> l = getContent(url);
		StringBuilder sb = new StringBuilder();
		for (String s : l) {
			sb.append(s);
		}
		return new String(sb.toString());
	}

	private String getCSS(String url) throws Exception {
		String css = null;
		try {
			List<String> conts = getContent(url);
			StringBuilder sb = new StringBuilder();
			for (String cont : conts) {
				String[] lines = cont.split("<");
				for (String line : lines) {
					line = "<" + line;
					if (line.matches(".*<link.*href=\".+.css.*\".*>.*")) {
						if (!line.matches(".+(http).*")) {
							String s = Util.replaceWithRegex(
									"(<link.*href=\")(.+.css.*\".*>)", line,
									"$1" + url + "/$2");
							if (!line.equals(s)) {
								sb.append(s);
							}
						} else {
							String s = Util.replaceWithRegex(
									"(<link.*href=\")(.+.css.*\".*>)", line,
									"$1$2");
							sb.append(s);
						}
					}
				}
			}
			css = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return css;
	}

	private List<String> getContent(String url) throws Exception {
		List<String> l = new ArrayList<String>();
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url)
					.openConnection();
			con.setRequestProperty("User-Agent", Util.UA);
			con.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()), 8192);
			while (true) {
				String s = reader.readLine();
				if (s == null) {
					break;
				}
				l.add(s);
			}
			con.disconnect();
			con = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
	}

	@Override
	protected void onPostExecute(String[] result) {
		String html = result[0];
		String css = result[1];
		String res = Util.replaceWithRegex("(.*)(</head>.*)", html, "$1" + css
				+ "$2");
		((MainActivity) parent).loaded(res);
	}

}
