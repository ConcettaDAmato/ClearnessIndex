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

import org.geotools.coverage.grid.GridCoverage2D;

import org.jgrasstools.gears.io.rasterreader.OmsRasterReader;
import org.jgrasstools.gears.io.rasterwriter.OmsRasterWriter;

import org.junit.Test;


import clearnessIndex.ClearnessIndexRasterCase;

/**
 * Test the CI module.
 * 
 * @author Marialaura Bancheri
 */
public class TestCIRasterCase{

	GridCoverage2D outCIDataGrid2 ;
	
	@Test
	public void Test() throws Exception {


		OmsRasterReader SWRBmeasuredReader = new OmsRasterReader();
		SWRBmeasuredReader.file = "resources/Input/dtm.asc";
		SWRBmeasuredReader.fileNovalue = -9999.0;
		SWRBmeasuredReader.geodataNovalue = Double.NaN;
		SWRBmeasuredReader.process();
		GridCoverage2D SWRBmeasured = SWRBmeasuredReader.outRaster;
		
		OmsRasterReader SWRBtopReader = new OmsRasterReader();
		SWRBtopReader.file = "resources/Input/dtm.asc";
		SWRBtopReader.fileNovalue = -9999.0;
		SWRBtopReader.geodataNovalue = Double.NaN;
		SWRBtopReader.process();	
		GridCoverage2D SWRBtop = SWRBtopReader.outRaster;
		
		OmsRasterReader demReader = new OmsRasterReader();
		demReader.file = "resources/Input/dtm.asc";
		demReader.fileNovalue = -9999.0;
		demReader.geodataNovalue = Double.NaN;
		demReader.process();	
		GridCoverage2D dem = demReader.outRaster;
		
		


		ClearnessIndexRasterCase CI = new ClearnessIndexRasterCase();
		CI.inSWRBMeasuredGrid=SWRBmeasured;
		CI.inSWRBTopATMGrid=SWRBtop;
		CI.inDem = dem;


		CI.process();


		GridCoverage2D CICoverage = CI.outCIDataGrid;
		
		OmsRasterWriter writerRainfallRaster = new OmsRasterWriter();
		writerRainfallRaster.inRaster = CICoverage;
		writerRainfallRaster.file = "resources/Output/CI.asc";
		writerRainfallRaster.process();



	}


}
