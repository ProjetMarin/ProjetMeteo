package bean;

public class Position {
	double latitude,longitude;
	Wind wind;
	
	public Position(double lat,double lon){
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public void createVent(Wind v){
		this.wind = v;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Wind getWind(){
		return this.wind;
	}
	public void setWind(Wind w){
		this.wind = w;
	}

	@Override
	public String toString() {
		return "\t[Position [latitude=" + latitude + ", longitude=" + longitude + "," + wind + "]\n\t";
	}
	
}
