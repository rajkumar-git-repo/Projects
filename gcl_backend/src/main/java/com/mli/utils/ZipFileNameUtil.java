package com.mli.utils;

import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.constants.Symbols;

public class ZipFileNameUtil {

	/**
	 * 
	 * @param mphType
	 * @param startDate
	 * @param endDate
	 * @return MIS file name based on mph type and date range
	 * @author rajkumar
	 */
	public static String getMISReportFileName(String mphType , String startDate , String endDate) {
		StringBuilder fileName = new StringBuilder();
		if(!StringUtils.isEmpty(mphType) && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			if(Constant.AXIS.equalsIgnoreCase(mphType)) {
				fileName.append("AxisBank");
			}
			if(Constant.AXIS_FINANCE.equalsIgnoreCase(mphType)) {
				fileName.append("AxisFinance");
			}
			if(Constant.YESBANKCC.equalsIgnoreCase(mphType)) {
				fileName.append("YBLCC");
			}
			fileName.append(Symbols.HYPHEN);
			fileName.append("applicationMIS");
			fileName.append(Symbols.HYPHEN);
			fileName.append(startDate.replace("/", ""));
			fileName.append(Symbols.HYPHEN);
			fileName.append(endDate.replace("/", ""));
		}else {
			fileName.append("customer");
		}
		return fileName.toString();
	}
	
	/**
	 * 
	 * @param mphType
	 * @param startDate
	 * @param endDate
	 * @return Application zip file name based on mphType and date range
	 * @author rajkumar
	 */
	public static String getZIPReportFileName(String fileType , String mphType , String startDate , String endDate) {
		StringBuilder fileName = new StringBuilder();
		if(!StringUtils.isEmpty(mphType) && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			if(Constant.AXIS.equalsIgnoreCase(mphType)) {
				fileName.append("AxisBank");
			}
			if(Constant.AXIS_FINANCE.equalsIgnoreCase(mphType)) {
				fileName.append("AxisFinance");
			}
			if(Constant.YESBANKCC.equalsIgnoreCase(mphType)) {
				fileName.append("YBLCC");
			}
			fileName.append(Symbols.HYPHEN);
			if("YBLCC_PROPOSAL".equalsIgnoreCase(fileType) || "PROPOSAL".equalsIgnoreCase(fileType) || "AFL_PROPOSAL".equalsIgnoreCase(fileType)) {
			     fileName.append("applicationForms");
			}
			if("PASSPORT".equalsIgnoreCase(fileType)) {
				 fileName.append("passports");
			}
			if("AFL_PASSPORT".equalsIgnoreCase(fileType)) {
				 fileName.append("passports");
			}
			if("CAM_REPORT".equalsIgnoreCase(fileType)) {
				 fileName.append("additionalDocuments");
			}
			if("AFL_CAM_REPORT".equalsIgnoreCase(fileType)) {
				 fileName.append("additionalDocuments");
			}
			if("PhysicalForm".equalsIgnoreCase(fileType)) {
				 fileName.append("physicalForms");
			}
			if("AFL_PhysicalForm".equalsIgnoreCase(fileType)) {
				 fileName.append("physicalForms");
			}
			if("CovidReport".equalsIgnoreCase(fileType)) {
				 fileName.append("covidReports");
			}
			if("AFL_CovidReport".equalsIgnoreCase(fileType)) {
				 fileName.append("covidReports");
			}
			fileName.append(Symbols.HYPHEN);
			fileName.append(startDate.replace("/", ""));
			fileName.append(Symbols.HYPHEN);
			fileName.append(endDate.replace("/", ""));
		}else {
			if(!StringUtils.isEmpty(mphType)) {
				if(Constant.AXIS.equalsIgnoreCase(mphType) || Constant.AXIS_FINANCE.equalsIgnoreCase(mphType)) {
					fileName.append("PROPOSAL");
				}
				if(Constant.YESBANKCC.equalsIgnoreCase(mphType)) {
					fileName.append("YBLCC_PROPOSAL");
				}
			}
		}
		return fileName.toString();
	} 
}
