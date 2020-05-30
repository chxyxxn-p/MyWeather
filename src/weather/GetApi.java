package weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetApi {

	Map<Long, WeatherValue> weatherMap = new HashMap<Long, WeatherValue>();
	ArrayList<Long> keyList;
	
	String searchVersion;
	String searchResult;
	String searchBaseDate;
	String searchBaseTime;
	String searchNx;
	String searchNy;
	String serviceKey = "osaxVtVoyJX00Z9XB30%2BFesbOmRxdyLka5QzNgyDa3JvSGJde0GkbFcUQDiPqCEWnNgSvo8Gr1cAwiH2Nz8dVg%3D%3D";

	String connectResult = null;

	public GetApi(String version, String result, String sbd, String sbt, String snx, String sny) {

		this.searchVersion = version;
		this.searchResult = result;
		this.searchBaseDate = sbd;
		this.searchBaseTime = sbt;
		this.searchNx = snx;
		this.searchNy = sny;
		
		System.out.println(this.searchVersion + "  : " + this.searchBaseDate + " " + this.searchBaseTime);
	}

	public void connectData() {

		System.out.println(searchVersion + " api connecting...");

		HttpURLConnection connection = null;
		BufferedReader reader = null;

		try {
			StringBuilder urlSb = new StringBuilder(
					"http://apis.data.go.kr/1360000/VilageFcstInfoService/" + searchVersion); /* URL */
			urlSb.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); /* Service Key */
			urlSb.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /* �������������п��� ���� ����Ű */
			urlSb.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* ��������ȣ */
			urlSb.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(searchResult, "UTF-8")); /* �� ������ ��� �� */
			urlSb.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /* ��û�ڷ�����(XML/JSON)Default: XML */
			urlSb.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(searchBaseDate, "UTF-8")); /* 15�� 12�� 1�� ��ǥ */
			urlSb.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(searchBaseTime, "UTF-8")); /* 06�� ��ǥ(���ô���) */
			urlSb.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(searchNx, "UTF-8")); /* ���������� X ��ǥ�� */
			urlSb.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(searchNy, "UTF-8")); /* �������� Y ��ǥ */

			URL url = new URL(urlSb.toString());

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-type", "application/json");

//			System.out.println("Response code: " + conn.getResponseCode());

			if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}

			StringBuilder resultSb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				resultSb.append(line);
			}

			connectResult = resultSb.toString();
