package weather;

public class Weather {
	
	private Object nx = null;
	private Object ny = null;
	
	private Object baseDate = null;
	private Object baseTime = null;
	
	private Object fcstDate = null;
	private Object fcstTime = null;
	
	private Object tmn = null;	//��ħ �������	//���׿���	//��
	private Object tmx = null;	//�� �ְ���	//���׿���	//��
	private Object t3h = null;	//3�ð� ���	//���׿���	//��
	private Object t1h = null;	//1�ð� ���	//�ʴܱ⿹��	///��
	private Object pop = null;	//����Ȯ��	//���׿���	//%
	private Object rn1 = null;	//�ѽð� ������	//�ʴܱ⿹�� 
	private Object pty = null;	//��������	//���׿���,�ʴܱ��Ȳ,�ʴܱ⿹��	//����(0), ��(1), ��/��(��������)(2), ��(3), �ҳ���(4)
	private Object reh = null;	//����	//���׿���,�ʴܱ��Ȳ,�ʴܱ⿹��	//%
	private Object sky = null;	//�ϴû���	//���׿���,�ʴܱ⿹��	//����(1), ��������(3), �帲(4)
	private Object vec = null;
	private Object wsd = null;	//ǳ��	//���׿���,�ʴܱ��Ȳ,�ʴܱ⿹��	//m/s	//��(+ǥ��), ��(-ǥ��)
	
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
