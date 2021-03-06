import java.io.IOException;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.lang.Math;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Group;
import ucar.nc2.NCdumpW;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.constants.FeatureType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateAxis1DTime;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDataset;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.ft.FeatureDataset;
import ucar.nc2.ft.FeatureDatasetFactoryManager;
import ucar.unidata.io.RandomAccessFile;

public class LectureGribUnidata {

	public LectureGribUnidata() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String filename = "H:\\Documents\\IDL\\20170314_142130_.grb";

		NetcdfFile ncfile = null;
		try {
			ncfile = NetcdfFile.open(filename);
			// System.out.println(ncfile.getDetailInfo());

			//		    System.out.println(ncfile.getGlobalAttributes());
			List<Dimension> dim = ncfile.getDimensions();
					    System.out.println(dim.toString());
			//  List<Variable> var = ncfile.getVariables();
			// System.out.println(var.toString());
			//System.out.println(ncfile.getRootGroup());
			// System.out.println(ncfile);
			//		    for(Dimension dimension : dim){
			//		    	System.out.println(dimension.getGroup());
			//		    }
			//System.out.println(ncfile.getGlobalAttributes());

			Group groupe = ncfile.getRootGroup();
			List<Dimension> list= groupe.getDimensions();
			System.out.println(list.toString());
			List<Variable> listVar = groupe.getVariables();
			System.out.println(listVar.toString());
			java.util.List<Array> array = ncfile.readArrays(listVar);
			//System.out.println(array.toString());
			List<Attribute> listAtt = groupe.getAttributes();

			//récupération et affichage lattitude
			Variable varLat = groupe.findVariable("lat");
			//System.out.println(varLat.toString());
			Array testlat = varLat.read();
			System.out.println(testlat.toString());

			Variable varLon = groupe.findVariable("lon");
		//	System.out.println(varLon.toString());
			Array testLon = varLon.read();
			System.out.println(testLon.toString());

			
			//Récupération et affichage du temp
			Variable varTemp = groupe.findVariable("time");
		//	System.out.println(varTemp.toString());
			Array testTemp = varTemp.read();
			System.out.println(testTemp.toString());
			System.out.println(varTemp.getDescription());
			System.out.println(varTemp.getDataType());
			System.out.println(varTemp.getUnitsString());
			String[] d = varTemp.getUnitsString().split(" ");
			String[] h = d[2].split("T");
			String day = h[0];
			String hours = h[1];
			hours = hours.replace("Z", "");
			System.out.println(day);
			System.out.println(hours);
			String[] dayToDate = day.split("-");
			String[] hoursToDate = hours.split(":");
			int y = Integer.parseInt(dayToDate[0]);
			int m = Integer.parseInt(dayToDate[1]);
			int da = Integer.parseInt(dayToDate[2]);
			System.out.println(hoursToDate[0]);
			int ho = Integer.parseInt(hoursToDate[0]);
			int min = Integer.parseInt(hoursToDate[1]);
			int sec = Integer.parseInt(hoursToDate[2]);
			
			Date date = new Date(y,m,da,ho,min,sec);
			System.out.println(date.toString());//Date date = new Date(varTemp.getUnitsString());
			Variable varTempRef = groupe.findVariable("reftime");
		//	System.out.println(varTempRef.toString());
			Array testTempRef = varTemp.read();
			System.out.println(testTempRef.toString());

			Variable varHeight = groupe.findVariable("height_above_ground");
			//	System.out.println(varTemp.toString());
			Array testHeight = varHeight.read();
			System.out.println(testHeight.toString());
			
			Variable varU = groupe.findVariable("u-component_of_wind_height_above_ground");
			//   System.out.println(varU.toString());
			Array testU = varU.read();
			System.out.println(testU.toString());
			
			Variable varV = groupe.findVariable("v-component_of_wind_height_above_ground");
			//   System.out.println(varU.toString());
			Array testV = varV.read();
			System.out.println(testV.toString());
			
			System.out.println(testlat.getSize());
			System.out.println(testLon.getSize());
			System.out.println(testHeight.getSize());
			System.out.println(testTemp.getSize());
			System.out.println(testU.getSize());
			System.out.println(testV.getSize());

			for(int iLat=0;iLat<testlat.getSize();iLat++){
				for(int iLon=0;iLon<testLon.getSize();iLon++){
					for(int iTime=0;/*iTime<testTemp.getSize()*/iTime<1;iTime++){								
						for(int iHeight=0;iHeight<testHeight.getSize();iHeight++){
							int iU = 0;
							int iTest;
							
							iU = (int) (iTime * (testLon.getSize()*testTemp.getSize()*testHeight.getSize()));
							iU = iU + (int) (iHeight * (testLon.getSize()*testTemp.getSize()));
							iU = iU + (int) (iLat * (testLon.getSize()));
							iU = iU + iLon;
//							if(iTime!=0&&iU!=0) iU=iU*iTime;
//							else if(iTime!=0) iU=iTime;
//							if(iHeight!=0&&iU!=0) iU=iU*iHeight;
//							else if(iHeight!=0) iU=iHeight;
//						
//							
//							if(iLat!=0&&iU!=0) iU=iU*iLat;
//							else if (iLat!=0) iU=iLat;
//							if(iLon!=0&&iU!=0) iU=iU*iLon;
//							else if(iLon!=0) iU=iLon;
//							
							System.out.println("iU = "+iU);
							
							double vit = Math.sqrt(Math.pow(testU.getFloat(iU),2)+Math.pow(testV.getFloat(iU),2));
							double angle2 = 180/Math.PI*Math.atan2(testU.getFloat(iU),testV.getFloat(iU))+180;
						
							System.out.println(testlat.getFloat(iLat));
							System.out.println("\t"+testLon.getFloat(iLon));
							System.out.println("\t\t"+testTemp.getFloat(iTime));
							System.out.println("\t\t\t"+testHeight.getFloat(iHeight));
							System.out.println("\t\t\t\t"+testU.getFloat(iU));
							System.out.println("\t\t\t\t"+testV.getFloat(iU));
							System.out.println("vit = "+vit);
							System.out.println("Angle = "+angle2);
						}
					}
				}
			}
						
					

	
//			for(int iLat=0;iLat<testlat.getSize();iLat++){
//				System.out.println(testlat.getFloat(iLat));
//				for(int iLon=0;iLon<testLon.getSize();iLon++){
//					System.out.println("\t"+testLon.getFloat(iLon));
//					for(int iTime=0;iTime<testTemp.getSize();iTime++){
//						System.out.println("\t\t"+testTemp.getFloat(iTime));
//						for(int iHeight=0;iHeight<testHeight.getSize();iHeight++){
//							System.out.println("\t\t\t"+testHeight.getFloat(iHeight));
//							for(int iU=0;iU<testU.getSize();iU++){
//								for(int iV=0;iV<testV.getSize();iV++){
//									double vit = Math.sqrt(Math.pow(testU.getFloat(iU),2)+Math.pow(testV.getFloat(iV),2));
//									float lonU = testU.getFloat(iU);
//									float latU = 0;
//									float lonV = lonU;
//									float latV = testV.getFloat(iV);
//									
//									double angle = Math.acos((lonU*lonV+latU+latV)/(Math.sqrt(Math.pow(lonU,2)+Math.pow(latU, 2))*Math.sqrt(Math.pow(lonU,2)+Math.pow(latU, 2)))))
//									System.out.println();
//								}
//							}
//						}
//					}
//				}
//			}
//			
		
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage()); 
		} finally { 
			if (null != ncfile) try {
				ncfile.close();
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
			}
		}


	}

}
