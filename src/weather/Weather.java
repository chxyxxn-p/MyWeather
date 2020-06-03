package weather;

public class Weather {
	
	private Object nx = null;
	private Object ny = null;
	
	private Object baseDate = null;
	private Object baseTime = null;
	
	private Object fcstDate = null;
	private Object fcstTime = null;
	
	private Object tmn = null;	//아침 최저기온	//동네예보	//℃
	private Object tmx = null;	//낮 최고기온	//동네예보	//℃
	private Object t3h = null;	//3시간 기온	//동네예보	//℃
	private Object t1h = null;	//1시간 기온	//초단기예보	///℃
	private Object pop = null;	//강수확률	//동네예보	//%
	private Object rn1 = null;	//한시간 강수량	//초단기예보 
	private Object pty = null;	//강수형태	//동네예보,초단기실황,초단기예보	//없음(0), 비(1), 비/눈(진눈개비)(2), 눈(3), 소나기(4)
	private Object reh = null;	//습도	//동네예보,초단기실황,초단기예보	//%
	private Object sky = null;	//하늘상태	//동네예보,초단기예보	//맑음(1), 구름많음(3), 흐림(4)
	private Object vec = null;
	private Object wsd = null;	//풍속	//동네예보,초단기실황,초단기예보	//m/s	//동(+표기), 서(-표기)
	
	public Object getNx() {
		return nx;
	}
	public void setNx(Object nx) {
		this.nx = nx;
	}
	public Object getNy() {
		return ny;
	}
	public void setNy(Object ny) {
		this.ny = ny;
	}
	public Object getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(Object baseDate) {
		this.baseDate = baseDate;
	}
	public Object getBaseTime() {
		return baseTime;
	}
	public void setBaseTime(Object baseTime) {
		this.baseTime = baseTime;
	}
	public Object getFcstDate() {
		return fcstDate;
	}
	public void setFcstDate(Object fcstDate) {
		this.fcstDate = fcstDate;
	}
	public Object getFcstTime() {
		return fcstTime;
	}
	public void setFcstTime(Object fcstTime) {
		this.fcstTime = fcstTime;
	}
	public Object getTmn() {
		return tmn;
	}
	public void setTmn(Object tmn) {
		this.tmn = tmn;
	}
	public Object getTmx() {
		return tmx;
	}
	public void setTmx(Object tmx) {
		this.tmx = tmx;
	}
	public Object getT3h() {
		return t3h;
	}
	public void setT3h(Object t3h) {
		this.t3h = t3h;
	}
	public Object getT1h() {
		return t1h;
	}
	public void setT1h(Object t1h) {
		this.t1h = t1h;
	}
	public Object getPop() {
		return pop;
	}
	public void setPop(Object pop) {
		this.pop = pop;
	}
	public Object getRn1() {
		return rn1;
	}
	public void setRn1(Object rn1) {
		this.rn1 = rn1;
	}
	public Object getPty() {
		return pty;
	}
	public void setPty(Object pty) {
		this.pty = pty;
	}
	public Object getReh() {
		return reh;
	}
	public void setReh(Object reh) {
		this.reh = reh;
	}
	public Object getSky() {
		return sky;
	}
	public void setSky(Object sky) {
		this.sky = sky;
	}	
	public Object getVec() {
		return vec;
	}
	public void setVec(Object vec) {
		this.vec = vec;
	}
	public Object getWsd() {
		return wsd;
	}
	public void setWsd(Object wsd) {
		this.wsd = wsd;
	}
}
