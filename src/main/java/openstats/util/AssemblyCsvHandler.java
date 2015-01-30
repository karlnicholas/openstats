package openstats.util;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

import openstats.dbmodel.Result;
import openstats.model.*;

public class AssemblyCsvHandler {

	public List<String> createHeader(Assembly assembly) throws Exception {
		List<String> csvHeader = new ArrayList<String>();

        // the header elements are used to map the bean valueList to each column (names must match)
        csvHeader.add("State");
        csvHeader.add("Session");
        csvHeader.add("District");
        csvHeader.add("Chamber");
        csvHeader.add("Legislator");
        csvHeader.add("Party");
//			  = districts.get(GROUPLABEL);

        for ( InfoItem infoItem: assembly.getInfoItems()  ) {
        	csvHeader.add(infoItem.getLabel());
        }

        return csvHeader;
	}


	public List<List<String>> createBody(Assembly assembly) throws Exception {
		List<List<String>> csvResult = new ArrayList<List<String>>();
		int rowOffset = 0;

        // the header elements are used to map the bean valueList to each column (names must match)
        List<String> row = new ArrayList<String>();


        // write data for Legislators 
        for ( final District dist: assembly.getDistrictList()) {
            for ( final Legislator legislator: dist.getLegislators()) {
            	row = new ArrayList<String>();
            	row.add(assembly.getState());
            	row.add(assembly.getSession());
            	row.add(dist.getDistrict());
            	row.add(dist.getChamber().toString());
            	row.add(legislator.getName());
            	row.add(legislator.getParty());
    	        for ( Result result: legislator.getResults() ) {
    	        	row.add(result.getValue().toString());
    	        }
    	        csvResult.add(row);
            }
        }
        // write data for districts 
        for ( final District dist: assembly.getDistrictList()) {
        	row = new ArrayList<String>();
        	row.add(assembly.getState());
        	row.add(assembly.getSession());
        	row.add(dist.getDistrict());
        	row.add(dist.getChamber().toString());
        	row.add("");
        	row.add("");
	        for ( Result result: dist.getResults() ) {
	        	row.add(result.getValue().toString());
	        }
	        csvResult.add(row);
        }
        rowOffset = row.size();
        // write data for assembly
    	row = new ArrayList<String>();
    	row.add(assembly.getState());
    	row.add(assembly.getSession());
    	row.add("");
    	row.add("");
    	row.add("");
    	row.add("");
/*    	
    	for ( int i=0; i<rowOffset; ++i ) {
    		row.add("");
    	}
*/    	
        for ( Result result: assembly.getResults() ) {
        	row.add(result.getValue().toString());
        }
	    csvResult.add(row);
        return csvResult;
	}

	public void writeCsv(Writer writer, Assembly assembly) throws Exception {
        
		List<String> csvHeader = createHeader(assembly);
		List<List<String>> csvBody = createBody(assembly);
		
		CSVPrinter printer = CSVFormat.DEFAULT.print(writer);
		printer.printRecord(csvHeader);
		printer.printRecords(csvBody);
	}

}
