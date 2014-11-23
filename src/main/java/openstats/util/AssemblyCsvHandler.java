package openstats.util;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

import openstats.model.*;

public class AssemblyCsvHandler {

	public List<String> createHeader(Assembly osAssembly) throws Exception {
		List<String> csvHeader = new ArrayList<String>();

        Districts districts = osAssembly.getDistricts();
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

    	groupInfo = osAssembly.getAggregateGroupInfo();
    	if ( groupInfo != null ) {
	        for ( InfoItem infoItem: groupInfo.getInfoItems()) {
	        	csvHeader.add(infoItem.getLabel());
	        }
    	}
    	groupInfo = osAssembly.getComputationGroupInfo();
    	if ( groupInfo != null ) {
	        for ( InfoItem infoItem: groupInfo.getInfoItems()) {
	        	csvHeader.add(infoItem.getLabel());
	        }
    	}
        return csvHeader;
	}


	public List<List<String>> createBody(Assembly osAssembly) throws Exception {
		List<List<String>> csvResult = new ArrayList<List<String>>();
		int rowOffset = 0;

        Districts districts = osAssembly.getDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        List<String> row = new ArrayList<String>();


        // write data for districts 
        for ( final District dist: districts.getDistrictList()) {
        	row = new ArrayList<String>();
        	row.add(dist.getName());
        	row.add(dist.getChamber().toString());
        	List<Long> aggs = dist.getAggregateValues();
            if ( aggs != null ) {
		        for ( Long agg: aggs ) {
		        	row.add(agg.toString());
		        }
            }
	        List<Double> comps = dist.getComputationValues();
	        if ( comps != null ) {
		        for ( Double comp: comps ) {
		        	row.add(comp.toString());
		        }
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

	public void writeCsv(Writer writer, Assembly osAssembly) throws Exception {
        
		List<String> csvHeader = createHeader(osAssembly);
		List<List<String>> csvBody = createBody(osAssembly);
		
		CSVPrinter printer = CSVFormat.DEFAULT.print(writer);
		printer.printRecord(csvHeader);
		printer.printRecords(csvBody);
	}
		

}
