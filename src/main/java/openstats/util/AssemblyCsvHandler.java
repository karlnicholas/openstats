package openstats.util;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

import openstats.osmodel.*;

public class AssemblyCsvHandler {

	public List<String> createHeader(OSAssembly osAssembly) throws Exception {
		List<String> csvHeader = new ArrayList<String>();

        OSDistricts districts = osAssembly.getOSDistricts();
        // the header elements are used to map the bean valueList to each column (names must match)
        csvHeader.add("District");
        csvHeader.add("Chamber");
//			Aggregate aggregate = districts.getAggregate(GROUPLABEL);

        for ( OSInfoItem infoItem: districts.getAggregateGroupInfo().getInfoItems() ) {
        	csvHeader.add(infoItem.getLabel());
        }
        for ( OSInfoItem infoItem: districts.getComputationGroupInfo().getInfoItems()) {
        	csvHeader.add(infoItem.getLabel());
        }

    	OSGroupInfo groupInfo = osAssembly.getAggregateGroupInfo();
    	if ( groupInfo != null ) {
	        for ( OSInfoItem infoItem: groupInfo.getInfoItems()) {
	        	csvHeader.add(infoItem.getLabel());
	        }
    	}
    	groupInfo = osAssembly.getComputationGroupInfo();
    	if ( groupInfo != null ) {
	        for ( OSInfoItem infoItem: groupInfo.getInfoItems()) {
	        	csvHeader.add(infoItem.getLabel());
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

	public void writeCsv(Writer writer, OSAssembly osAssembly) throws Exception {
        
		List<String> csvHeader = createHeader(osAssembly);
		List<List<String>> csvBody = createBody(osAssembly);
		
		CSVPrinter printer = CSVFormat.DEFAULT.print(writer);
		printer.printRecord(csvHeader);
		printer.printRecords(csvBody);
	}
		

}
