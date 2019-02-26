package com.rb.projmgmt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rb.projmgmt.pojo.Analysis;
import com.rb.projmgmt.pojo.Properties;
import com.rb.projmgmt.pojo.TeamMember;

public class GenericRules implements ProjectRule {

	private static final String PROFILE = System.getProperty("spring.profiles.active");
	private static final String SAMPLE_XLSX_FILE_PATH = "C:\\rb-home\\ws\\projmgmt\\src\\main\\resources\\Input_" + PROFILE + ".xlsx";
	private XSSFWorkbook workbook;
	private Map<String, TeamMember> mapTeamMembers = new HashedMap<>();
	private Map<String, String> mapTicketStatus = new HashedMap<>();
	private Properties props;
	private LocalDate reviewStartDate;

	public GenericRules(Properties props) {
		this.props = props;
		this.execute();
	}

	@Override
	public void execute() {

		try (XSSFWorkbook myWorkbook = new XSSFWorkbook(new File(SAMPLE_XLSX_FILE_PATH));) {
			this.workbook = myWorkbook;
			this.loadTeamMembers();
			this.loadTicketStatus();
			this.computeEffort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadTicketStatus() {
		Sheet teamMembers = this.workbook.getSheet("Ticket Status");
		int rowNum = 0;
		for (Row row : teamMembers) {
			if (rowNum > 0) { // ignore header row;
				String ticketId = row.getCell(0).getStringCellValue();
				String status = row.getCell(1).getStringCellValue();
				mapTicketStatus.put(ticketId, status);
			}
			rowNum++;
		}
		System.out.println("Rows Processed: " + (rowNum - 1) + " (excluding header row). mapTicketStatus = " + mapTicketStatus);
	}

	private void loadTeamMembers() {
		Sheet teamMembers = this.workbook.getSheet("Team Members");
		int rowNum = 0;
		for (Row row : teamMembers) {
			if (rowNum > 0) { // ignore header row;
				String name = row.getCell(0).getStringCellValue();
				Date startDate = row.getCell(1).getDateCellValue();
				Date endDate = row.getCell(2).getDateCellValue();
				TeamMember tm = new TeamMember(name, startDate, endDate);
				mapTeamMembers.put(name, tm);
				if (this.reviewStartDate == null || tm.getStartDate().isBefore(this.reviewStartDate)) {
					this.reviewStartDate = tm.getStartDate();
				}
			}
			rowNum++;
		}
		System.out.println("Rows Processed: " + (rowNum - 1) + " (excluding header row). mapTeamMembers = " + mapTeamMembers);
	}

	private void computeEffort() throws IOException {

		Sheet ttsExtract = this.workbook.getSheet("TTS Extract");
		int rowNum = 0;
		Map<String, Analysis> mapTicketIds = new HashMap<>();
		Map<String, Analysis> mapTicketDetails = new HashMap<>();
		//Map<String, LocalDate> mapEndDate = new HashMap<>();
		
		for (Row row : ttsExtract) {
			if (rowNum > 0) { // ignore header row
				Cell date = row.getCell(0);
				Cell teamMember = row.getCell(1);
				
				String teamMemberName = teamMember.getStringCellValue();
				TeamMember pojoTM = this.mapTeamMembers.get(teamMemberName);
				LocalDate dateTTSCharged = date.getDateCellValue().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				
				Cell ticketIdCell = row.getCell(7);
				String ticketId = null;
				if (ticketIdCell != null) {
					//ticketId = ticketIdCell.getStringCellValue();
					CellType cellType = ticketIdCell.getCellType();
					if (CellType.STRING.equals(cellType)) {
						ticketId = ticketIdCell.getStringCellValue();
					} else {
						double d = ticketIdCell.getNumericCellValue();
						if (d == 0) {
							ticketId = "(blank)";
						} else {
							ticketId = String.valueOf(d);
						}
					}
				} else {
					ticketId = "(blank)";
				}
				
				if (ticketId == null || "".equals(ticketId.trim())) {
					ticketId = "(blank)";
				}
				Analysis aTicket = null;
				if (mapTicketDetails.containsKey(ticketId)) {
					aTicket = mapTicketDetails.get(ticketId);
					LocalDate existingStartDate = aTicket.getStartDate();
					if (dateTTSCharged.isBefore(existingStartDate)) {
						aTicket.setStartDate(dateTTSCharged);
					}
					LocalDate existingEndDate = aTicket.getEndDate();
					if (dateTTSCharged.isAfter(existingEndDate)) {
						aTicket.setEndDate(dateTTSCharged);
						aTicket.setOwner(teamMemberName);
					}
					
				} else {
					aTicket = new Analysis(ticketId);
					aTicket.setStartDate(dateTTSCharged);
					aTicket.setEndDate(dateTTSCharged);
					aTicket.setOwner(teamMemberName);
					mapTicketDetails.put(ticketId, aTicket);
				}
				
				if (pojoTM != null && !dateTTSCharged.isBefore(pojoTM.getStartDate())
						&& !dateTTSCharged.isAfter(pojoTM.getEndDate())) {
					// Date is in between range
					
					String activityToMatch = row.getCell(5).getStringCellValue();
					double activityEffort = row.getCell(8).getNumericCellValue();
					Analysis analysis = mapTicketIds.getOrDefault(ticketId, new Analysis(ticketId));
					boolean matched = this.computeEffort(activityToMatch, props.getRequirementActivities(), a -> a.addEffortRequirement(activityEffort), analysis);
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getDesignActivities(), a -> a.addEffortDesign(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getCodingActivities(), a -> a.addEffortCoding(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getCodeReviewActivities(), a -> a.addEffortCodeReview(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getUtActivities(), a -> a.addEffortUt(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getUtReviewActivities(), a -> a.addEffortUtReview(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getQaSupportActivities(), a -> a.addEffortQaSupport(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getReworkActivities(), a -> a.addEffortRework(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getLeavesActivities(), a -> a.addEffortLeaves(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getCompanyHolidayActivities(), a -> a.addEffortCompanyHolidays(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getNonProjectActivities(), a -> a.addEffortNonProject(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getPmActivities(), a -> a.addEffortPm(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getBauActivities(), a -> a.addEffortBau(activityEffort), analysis);
					}
					if (!matched) {
						matched = this.computeEffort(activityToMatch, props.getSupportActivities(), a -> a.addEffortSupport(activityEffort), analysis);
					}
					if (!matched) { //Did not matched with anything
						analysis.addErrorMsg("Invalid Activity Charged: " + activityToMatch + " for " + activityEffort + " hours." );
					}
					mapTicketIds.put(ticketId, analysis);
				}
			}
			rowNum++;
		}
		System.out.println("MAP SIZE : " + mapTicketIds.size());
		this.createOutputXls(mapTicketIds, mapTicketDetails);
		System.out.println("Rows Processed: " + (rowNum - 1) + " (excluding header row).");

	}
	
	private boolean computeEffort(String activityToMatch, List<String> listSourceActivites, Consumer<Analysis> consumer,
			Analysis analysis) {

		boolean matched = false;
		if (listSourceActivites.contains(activityToMatch)) {
			consumer.accept(analysis);
			matched = true;
		} 
		return matched;
	}

	private void createOutputXls(Map<String, Analysis> map, Map<String, Analysis> mapTicketDetails) throws IOException {

		int analysisSheetRowNum = 0;
		XSSFWorkbook output = new XSSFWorkbook();
		XSSFSheet analysisSheet = output.createSheet("Analysis");
		Row newRow = analysisSheet.createRow(analysisSheetRowNum++);
		int cellNum = 0;
		Cell newCell = newRow.createCell(cellNum++);
		Cell newCellErrorMsg = null;
		newCell.setCellValue("TTS Ticket ID");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Errors");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Requirement / Impact Analysis");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Design");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Code");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Code Review");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("UT");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("UT Review");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("QA Support");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Rework");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Leaves");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Company Holiday");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Non Project");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Project Management");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Project BAU");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Project Support");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Start Date");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("End Date");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Status");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Type");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Owner");
		newCell = newRow.createCell(cellNum++);
		newCell.setCellValue("Converted Ticket ID");
		
		for (Analysis anObj : map.values()) {
			String convertedId = null;
			cellNum = 0;
			newRow = analysisSheet.createRow(analysisSheetRowNum++);
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getTicketId());
			String status = this.mapTicketStatus.getOrDefault(anObj.getTicketId(), "");
			if ("".equals(status.trim()))  {
				int index = this.props.getRedirectedOld().indexOf(anObj.getTicketId());
				if (index >= 0) {
					convertedId = this.props.getRedirectedNew().get(index);
					status = this.mapTicketStatus.getOrDefault(convertedId, "");
					if ("".equals(status.trim())) {
						anObj.addErrorMsg("Status not found");
					} else {
						anObj.setStatus(status);
					}
				} else {
					anObj.addErrorMsg("Status not found");
				}
			} else {
				anObj.setStatus(status);
			}
			
			newCellErrorMsg = newRow.createCell(cellNum++);
			
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortRequirement());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortDesign());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortCoding());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortCodeReview());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortUt());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortUtReview());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortQaSupport());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortRework());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortLeaves());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortCompanyHolidays());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortNonProject());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortPm());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortBau());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getEffortSupport());
			
			
			CellStyle cellStyle = output.createCellStyle();
			CreationHelper createHelper = output.getCreationHelper();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d-mmm-yy"));
			
			newCell = newRow.createCell(cellNum++);
			newCell.setCellStyle(cellStyle);
			anObj.setStartDate(mapTicketDetails.get(anObj.getTicketId()).getStartDate());
			newCell.setCellValue(asDate(anObj.getStartDate()));
			newCell = newRow.createCell(cellNum++);
			newCell.setCellStyle(cellStyle);
			anObj.setEndDate(mapTicketDetails.get(anObj.getTicketId()).getEndDate());
			newCell.setCellValue(asDate(anObj.getEndDate()));
			
			
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(anObj.getStatus());
			newCell = newRow.createCell(cellNum++);
			anObj.setType(this.findTicketType(anObj));
			newCell.setCellValue(anObj.getType());
			newCell = newRow.createCell(cellNum++);
			anObj.setOwner(mapTicketDetails.get(anObj.getTicketId()).getOwner());
			newCell.setCellValue(anObj.getOwner());
			newCell = newRow.createCell(cellNum++);
			newCell.setCellValue(convertedId);
			newCellErrorMsg.setCellValue(anObj.getErrorMsg().toString());
		}
		
		for (int i = 16; i < 18; i++) {
			analysisSheet.autoSizeColumn(i);
		}
		FileOutputStream outputStream = new FileOutputStream("C:\\rb-home\\ws\\projmgmt\\src\\main\\resources\\Output_" + PROFILE + ".xlsx");
		output.write(outputStream); 
		output.close(); 
		outputStream.close();
		 
	}
	
	private String findTicketType(Analysis analysis) {
		
		String type = "";
		if (props.getBauTypes().contains(analysis.getTicketId())) {
			type = "BAU";
		} else if (props.getSupportTypes().contains(analysis.getTicketId())) {
			type = "SUPPORT";
		} else if (props.getClosedStatuses().contains(analysis.getStatus())) {
			if (analysis.getEffortRequirement() <= 0 && analysis.getEffortDesign() <= 0 &&
					analysis.getEffortCoding() <= 0 && analysis.getEffortCodeReview() <= 0 &&
					analysis.getEffortUt() <= 0 && analysis.getEffortUtReview() <= 0 && analysis.getEffortRework() > 0) {
				type = "DEFECT";
			} else if (analysis.getEffortRequirement() > 0 && analysis.getEffortDesign() <= 0 &&
					analysis.getEffortCoding() <= 0 && analysis.getEffortCodeReview() <= 0 &&
					analysis.getEffortUt() <= 0 && analysis.getEffortUtReview() <= 0 && analysis.getEffortRework() <= 0) {
				type = "ANALYSIS";
			} else {
				type = "ENHANCEMENT";
				if (analysis.getEffortRequirement() <= 0 || analysis.getEffortCoding() <= 0 || analysis.getEffortCodeReview() <= 0 ||
						analysis.getEffortUt() <= 0 || analysis.getEffortUtReview() <= 0) {
					analysis.addErrorMsg("Not all phases charged in TTS");
				}
			}
		} else if (analysis.getStatus() != null && !"".equals(analysis.getStatus().trim())) {
			if (analysis.getStartDate().isBefore(this.reviewStartDate)) {
				type = "BACKLOG";
			} else {
				type = "WIP";
			}
			
		}
		return type; 
	}
	
	private static Date asDate(LocalDate localDate) {
	    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  }
}
