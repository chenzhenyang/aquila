import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtil {

	public static boolean saveConnection(String path, String name, String driverClass0, String url0, String user0,
			String password0) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		Document doc = db.parse(path);
		doc.normalize();

		Element namedconfig = doc.createElement("named-config");
		namedconfig.setAttribute("name", name);
		Element driverClass = doc.createElement("property");
		driverClass.setAttribute("name", "driverClass");
		driverClass.setTextContent(driverClass0);
		Element jdbcUrl = doc.createElement("property");
		jdbcUrl.setAttribute("name", "jdbcUrl");
		jdbcUrl.setTextContent(url0);
		Element user = doc.createElement("property");
		user.setAttribute("name", "user");
		user.setTextContent(user0);
		Element password = doc.createElement("property");
		password.setAttribute("name", "password");
		password.setTextContent(password0);

		namedconfig.appendChild(driverClass);
		namedconfig.appendChild(jdbcUrl);
		namedconfig.appendChild(user);
		namedconfig.appendChild(password);

		doc.adoptNode(namedconfig);
		doc.getDocumentElement().appendChild(namedconfig);
		doc.normalizeDocument();

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new java.io.File(path));
		transformer.transform(source, result);
		return true;
	}

	public static List<AConnection> getConnections(String path) {
		List<AConnection> list = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(path);
			doc.normalize();
			NodeList nameds = doc.getElementsByTagName("named-config");
			for (int i = 0; i < nameds.getLength(); i++) {
				Element namedConfig = (Element) nameds.item(i);

				String name = namedConfig.getAttribute("name");
				AConnection ac = new AConnection();
				ac.addProperty("name", name);

				NodeList cnodes = namedConfig.getElementsByTagName("property");
				for (int j = 0; j < cnodes.getLength(); j++) {
					Element node = (Element) cnodes.item(j);
					String key = node.getAttribute("name");
					String value = node.getTextContent();
					ac.addProperty(key, value);
				}
				list.add(ac);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public static AConnection getConnection(String path, String dataSourceName) {
		AConnection ac = new AConnection();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(path);
			doc.normalize();
			NodeList nameds = doc.getElementsByTagName("named-config");
			for (int i = 0; i < nameds.getLength(); i++) {
				Element namedConfig = (Element) nameds.item(i);
				String name = namedConfig.getAttribute("name");
				if (!name.equals(dataSourceName)) {
					continue;
				}
				ac.addProperty("name", name);
				NodeList cnodes = namedConfig.getElementsByTagName("property");
				for (int j = 0; j < cnodes.getLength(); j++) {
					Element node = (Element) cnodes.item(j);
					String key = node.getAttribute("name");
					String value = node.getTextContent();
					ac.addProperty(key, value);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return ac;
	}

	public static boolean deleteConnection(String name) {
		//TODO
		return true;
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		// saveConnection("D:\\code\\ASqlServer-C3P0\\src\\c3p0-config-2.xml",
		// "Test",
		// "com.microsoft.sqlserver.jdbc.SQLServerDriver",
		// "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=sqoop",
		// "sa", "highgo");
		List<AConnection> list = getConnections("D:\\code\\ASqlServer-C3P0\\src\\c3p0-config-2.xml");
		for (AConnection ac : list) {
			System.out.println(ac.toString());
		}
	}
}
