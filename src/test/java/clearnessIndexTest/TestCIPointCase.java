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

import org.hortonmachine.gears.io.timedependent.OmsTimeSeriesIteratorReader;
import org.hortonmachine.gears.io.timedependent.OmsTimeSeriesIteratorWriter;


import org.junit.Test;

import clearnessIndex.ClearnessIndexPointCase;



/**
 * Test the {@link ClearnessIndexPointCase} module.
 * 
 * @author Marialaura Bancheri
 */
public class TestCIPointCase{

	@Test
	public void Test() throws Exception {


		String startDate = "2007-10-17 00:00" ;
		String endDate = "2007-10-17 23:00";
		int timeStepMinutes = 60*24;
		String fId = "ID";

		String inPathToSWRBmeasured ="resources/Input/SWRBMeasured.csv";
		String inPathToTopATM ="resources/Input/TopATM.csv";
		String pathToCI= "resources/Output/CI.csv";


		OmsTimeSeriesIteratorReader SWRBreader = getTimeseriesReader(inPathToSWRBmeasured, fId, startDate, endDate, timeStepMinutes);
		OmsTimeSeriesIteratorReader topATMreader = getTimeseriesReader(inPathToTopATM, fId, startDate, endDate, timeStepMinutes);



		OmsTimeSeriesIteratorWriter writerCI = new OmsTimeSeriesIteratorWriter();


		
		writerCI.file = pathToCI;
		writerCI.tStart = startDate;
		writerCI.tTimestep = timeStepMinutes;
		writerCI.fileNovalue="-9999";

		 

		ClearnessIndexPointCase CI = new ClearnessIndexPointCase();

		while( topATMreader.doProcess  ) { 


			
			topATMreader.nextRecord();	
			HashMap<Integer, double[]> id2ValueMap = topATMreader.outData;
			CI.inSWRBTopATMValues= id2ValueMap;

			SWRBreader.nextRecord();
			id2ValueMap = SWRBreader.outData;
			CI.inSWRBMeasuredValues = id2ValueMap;
			
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
