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
package clearnessIndexTest;

import java.net.URISyntaxException;
import java.util.HashMap;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.jgrasstools.gears.io.shapefile.OmsShapefileFeatureReader;
import org.jgrasstools.gears.io.timedependent.OmsTimeSeriesIteratorReader;
import org.jgrasstools.gears.io.timedependent.OmsTimeSeriesIteratorWriter;
import org.jgrasstools.hortonmachine.utils.HMTestCase;



import clearnessIndex.ClearnessIndexPointCase;



/**
 * Test the {@link ClearnessIndexPointCase} module.
 * 
 * @author Marialaura Bancheri
 */
public class TestCIPointCase extends HMTestCase {

	
	public TestCIPointCase() throws Exception {


		String startDate = "2007-10-17 00:00" ;
		String endDate = "2007-10-18 00:00";
		int timeStepMinutes = 60;
		String fId = "ID";

		String inPathToSWRBmeasured ="resources/Input/SWRBMeasured.csv";
		String inPathToTopATM ="resources/Input/TopATM.csv";
		String pathToCI= "resources/Output/CI.csv";


		OmsTimeSeriesIteratorReader SWRBreader = getTimeseriesReader(inPathToSWRBmeasured, fId, startDate, endDate, timeStepMinutes);
		OmsTimeSeriesIteratorReader topATMreader = getTimeseriesReader(inPathToTopATM, fId, startDate, endDate, timeStepMinutes);


		
		OmsShapefileFeatureReader stationsReader = new OmsShapefileFeatureReader();
		stationsReader.file = "resources/Input/stations.shp";
		stationsReader.readFeatureCollection();
		SimpleFeatureCollection stationsFC = stationsReader.geodata;



		OmsTimeSeriesIteratorWriter writerCI = new OmsTimeSeriesIteratorWriter();


		
		writerCI.file = pathToCI;
		writerCI.tStart = startDate;
		writerCI.tTimestep = timeStepMinutes;
		writerCI.fileNovalue="-9999";

		 

		ClearnessIndexPointCase CI = new ClearnessIndexPointCase();
		CI.inStations = stationsFC;
		CI.fStationsid = "cat" ;

		while( topATMreader.doProcess  ) { 


			
			topATMreader.nextRecord();	
			HashMap<Integer, double[]> id2ValueMap = topATMreader.outData;
			CI.inSWRBTopATMValues= id2ValueMap;

			SWRBreader.nextRecord();
			id2ValueMap = SWRBreader.outData;
			CI.inSWRBMeasuredValues = id2ValueMap;
			

			CI.pm = pm;

			CI.process();
			
			
			 HashMap<Integer, double[]> outHM = CI.outCIHM;
	            
				writerCI.inData = outHM;
				writerCI.writeNextLine();
				
				
				
				if (pathToCI != null) {
					writerCI.close();
				}

	        
			
		}
		
		topATMreader.close();
		SWRBreader.close();


	}

	private OmsTimeSeriesIteratorReader getTimeseriesReader( String inPath, String id, String startDate, String endDate,
			int timeStepMinutes ) throws URISyntaxException {
		OmsTimeSeriesIteratorReader reader = new OmsTimeSeriesIteratorReader();
		reader.file = inPath;
		reader.idfield = "ID";
		reader.tStart = startDate;
		reader.tTimestep = timeStepMinutes;
		reader.tEnd = endDate;
		reader.fileNovalue = "-9999";
		reader.initProcess();
		return reader;
	}

}
