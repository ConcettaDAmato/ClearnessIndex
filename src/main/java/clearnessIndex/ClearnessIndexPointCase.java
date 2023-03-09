/*
 * GNU GPL v3 License
 *
 * Copyright 2015 Marialaura Bancheri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package clearnessIndex;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.Label;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Out;
import oms3.annotations.Status;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.hortonmachine.gears.libs.modules.HMConstants;
import org.hortonmachine.gears.libs.modules.HMModel;
import org.opengis.feature.simple.SimpleFeature;

import static org.hortonmachine.gears.libs.modules.HMConstants.doubleNovalue;

import com.vividsolutions.jts.geom.Coordinate;

@Description("The component computes the Clearness index, which is the ratio between the incoming shortwave"
		+ "and the shortwave at the top of the atmosphere")
@Author(name = "Marialaura Bancheri and Giuseppe Formetta", contact = "maryban@hotmail.it")
@Keywords("Hydrology, clearness index")
@Label(HMConstants.HYDROGEOMORPHOLOGY)
@Name("ClearnessIndexPointCase")
@Status(Status.CERTIFIED)
@License("General Public License Version 3 (GPLv3)")
public class ClearnessIndexPointCase extends HMModel {

	@Description("The Hashmap with the time series of the SWRB meadured values")
	@In
	public HashMap<Integer, double[]> inSWRBMeasuredValues;

	@Description("The double value of the SWRB meadured, once read from the HashMap")
	double SWRBMeasured;
	
	@Description("The Hashmap with the time series of the SWRB at the top of the atmosphere values")
	@In
	public HashMap<Integer, double[]> inSWRBTopATMValues;

	@Description("The double value of the SWRB at the top of the atmosphere, once read from the HashMap")
	double SWRBTopATM;
	

	@Description("the linked HashMap with the coordinate of the stations")
	LinkedHashMap<Integer, Coordinate> stationCoordinates;
	
	@Description(" The output CI HashMap")
	@Out
	public HashMap<Integer, double[]> outCIHM= new HashMap<Integer, double[]>();



	@Execute
	public void process() throws Exception { 
		checkNull(inSWRBMeasuredValues);


		// reading the ID of all the stations 
		Set<Entry<Integer, double[]>> entrySet = inSWRBMeasuredValues.entrySet();

		for (Entry<Integer, double[]> entry : entrySet) {
			
			Integer ID = entry.getKey();

			// try to read the input data for the given station
			try {
				SWRBMeasured=inSWRBMeasuredValues.get(ID)[0];
				SWRBTopATM=inSWRBTopATMValues.get(ID)[0];
			} catch (Exception e) {
				// skip loop
				String warningMessage = "Missing data for station " + ID + "\n";
				warningMessage += "\t No computation will be done. Skipped.";
				System.out.println(warningMessage);
				continue;
			}


			// compute the clearness index
			double CI=(SWRBTopATM==0)?doubleNovalue:SWRBMeasured/SWRBTopATM;

			//store the results
			storeResult_series(ID,CI);

		}

	}

	
	/**
	 * Store result_series stores the results in the hashMaps .
	 *
	 * @param ID is the id of the station 
	 * @param CI is the clearness Index
	 * @throws SchemaException 
	 */

	private void storeResult_series(Integer ID,double CI) throws SchemaException {
		outCIHM.put(ID, new double[]{CI});

	}

}
