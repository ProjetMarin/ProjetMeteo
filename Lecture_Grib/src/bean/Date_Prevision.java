package bean;

import java.util.ArrayList;
import java.util.Date;

public class Date_Prevision {
	Date datePrevision;
	ArrayList<Position> listPos;
	
	public Date_Prevision(Date d){
		this.datePrevision = d;
		this.listPos = new ArrayList<Position>();
	}
	public void addPosition(Position p){
		this.listPos.add(p);
	}
	public void removePosition(Position p){
		this.listPos.remove(p);
	}
	public Date getDatePrevision() {
		return datePrevision;
	}
	public void setDatePrevision(Date datePrevision) {
		this.datePrevision = datePrevision;
	}
	public ArrayList<Position> getListPos() {
		return listPos;
	}
	@Override
	public String toString() {
		String res = "Date_Prevision [" + datePrevision+" ,";
		for(Position p : listPos){
			res += "\n"+p.toString();
		}
		return res+"";
	}
	
	public boolean equalsDate(Date_Prevision dp){
		Position p1,p;
		if(dp.getDatePrevision().equals(this.datePrevision)){
			for(int i = 0;i<this.listPos.size();i++){
				p = listPos.get(i);
				for(int j = 0;j<dp.getListPos().size();j++){
					p1 = dp.getListPos().get(j);
					if(p.getLatitude() == p1.getLatitude() && p.getLongitude() == p1.getLongitude()){
						return  true;
					}
				}
			}
		}
		return false;
	}
	
}
