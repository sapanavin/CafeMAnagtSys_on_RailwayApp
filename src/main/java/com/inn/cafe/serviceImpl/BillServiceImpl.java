package com.inn.cafe.serviceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.BillDao;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.service.BillService;
import com.inn.cafe.utils.CafeUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.inn.cafe.pojo.Bill;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillServiceImpl implements BillService{
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	BillDao billDao;
	
	
	
	@Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
	
	log.info("Inside generateReport"+requestMap);
	
	try {
		String fileName;
		
		if(validateRequestMap(requestMap)) {
			
			if(requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
				fileName = (String) requestMap.get("uuid");			
		    }
			else {
				fileName = CafeUtils.getUUID();
				log.info("Inside generateReport fileName :   "+fileName);
				requestMap.put("uuid", fileName);
				insertBill(requestMap);//this will call private method in which dao method called to save bill details to the database.
			}
			
			String data  = 	"Name: "+requestMap.get("name")+"\n" +
							"ContactNumber: "+requestMap.get("contactNumber")+"\n"+
							"Email: "+requestMap.get("email")+"\n" +
							"Payment Method: "+requestMap.get("paymentMethod")+"\n\n";
			log.info("Inside generateReport data :   "+data);
			
		 Document document = new Document();
		 PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION+"\\"+fileName +".pdf")); 
		 
		 document.open();
		 setRectangleInPdf(document);
		 
		 Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
		 chunk.setAlignment(Element.ALIGN_CENTER);
		 document.add(chunk);
			
		 Paragraph paragraph = new Paragraph(data, getFont("Data"));
		 document.add(paragraph);
		 
		 PdfPTable table= new PdfPTable(5);
		 table.setWidthPercentage(100);
		 addTableHeader(table);
		 
		 JSONArray jsonArray = CafeUtils.getJSONArrayFromString((String)requestMap.get("productDetails"));
		 for(int i = 0; i < jsonArray.length(); i++ ) {
			 log.info("Inside  addRows and i = "+i);
			 addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
		 }
			
		 document.add(table);
		 Paragraph footer = new Paragraph("Total : "+ requestMap.get("totalAmount")
				 			+ "Thank you for visiting.  Please visit again !!",
				 			  getFont("Data"));
		 document.add(footer);
		 document.close();
		 
		 return new ResponseEntity<>("{\"uuid\":\""+fileName +"\"}",HttpStatus.OK);
				 
		}
		return CafeUtils.getResponseEntity("Required Data Not Found", HttpStatus.BAD_REQUEST);
		
	}
	catch(Exception ex) {
		ex.printStackTrace();
	}
	return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

}

	
	@Override
	public ResponseEntity<List<Bill>> getBills() {
		List<Bill> list = new ArrayList<>();
		
		try {
			if(jwtFilter.isAdmin()) {
				list = billDao.getAllBills();
			}else {
				list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
			}
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<Bill>(),HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		log.info("Inside getPdf requestMap : ",   requestMap);
		try {
			byte[] byteArray = new byte[0];
			
			if(!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
				return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
			}	
			String filePath = CafeConstants.STORE_LOCATION+ "\\" +(String) requestMap.get("uuid")+".pdf";   
				
			if(CafeUtils.isFileExist(filePath)) {
					byteArray = getByteArray(filePath);
					return new ResponseEntity<>(byteArray, HttpStatus.OK);
			}else {
				requestMap.put("isGenerate", false);
				generateReport(requestMap);
				byteArray = getByteArray(filePath);
				return new ResponseEntity<>(byteArray, HttpStatus.OK);
			}
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new byte[0],HttpStatus.INTERNAL_SERVER_ERROR);

		
	}
	
	
	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
	try {
		Optional<Bill> optional = billDao.findById(id);
		if(optional.isPresent()) {
			billDao.deleteById(id);
			return CafeUtils.getResponseEntity("Bill Deleted Successfully ",HttpStatus.OK);
		}
			
		return CafeUtils.getResponseEntity("Bill id does not exists ",HttpStatus.OK);
	}
	catch(Exception ex) {
		ex.printStackTrace();
	}
	return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

}
	
	
	
	
	
	private void addRows(PdfPTable table, Map<String, Object> data) {
		log.info("Inside  addRows");
			table.addCell((String)data.get("name"));
			table.addCell((String)data.get("category"));
			table.addCell((String)data.get("quantity"));
			table.addCell(Double.toString((Double)data.get("price")));
			table.addCell(Double.toString((Double)data.get("total")));
		
	}

	private void addTableHeader(PdfPTable table) {
		log.info("Inside addTableHeader");
		Stream.of("Name","Category","Quantity","Price","Sub Total")
		.forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorder(2);
			header.setPhrase(new Phrase(columnTitle));
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
			
			
		});
		
	}

	private Font getFont(String type) {
		log.info("Inside getFont");
	switch(type) {
	case "Header" :
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
		headerFont.setStyle(Font.BOLD);
		return headerFont;
		
	case "Data" :
		Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
		dataFont.setStyle(Font.BOLD);
		return dataFont;
		
		default:
			return new Font();
	
	}
	
}

	private void setRectangleInPdf(Document document) throws DocumentException{
		log.info("Inside setRectangleInPdf" );
		
		Rectangle rect = new Rectangle(577,825,18,15);
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		rect.setBorderColor(BaseColor.BLACK);
		rect.setBorderWidth(1);
		document.add(rect);
		
	}

	private void insertBill(Map<String, Object> requestMap) {
	try {
	
	
		
		log.info("came to insertBill"+requestMap);
		Bill bill = new Bill();
		bill.setUuid((String)requestMap.get("uuid"));
		bill.setName((String)requestMap.get("name"));
		bill.setEmail((String)requestMap.get("email"));
		bill.setContactNumber((String)requestMap.get("contactNumber"));
		bill.setPaymentMethod((String)requestMap.get("paymentMethod"));
		bill.setTotal((Integer) requestMap.get("totalAmount"));
		bill.setProductDetails((String)requestMap.get("productDetails"));
		bill.setCreatedBy(jwtFilter.getCurrentUser());
		Bill status = billDao.save(bill);
		
		log.info("billDao.save(bill); " +status);
		
	}
	catch(Exception ex) {
		ex.printStackTrace();
	}
	
}

	private boolean validateRequestMap(Map<String, Object> requestMap) {
	
	
	return requestMap.containsKey("name")&&
			requestMap.containsKey("contactNumber")&&
			requestMap.containsKey("email")&&
			requestMap.containsKey("paymentMethod")&&
			requestMap.containsKey("productDetails")&&
			requestMap.containsKey("totalAmount");
}

	private byte[] getByteArray(String filePath) throws IOException {
	File initialFile = new File(filePath);
	InputStream targetStream = new FileInputStream(initialFile);
	byte[] byteArray = IOUtils.toByteArray(targetStream);
	return byteArray;
}


	}