//			System.out.println(connectResult);

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

		System.out.println(searchVersion + " api connect done");
	}

	public void setWeatherMap() {

		if (connectResult == null) {
			System.out.println("please connect" + searchVersion + " api");

		} else {
			System.out.println(searchVersion + " api data loading...");

			JSONParser parser = new JSONParser();
			JSONObject obj;

			try {
				obj = (JSONObject) parser.parse(connectResult);

				JSONObject parse_response = (JSONObject) obj.get("response");
				JSONObject parse_body = (JSONObject) parse_response.get("body");
				JSONObject parse_items = (JSONObject) parse_body.get("items");
				JSONArray parse_item = (JSONArray) parse_items.get("item");

				for (int i = 0; i < parse_item.size(); i++) {
//					JSONArray �� �� ������Ʈ�� �����ͼ� ó��
					JSONObject weather = (JSONObject) parse_item.get(i);

					Object baseDate = weather.get("baseDate");
					Object baseTime = weather.get("baseTime");
					 
//					version���� �̸��� �ٸ��⶧���� value get�Ҷ� �ٸ� �̸����� �����ͼ� ��� ������ fcstValue�� �ڵ� ����
//					�ʴܱ��Ȳ�� ������¥ = ��ǥ��¥
					Object fcstDate = null;
					Object fcstTime = null;
					Object fcstValue = null;
					
					if(searchVersion.equals("getVilageFcst")) {
						fcstValue = weather.get("fcstValue");
						fcstDate = weather.get("fcstDate");
						fcstTime = weather.get("fcstTime");
					}
					else if(searchVersion.equals("getUltraSrtNcst")) {
						fcstValue = weather.get("obsrValue");
						fcstDate = baseDate;
						fcstTime = baseTime;
					}

//					������¥�� �ð��� �����ͼ� map��  key�� ���
//					String mapKey = (String)fcstDate + "-" + (String)fcstTime;
					long mapKey = Long.parseLong((String) fcstDate + (String) fcstTime);

//					���� map�� key���� Set���� ������ iterator�� ���� ���� JSONObject�κ��� ����� Ű�� ������ �˻�
//					���� Ű�� ������ �� key�� �ش��ϴ� WeatherValue�� ī�װ����� ������ �����ϰ�
//					���� Ű�� ������ �� key�� �ش��ϴ� WeatherValue ���� ���� �Ŀ� ī�װ��� �������� ����

					WeatherValue wv = null;

					if (weatherMap.containsKey(mapKey)) {
//							�ʿ� ���� Ű�� ���� ��� �� Ű�� �ش��ϴ� value�� �ҷ��� ����
						wv = weatherMap.get(mapKey);

					} else {
//							�ʿ� ���� Ű�� ���� ��� �� Ű�� �ش��ϴ� value�� ���� ����
						wv = new WeatherValue();
					}

//					�̹� ���ߴ� baseDate, baseTime, fcstDate, fcstTime�� �ٷ� ���� 
					wv.setBaseDate(baseDate);
					wv.setBaseTime(baseTime);
					wv.setFcstDate(fcstDate);
					wv.setFcstTime(fcstTime);


//					�� �̿��� ���� weather�� ���� ���ͼ� ����
					wv.setNx(weather.get("nx"));
					wv.setNy(weather.get("ny"));

//					fcstValue�� category���� ������ ������ �ٸ��� ������ �б� ó��	
					String category = (String) weather.get("category");

					if (category.equals("TMN")) {
						wv.setTmn(fcstValue);

					} else if (category.equals("TMX")) {
						wv.setTmx(fcstValue);

					} else if (category.equals("T3H")) {
						wv.setT3h(fcstValue);

					} else if (category.equals("T1H")) {
						wv.setT1h(fcstValue);

					} else if (category.equals("POP")) {
						wv.setPop(fcstValue);

					} else if (category.equals("RN1")) {
						wv.setRn1(fcstValue);

					} else if (category.equals("PTY")) {
						wv.setPty(fcstValue);

					} else if (category.equals("REH")) {
						wv.setReh(fcstValue);

					} else if (category.equals("SKY")) {
						wv.setSky(fcstValue);

					} else if (category.equals("VEC")) {
						wv.setVec(fcstValue);

					} else if (category.equals("WSD")) {
						wv.setWsd(fcstValue);
					}

//					map���κ��� �޾ƿ� ��  or ���� �����Ͽ� �� ������ wv�� �ٽ� ���� Ű�� map�� put -> ���� WeatherValue��� wv�� ��������� ��
					weatherMap.put(mapKey, wv);
				}
				System.out.println(searchVersion + " api data load done");
				
				keyList = new ArrayList<Long>();
				Iterator<Long> it = weatherMap.keySet().iterator();

				while (it.hasNext()) {

					long key = it.next();
					keyList.add(key);
				}

//			iterator�� �������� keyList�� add�߱� ������ �������� ����
				Collections.sort(keyList);
				
				System.out.println(searchVersion + " api key sort done");

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public Object getValueByCategory(String date, String time, String category) {
		Object obj = null;

		if (connectResult == null) {
			System.out.println("please connect" + searchVersion + " api");

		} else {
			long key = Long.parseLong(date + time);
			category = category.toUpperCase();

			if (weatherMap.containsKey(key)) {
				WeatherValue wv = weatherMap.get(key);

				if (category.equals("TMN")) {
					obj = wv.getTmn();

				} else if (category.equals("TMX")) {
					obj = wv.getTmx();

				} else if (category.equals("T3H")) {
					obj = wv.getT3h();

				} else if (category.equals("T1H")) {
					obj = wv.getT3h();

				} else if (category.equals("POP")) {
					obj = wv.getPop();

				} else if (category.equals("RN1")) {
					obj = wv.getPop();

				} else if (category.equals("PTY")) {
					obj = wv.getPty();

				} else if (category.equals("REH")) {
					obj = wv.getReh();

				} else if (category.equals("SKY")) {
					obj = wv.getSky();

				} else if (category.equals("VEC")) {
					obj = wv.getVec();

				} else if (category.equals("WSD")) {
					obj = wv.getWsd();
				}

				if (obj == null) {
					System.out.println(searchVersion + " ��ȿ���� ���� category");
				}

			} else {
				System.out.println(searchVersion + " ��ȿ���� ���� date, time");
			}
		}
		return obj;
	}

	public void printAllWeatherMapValue() {
		if (connectResult == null) {
			System.out.println("please connect" + searchVersion + " api");

		} else {

			for (int i = 0; i < keyList.size(); i++) {
//			map���κ��� ������ key�� �̷���� list -> key ��ȿ�� �˻��� �ʿ� ����

				WeatherValue wv = weatherMap.get(keyList.get(i));

				
				if(searchVersion.equals("getVilageFcst")) {
					System.out.println("\n���� ��¥ : " + wv.getFcstDate());
					System.out.println("���� �ð� : " + wv.getFcstTime());

				}
				else if(searchVersion.equals("getUltraSrtNcst")) {
					System.out.println("\n���� ��¥ : " + wv.getFcstDate());
					System.out.println("���� �ð� : " + wv.getFcstTime());
				}

				if (wv.getTmn() != null)
					System.out.println("��ħ ������� : " + wv.getTmn() + "��");

				if (wv.getTmx() != null)
					System.out.println("�� �ְ��� : " + wv.getTmx() + "��");

				if (wv.getT3h() != null)
					System.out.println("3�ð� ��� : " + wv.getT3h() + "��");

				if (wv.getT1h() != null)
					System.out.println("1�ð� ��� : " + wv.getT1h() + "��");

				if (wv.getPop() != null)
					System.out.println("���� Ȯ�� : " + wv.getPop() + "%");

				if (wv.getRn1() != null)
					System.out.println("1�ð� ������ : " + wv.getRn1() + "mm");

				if (wv.getPty() != null) {
					System.out.print("���� ���� : ");
					switch (wv.getPty().toString()) {

					case "0":
						System.out.println("����");
						break;
					case "1":
						System.out.println("��");
						break;
					case "2":
						System.out.println("��������");
						break;
					case "3":
						System.out.println("��");
						break;
					case "4":
						System.out.println("�ҳ���");
						break;
					}
				}

				if (wv.getReh() != null)
					System.out.println("���� : " + wv.getReh() + "%");

				if (wv.getSky() != null) {
					System.out.print("�ϴû��� : ");
					switch (wv.getSky().toString()) {

					case "1":
						System.out.println("����");
						break;
					case "3":
						System.out.println("���� ����");
						break;
					case "4":
						System.out.println("�帲");
						break;
					}
				}

				if (wv.getVec() != null) {
					System.out.print("ǳ�� : ");

//				�ҷ��� Object���� String���� �޾� Double������ ��ȯ -> ǳ�� �������� ��ģ �� Math�Լ��� ���� �Ҽ����� ���� �� int������ ��ȯ
					int vecCode = (int) Math.floor((Double.parseDouble((String) wv.getVec()) + 22.5 * 0.5) / 22.5);

					switch (vecCode) {

					case 0:
					case 16:
						System.out.println("��");
						break;
					case 1:
						System.out.println("�Ϻϵ�");
						break;
					case 2:
						System.out.println("�ϵ�");
						break;
					case 3:
						System.out.println("���ϵ�");
						break;
					case 4:
						System.out.println("��");
						break;
					case 5:
						System.out.println("������");
						break;
					case 6:
						System.out.println("����");
						break;
					case 7:
						System.out.println("������");
						break;
					case 8:
						System.out.println("��");
						break;
					case 9:
						System.out.println("������");
						break;
					case 10:
						System.out.println("����");
						break;
					case 11:
						System.out.println("������");
						break;
					case 12:
						System.out.println("��");
						break;
					case 13:
						System.out.println("���ϼ�");
						break;
					case 14:
						System.out.println("�ϼ�");
						break;
					case 15:
						System.out.println("�Ϻϼ�");
						break;
					}

					if (wv.getWsd() != null) {
						System.out.println("ǳ�� : " + wv.getWsd() + "m/s");
					}
				}
			}
		}
	}
}
