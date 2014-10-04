package openstats.util;
import java.io.*;
import java.util.*;

import openstats.model.*;

import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.prefs.CsvPreference;


public class AssemblyCsvHandler {
	private 
	
	class MyCsvWriter extends AbstractCsvWriter {

		public MyCsvWriter(Writer writer, CsvPreference preference) {
			super(writer, preference);
		}
		public void writeRow(String... columns ) throws IOException {
			super.writeRow(columns);
		}
	}
	
	public List<String> createHeader(Assembly assembly, List<GroupName> groups) throws Exception {
		List<String> csvHeader = new ArrayList<String>();

        Districts districts = assembly.getDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        csvHeader.add("District");
        csvHeader.add("Chamber");
//			Aggregate aggregate = districts.getAggregate(GROUPLABEL);

        for ( GroupName group: groups ) {
	        for ( String label: districts.getAggregateGroupMap().get(group).getGroupLabels()) {
	        	csvHeader.add(label);
	        }
	        for ( String label: districts.getComputationGroupMap().get(group).getGroupLabels()) {
	        	csvHeader.add(label);
	        }
        }

        for ( GroupName group: groups ) {
        	GroupInfo groupInfo = assembly.getAggregateGroupMap().get(group);
        	if ( groupInfo != null ) {
		        for ( String label: groupInfo.getGroupLabels()) {
		        	csvHeader.add(label);
		        }
        	}
        	groupInfo = assembly.getComputationGroupMap().get(group);
        	if ( groupInfo != null ) {
		        for ( String label: groupInfo.getGroupLabels()) {
		        	csvHeader.add(label);
		        }
        	}
        }
        return csvHeader;
	}


	public List<List<String>> createBody(Assembly assembly, List<GroupName> groups) throws Exception {
		List<List<String>> csvResult = new ArrayList<List<String>>();
		int rowOffset = 0;

        Districts districts = assembly.getDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        List<String> row = new ArrayList<String>();


        // write data for districts 
        for ( final District dist: districts.getDistrictList()) {
        	row = new ArrayList<String>();
        	row.add(dist.getDistrict());
        	row.add(dist.getChamber());
	        for ( GroupName group: groups ) {
	        	AggregateValues aggs = dist.getAggregateMap().get(group);
    	        for ( Long agg: aggs.getValueList() ) {
    	        	row.add(agg.toString());
    	        }
    	        ComputationValues comps = dist.getComputationMap().get(group);
    	        for ( Double comp: comps.getValueList() ) {
    	        	row.add(comp.toString());
    	        }
    	        csvResult.add(row);
	        }
        }
        rowOffset = row.size();
        // write data for assembly
    	row = new ArrayList<String>();
    	for ( int i=0; i<rowOffset; ++i ) {
    		row.add("");
    	}
        for ( GroupName group: groups ) {
	        AggregateValues aggs = assembly.getAggregateMap().get(group);
	        if ( aggs != null ) {
    	        for ( Long agg: aggs.getValueList() ) {
    	        	row.add(agg.toString());
    	        }
	        }
	        ComputationValues comps = assembly.getComputationMap().get(group);
	        if ( comps != null ) {
    	        for ( Double comp: comps.getValueList() ) {
    	        	row.add(comp.toString());
    	        }
	        }
        }
        csvResult.add(row);
        return csvResult;
	}

	public void writeCsv(OutputStream out, Assembly assembly, List<GroupName> groups) throws Exception {
        
		OutputStreamWriter writer = new OutputStreamWriter(out);
		List<String> csvHeader = createHeader(assembly, groups);
		List<List<String>> csvBody = createBody(assembly, groups);
		
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
