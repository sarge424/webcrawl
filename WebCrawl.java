import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawl {
	public static Queue<String> q = new LinkedList<>();
	public static Set<String> sitesCrawled = new HashSet<>();
	public static String regex = "http[s]*://(\\w+\\.)*(\\w+)";
	
	public static void crawl(String root) throws IOException{
		q.add(root);
		String curr = q.poll();
		BufferedReader br = null;
		System.out.println("===Site crawled - " + curr);
		
		while(!q.isEmpty()) {
			if(sitesCrawled.size() == 100)
				return;
			
			URL url = null;
			boolean ok = false;
			
			while(!ok) {
				try {
					url = new URL(curr);
					br = new BufferedReader(new InputStreamReader(url.openStream()));
					ok = true;
				}catch(MalformedURLException m){
					System.out.println("***MalformedURL - " + curr);
					curr = q.poll();
					ok = false;
				}catch(IOException e) {
					System.out.println("***IOEXception - " + curr);
					curr = q.poll();
					ok = false;
				}
			}
			
			StringBuilder sb = new StringBuilder();
			String tmp = null;
			
			while((tmp = br.readLine()) != null) {
				sb.append(tmp);
			}
			
			tmp = sb.toString();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(tmp);
			
			while(matcher.find()) {
				String w = matcher.group();
				
				if(!sitesCrawled.contains(w)) {
					sitesCrawled.add(w);
					System.out.println("!!!Site added - " + w);
					q.add(w);
				}
			}
		}
		if(br!=null) {
			br.close();
		}
	}
	
	public static void showResults() {
		System.out.println("---------------------------RESULTS-----------------------");
		System.out.println(sitesCrawled.size() + " sites crawled:-\n");
		
		for(String s : sitesCrawled) {
			System.out.println("* " + s);
		}
	}
	
	public static void main(String[] args) {
		try {
			crawl("https://ishahomeschool.org");
			showResults();
		} catch (IOException e) {}
	}
}
