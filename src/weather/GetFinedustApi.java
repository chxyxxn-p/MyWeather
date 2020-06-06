package weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GetFinedustApi {
	
	String searchLocation;
	String transformedSearchLocation;
	
	String serviceUrl="http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst";
	String serviceKey="8kL6j5npaYfEumELz%2FZOSev5k7Q2fITc0%2BbY8lATUjNzDgxyjALsK9kDn0TBs%2BNK6IfviiDBlUgIxNrAB1YvyQ%3D%3D";

	String connectResult;
	String finedustCase;
	
	int finedustValue;
	
	public GetFinedustApi(String searchLocation) {
		this.searchLocation = searchLocation;
	}
	
	public void transformSearchLocationString() {
		switch(searchLocation) {
		case "����Ư����" : 	transformedSearchLocation = "seoul"; break;
		case "�λ걤����" : 	transformedSearchLocation = "busan"; break;
		case "�뱸������" : 	transformedSearchLocation = "daegu"; break;
		case "��õ������" : 	transformedSearchLocation = "incheon"; break;
		case "���ֱ�����" : 	transformedSearchLocation = "gwangju"; break;
		case "����������" : 	transformedSearchLocation = "daejeon"; break;
		case "��걤����" : 	transformedSearchLocation = "ulsan"; break;
		case "��⵵" : 	transformedSearchLocation = "gyeonggi"; break;
		case "������" : 	transformedSearchLocation = "gangwon"; break;
		case "��û�ϵ�" : 	transformedSearchLocation = "chungbuk"; break;
		case "��û����" : 	transformedSearchLocation = "chungnam"; break;
		case "����ϵ�" : 	transformedSearchLocation = "jeonbuk"; break;
		case "���󳲵�" : 	transformedSearchLocation = "jeonnam"; break;
		case "���ϵ�" : 	transformedSearchLocation = "gyeongbuk"; break;
		case "��󳲵�" : 	transformedSearchLocation = "gyeongnam"; break;
		case "����Ư����ġ��" : 	transformedSearchLocation = "jeju"; break;
		case "����Ư����ġ��" : 	transformedSearchLocation = "sejong"; break;
		}
		System.out.println(transformedSearchLocation);
	}
	
	public void connectData() {
		System.out.println("finedust\tapi\tconnecting...");

		HttpURLConnection connection = null;
		BufferedReader reader = null;
		
		StringBuilder resultSb;
		try {
			StringBuilder urlBuilder = new StringBuilder(
					"http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst");
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("searchCondition", "UTF-8") + "=" + URLEncoder.encode("WEEK", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("dataGubun", "UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("itemCode", "UTF-8") + "=" + URLEncoder.encode("PM10", "UTF-8"));
			
			URL url = new URL(urlBuilder.toString());

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-type", "application/json");
			
			if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}

			resultSb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				resultSb.append(line);
			}
			
			connectResult = resultSb.toString();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("finedust\tapi\tconnected");
	}


	public void setFinedustString() {// xml document ����
		try {
			InputSource is = new InputSource(new StringReader(connectResult));
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			XPath xpath = XPathFactory.newInstance().newXPath();

			finedustValue = Integer.parseInt(((NodeList) xpath.evaluate("/response/body/items/item/" + transformedSearchLocation, document, XPathConstants.NODESET)).item(0).getTextContent());
		
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getFinedustCase() {
		if(finedustValue<=30) return "����";
		else if(finedustValue<=50) return "����";
		else if(finedustValue<=100) return "����";
		else return "�ſ쳪��";
	}
	
	
}


