package com.newlight77.kata.survey.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newlight77.kata.survey.model.AddressStatus;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;
import com.newlight77.kata.survey.service.MailService;

@Component
public class ExportCampaignServiceImpl implements ExportCampaignService {

	private MailService mailService;
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public ExportCampaignServiceImpl(MailService mailService) {
		this.mailService = mailService;
	}

	@Override
	public void sendResults(Campaign campaign, Survey survey) {
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Survey");
		sheet.setColumnWidth(0, 10500);
		for (int i = 1; i <= 18; i++) {
			sheet.setColumnWidth(i, 6000);
		}
		createHeaderRow(workbook, sheet);

		CellStyle titleStyle = createTitleStyle(workbook);

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		// section client

		writeSectionClient(sheet, titleStyle, style, survey);
		// section Survey
		writeSectionLabelSurvey(sheet, style, survey, campaign);
		writeDataSurvey(sheet, style, campaign);
		// Write File and Send
		writeFileAndSend(survey, workbook);

	}

	public void writeFileAndSend(Survey survey, Workbook workbook) {
		try {
			File resultFile = new File(System.getProperty("java.io.tmpdir"),
					"survey-" + survey.getId() + "-" + dateTimeFormatter.format(LocalDate.now()) + ".xlsx");

			FileOutputStream outputStream = new FileOutputStream(resultFile);
			workbook.write(outputStream);

			mailService.send(resultFile);
			resultFile.deleteOnExit();
		} catch (Exception ex) {
			throw new RuntimeException("Errorr while trying to send email", ex);
		}
	}

	private void createHeaderRow(Workbook workbook, Sheet sheet) {
		Row header = sheet.createRow(0);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 14);
		font.setBold(true);
		headerStyle.setFont(font);
		headerStyle.setWrapText(false);

		createCell(header, 0, "Survey", headerStyle);

	}

	private void createCell(Row row, int columnCount, String value, CellStyle style) {
		Cell cell = row.createCell(columnCount);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	private void writeSectionClient(Sheet sheet, CellStyle titleStyle, CellStyle style, Survey survey) {

		Row row = sheet.createRow(2);
		createCell(row, 0, "Client", titleStyle);

		Row clientRow = sheet.createRow(3);
		createCell(clientRow, 0, survey.getClient(), style);

		StringBuffer clientAddress = new StringBuffer();

		if (Optional.ofNullable(survey.getClientAddress()).isPresent()) {
			clientAddress.append(survey.getClientAddress().getStreetNumber()).append(" ")
					.append(survey.getClientAddress().getStreetName()).append(survey.getClientAddress().getPostalCode())
					.append(" ").append(survey.getClientAddress().getCity());
		}

		Row clientAddressLabelRow = sheet.createRow(4);
		createCell(clientAddressLabelRow, 0, clientAddress.toString(), style);

	}

	private void writeSectionLabelSurvey(Sheet sheet, CellStyle style, Survey survey, Campaign campaign) {
		Row row = sheet.createRow(6);

		createCell(row, 0, "Number of surveys", style);

		Cell cell = row.createCell(1);
		cell.setCellValue(campaign.getAddressStatuses().size());

		Row surveyLabelRow = sheet.createRow(8);
		createCell(surveyLabelRow, 0, "N° street", style);
		createCell(surveyLabelRow, 1, "Streee", style);
		createCell(surveyLabelRow, 2, "Postal code", style);
		createCell(surveyLabelRow, 3, "City", style);
		createCell(surveyLabelRow, 4, "Status", style);
	}

	private void writeDataSurvey(Sheet sheet, CellStyle style, Campaign campaign) {
		int startIndex = 9;
		int currentIndex = 0;

		for (AddressStatus addressStatus : campaign.getAddressStatuses()) {

			Row surveyRow = sheet.createRow(startIndex + currentIndex);
			createCell(surveyRow, 0, addressStatus.getAddress().getStreetNumber(), style);
			createCell(surveyRow, 1, addressStatus.getAddress().getStreetName(), style);
			createCell(surveyRow, 2, addressStatus.getAddress().getPostalCode(), style);
			createCell(surveyRow, 3, addressStatus.getAddress().getCity(), style);
			createCell(surveyRow, 4, addressStatus.getStatus().toString(), style);
			currentIndex++;

		}

	}

	private CellStyle createTitleStyle(Workbook workbook) {
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont titleFont = ((XSSFWorkbook) workbook).createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setUnderline(FontUnderline.SINGLE);
		titleStyle.setFont(titleFont);

		return titleStyle;

	}

}
