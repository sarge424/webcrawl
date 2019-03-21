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
	public static int maxSites = 10;
	public static boolean[][] matrix;
	
	public static void crawl(String root) throws IOException{
		q.add(root);
		BufferedReader br = null;
		
		while(!q.isEmpty()) {
			String curr = q.poll();																																									
			System.out.println("===Crawling in - " + curr);
			System.out.println("   (" + sitesCrawled.size() + " found so far)");
			
			if(sitesCrawled.size() >= maxSites)
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
	
	public static boolean[] checkLink(String root, String[] target) {
		BufferedReader br = null;
		URL url = null;
		
		try {
			url = new URL(root);
			br = new BufferedReader(new InputStreamReader(url.openStream()));
		}catch(Exception e){
		}
		
		System.out.println("--URL valid");
		
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		
		try {
			while((tmp = br.readLine()) != null) {
				sb.append(tmp);
			}
		}catch(IOException e) {
		}
		
		System.out.println("--got string");
		
		tmp = sb.toString();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(tmp);
		
		boolean[] ans = new boolean[target.length];
		
		while(matcher.find()) {
			System.out.println("Matching...");
			String w = matcher.group();
			
			for(int x = 0; x < target.length; x++) {
				if(w.equals(target[x]))
					ans[x] = true;
				else
					ans[x] = false;
			}
		}
		return ans;
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
			System.out.println("DONE!!!!");
			showResults();
		} catch (IOException e) {}
		
		System.out.println("Results Complete");
		
		matrix = new boolean[sitesCrawled.size()][sitesCrawled.size()];
		
		String[] sites = new String[sitesCrawled.size()];

		int k = 0;
		for (String i: sitesCrawled)
			sites[k++] = i;
		
		System.out.println("Site List Complete");
		
		for(int x = 0; x < sites.length; x++) {
			for(int y = 0; y < sites.length; y++) {
				matrix[x][y] = checkLink(sites[x], sites[y]);
			}
		}
		
		System.out.println("Checking Links Complete");
		
		for(int x = 0; x < sites.length; x++) {
			for(int y = 0; y < sites.length; y++) {
				if(matrix[x][y])
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
	}
}
