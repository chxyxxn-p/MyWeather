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
		case "서울특별시" : 	transformedSearchLocation = "seoul"; break;
		case "부산광역시" : 	transformedSearchLocation = "busan"; break;
		case "대구광역시" : 	transformedSearchLocation = "daegu"; break;
		case "인천광역시" : 	transformedSearchLocation = "incheon"; break;
		case "광주광역시" : 	transformedSearchLocation = "gwangju"; break;
		case "대전광역시" : 	transformedSearchLocation = "daejeon"; break;
		case "울산광역시" : 	transformedSearchLocation = "ulsan"; break;
		case "경기도" : 	transformedSearchLocation = "gyeonggi"; break;
		case "강원도" : 	transformedSearchLocation = "gangwon"; break;
		case "충청북도" : 	transformedSearchLocation = "chungbuk"; break;
		case "충청남도" : 	transformedSearchLocation = "chungnam"; break;
		case "전라북도" : 	transformedSearchLocation = "jeonbuk"; break;
		case "전라남도" : 	transformedSearchLocation = "jeonnam"; break;
		case "경상북도" : 	transformedSearchLocation = "gyeongbuk"; break;
		case "경상남도" : 	transformedSearchLocation = "gyeongnam"; break;
		case "제주특별자치도" : 	transformedSearchLocation = "jeju"; break;
		case "세종특별자치시" : 	transformedSearchLocation = "sejong"; break;
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


	public void setFinedustString() {// xml document 생성
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
		if(finedustValue<=30) return "맑음";
		else if(finedustValue<=50) return "보통";
		else if(finedustValue<=100) return "나쁨";
		else return "매우나쁨";
	}
	
	
}


