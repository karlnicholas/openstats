package openstats.util;
import java.io.*;
import java.util.*;

import openstats.model.*;

import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.prefs.CsvPreference;


public class WriteCsv {
	
	class MyCsvWriter extends AbstractCsvWriter {

		public MyCsvWriter(Writer writer, CsvPreference preference) {
			super(writer, preference);
		}
		public void writeRow(String... columns ) throws IOException {
			super.writeRow(columns);
		}
	}

	public void writeCsv(OutputStream out, Assembly assembly, List<String> groups) throws Exception {
        
		OutputStreamWriter writer = new OutputStreamWriter(out);
		MyCsvWriter csvWriter = null;
        try {
        	
        	csvWriter = new MyCsvWriter(writer, CsvPreference.STANDARD_PREFERENCE);
	        Districts districts = assembly.getDistricts();
	        // the header elements are used to map the bean valueList to each column (names must match)
	        List<String> columns = new ArrayList<String>();

	        columns.add("district");
	        columns.add("chamber");
//			Aggregate aggregate = districts.getAggregate(GROUPLABEL);

	        for ( String group: groups ) {
		        for ( String label: districts.getAggregateGroupMap().get(group).getGroupLabels()) {
		        	columns.add(label);
		        }
		        for ( String label: districts.getComputationGroupMap().get(group).getGroupLabels()) {
		        	columns.add(label);
		        }
	        }

            // write the header
	        String[] sColumns = new String[columns.size()];
	        sColumns = columns.toArray(sColumns);
            csvWriter.writeHeader(sColumns);
            // write the customer lists
            for ( final District dist: districts.getDistrictList()) {
            	columns.clear();
    	        columns.add(dist.getDistrict());
    	        columns.add(dist.getChamber());
    	        for ( String group: groups ) {
	    	        List<Long> aggs = dist.getAggregates().get(group);
	    	        for ( Long agg: aggs ) {
	    	        	columns.add(agg.toString());
	    	        }
	    	        List<Double> comps = dist.getComputations().get(group);
	    	        for ( Double comp: comps ) {
	    	        	columns.add(comp.toString());
	    	        }
	    	        csvWriter.writeRow(columns.toArray(sColumns));
    	        }
            }
            columns.clear();
	        for ( String group: groups ) {
	        	GroupInfo groupInfo = assembly.getAggregateGroupMap().get(group);
	        	if ( groupInfo != null ) {
			        for ( String label: groupInfo.getGroupLabels()) {
			        	columns.add(label);
			        }
	        	}
	        	groupInfo = assembly.getComputationGroupMap().get(group);
	        	if ( groupInfo != null ) {
			        for ( String label: groupInfo.getGroupLabels()) {
			        	columns.add(label);
			        }
	        	}
	        }
	        sColumns = new String[columns.size()];
	        sColumns = columns.toArray(sColumns);
            csvWriter.writeHeader(sColumns);
//            csvWriter.writeHeader(group);
            columns.clear();
	        for ( String group: groups ) {
    	        List<Long> aggs = assembly.getAggregates().get(group);
    	        if ( aggs != null ) {
	    	        for ( Long agg: aggs ) {
	    	        	columns.add(agg.toString());
	    	        }
    	        }
    	        List<Double> comps = assembly.getComputations().get(group);
    	        if ( comps != null ) {
	    	        for ( Double comp: comps ) {
	    	        	columns.add(comp.toString());
	    	        }
    	        }
	        }
            csvWriter.writeRow(columns.toArray(sColumns));
        }
        finally {
            if( csvWriter != null ) {
            	csvWriter.close();
            }
        }
	}

}
