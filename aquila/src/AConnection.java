import java.util.HashMap;
import java.util.Map;

public class AConnection {
	public Map<String, String> map = new HashMap<>();

	public AConnection() {
		// TODO Auto-generated constructor stub
	}
	
	public AConnection(String driver, String url, String username, String password) {
		map.put("driver", driver);
		map.put("url", url);
		map.put("username", username);
		map.put("password", password);
	}

	public void addProperty(String key, String value) {
		map.put(key, value);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : map.keySet()) {
			sb.append(str + ":" + map.get(str) + "\n");
		}
		return sb.toString();
	}
}
