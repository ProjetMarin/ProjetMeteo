package gribReader;

import java.io.IOException;
import java.util.Date;

import ucar.ma2.Array;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

public class GribReader {
	
	/*	Fichier permettant la lecture des données	*/
	public NetcdfFile ncdfFile;
	/*	Contient le nom du fichier grib qui sera ouvert avec netCDF	*/
	public String fileName;
	
	public GribReader(String name){
		this.fileName = name;
	}
	
	/*	Gestion du fichier NCDF	*/
	public void openFile() throws IOException{
		ncdfFile = ncdfFile.open(this.fileName);
		if(ncdfFile == null){
			System.out.println("null");
			throw new IOException();
		}
	}
	public void closeFile() throws IOException{
		ncdfFile.close();
	}
	
	/*	Lecture dans le fichier	*/
	public void readFile() throws IOException{
		/*	Variable utilisé pour creer une nouvelle prévision	*/
		Float lat,lon,time,u,v;
		double spd,angle;
		Date date;
		/*	Variable utilisé pour stocker les valeurs du fichier	*/
		Variable var;
		Array latitudes,longitudes,times,heights,aU,aV;
		/*	Récupération du RootGroup	*/
		Group groupe = ncdfFile.getRootGroup();
		
		/*	Recuperation de la Lattitude	*/
		var = groupe.findVariable("lat");
		latitudes = var.read();
		if(latitudes == null){System.out.println("latitudes -  NULL");throw new IOException();}
		/*	Recuperation de la Longitude	*/
		var = groupe.findVariable("lon");
		longitudes = var.read();
		if(longitudes == null){System.out.println("longitudes -  NULL");throw new IOException();}
		/*	Recuperation times	*/
		var = groupe.findVariable("time");
		times = var.read();
		if(times == null){System.out.println("times -  NULL");throw new IOException();}
		date = getDate(var);
		
		/*	Recuperation heights	*/
		var = groupe.findVariable("height_above_ground");
		heights = var.read();
		if(heights == null){System.out.println("heights -  NULL");throw new IOException();}

		/*	Recuperation u	*/
		var = groupe.findVariable("u-component_of_wind_height_above_ground");
		aU = var.read();
		if(aU == null){System.out.println("U -  NULL");throw new IOException();}

		/*	Recuperation v	*/
		var = groupe.findVariable("v-component_of_wind_height_above_ground");
		aV = var.read();
		if(aV == null){System.out.println("V -  NULL");throw new IOException();}

	
	}
	public Date getDate(Variable v){
		Date res;
		String[] stringWithDate = v.getUnitsString().split(" ");
		String[] stringDate = stringWithDate[2].split("T");
		String date = stringDate[0];
		String time = stringDate[1];
		time = time.replace("Z","");
		String[] dayToDate = date.split("-");
		String[] timeToDate = time.split(":");

		res = new Date(Integer.parseInt(dayToDate[0]),Integer.parseInt(dayToDate[1]),Integer.parseInt(dayToDate[2]),
				Integer.parseInt(timeToDate[0]),Integer.parseInt(timeToDate[1]),Integer.parseInt(timeToDate[2]));
		System.out.println(res);
		return res;
	}
	public static void main(String[] args){
		String filename = "H:\\Documents\\IDL\\20170314_142130_.grb";
		GribReader lg = new GribReader(filename);
		System.out.println(lg.fileName);

		try{
			lg.openFile();
			lg.readFile();
			lg.closeFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
