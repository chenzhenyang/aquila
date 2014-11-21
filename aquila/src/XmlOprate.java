

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlOprate {
	Document doc;
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder;
	NodeList imags;
	String path;

	public NodeList getImags() {
		return imags;
	}

	public void setImags(NodeList imags) {
		this.imags = imags;
	}

	/**
	 * 构造方法
	 * 
	 * @param path
	 *            :xml文件的路径
	 * @param nodes
	 *            ：要解析的xml节点名称
	 */
	public XmlOprate(String path) {
		super();
		this.path = path;
		System.out.println(System.getProperty("user.dir"));
	}

	/**
	 * 解析XML
	 * 
	 * @param path
	 */
	public void readXml() {
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(path);
			doc.normalize();
			NodeList imags = doc.getElementsByTagName("imags");
			this.setImags(imags);
			for (int i = 0; i < imags.getLength(); i++) {
				Element link = (Element) imags.item(i);
				System.out.print("title: ");
				System.out.println(link.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
				System.out.print("URL: ");
				System.out.println(link.getElementsByTagName("url").item(0).getFirstChild().getNodeValue());
				System.out.print("imgsrc: ");
				System.out.println(link.getElementsByTagName("imgsrc").item(0).getFirstChild().getNodeValue());
				System.out.println();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param dataSourceName
	 * @return
	 */
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
				if(!name.equals(dataSourceName))
					continue;
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

	/**
	 * addCode
	 * 
	 * @param path
	 */
	public void addXmlCode(String driverClass0, String url0, String user0, String password0) {
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(path);
			doc.normalize();

			Element namedconfig = doc.createElement("named-config");
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

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new java.io.File(path));
			transformer.transform(source, result);
		} catch (Exception e) {
		}
	}

	/**
	 * delete xml code
	 * 
	 * @param path
	 */
	public void delXmlCode() {
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(path);
			doc.normalize();
			NodeList imags = doc.getElementsByTagName("imags");
			Element elink = (Element) imags.item(0);
			elink.removeChild(elink.getElementsByTagName("imgsrc").item(0));
			elink.removeChild(elink.getElementsByTagName("title").item(0));
			elink.removeChild(elink.getElementsByTagName("url").item(0));
			doc.getFirstChild().removeChild(elink);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new java.io.File(path));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XmlOprate xo = new XmlOprate("D:\\code\\ASqlServer-C3P0\\src\\c3p0-config-2.xml");
		// xo.addXmlCode("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://127.0.0.1:1433;DatabaseName=sqoop","sa","highgo");
		AConnection ac = XmlOprate.getConnection("D:\\code\\ASqlServer-C3P0\\src\\c3p0-config-2.xml", "postgres");
//		for (AConnection str : list) {
//			System.out.println("this is a datasource:");
//			System.out.println(str);
//		}
		
		System.out.println(ac);
	}
}