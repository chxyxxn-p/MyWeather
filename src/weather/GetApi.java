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
			urlSb.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /* 공공데이터포털에서 받은 인증키 */
			urlSb.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지번호 */
			urlSb.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(searchResult, "UTF-8")); /* 한 페이지 결과 수 */
			urlSb.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /* 요청자료형식(XML/JSON)Default: XML */
			urlSb.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(searchBaseDate, "UTF-8")); /* 15년 12월 1일 발표 */
			urlSb.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(searchBaseTime, "UTF-8")); /* 06시 발표(정시단위) */
			urlSb.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(searchNx, "UTF-8")); /* 예보지점의 X 좌표값 */
			urlSb.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(searchNy, "UTF-8")); /* 예보지점 Y 좌표 */

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
//					JSONArray 로 한 컴포넌트씩 가져와서 처리
					JSONObject weather = (JSONObject) parse_item.get(i);

					Object baseDate = weather.get("baseDate");
					Object baseTime = weather.get("baseTime");
					 
//					version별로 이름이 다르기때문에 value get할때 다른 이름으로 가져와서 담는 변수는 fcstValue로 코드 통일
//					초단기실황은 예보날짜 = 발표날짜
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

//					예측날짜와 시간을 가져와서 map의  key로 사용
//					String mapKey = (String)fcstDate + "-" + (String)fcstTime;
					long mapKey = Long.parseLong((String) fcstDate + (String) fcstTime);

//					현재 map의 key들을 Set으로 가져와 iterator를 통해 현재 JSONObject로부터 도출된 키와 같은지 검사
//					같은 키가 있으면 그 key에 해당하는 WeatherValue에 카테고리별로 측정값 대입하고
//					같은 키가 없으면 그 key에 해당하는 WeatherValue 새로 생성 후에 카테고리별 측정값을 대입

					WeatherValue wv = null;

					if (weatherMap.containsKey(mapKey)) {
//							맵에 같은 키가 있을 경우 그 키에 해당하는 value를 불러와 대입
						wv = weatherMap.get(mapKey);

					} else {
//							맵에 같은 키가 없을 경우 그 키에 해당하는 value를 새로 생성
						wv = new WeatherValue();
					}

//					이미 구했던 baseDate, baseTime, fcstDate, fcstTime은 바로 대입 
					wv.setBaseDate(baseDate);
					wv.setBaseTime(baseTime);
					wv.setFcstDate(fcstDate);
					wv.setFcstTime(fcstTime);


//					그 이외의 값은 weather로 부터 얻어와서 대입
					wv.setNx(weather.get("nx"));
					wv.setNy(weather.get("ny"));

//					fcstValue는 category별로 대입할 변수가 다르기 때문에 분기 처리	
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

//					map으로부터 받아온 뒤  or 새로 생성하여 값 설정한 wv를 다시 같은 키로 map에 put -> 기존 WeatherValue대신 wv로 덮어씌워지게 됨
					weatherMap.put(mapKey, wv);
				}
				System.out.println(searchVersion + " api data load done");
				
				keyList = new ArrayList<Long>();
				Iterator<Long> it = weatherMap.keySet().iterator();

				while (it.hasNext()) {

					long key = it.next();
					keyList.add(key);
				}

//			iterator로 순서없이 keyList에 add했기 때문에 오름차순 정렬
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
					System.out.println(searchVersion + " 유효하지 않은 category");
				}

			} else {
				System.out.println(searchVersion + " 유효하지 않은 date, time");
			}
		}
		return obj;
	}

	public void printAllWeatherMapValue() {
		if (connectResult == null) {
			System.out.println("please connect" + searchVersion + " api");

		} else {

			for (int i = 0; i < keyList.size(); i++) {
//			map으로부터 가져온 key로 이루어진 list -> key 유효성 검사할 필요 없다

				WeatherValue wv = weatherMap.get(keyList.get(i));

				
				if(searchVersion.equals("getVilageFcst")) {
					System.out.println("\n예측 날짜 : " + wv.getFcstDate());
					System.out.println("예측 시간 : " + wv.getFcstTime());

				}
				else if(searchVersion.equals("getUltraSrtNcst")) {
					System.out.println("\n현재 날짜 : " + wv.getFcstDate());
					System.out.println("측정 시간 : " + wv.getFcstTime());
				}

				if (wv.getTmn() != null)
					System.out.println("아침 최저기온 : " + wv.getTmn() + "℃");

				if (wv.getTmx() != null)
					System.out.println("낮 최고기온 : " + wv.getTmx() + "℃");

				if (wv.getT3h() != null)
					System.out.println("3시간 기온 : " + wv.getT3h() + "℃");

				if (wv.getT1h() != null)
					System.out.println("1시간 기온 : " + wv.getT1h() + "℃");

				if (wv.getPop() != null)
					System.out.println("강수 확률 : " + wv.getPop() + "%");

				if (wv.getRn1() != null)
					System.out.println("1시간 강수량 : " + wv.getRn1() + "mm");

				if (wv.getPty() != null) {
					System.out.print("강수 형태 : ");
					switch (wv.getPty().toString()) {

					case "0":
						System.out.println("없음");
						break;
					case "1":
						System.out.println("비");
						break;
					case "2":
						System.out.println("진눈개비");
						break;
					case "3":
						System.out.println("눈");
						break;
					case "4":
						System.out.println("소나기");
						break;
					}
				}

				if (wv.getReh() != null)
					System.out.println("습도 : " + wv.getReh() + "%");

				if (wv.getSky() != null) {
					System.out.print("하늘상태 : ");
					switch (wv.getSky().toString()) {

					case "1":
						System.out.println("맑음");
						break;
					case "3":
						System.out.println("구름 많음");
						break;
					case "4":
						System.out.println("흐림");
						break;
					}
				}

				if (wv.getVec() != null) {
					System.out.print("풍향 : ");

//				불러온 Object형을 String으로 받아 Double형으로 변환 -> 풍향 계산과정을 거친 후 Math함수를 통해 소수점을 버린 뒤 int형으로 변환
					int vecCode = (int) Math.floor((Double.parseDouble((String) wv.getVec()) + 22.5 * 0.5) / 22.5);

					switch (vecCode) {

					case 0:
					case 16:
						System.out.println("북");
						break;
					case 1:
						System.out.println("북북동");
						break;
					case 2:
						System.out.println("북동");
						break;
					case 3:
						System.out.println("동북동");
						break;
					case 4:
						System.out.println("동");
						break;
					case 5:
						System.out.println("동남동");
						break;
					case 6:
						System.out.println("남동");
						break;
					case 7:
						System.out.println("남남동");
						break;
					case 8:
						System.out.println("남");
						break;
					case 9:
						System.out.println("남남서");
						break;
					case 10:
						System.out.println("남서");
						break;
					case 11:
						System.out.println("서남서");
						break;
					case 12:
						System.out.println("서");
						break;
					case 13:
						System.out.println("서북서");
						break;
					case 14:
						System.out.println("북서");
						break;
					case 15:
						System.out.println("북북서");
						break;
					}

					if (wv.getWsd() != null) {
						System.out.println("풍속 : " + wv.getWsd() + "m/s");
					}
				}
			}
		}
	}
}
