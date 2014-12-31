package openstats.util;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

import openstats.dbmodel.AggregateResult;
import openstats.dbmodel.ComputationResult;
import openstats.model.*;

public class AssemblyCsvHandler {

	public List<String> createHeader(Assembly assembly) throws Exception {
		List<String> csvHeader = new ArrayList<String>();

        Districts districts = assembly.getDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        csvHeader.add("District");
        csvHeader.add("Chamber");
//			Aggregate aggregate = districts.getAggregate(GROUPLABEL);

    	GroupInfo groupInfo = districts.getAggregateGroupInfo();
    	if ( groupInfo != null ) {
	        for ( InfoItem infoItem: districts.getAggregateGroupInfo().getInfoItems() ) {
	        	csvHeader.add(infoItem.getLabel());
	        }
    	}
    	groupInfo = districts.getComputationGroupInfo();
    	if ( groupInfo != null ) {
	        for ( InfoItem infoItem: districts.getComputationGroupInfo().getInfoItems()) {
	        	csvHeader.add(infoItem.getLabel());
	        }
    	}

    	groupInfo = assembly.getAggregateGroupInfo();
    	if ( groupInfo != null ) {
	        for ( InfoItem infoItem: groupInfo.getInfoItems()) {
	        	csvHeader.add(infoItem.getLabel());
	        }
    	}
    	groupInfo = assembly.getComputationGroupInfo();
    	if ( groupInfo != null ) {
	        for ( InfoItem infoItem: groupInfo.getInfoItems()) {
	        	csvHeader.add(infoItem.getLabel());
	        }
    	}
        return csvHeader;
	}


	public List<List<String>> createBody(Assembly assembly) throws Exception {
		List<List<String>> csvResult = new ArrayList<List<String>>();
		int rowOffset = 0;

        Districts districts = assembly.getDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        List<String> row = new ArrayList<String>();


        // write data for districts 
        for ( final District dist: districts.getDistrictList()) {
        	row = new ArrayList<String>();
        	row.add(dist.getDistrict());
        	row.add(dist.getChamber().toString());
        	if ( dist.getAggregateResults() != null ) {
		        for ( AggregateResult result: dist.getAggregateResults() ) {
		        	row.add(Long.toString(result.value));
		        }
        	}
        	if ( dist.getComputationResults() != null ) {
		        for ( ComputationResult comp: dist.getComputationResults() ) {
		        	row.add(Double.toString(comp.value));
		        }
        	}
	        csvResult.add(row);
        }
        rowOffset = row.size();
        // write data for assembly
    	row = new ArrayList<String>();
    	for ( int i=0; i<rowOffset; ++i ) {
    		row.add("");
    	}
    	if ( assembly.getAggregateResults() != null ) {
	        for ( AggregateResult result: assembly.getAggregateResults() ) {
	        	row.add(Long.toString(result.value));
	        }
    	}
    	if ( assembly.getComputationResults() != null ) {
	        for ( ComputationResult result: assembly.getComputationResults() ) {
	        	row.add(Double.toString(result.value));
	        }
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
