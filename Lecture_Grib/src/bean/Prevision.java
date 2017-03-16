package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Prevision {
	ArrayList<Date_Prevision> listDate;
	
	public Prevision(){
		listDate = new ArrayList<Date_Prevision>();
	}
	
	public Wind getWind(Date d,double lat,double lon){
		Wind res = null;
		for(Date_Prevision date : listDate){
			if(date.getDatePrevision().equals(d)){
				ArrayList<Position> alPos = date.getListPos();
				for(Position p : alPos){
					if(p.getLatitude() == lat && p.getLongitude()== lon){
						res = p.getWind();
					}
				}
			}
		}
		return res;
	}
	
	public void addDate(Date_Prevision dp){
		if(!this.containsDate(dp)){
			listDate.add(dp);
		}else{

			for(int i =0; i< listDate.size();i++){

				if(listDate.get(i).equalsDate(dp)){

					listDate.set(i, dp);

				}
			}
		}
	}

	@Override
	public String toString() {
		String res = "Prevision : ";
		for(Date_Prevision dp : listDate){
			res+= dp.toString();
		}
		return res;
	}
	
	private boolean containsDate(Date_Prevision dp){
		// Test contain date_prevision
		for(Date_Prevision d : listDate){
			if(d.equalsDate(dp)){
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args){
		Prevision p = new Prevision();
		Wind w1 = new Wind(15, 0);
		Wind w2 = new Wind(20, 0);
		Position pos1 = new Position(0, 0);
		pos1.createVent(w1);
		Date_Prevision dp1 = new Date_Prevision(new Date());
		Date_Prevision dp2 = new Date_Prevision(new Date());
		Position pos2 = new Position(10,10);
		pos2.createVent(w1);
		dp2.addPosition(pos2);
		dp1.addPosition(pos1);
System.out.println("1");
		p.addDate(dp1);
		System.out.println(p.toString());
System.out.println("2");
		p.addDate(dp2);
		System.out.println(p.toString());

System.out.println("3");
		pos1.setWind(w2);
		p.addDate(dp1);
		System.out.println(p.toString());

	}

}
