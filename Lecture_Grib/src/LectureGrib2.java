import java.io.IOException;

import ucar.ma2.IndexIterator;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

public class LectureGrib2 {

	public LectureGrib2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = "C:\\Users\\mathieu\\Downloads\\20170314_142130_.grb";
		NetcdfFile nc = null;
		nc.open(filename);
			    /* Get the value of the global attribute named "title" */
			    Attribute titleA = nc.findAttribute("title");
			    String title = titleA.getStringValue();
						    /* Read the latitudes into an array of double, reading one
		               value at a time.  This works regardless of the external
		               type of the "lat" variable. */
			    Variable lat = nc.findVariable("lat");
			    assert(lat.getRank() == 1);	// make sure it's 1-dimensional
			    int nlats = (int) lat.getSize(); // number of latitudes
			    double [] lats = new double[nlats];	// where to put them
			    int[] index = new int[1]; // index array to specify which value
			    for (int ilat = 0; ilat < nlats; ilat++) {
				index[0] = ilat;
				lats[ilat] = lat.getDouble(index);
			    }
			    /* Read units attribute of lat variable */
			    String latUnits = lat.findAttribute("units").getStringValue();
			
			    /* Read the longitudes.  This illustrates a different
		               approach, using a MultiArray to read them all at once. */
			    Variable lon =  nc.findVariable("lon");
			    int[] origin = new int[lon.getRank()]; // where to start
			    int[] extent = lon.getLengths(); // how many along each dimension
			    MultiArray lonMa = lon.copyout(origin, extent);

			    /* Now we can just use the MultiArray to access values, or
			       we can copy the MultiArray elements to another array with
			       toArray(), or we can get access to the MultiArray storage
			       without copying.  Each of these approaches to accessing
			       the data are illustrated below. */
			    debug("lons[3]: " + lonMa.getFloat(new int[] {3}));
			    
			    /* Read the times */
			    Variable time = nc.get("time");
			    origin = new int[time.getRank()];
			    extent = time.getLengths();
			    MultiArray timeMa = time.copyout(origin, extent);
			    debug("time[1]: " + timeMa.getFloat(new int[] {1}));
			    
			    /* Read the relative humidity data */
			    Variable rh = nc.get("rh");
			    int[] rhShape = rh.getLengths();
			    /* assume we know rank is 3 */
			    int[][][] rhData = new int[rhShape[0]][rhShape[1]][rhShape[2]];
			    /* reading a single value is simple */
			    rhData[1][0][2] = rh.getInt(new int[] {1,0,2});
			    debug("rh[1][0][2]: " + rhData[1][0][2]);

			    /* Reading all the values, one at a time, is similar */
			    int[] ix = new int[3];
			    for (int itime = 0; itime < rhData.length; itime++) {
				ix[0] = itime;
				for (int ilat = 0; ilat < rhData[0].length; ilat++) {
				    ix[1] = ilat;
				    for (int ilon = 0; ilon < rhData[0][0].length; ilon++) {
					ix[2] = ilon;
					rhData[itime][ilat][ilon] = rh.getInt(ix);
				    }
				}
			    }
			    debug("rh[0][0][0]: " + rhData[0][0][0]);

			    /* Read the relative humidity data one value at a time
		               again, but this time use an IndexIterator instead to
		               avoid the nested loops. */

			    /* First create a MultiArray wrapper for rhData array.  Note
			       this does not copy storage, but shares the same storage as
			       rhData. */
			    MultiArray rhMa = new ArrayMultiArray(rhData);
			    /* Now read the data into the MultiArray, which also reads
		               it into the shared storage of the array. */
			    for (IndexIterator rhIx = new IndexIterator(rhShape);
				 rhIx.notDone(); 
				 rhIx.incr()) {
				rhMa.setInt(rhIx.value(), rh.getInt(rhIx.value()));
			    }
			    debug("rh[1][1][1]: " + rhData[1][1][1]);

			    /* Read the temperature data all at once, as a MultiArray */
			    Variable temperature = nc.get("T");
			    origin = new int[temperature.getRank()];
			    extent = temperature.getLengths();
			    MultiArray tMa = temperature.copyout(origin, extent);
			    /* Access the value of the [0][0][0] temperature as a double. */
			    double t000 = tMa.getDouble(new int[]{0,0,0});
			    debug("T[0][0][0]: " + t000);

		            /* Now access a value as a float.  Note that we can't use
			       getFloat() to get the value as a float, because
			       temperature's external type is double and converting from
			       double to float is not a "widening conversion" (it can
			       lose information).  But we can use getDouble() and then
			       explicitly cast the double result to float, which
			       accomplishes the same thing ... */
			    float t001 = (float) tMa.getDouble(new int[]{0,0,1});
			    debug("T[0][0][1]: " + t001);

			    int[] tLengths = tMa.getLengths();
			    
			    /* Use the toArray() method of a MultiArray to convert it
			       into a 1-dimensional array of doubles, then access the
			       [1][2][3] element by computing where it would be in the
			       resulting 1-dimensional array.  Note: the toArray()
			       method is only available in versions of MultiArray after
			       1998-04-19. */
			    double[] t1d = (double[]) tMa.toArray();
			    double t123 = t1d[(1 * tLengths[1] + 2) * tLengths[2] + 3];
			    debug("T[1][2][3]: " + t123);

			    /* Use the public storage member of an ArrayMultiArray to
		               get at the array storage and then access the [0][1][2]
		               element by computing where it would be in a 1-dimensional
		               array. */
			    MultiArrayImpl tAma = (MultiArrayImpl) tMa;
			    double[] tD = (double[]) tAma.storage;
			    double t012 = tD[(0 * tLengths[1] + 1) * tLengths[2] + 2];
			    debug("T[0][1][2]: " + t012);

			    System.out.println("read " + fileName + " successfully");

			} catch (java.io.IOException e) {
			    e.printStackTrace();
			}

		    }

		    /**
		     * Rudimentary assertion checker.
		     *
		     * @param pred assertion to be checked.  If false, stacktrace is printed
		     *             and program exits.
		     */
		    static void assert(boolean pred) {
			if (pred)
			    return;
			try {
			    throw new Exception("assertion violated");
			} catch (Exception e) {
			    e.printStackTrace();
			}
			System.exit(1);
		    }
						
					

		
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
