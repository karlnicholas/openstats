package openstats.util;

import java.io.*;
import java.util.*;

import openstats.osmodel.*;

import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.prefs.CsvPreference;

public class AssemblyCsvHandler {

	private class MyCsvWriter extends AbstractCsvWriter {

		public MyCsvWriter(Writer writer, CsvPreference preference) {
			super(writer, preference);
		}
		public void writeRow(String... columns ) throws IOException {
			super.writeRow(columns);
		}
	}
	
	public List<String> createHeader(OSAssembly osAssembly) throws Exception {
		List<String> csvHeader = new ArrayList<String>();

        OSDistricts districts = osAssembly.getOSDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        csvHeader.add("District");
        csvHeader.add("Chamber");
//			Aggregate aggregate = districts.getAggregate(GROUPLABEL);

        for ( String label: districts.getAggregateGroupInfo().getGroupLabels()) {
        	csvHeader.add(label);
        }
        for ( String label: districts.getComputationGroupInfo().getGroupLabels()) {
        	csvHeader.add(label);
        }

    	OSGroupInfo groupInfo = osAssembly.getAggregateGroupInfo();
    	if ( groupInfo != null ) {
	        for ( String label: groupInfo.getGroupLabels()) {
	        	csvHeader.add(label);
	        }
    	}
    	groupInfo = osAssembly.getComputationGroupInfo();
    	if ( groupInfo != null ) {
	        for ( String label: groupInfo.getGroupLabels()) {
	        	csvHeader.add(label);
	        }
    	}
        return csvHeader;
	}


	public List<List<String>> createBody(OSAssembly osAssembly) throws Exception {
		List<List<String>> csvResult = new ArrayList<List<String>>();
		int rowOffset = 0;

        OSDistricts districts = osAssembly.getOSDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        List<String> row = new ArrayList<String>();


        // write data for districts 
        for ( final OSDistrict dist: districts.getOSDistrictList()) {
        	row = new ArrayList<String>();
        	row.add(dist.getDistrict());
        	row.add(dist.getChamber());
        	List<Long> aggs = dist.getAggregateValues();
	        for ( Long agg: aggs ) {
	        	row.add(agg.toString());
	        }
	        List<Double >comps = dist.getComputationValues();
	        for ( Double comp: comps ) {
	        	row.add(comp.toString());
	        }
	        csvResult.add(row);
        }
        rowOffset = row.size();
        // write data for osAssembly
    	row = new ArrayList<String>();
    	for ( int i=0; i<rowOffset; ++i ) {
    		row.add("");
    	}
        List<Long> aggs = osAssembly.getAggregateValues();
        if ( aggs != null ) {
	        for ( Long agg: aggs ) {
	        	row.add(agg.toString());
	        }
        }
        List<Double> comps = osAssembly.getComputationValues();
        if ( comps != null ) {
	        for ( Double comp: comps ) {
	        	row.add(comp.toString());
	        }
        }
        csvResult.add(row);
        return csvResult;
	}

	public void writeCsv(OutputStream out, OSAssembly osAssembly) throws Exception {
        
		OutputStreamWriter writer = new OutputStreamWriter(out);
		List<String> csvHeader = createHeader(osAssembly);
		List<List<String>> csvBody = createBody(osAssembly);
		
		MyCsvWriter csvWriter = null;
        try {
        	csvWriter = new MyCsvWriter(writer, CsvPreference.STANDARD_PREFERENCE);
	        String[] sColumns = new String[csvHeader.size()];
	        sColumns = csvHeader.toArray(sColumns);
            csvWriter.writeHeader(sColumns);

	        for ( List<String> row: csvBody) {
		        sColumns = row.toArray(sColumns);
	            csvWriter.writeHeader(sColumns);
	        }
        }
        finally {
            if( csvWriter != null ) {
            	csvWriter.close();
            }
        }
	}

}
