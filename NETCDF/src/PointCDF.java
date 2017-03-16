import java.util.ArrayList;
import java.util.Date;

public class PointCDF {
	


	ArrayList<Integer> Longitude;
	ArrayList<Integer> Latitude;
	ArrayList<Integer> Vitesse;
	ArrayList<Integer> Direction;
	
	Date Date;
	
	
	public PointCDF(ArrayList<Integer> longitude, ArrayList<Integer> latitude, ArrayList<Integer> vitesse,
			ArrayList<Integer> direction, Date date) {
		this.Longitude = longitude;
		this.Latitude = latitude;
		Vitesse = vitesse;
		Direction = direction;
		this.Date = date;
	}


	public ArrayList<Integer> getLongitude() {
		return Longitude;
	}


	public void setLongitude(ArrayList<Integer> longitude) {
		this.Longitude = longitude;
	}


	public ArrayList<Integer> getLatitude() {
		return Latitude;
	}


	public void setLatitude(ArrayList<Integer> latitude) {
		this.Latitude = latitude;
	}


	public ArrayList<Integer> getVitesse() {
		return Vitesse;
	}


	public void setVitesse(ArrayList<Integer> vitesse) {
		Vitesse = vitesse;
	}


	public ArrayList<Integer> getDirection() {
		return Direction;
	}


	public void setDirection(ArrayList<Integer> direction) {
		Direction = direction;
	}


	public Date getDate() {
		return Date;
	}


	public void setDate(Date date) {
		this.Date = date;
	}
	
	
	
}
