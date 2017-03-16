import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.util.xml.Parse;


public class CDF {


	public static void EcritureCDF(PointCDF p, String path) throws IOException{

		String location = path;
		NetcdfFileWriter writer = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, location, null);

		Dimension lat = null;
		Dimension lon = null;
		Dimension vit = null;
		Dimension dir = null;

		for (int i = 0; i < p.getLatitude().size() ; i++) {
			lon = writer.addDimension(null, "lon"+i, p.getLongitude().get(i));
			lat = writer.addDimension(null, "lat"+i, p.getLatitude().get(i));
			writer.addGroupAttribute(null, new Attribute("vitesse"+i,p.getVitesse().get(i)));
			writer.addGroupAttribute(null, new Attribute("dir"+i,p.getDirection().get(i)));
		}

		try {
			writer.create();
		} catch (IOException e) {
			System.err.printf("ERROR creating file %s%n%s", location, e.getMessage());
		}

		writer.close();

	}


	public static PointCDF LectureCDF(String path) throws IOException{

		NetcdfFile ncfile;

		ArrayList<Integer> longitude = new ArrayList<Integer>();
		ArrayList<Integer> latitude = new ArrayList<Integer>();
		ArrayList<Integer> Vitesse = new ArrayList<Integer>();
		ArrayList<Integer> Direction = new ArrayList<Integer>();


		ncfile = NetcdfFile.open(path);

		System.out.println(ncfile.getDetailInfo());
		System.out.println("-------------------------");

		List<Dimension> dim = ncfile.getDimensions();
		List<Attribute> att = ncfile.getGlobalAttributes();


		for(int i = 0; i < dim.size()/2 ; i++){

		/*	System.out.println("--------Mesure "+i+"---------");
			System.out.println("-------------------------");
			System.out.println(ncfile.findDimension("lon"+i));
			System.out.println(ncfile.findDimension("lat"+i));
			System.out.println(ncfile.findGlobalAttribute("vitesse"+i));
			System.out.println(ncfile.findGlobalAttribute("dir"+i));
			System.out.println("-------------------------");
	*/	
			

			longitude.add(Integer.parseInt(ncfile.findDimension("lon"+i).toString().split("=")[1].replaceAll("[^0-9-]", "")));
			latitude.add(Integer.parseInt((ncfile.findDimension("lat"+i).toString().split("=")[1].replaceAll("[^0-9-]", ""))));
			Vitesse.add(Integer.parseInt((ncfile.findGlobalAttribute("vitesse"+i).toString().split("=")[1].replaceAll("[^0-9-]", ""))));
			Direction.add(Integer.parseInt(ncfile.findGlobalAttribute("dir"+i).toString().split("=")[1].replaceAll("[^0-9-]", "")));

			/*
			System.out.println(longitude);
			System.out.println(latitude);
			System.out.println(Vitesse);
			System.out.println(Direction);
			 */
		}


		PointCDF p = new PointCDF(longitude, latitude, Vitesse, Direction, null);

		return p;

	}

	

	public static void main(String[] args) throws IOException {

		ArrayList<Integer> longitude = new ArrayList<Integer>();
		ArrayList<Integer> latitude = new ArrayList<Integer>();
		ArrayList<Integer> Vitesse = new ArrayList<Integer>();
		ArrayList<Integer> Direction = new ArrayList<Integer>();

		Date date = new Date();

		longitude.add(1);
		longitude.add(2);
		longitude.add(3);

		latitude.add(4);
		latitude.add(5);
		latitude.add(6);

		Vitesse.add(10);
		Vitesse.add(25);
		Vitesse.add(14);


		Direction.add(-100);
		Direction.add(45);
		Direction.add(180);


		PointCDF p = new PointCDF(longitude,latitude,Vitesse,Direction,date);


		String path = "test.nc";

		EcritureCDF(p,path);
		PointCDF p1 = LectureCDF(path);
		
		System.out.println(p1.getDirection());
		System.out.println(p1.getLatitude());
		System.out.println(p1.getLongitude());
		System.out.println(p1.getVitesse());


	}


}
