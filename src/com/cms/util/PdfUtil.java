package com.cms.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import com.cms.front.entity.Application;
import com.cms.front.entity.Client;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 填充 pdf 并插入 二维码，如果有多个 pdf 进行合并操作
 *
 * @author Chengloong
 */
public class PdfUtil {
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsssSSS");
	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMMdd");
	private static String uploadroot = "/upload/";
	public static void main(String[] args) throws Exception {
//		String dest = "/Users/robin/demo/hello.pdf";
//		String src = "/Users/robin/demo/title.pdf";
//		String src2 = "/Users/robin/demo/security.pdf";
//		PdfReader[] readers = new PdfReader[2];
//		readers[0]=new PdfReader(src);
//		readers[1]=new PdfReader(src2);
//		manipulatePdf(dest,readers);
		
		//manipulatePdf(dest,src2);
		
		//manipulatePdf3(dest,src);
		//createPdf3();
		//填充 pdf 模板
		//fillTemplate();
		
		//PDFJoin.mergepdf(fileList,"d:/pdfjoin/all.pdf");
		//change logo
//		String dest = "/Users/robin/eclipse-workspace/Fynco/WebRoot/upload/sm_2_0.png";
//		String src = "/Users/robin/eclipse-workspace/Fynco/WebRoot/upload/sm_2_0.pdf";
//		pdf2Img(dest,src);
		String dest = "/Users/robin/eclipse-workspace/Fynco/WebRoot/upload/sm_1.pdf";
		String src = "/Users/robin/eclipse-workspace/Fynco/WebRoot/pdf/sm_1.pdf";
		String logo = "/Users/robin/demo/logo.png";
		replaceImage(dest,src,logo);
		
	}
	public static Image addTitle(String realPath,String dest,String filename) throws Exception{
		String src = realPath+"/pdf/"+filename+".pdf";
        pdf2Img(dest+filename+".png", src);
        ImageData imageData = ImageDataFactory.create(dest+filename+".png");
        Image pdfImg = new Image(imageData);
        return pdfImg;
	}
	public static String pdf(String realPath,Client[] clients,Application app,String logo,int useLogo) throws Exception {
		String path=ym.format(new Date())+"_"+app.getInt("id");
		String dest = realPath+uploadroot+path+"/";
		String path0=ym.format(new Date());
		String dest0 = realPath+uploadroot+path0+"/";
		File dir0 = new File(dest0);
		String timestamp = sf.format(new Date());
		String file=dest0+"sm"+"_"+app.getInt("id")+"_"+timestamp+".pdf";
		String url = uploadroot+path0+"/"+"sm"+"_"+app.getInt("id")+"_"+timestamp+".pdf";
		try {
			if(!dir0.exists()){
				dir0.mkdir();
			}
			File dir = new File(dest);
			if(!dir.exists()){
				dir.mkdir();
			}
			ArrayList<String> src = new ArrayList<String>();
			
 			src.add(pdf_1( dest, realPath, app,logo,useLogo));
			src.add(pdf_2( dest, realPath,clients, app));
			src.add(pdf_3( dest, realPath, app));
			src.add(pdf_4(dest,realPath,app));
			src.add(pdf_5( dest, realPath, app));
			MergeFiles(file,src);
		
		}
		catch(Exception e) {
			url = "";
			e.printStackTrace();
		}
		finally {
			FileUtil.deleteAll(dest);
		}
		return url;
	}
	public static String pdf_4(String dest,String realPath,Application app) {
		
		String url = dest+"sm_4"+"_"+app.getInt("id")+".pdf";
		try {
			File file = new File(url);
			PdfWriter pdfWriter = new PdfWriter(file);
	        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
	
	        Document document = new Document(pdfDocument);
	        document.setMargins(0f, 0f, 0f, 0f);
	        
	        document.add(addTitle(realPath,dest,"sm_4_title")); 
	        
	        
	      //asset
	        String src2 = realPath+"/pdf/sm_4_asset.pdf";
	        JSONObject assetJson = JSONObject.fromObject(app.getStr("asset"));
	        Iterator iterator = assetJson.keys();
	        int i=0;
	        while(iterator.hasNext()){
	        		i++;
	                String key = (String) iterator.next();
	                Object value = assetJson.get(key);
	                if (value instanceof JSONArray){
	                	JSONArray jsonArray = (JSONArray)value;
	    	            for (int k = 0; k < jsonArray.size(); k++) {
	    	                Object object = jsonArray.get(k);
					        document.add(getImg(dest,src2,i,object));
	    	            }
	                }else if (value instanceof JSONObject) {
	                	document.add(getImg(dest,src2,i,value));
	    			}
	        }
	        //assets total
	        document.add(getTotal( realPath, dest,"sm_4_asset_total","name","Total Assets","total",assetJson.getString("total_value")));
	        //liability_header
	        document.add(addTitle(realPath,dest,"sm_4_liability_header")); 
	        //liability body1-- 3 columns loop
	        
	        JSONObject liabilityJson = JSONObject.fromObject(app.getStr("liability"));
	        JSONArray loandropArray = liabilityJson.getJSONArray("loandrop");
	        String[] pdf_fields = {"liabilities","liabilities_d","liabilities_v"};
	        String[] database_fields = {"loandrop_sel","loandrop_d","loandrop_v"};
	        Integer[] position = {0,0,1};
	        Integer[] format = {0,0,1};
	        ArrayList<JSONArray> list = new ArrayList<JSONArray>();
	        list.add(loandropArray);
	        document = getLoopContent(document, realPath, dest,"sm_4_liability_body_1",
	        		pdf_fields, database_fields,position,format,list) ;
	        //other
	        ArrayList<JSONArray> list5 = new ArrayList<JSONArray>();
	        String[] pdf_fields5 = {"liabilities","liabilities_d","liabilities_v"};
	        String[] database_fields5 = {"name","description","balance"};
	        list5.add(liabilityJson.getJSONArray("otherliability"));
	        document = getLoopContent(document, realPath, dest,"sm_4_liability_body_1",
	        		pdf_fields5, database_fields5,position,format,list5) ;
	        
	      //liability_header2
	        document.add(addTitle(realPath,dest,"sm_4_liability_header_2")); 
	      //liability body2-- 4 columns loop
	        

	        String[] pdf_fields2 = {"name","provider","limit","balance"};
	        String[] database_fields2 = {"name","provider","limit","balance"};
	        Integer[] position2 = {0,0,1,1};
	        Integer[] format2 = {0,0,1,1};
	        ArrayList<JSONArray> list2 = new ArrayList<JSONArray>();
	        list2.add(liabilityJson.getJSONArray("revcredit"));
	        list2.add(liabilityJson.getJSONArray("overlimit"));
	        list2.add(liabilityJson.getJSONArray("creditcard"));
	        list2.add(liabilityJson.getJSONArray("storecard"));
	        document = getLoopContent(document, realPath, dest,"sm_4_liability_body_2",
	        		pdf_fields2, database_fields2,position2,format2,list2) ;
	        
	      //liability_header3
	        document.add(addTitle(realPath,dest,"sm_4_liability_header_3")); 
	        
	        //liability body3-- 4 columns loop
	        
	        String[] database_fields3 = {"name","provider","monthly_cost","balance"};
	        ArrayList<JSONArray> list3 = new ArrayList<JSONArray>();
	        list3.add(liabilityJson.getJSONArray("hirepurchase"));
	        document = getLoopContent(document, realPath, dest,"sm_4_liability_body_2",
	        		pdf_fields2, database_fields3,position2,format2,list3) ;
	        
	        //liability total
	        document.add(getTotal( realPath, dest,"sm_4_asset_total","name","Total Liability","total",String.valueOf(app.getBigDecimal("total_liability"))));
	        //equity
	        document.add(getTotal( realPath, dest,"sm_4_asset_total","name","Equity","total",String.valueOf(app.getBigDecimal("equity"))));
	        document.close();
	        
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			//FileUtil.deleteAll(dest);
		}
		return url;
	}
	public static Document getLoopContent(Document document,String realPath,String dest,String filename,String[] pdf_fields,String[] database_fields,Integer[] position,Integer[] format,ArrayList<JSONArray> list) throws Exception {
		for(JSONArray jsonArray:list) {
			int size = jsonArray.size();
			for(int i=0;i<size;i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				document.add(getImg( realPath, dest, filename, obj,pdf_fields, database_fields,position,format,i));
			}
		}
		return document;
	}
	public static Image getImg(String realPath,String dest,String filename,JSONObject obj,String[] pdf_fields,String[] database_fields,Integer[] position,Integer[] format,int index) throws Exception {
		String src = realPath+"/pdf/"+filename+".pdf";
		String pdf = dest+filename+"_"+index+".pdf"; 
		String png = dest+filename+"_"+index+".png"; 
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(pdf));
		PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
		Map<String, PdfFormField> fields = form.getFormFields();
		
		for(int j=0;j<pdf_fields.length;j++) {
			String fv = obj.getString(database_fields[j]);
			if(format[j]==1) {
				if(!fv.equals("")) {
					if(fv.indexOf("$")<0) {
						fv = StrUtil.formatString(Double.valueOf(fv));
					}
					else {
						System.out.println("fv:"+fv);
						fv = fv.replaceAll("\\$", "");
					}
				}
			}
			if(position[j]==0) {
				fields.get(pdf_fields[j]).setValue(fv).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
			}
			else {
				fields.get(pdf_fields[j]).setValue(fv).setFontSize(10).setJustification(PdfFormField.ALIGN_RIGHT);
			}
		}
		
		
    	form.flattenFields();//设置表单域不可编辑  
        pdfDoc.close();
        pdf2Img(png, pdf);
        ImageData imageData = ImageDataFactory.create(png);
        Image pdfImg = new Image(imageData);
        return pdfImg;
	}
	public static Image getTotal(String realPath,String dest,String file,String pdf_txt_field_name,String pdf_txt_field_value,String pdf_field,String total_value) throws Exception {
		//total_monthly_income
		String src6 = realPath+"/pdf/"+file+".pdf";
		String total_income_pdf =dest+file+".pdf";
		String total_income_png =dest+file+".png";
		PdfDocument pdfDoc6 = new PdfDocument(new PdfReader(src6), new PdfWriter(total_income_pdf));
		PdfAcroForm form6 = PdfAcroForm.getAcroForm(pdfDoc6, true);
		Map<String, PdfFormField> fields6 = form6.getFormFields();
		fields6.get(pdf_txt_field_name).setValue(pdf_txt_field_value).setFontSize(11).setJustification(PdfFormField.ALIGN_RIGHT);
        fields6.get(pdf_field).setValue(StrUtil.formatString(Double.valueOf(total_value))).setFontSize(10);
        form6.flattenFields();//设置表单域不可编辑  
        pdfDoc6.close();
        pdf2Img(total_income_png, total_income_pdf);
        
        ImageData imageData6 = ImageDataFactory.create(total_income_png);
        Image pdfImg6 = new Image(imageData6);
        return pdfImg6;
	}
	public static Image getImg(String dest,String src,int i,Object value) throws Exception {
		String asset_file = dest+"sm_4_asset_"+i+".pdf"; 
		String asset_png_file = dest+"sm_4_asset_"+i+".png"; 
		PdfDocument pdfDoc2 = new PdfDocument(new PdfReader(src), new PdfWriter(asset_file));
		PdfAcroForm form2 = PdfAcroForm.getAcroForm(pdfDoc2, true);
		Map<String, PdfFormField> fields2 = form2.getFormFields();
		
    	JSONObject assetObj = (JSONObject)value;
    	Iterator it = assetObj.keys();
    	while(it.hasNext()){
    		String key2 = (String) it.next();
    		 String value2 = StrUtil.null2Blank(assetObj.getString(key2));
    		
    		if(key2.equals("text")) {
    			fields2.get("assets").setValue(value2).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
    		}
    		else {
    			if(key2.indexOf("_v")>0) {
    				if(!value2.equals("")) {
    					fields2.get("assets_v").setValue(StrUtil.formatString(Double.parseDouble(value2))).setFontSize(10).setJustification(PdfFormField.ALIGN_RIGHT);
    				}
    			}
    			else {
    				fields2.get("assets_d").setValue(value2).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
    			}
    		}
    	}
    	form2.flattenFields();//设置表单域不可编辑  
        pdfDoc2.close();
        pdf2Img(asset_png_file, asset_file);
        ImageData imageData2 = ImageDataFactory.create(asset_png_file);
        Image pdfImg2 = new Image(imageData2);
        return pdfImg2;
	}
	public static String pdf_5(String dest,String realPath,Application app) {
		String url = dest+"sm_5"+"_"+app.getInt("id")+".pdf";
		try {
		String src = realPath+"/pdf/sm_5_title.pdf";
		String src2 = realPath+"/pdf/sm_5_applicant_header.pdf";
		String src3 = realPath+"/pdf/sm_5_applicant.pdf";
		//
//		ArrayList<String> filelist = new ArrayList<String>();
//		filelist.add(src);
//		filelist.add(src2);
//		MergeFiles(dest+"sm_5.pdf",filelist);

		//String realPath = this.getRequest().getRealPath("/");


        pdf2Img(dest+"sm_5_title.png", src);
        
        
        File file = new File(url);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        Document document = new Document(pdfDocument);
        document.setMargins(0f, 0f, 0f, 0f);
        ImageData imageData = ImageDataFactory.create(dest+"sm_5_title.png");
        Image pdfImg = new Image(imageData);
        
        document.add(pdfImg);
		//monthincome
		JSONArray monthincomeJsonArray = JSONArray.fromObject(app.getStr("monthincome"));
		if(monthincomeJsonArray.size()>0) {
			int applicant_size =monthincomeJsonArray.size(); 
			//int applicant_size =1;
			for (int i = 0; i < applicant_size; i++) {
				
				
				String applicant_name = "";
				String income="";
				String monthincome_d = "";
		    	String monthincome_v = "";
		    	String monthincome_n = "";
				int j=i+1;
				JSONObject monthincomeObj = monthincomeJsonArray.getJSONObject(i);
				if(monthincomeObj!=null) {
					applicant_name = StrUtil.null2Blank(monthincomeObj.getString("applicant_name"));
					
					String applicant_header_pdf =dest+"sm_5_applicant_header_"+j+".pdf";
					String applicant_header_png =dest+"sm_5_applicant_header_"+j+".png";
					PdfDocument pdfDoc = new PdfDocument(new PdfReader(src2), new PdfWriter(applicant_header_pdf));
					PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
					Map<String, PdfFormField> fields = form.getFormFields();
			        fields.get("applicant_no").setValue(j+" - "+applicant_name).setFontSize(10);
			        form.flattenFields();//设置表单域不可编辑  
			        pdfDoc.close();
			        pdf2Img(applicant_header_png, applicant_header_pdf);
			        
			        ImageData imageData2 = ImageDataFactory.create(applicant_header_png);
			        Image pdfImg2 = new Image(imageData2);
			        document.add(pdfImg2);
			        
					JSONArray incomeArray = monthincomeObj.getJSONArray("monthincome");
					
					int size = incomeArray.size();
					
					for (int k = 0;  k< size; k++) {
						String incomeline_file = dest+"sm_5_applicant_"+k+".pdf"; 
						String incomeline_png_file = dest+"sm_5_applicant_"+k+".png"; 
						PdfDocument pdfDoc2 = new PdfDocument(new PdfReader(src3), new PdfWriter(incomeline_file));
						PdfAcroForm form2 = PdfAcroForm.getAcroForm(pdfDoc2, true);
						Map<String, PdfFormField> fields2 = form2.getFormFields();
						if(incomeArray.getJSONObject(k)!=null) {
							JSONObject incomeObj = incomeArray.getJSONObject(k);
							//System.out.println(incomeObj.toString());
							income = StrUtil.null2Blank(incomeObj.getString("monthincome_income"));
							monthincome_d = StrUtil.null2Blank(incomeObj.getString("monthincome_description"));
							monthincome_v = StrUtil.formatString(StrUtil.null2Blank(incomeObj.getString("monthincome_gross")));
							monthincome_n = StrUtil.formatString(StrUtil.null2Blank(incomeObj.getString("monthincome_net")));
							if(fields2.get("income")!=null) {
								fields2.get("income").setValue(income).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
						        fields2.get("income_d").setValue(monthincome_d).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
						        fields2.get("income_v").setValue(monthincome_v).setFontSize(10).setJustification(PdfFormField.ALIGN_RIGHT);
						        fields2.get("income_n").setValue(monthincome_n).setFontSize(10).setJustification(PdfFormField.ALIGN_RIGHT);
							}
						}
						form2.flattenFields();//设置表单域不可编辑  
				        pdfDoc2.close();
				        pdf2Img(incomeline_png_file, incomeline_file);
				        ImageData imageData3 = ImageDataFactory.create(incomeline_png_file);
				        Image pdfImg3 = new Image(imageData3);
				        document.add(pdfImg3);
					}
				}
			}
		}
		
		//monthincome_rental
		String src4 = realPath+"/pdf/sm_5_income_rental_header.pdf";
		String dest4 = dest+"sm_5_income_rental_header.png";
		pdf2Img(dest4, src4);
		ImageData imageData4 = ImageDataFactory.create(dest4);
        Image pdfImg4 = new Image(imageData4);
        document.add(new Paragraph(" "));
        document.add(pdfImg4);
        
        String src5 = realPath+"/pdf/sm_5_application_rental.pdf";
        JSONArray rentalJsonArray = JSONArray.fromObject(app.getStr("monthincome_from_rent"));
		if(rentalJsonArray.size()>0) {
			int rental_size =rentalJsonArray.size(); 
			//int applicant_size =1;
			for (int i = 0; i < rental_size; i++) {

				String rental="";
				String rental_d = "";
		    	String rental_n = "";

				JSONObject rentalObj = rentalJsonArray.getJSONObject(i);
				if(rentalObj!=null) {
					
					String rental_file = dest+"sm_5_applicant_rental_"+i+".pdf"; 
					String rental_png_file = dest+"sm_5_applicant_rental_"+i+".png"; 
					PdfDocument pdfDoc5 = new PdfDocument(new PdfReader(src5), new PdfWriter(rental_file));
					PdfAcroForm form5 = PdfAcroForm.getAcroForm(pdfDoc5, true);
					Map<String, PdfFormField> fields5 = form5.getFormFields();
					
					//System.out.println(incomeObj.toString());
					rental = StrUtil.null2Blank(rentalObj.getString("monthincomefr_income"));
					rental_d = StrUtil.null2Blank(rentalObj.getString("monthincomefr_description"));
					rental_n = StrUtil.formatString(StrUtil.null2Blank(rentalObj.getString("monthincomefr_net")));
					if(fields5.get("rental")!=null) {
						fields5.get("rental").setValue(rental).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
				        fields5.get("rental_d").setValue(rental_d).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
				        fields5.get("rental_n").setValue(rental_n).setFontSize(10).setJustification(PdfFormField.ALIGN_RIGHT);
					}
					
					form5.flattenFields();//设置表单域不可编辑  
			        pdfDoc5.close();
			        pdf2Img(rental_png_file, rental_file);
			        ImageData imageData5 = ImageDataFactory.create(rental_png_file);
			        Image pdfImg5 = new Image(imageData5);
			        document.add(pdfImg5);

				}
			}
		}
		//total_monthly_income
		String src6 = realPath+"/pdf/sm_5_income_total.pdf";
		String total_income_pdf =dest+"sm_5_income_total.pdf";
		String total_income_png =dest+"sm_5_income_total.png";
		PdfDocument pdfDoc6 = new PdfDocument(new PdfReader(src6), new PdfWriter(total_income_pdf));
		PdfAcroForm form6 = PdfAcroForm.getAcroForm(pdfDoc6, true);
		Map<String, PdfFormField> fields6 = form6.getFormFields();
        fields6.get("total_monthly_income_value").setValue(StrUtil.formatString(app.getBigDecimal("total_income").doubleValue())).setFontSize(10);
        form6.flattenFields();//设置表单域不可编辑  
        pdfDoc6.close();
        pdf2Img(total_income_png, total_income_pdf);
        
        ImageData imageData6 = ImageDataFactory.create(total_income_png);
        Image pdfImg6 = new Image(imageData6);
        document.add(pdfImg6);
		
        //expense
        String src7 = realPath+"/pdf/sm_5_expense_header.pdf";
		String dest7 = dest+"sm_5_expense_header.png";
		pdf2Img(dest7, src7);
		ImageData imageData7 = ImageDataFactory.create(dest7);
        Image pdfImg7 = new Image(imageData7);
        document.add(pdfImg7);
        
        String src8 = realPath+"/pdf/sm_5_expense.pdf";
        JSONArray expenseJsonArray = JSONArray.fromObject(app.getStr("monthexpend"));
		if(expenseJsonArray.size()>0) {
			int expense_size =expenseJsonArray.size(); 
			//int applicant_size =1;
			for (int i = 0; i < expense_size; i++) {

				String expense="";
				String expense_d = "";
		    	String expense_n = "";

				JSONObject expenseObj = expenseJsonArray.getJSONObject(i);
				if(expenseObj!=null) {
					
					String expense_file = dest+"sm_5_applicant_expense_"+i+".pdf"; 
					String expense_png_file = dest+"sm_5_applicant_expense_"+i+".png"; 
					PdfDocument pdfDoc8 = new PdfDocument(new PdfReader(src8), new PdfWriter(expense_file));
					PdfAcroForm form8 = PdfAcroForm.getAcroForm(pdfDoc8, true);
					Map<String, PdfFormField> fields8 = form8.getFormFields();
					
					//System.out.println(incomeObj.toString());
					expense = StrUtil.null2Blank(expenseObj.getString("monthexpend_expend"));
					expense_d = StrUtil.null2Blank(expenseObj.getString("monthexpend_description"));
					expense_n = StrUtil.formatString(StrUtil.null2Blank(expenseObj.getString("monthexpend_expense")));
					if(fields8.get("expense")!=null) {
						fields8.get("expense").setValue(expense).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
				        fields8.get("expense_d").setValue(expense_d).setFontSize(10).setJustification(PdfFormField.ALIGN_LEFT);
				        fields8.get("expense_v").setValue(expense_n).setFontSize(10).setJustification(PdfFormField.ALIGN_RIGHT);
					}
					
					form8.flattenFields();//设置表单域不可编辑  
			        pdfDoc8.close();
			        pdf2Img(expense_png_file, expense_file);
			        ImageData imageData8 = ImageDataFactory.create(expense_png_file);
			        Image pdfImg8 = new Image(imageData8);
			        document.add(pdfImg8);

				}
			}
		}
		//total_monthly_expense
				String src9 = realPath+"/pdf/sm_5_expense_bottom.pdf";
				String total_expense_pdf =dest+"sm_5_expense_bottom.pdf";
				String total_expense_png =dest+"sm_5_expense_bottom.png";
				PdfDocument pdfDoc9 = new PdfDocument(new PdfReader(src9), new PdfWriter(total_expense_pdf));
				PdfAcroForm form9 = PdfAcroForm.getAcroForm(pdfDoc9, true);
				Map<String, PdfFormField> fields9 = form9.getFormFields();
		        fields9.get("total_monthly_expense_value").setValue(StrUtil.formatString(app.getBigDecimal("total_expense").doubleValue())).setFontSize(10);
		        fields9.get("total_surplus_value").setValue(StrUtil.formatString(app.getBigDecimal("surplus").doubleValue())).setFontSize(10);
		        form9.flattenFields();//设置表单域不可编辑  
		        pdfDoc9.close();
		        pdf2Img(total_expense_png, total_expense_pdf);
		        
		        ImageData imageData9 = ImageDataFactory.create(total_expense_png);
		        Image pdfImg9 = new Image(imageData9);
		        document.add(pdfImg9);
        
        document.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			//FileUtil.deleteAll(dest);
		}
		return url;
    }
	protected void cutAndPaste(String dest,String src) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(src));
        Rectangle pageSize = srcDoc.getFirstPage().getPageSize();
        PdfDocument resultPdfDoc = new PdfDocument(new PdfWriter(dest));
        resultPdfDoc.setDefaultPageSize(new PageSize(pageSize));
        resultPdfDoc.addNewPage();
 
        PdfFormXObject pageXObject = srcDoc.getFirstPage().copyAsFormXObject(resultPdfDoc);
        Rectangle toMove = new Rectangle(100, 500, 100, 100);
 
        // Create a formXObject of a page content, in which the area to move is cut.
        PdfFormXObject formXObject1 = new PdfFormXObject(pageSize);
        PdfCanvas canvas1 = new PdfCanvas(formXObject1, resultPdfDoc);
        canvas1.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas1.rectangle(toMove);
 
        // This method uses the even-odd rule to determine which regions lie inside the clipping path.
        canvas1.eoClip();
        canvas1.endPath();
        canvas1.addXObject(pageXObject, 0, 0);
 
        // Create a formXObject of the area to move.
        PdfFormXObject formXObject2 = new PdfFormXObject(pageSize);
        PdfCanvas canvas2 = new PdfCanvas(formXObject2, resultPdfDoc);
        canvas2.rectangle(toMove);
 
        // This method uses the nonzero winding rule to determine which regions lie inside the clipping path.
        canvas2.clip();
        canvas2.endPath();
        canvas2.addXObject(pageXObject, 0, 0);
 
        PdfCanvas canvas = new PdfCanvas(resultPdfDoc.getFirstPage());
        canvas.addXObject(formXObject1, 0, 0);
 
        // Add the area to move content, shifted 10 points to the left and 2 points to the bottom.
        canvas.addXObject(formXObject2, -20, -2);
 
        srcDoc.close();
        resultPdfDoc.close();
    }
	public static String pdf_2(String dest,String realPath,Client[] clients,Application apt) throws Exception {
		ArrayList<String> filelist = new ArrayList<String>();
		Client[] client = new Client[2];
		ArrayList<Client[]> clientArray = new ArrayList<Client[]>();
		for(int t=0;t<clients.length;t++) {
			int mod = t%2;
			if(mod==0&&t>0) {
				client = new Client[2];
			}
			client[mod]=clients[t];
			if(mod==1||t+1==clients.length) {
				clientArray.add(client);
			}
		}
		String url = dest+"sm_2.pdf";
//		File file = new File(url);
//		PdfWriter pdfWriter = new PdfWriter(file);
//        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
//
//        Document document = new Document(pdfDocument);
//        document.setMargins(0f, 0f, 0f, 0f);
        
		int foot = 0;
		for(Client[] app:clientArray) {
			foot++;
			
			
			int client_size = app.length;
			int pra_size1 = 1;
			int pra_size2 = 1;
			int pra_max_size=1;
			if(app[0].getStr("previous_residential_address")!=null) {
	              JSONArray praJsonArray = JSONArray.fromObject(app[0].getStr("previous_residential_address"));
	              pra_size1 = praJsonArray.size();
	        }
			if(client_size==2) {
				if(app[1]!=null&&app[1].getStr("previous_residential_address")!=null) {
		              JSONArray praJsonArray2 = JSONArray.fromObject(app[1].getStr("previous_residential_address"));
		              pra_size2 = praJsonArray2.size();
		        }
			}
			if(pra_size1>1||pra_size2>1) {
				pra_max_size=2;
			}
			
			int ce_size1 = 1;
			int ce_size2 = 1;
			int ce_max_size=1;
			if(app[0].getStr("previous_residential_address")!=null) {
	              JSONArray ceJsonArray = JSONArray.fromObject(app[0].getStr("current_employment"));
	              ce_size1 = ceJsonArray.size();
	        }
			if(client_size==2) {
				if(app[1]!=null&&app[1].getStr("previous_residential_address")!=null) {
		              JSONArray ceJsonArray2 = JSONArray.fromObject(app[1].getStr("current_employment"));
		              ce_size2 = ceJsonArray2.size();
		        }
			}
			if(ce_size1>1||ce_size2>1) {
				ce_max_size=2;
			}
			String destfile = dest+"sm_2_"+foot+"_1_"+pra_max_size+".pdf";
			String destfile2 = dest+"sm_2_"+foot+"_2_"+ce_max_size+".pdf";
			String src = realPath+"/pdf/sm_2_"+foot+"_1_"+pra_max_size+".pdf";
			String src2 = realPath+"/pdf/sm_2_1_2_"+ce_max_size+".pdf";
			filelist.add(destfile);
			filelist.add(destfile2);
			
			PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(destfile));
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
	        Map<String, PdfFormField> fields = form.getFormFields();
	        
	        PdfDocument pdfDoc2 = new PdfDocument(new PdfReader(src2), new PdfWriter(destfile2));
			PdfAcroForm form2 = PdfAcroForm.getAcroForm(pdfDoc2, true);
	        Map<String, PdfFormField> fields2 = form2.getFormFields();
	        int m=0;
	        for(int k=0;k<app.length;k++) {
	        	m=k+1;
	        	//int applicant_no = foot*2+m;
	        	//fields.get("applicant_no"+m).setValue(String.valueOf(applicant_no)).setFontSize(18);
	        	if(app[k]==null) {
	        		break;
	        	}
	        	//System.out.println(app.length+":k="+k+":age:"+app[k].getStr("dependants_age"));
	        	if(app[k].getStr("dependants_age")!=null) {
			        JSONArray ageJsonArray = JSONArray.fromObject(app[k].getStr("dependants_age"));
			        for (int i = 0; i < ageJsonArray.size(); i++) {
			        	int j=i+1;
			    		fields.get("app"+m+"_nod_"+j).setValue(ageJsonArray.getString(i)).setFontSize(9);
					}
	        	}
		//  
		        JSONArray mobileJsonArray = JSONArray.fromObject(app[k].getStr("mobile"));
		        //System.out.println(purposeJsonArray.join(","));
		        String mobile = "";
		        for (int i = 0; i < mobileJsonArray.size(); i++) {
		        	if(i>0)mobile += ", ";
					mobile += mobileJsonArray.getString(i);
					//System.out.println(lending_purpose);
				}
		        String[] gender = {"Female","Male","Unknown"};
		        //0-None,1-Permanent Resident,2-New Zealand Citizenship
		        //`marital_status` tinyint(2) DEFAULT NULL COMMENT '0-Married,1-Single,2-De Facto,3-Other',
		        String[] residency_nz = {"None","Permanent Resident","New Zealand Citizenship"};
		        String[] marital_status = {"Married","Single","De Facto","Other"};
		        
		        String birthday = "";
		        if(app[k].getDate("birthday")!=null) {
		        	birthday = DateFmt.formatDate(app[k].getDate("birthday").toString(),"yyyy-MM-dd","dd/MM/yyyy");
		        }
		        if(app[k].getStr("title")!=null) {
		        	fields.get("app"+m+"_title_d").setValue(app[k].getStr("title")).setFontSize(9);
		        }
		        
		        fields.get("app"+m+"_dob").setValue(birthday).setFontSize(9);
		        fields.get("app"+m+"_gender").setValue(gender[app[k].getInt("gender")]).setFontSize(9);
		        fields.get("app"+m+"_last_name").setValue(StrUtil.null2Blank(app[k].getStr("last_name"))).setFontSize(9);
		        fields.get("app"+m+"_first_name").setValue(StrUtil.null2Blank(app[k].getStr("first_name"))).setFontSize(9);
		        fields.get("app"+m+"_nod").setValue(StrUtil.null2Blank(app[k].getInt("dependants_number"))).setFontSize(9);
		        int rnz = 0;
		        if(app[k].getInt("residency_nz")!=null) {
		        	rnz = app[k].getInt("residency_nz");
		        }
		        fields.get("app"+m+"_nz_residency").setValue(residency_nz[rnz]).setFontSize(9);
		        int ms = 0;
		        if(app[k].getInt("marital_status")!=null) {
		        	ms = app[k].getInt("marital_status");
		        }
		        fields.get("app"+m+"_marital_status").setValue(marital_status[ms]).setFontSize(9);
		        fields.get("app"+m+"_current_address").setValue(StrUtil.null2Blank(app[k].getStr("current_residential_address"))).setFontSize(9);
		        fields.get("app"+m+"_current_address_ts_y").setValue(StrUtil.null2Blank(app[k].getInt("time_stayed_years"))).setFontSize(9);
		        fields.get("app"+m+"_current_address_ts_m").setValue(StrUtil.null2Blank(app[k].getInt("time_stayed_months"))).setFontSize(9);
		        
		        if(app[k].getStr("previous_residential_address")!=null) {
		              JSONArray praJsonArray = JSONArray.fromObject(app[k].getStr("previous_residential_address"));
		              
		              for (int i = 0; i < praJsonArray.size(); i++) {
		                JSONObject praObj = praJsonArray.getJSONObject(i);
		                int j=i+1;
		                String preaddress = "";
		                String p_time_stayed_years = "";
		                String p_time_stayed_months = "";
		                if(praObj!=null) {
		                	preaddress = StrUtil.null2Blank(praObj.getString("preaddress"));
		                  p_time_stayed_years = StrUtil.null2Blank(praObj.getString("p_time_stayed_years"));
		                  p_time_stayed_months = StrUtil.null2Blank(praObj.getString("p_time_stayed_months"));
		                }
		                fields.get("app"+m+"_previous_address_"+j).setValue(preaddress).setFontSize(9);
		                fields.get("app"+m+"_previous_address_ts_y_"+j).setValue(p_time_stayed_years).setFontSize(9);
		                fields.get("app"+m+"_previous_address_ts_m_"+j).setValue(p_time_stayed_months).setFontSize(9);
		              }
		        }

		        
		        
		        
		        
		        
		        
		        
		        fields.get("app"+m+"_postal_address").setValue(StrUtil.null2Blank(app[k].getStr("postal_address"))).setFontSize(9);
		//        `current_living_situation` tinyint(2) DEFAULT NULL COMMENT '0-Renting,1-Living in own home,2-Boarding,3-Other',
		        String[] current_living_situation = {"Renting","Living in own home","Boarding","Other"};
		        int cls = 0;
		        if(app[k].getInt("current_living_situation")!=null) {
		        	cls = app[k].getInt("current_living_situation");
		        }
		        fields.get("app"+m+"_current_living_situation").setValue(current_living_situation[cls]).setFontSize(9);
		        fields.get("app"+m+"_contact_mobile").setValue(mobile).setFontSize(9);
		        fields.get("app"+m+"_contact_home").setValue(StrUtil.null2Blank(app[k].getStr("home_phone"))).setFontSize(9);
		        fields.get("app"+m+"_contact_work").setValue(StrUtil.null2Blank(app[k].getStr("work_phone"))).setFontSize(9);
		        //fields.get("app"+m+"_contact_wechat").setValue(StrUtil.null2Blank(app[k].getStr("wechat"))).setFontSize(9);
		        
		        JSONArray emailJsonArray = JSONArray.fromObject(app[k].getStr("personal_email"));
		        String personal_email = "";
		        for (int i = 0; i < emailJsonArray.size(); i++) {
		        	if(i>0) {
		        		break;
		        		//personal_email += ", ";
		        	}
		        	personal_email += emailJsonArray.getString(i);
				}
		        
		        fields.get("app"+m+"_contact_personal_email").setValue(personal_email).setFontSize(9);
		        fields.get("app"+m+"_contact_work_email").setValue(StrUtil.null2Blank(app[k].getStr("work_email"))).setFontSize(9);
//		        
		        JSONArray sourceofincomeJsonArray = JSONArray.fromObject(app[k].getStr("source_of_income"));
		        String soi = "";
		        for (int i = 0; i < sourceofincomeJsonArray.size(); i++) {
		        	if(i>0)soi += ", ";
		        	soi += sourceofincomeJsonArray.getString(i);
				}
		        fields2.get("app"+m+"_employment_soi").setValue(soi).setFontSize(9);
//		        fields2.get("app"+m+"_employment_occupation").setValue((StrUtil.null2Blank(app[k].getStr("occupation")))).setFontSize(9);
//		        fields2.get("app"+m+"_employment_company").setValue(StrUtil.null2Blank(app[k].getStr("company"))).setFontSize(9);
//		        fields2.get("app"+m+"_employment_duration").setValue(StrUtil.null2Blank(app[k].getStr("duration"))).setFontSize(9);
//		        fields2.get("app"+m+"_employment_gi").setValue(StrUtil.null2Blank(StrUtil.formatString(app[k].getStr("gross_income")))).setFontSize(9);
//		        
		        if(app[k].getStr("current_employment")!=null) {
			        JSONArray ceJsonArray = JSONArray.fromObject(app[k].getStr("current_employment"));
			        
			        for (int i = 0; i < ceJsonArray.size(); i++) {
			        	JSONObject ceObj = ceJsonArray.getJSONObject(i);
			        	int j=i+1;
			        	String curcompany = "";
			        	String curoccupation = "";
			        	String curduration = "";
			        	String gross_income = "";
			        	if(ceObj!=null) {
			        		curcompany = StrUtil.null2Blank(ceObj.getString("curcompany"));
			        		curoccupation = StrUtil.null2Blank(ceObj.getString("curoccupation"));
			        		curduration = StrUtil.null2Blank(ceObj.getString("curduration"));
			        		gross_income = StrUtil.null2Blank(ceObj.getString("gross_income"));
			        	}
			        	System.out.print("app"+m+"_employment_company_"+j);
				        fields2.get("app"+m+"_employment_company_"+j).setValue(curcompany).setFontSize(9);
				        fields2.get("app"+m+"_employment_occupation_"+j).setValue(curoccupation).setFontSize(9);
				        fields2.get("app"+m+"_employment_duration_"+j).setValue(curduration).setFontSize(9);
				        fields2.get("app"+m+"_employment_gi_"+j).setValue(gross_income).setFontSize(9);
					}
		        }
		        
		        if(app[k].getStr("previous_employment")!=null) {
			        JSONArray peJsonArray = JSONArray.fromObject(app[k].getStr("previous_employment"));
			        
			        for (int i = 0; i < peJsonArray.size(); i++) {
			        	JSONObject peObj = peJsonArray.getJSONObject(i);
			        	int j=i+1;
			        	String company_name = "";
			        	String time_stayed = "";
			        	if(peObj!=null) {
			        		company_name = StrUtil.null2Blank(peObj.getString("company_name"));
			        		time_stayed = StrUtil.null2Blank(peObj.getString("time_stayed"));
			        	}
			    		fields2.get("app"+m+"_employment_p_cn"+j).setValue(company_name).setFontSize(9);
			    		fields2.get("app"+m+"_employment_p_ts"+j).setValue(time_stayed).setFontSize(9);
					}
		        }
	        }
	        
	        form.flattenFields();//设置表单域不可编辑       
	        pdfDoc.close();
	        form2.flattenFields();//设置表单域不可编辑       
	        pdfDoc2.close();
		}
		MergeFiles(url,filelist);
		return url;
    }
	public static void pdf_4_old(String dest,Application app) throws Exception {
		String src = "/Users/robin/pdf/sm_4.pdf";
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
		PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();


        if(app.getStr("asset")!=null) {

	    	JSONObject assetObj = JSONObject.fromObject(app.getStr("asset"));
	    	
	    	
	    	if(assetObj!=null) {
	    		String total_value = StrUtil.null2Blank(assetObj.getString("total_value"));
	    		fields.get("total_assets_value").setValue(total_value).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    		
	    		//cash
	    		if(assetObj.getJSONObject("cash")!=null) {
	    			String cash_d = "";
	    	    	String cash_v = "";
		    		cash_d = StrUtil.null2Blank(assetObj.getJSONObject("cash").getString("cash_d"));
		    		cash_v = StrUtil.null2Blank(assetObj.getJSONObject("cash").getString("cash_v"));
		    		fields.get("assets_cash_d").setValue(cash_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
					fields.get("assets_cash_v").setValue(cash_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    		}
	    		//saving
	    		JSONArray saveJsonArray = JSONArray.fromObject(assetObj.getJSONArray("saving"));
	    		if(saveJsonArray.size()>0) {
	    			for (int i = 0; i < saveJsonArray.size(); i++) {
	    				String saving_d = "";
	    		    	String saving_v = "";
	    				int j=i+1;
	    				JSONObject saveObj = saveJsonArray.getJSONObject(i);
	    				if(saveObj!=null) {
	    					saving_d = StrUtil.null2Blank(saveObj.getString("saving_d"));
	    					saving_v = StrUtil.null2Blank(saveObj.getString("saving_v"));
	    					if(fields.get("assets_cheque_d"+j)!=null) {
	    						fields.get("assets_cheque_d"+j).setValue(saving_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    						fields.get("assets_cheque_v"+j).setValue(saving_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    					}
	    				}
	    			}
	    		}
	    		//term_deposit
	    		if(assetObj.getJSONObject("term_deposit")!=null) {
	    			String term_deposit_d = "";
	    	    	String term_deposit_v = "";
	    	    	term_deposit_d = StrUtil.null2Blank(assetObj.getJSONObject("term_deposit").getString("term_deposit_d"));
	    	    	term_deposit_v = StrUtil.null2Blank(assetObj.getJSONObject("term_deposit").getString("term_deposit_v"));
		    		fields.get("assets_term_deposit_d").setValue(term_deposit_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
					fields.get("assets_term_deposit_v").setValue(term_deposit_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    		}
	    		//other_currency_account
	    		if(assetObj.getJSONObject("other_currency_account")!=null) {
	    			String other_currency_account_d = "";
	    	    	String other_currency_account_v = "";
	    	    	other_currency_account_d = StrUtil.null2Blank(assetObj.getJSONObject("other_currency_account").getString("other_currency_account_d"));
	    	    	other_currency_account_v = StrUtil.null2Blank(assetObj.getJSONObject("other_currency_account").getString("other_currency_account_v"));
		    		fields.get("assets_term_oca_d").setValue(other_currency_account_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
					fields.get("assets_term_oca_v").setValue(other_currency_account_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    		}
	    		//motor_vehicle
	    		JSONArray vehicleJsonArray = JSONArray.fromObject(assetObj.getJSONArray("vehicle"));
	    		if(vehicleJsonArray.size()>0) {
	    			for (int i = 0; i < vehicleJsonArray.size(); i++) {
	    				String vehicle_d = "";
	    		    	String vehicle_v = "";
	    				int j=i+1;
	    				JSONObject vehicleObj = vehicleJsonArray.getJSONObject(i);
	    				if(vehicleObj!=null) {
	    					vehicle_d = StrUtil.null2Blank(vehicleObj.getString("vehicle_d"));
	    					vehicle_v = StrUtil.null2Blank(vehicleObj.getString("vehicle_v"));
	    					if(fields.get("assets_motor_vehicle_d"+j)!=null) {
	    						fields.get("assets_motor_vehicle_d"+j).setValue(vehicle_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    						fields.get("assets_motor_vehicle_v"+j).setValue(vehicle_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    					}
	    				}
	    			}
	    		}
	    		//furniture
	    		if(assetObj.getJSONObject("furniture")!=null) {
	    			String furniture_d = "";
	    	    	String furniture_v = "";
		    		furniture_d = StrUtil.null2Blank(assetObj.getJSONObject("furniture").getString("furniture_d"));
		    		furniture_v = StrUtil.null2Blank(assetObj.getJSONObject("furniture").getString("furniture_v"));
		    		fields.get("assets_furniture_d").setValue(furniture_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
					fields.get("assets_furniture_v").setValue(furniture_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    		}
	    		//deposit
	    		if(assetObj.getJSONObject("deposit")!=null) {
	    			String deposit_d = "";
	    	    	String deposit_v = "";
		    		deposit_d = StrUtil.null2Blank(assetObj.getJSONObject("deposit").getString("deposit_d"));
		    		deposit_v = StrUtil.null2Blank(assetObj.getJSONObject("deposit").getString("deposit_v"));
		    		fields.get("assets_dop_d").setValue(deposit_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
					fields.get("assets_dop_v").setValue(deposit_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    		}
	    		//property
	    		JSONArray propertyJsonArray = JSONArray.fromObject(assetObj.getJSONArray("property"));
	    		if(propertyJsonArray.size()>0) {
	    			for (int i = 0; i < propertyJsonArray.size(); i++) {
	    				String property_d = "";
	    		    	String property_v = "";
	    				int j=i+1;
	    				JSONObject propertyObj = propertyJsonArray.getJSONObject(i);
	    				if(propertyObj!=null) {
	    					property_d = StrUtil.null2Blank(propertyObj.getString("property_d"));
	    					property_v = StrUtil.null2Blank(propertyObj.getString("property_v"));
	    					if(fields.get("assets_pp_d"+j)!=null) {
	    						fields.get("assets_pp_d"+j).setValue(property_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    						fields.get("assets_pp_v"+j).setValue(property_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    					}
	    				}
	    			}
	    		}
	    		//businessown
	    		JSONArray businessownJsonArray = JSONArray.fromObject(assetObj.getJSONArray("businessown"));
	    		if(businessownJsonArray.size()>0) {
	    			for (int i = 0; i < businessownJsonArray.size(); i++) {
	    				String businessown_d = "";
	    		    	String businessown_v = "";
	    				int j=i+1;
	    				JSONObject businessownObj = businessownJsonArray.getJSONObject(i);
	    				if(businessownObj!=null) {
	    					businessown_d = StrUtil.null2Blank(businessownObj.getString("businessown_d"));
	    					businessown_v = StrUtil.null2Blank(businessownObj.getString("businessown_v"));
	    					if(fields.get("assets_business_owned_d"+j)!=null) {
	    						fields.get("assets_business_owned_d"+j).setValue(businessown_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    						fields.get("assets_business_owned_v"+j).setValue(businessown_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    					}
	    				}
	    			}
	    		}
	    		//kiwisaver
	    		JSONArray kiwisaverJsonArray = JSONArray.fromObject(assetObj.getJSONArray("kiwisaver"));
	    		if(kiwisaverJsonArray.size()>0) {
	    			for (int i = 0; i < kiwisaverJsonArray.size(); i++) {
	    				String kiwisaver_d = "";
	    		    	String kiwisaver_v = "";
	    				int j=i+1;
	    				JSONObject kiwisaverObj = kiwisaverJsonArray.getJSONObject(i);
	    				if(kiwisaverObj!=null) {
	    					kiwisaver_d = StrUtil.null2Blank(kiwisaverObj.getString("kiwisaver_d"));
	    					kiwisaver_v = StrUtil.null2Blank(kiwisaverObj.getString("kiwisaver_v"));
	    					if(fields.get("assets_kss_d"+j)!=null) {
	    						fields.get("assets_kss_d"+j).setValue(kiwisaver_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    						fields.get("assets_kss_v"+j).setValue(kiwisaver_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    					}
	    				}
	    			}
	    		}
	    		//otherasset
	    		JSONArray otherassetJsonArray = JSONArray.fromObject(assetObj.getJSONArray("otherasset"));
	    		if(otherassetJsonArray.size()>0) {
	    			for (int i = 0; i < otherassetJsonArray.size(); i++) {
	    				String otherasset_d = "";
	    		    	String otherasset_v = "";
	    				int j=i+1;
	    				JSONObject otherassetObj = otherassetJsonArray.getJSONObject(i);
	    				if(otherassetObj!=null) {
	    					otherasset_d = StrUtil.null2Blank(otherassetObj.getString("otherasset_d"));
	    					otherasset_v = StrUtil.null2Blank(otherassetObj.getString("otherasset_v"));
	    					if(fields.get("assets_other_assets_d"+j)!=null) {
	    						fields.get("assets_other_assets_d"+j).setValue(otherasset_d).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    						fields.get("assets_other_assets_v"+j).setValue(otherasset_v).setFontSize(9).setJustification(PdfFormField.ALIGN_RIGHT);
	    					}
	    				}
	    			}
	    		}
	    	}
			
			
	
        }
        form.flattenFields();//设置表单域不可编辑       
        pdfDoc.close();
    }
	public static String pdf_3(String dest,String realPath,Application app) throws Exception {
		String src = realPath+"/pdf/sm_3.pdf";
		String url = dest+"sm_3.pdf";
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(url));
		PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();


        JSONArray noteJsonArray = JSONArray.fromObject(app.getStr("note"));
        String notes = "";
        for (int i = 0; i < noteJsonArray.size(); i++) {
        	if(i>0)notes += "\n\n";
        	notes += noteJsonArray.getString(i);
		}
        
        fields.get("diary_note_1").setValue(notes).setFontSize(9);
        
        form.flattenFields();//设置表单域不可编辑       
        pdfDoc.close();
        return url;
    }
	public static String pdf_1(String dest,String realPath,Application app,String logo,int useLogo) throws Exception {
		String src = realPath+"/pdf/sm_1.pdf";
		String src2 = realPath+"/pdf/sm_1_s8.pdf";
		String url0 = dest+"sm_1_0.pdf";
		String url = dest+"sm_1.pdf";
		JSONArray securityJsonArray = JSONArray.fromObject(app.getStr("security"));
		
		if(securityJsonArray.size()>4) {
			src = src2;
		}
		if(logo!=null&&!logo.equals("")&&useLogo==1) {
			replaceImage(url0,src,realPath+logo);
			src = url0;
		}
		
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(url));
		PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        for (int i = 0; i < securityJsonArray.size(); i++) {
        	JSONObject securityObj = securityJsonArray.getJSONObject(i);
        	int j=i+1;
        	//System.out.println("j:"+j);
    		fields.get("security_pty"+j).setValue(securityObj.getString("property")).setFontSize(9);
    		fields.get("security_p"+j+"_type").setValue(securityObj.getString("property_type")).setFontSize(9);
    		fields.get("security_p"+j+"_use").setValue(securityObj.getString("use")).setFontSize(9);
    		fields.get("security_p"+j+"_value").setValue(StrUtil.formatString(securityObj.getString("value"))).setFontSize(9);
    		fields.get("security_p"+j+"_holder").setValue(securityObj.getString("holder")).setFontSize(9);
    		fields.get("security_p"+j+"_borrower").setValue(securityObj.getString("borrower")).setFontSize(9);
		}
		
        
        PdfPage page = pdfDoc.getFirstPage();
        PdfDictionary dict = page.getPdfObject();
 
        PdfObject object = dict.get(PdfName.Contents);
        if (object instanceof PdfStream) {
            PdfStream stream = (PdfStream) object;
            byte[] data = stream.getBytes();
            String replacedData = new String(data)
            		.replace("Application Date","Lender")
            		.replace("Bank", "Application Date")
            		.replace("Email","Adviser Email")
            		.replace("Mobile","Adviser Contact Number");
            //System.out.println(replacedData);
            stream.setData(replacedData.getBytes(StandardCharsets.UTF_8));
        }
        
//        java.util.Iterator<String> it = fields.keySet().iterator();
//        while (it.hasNext()) {
//            //获取文本域名称
//            String name = it.next().toString();
//            System.out.println(name);
//            //填充文本域
//            //fields.get(name).setValue("111").setFontSize(12);
//        }    
        JSONArray purposeJsonArray = JSONArray.fromObject(app.getStr("lending_purpose"));
        //System.out.println(purposeJsonArray.join(","));
        String lending_purpose = "";
        for (int i = 0; i < purposeJsonArray.size(); i++) {
        	if(i>0)lending_purpose += ", ";
			lending_purpose += purposeJsonArray.getString(i);
			//System.out.println(lending_purpose);
		}
        
        String application_date = DateFmt.formatDate(app.getDate("application_date").toString(),"yyyy-MM-dd","dd/MM/yyyy");
        fields.get("application_date").setValue(application_date).setFontSize(9);
        fields.get("adviser_email").setValue(app.getStr("adviser_email")).setFontSize(9);
        fields.get("adviser_name").setValue(app.getStr("adviser_name")).setFontSize(9);
        fields.get("adviser_contact_number").setValue(app.getStr("adviser_contact_number")).setFontSize(9);
        fields.get("lender").setValue(StrUtil.null2Blank(app.getStr("lender"))).setFontSize(9);
        String lar = "";
        if(app.getBigDecimal("lending_amount_required")!=null) {
        	lar = StrUtil.formatString(app.getBigDecimal("lending_amount_required").doubleValue());
        }
        fields.get("lending_amount_required").setValue(lar).setFontSize(9);
        fields.get("LVR").setValue(StrUtil.null2Blank(app.getStr("LVR"))).setFontSize(9);
        fields.get("UMI").setValue(StrUtil.null2Blank(app.getStr("UMI"))).setFontSize(9);
        if(app.getDate("finance_date")!=null) {
        	fields.get("finance_date").setValue(DateFmt.formatDate(app.getDate("finance_date").toString(),"yyyy-MM-dd","dd/MM/yyyy")).setFontSize(9);
        }
        if(app.getDate("settlement_date")!=null) {
        	fields.get("settlement_date").setValue(DateFmt.formatDate(app.getDate("settlement_date").toString(),"yyyy-MM-dd","dd/MM/yyyy")).setFontSize(9);
        }
//        if(app.getBigDecimal("existing_loan_amount")!=null) {
//        	fields.get("existing_loan_amount").setValue(StrUtil.formatString(app.getBigDecimal("existing_loan_amount").doubleValue())).setFontSize(9);
//        }
//        if(app.getBigDecimal("total_loan_amount")!=null) {
//        	fields.get("total_loan_amount").setValue(StrUtil.formatString(app.getBigDecimal("total_loan_amount").doubleValue())).setFontSize(9);
//        }
        fields.get("purpose").setValue(lending_purpose+"\n"+StrUtil.null2Blank(app.getStr("purpose"))).setFontSize(9);
        
        form.flattenFields();//设置表单域不可编辑       
        pdfDoc.close();
        return url;
    }
	protected static void replaceImage(String dest,String src,String logo) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
 
        // Assume that there is a single XObject in the source document
        // and this single object is an image.
        PdfDictionary pageDict = pdfDoc.getFirstPage().getPdfObject();
        PdfDictionary resources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary xObjects = resources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = xObjects.keySet().iterator().next();
        PdfStream stream = xObjects.getAsStream(imgRef);
        //Image img = convertToBlackAndWhitePng(new PdfImageXObject(stream));
        Image img = new Image(ImageDataFactory.create(logo));
 
        // Replace the original image with the grayscale image
        xObjects.put(imgRef, img.getXObject().getPdfObject());
 
        pdfDoc.close();
    }
 
    private static Image convertToBlackAndWhitePng(PdfImageXObject image) throws IOException {
        BufferedImage bi = image.getBufferedImage();
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        newBi.getGraphics().drawImage(bi, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return new Image(ImageDataFactory.create(baos.toByteArray()));
    }
	protected static void manipulatePdf3(String dest,String src) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
 
        for (int p = 1; p <= pdfDoc.getNumberOfPages(); p++) {
            PdfPage page = pdfDoc.getPage(p);
            Rectangle media = page.getCropBox();
 
            if (media == null) {
                media = page.getMediaBox();
            }
            float llx = media.getX();
            float lly = media.getY();
            float w = media.getWidth();
            float h = media.getHeight();
 
            // It's important to write explicit Locale settings, because decimal separator differs in
            // different regions and in PDF only dot is respected
            String command = String.format(Locale.ENGLISH,
 
                    // re operator constructs a rectangle
                    // W operator - sets the clipping path
                    // n operator - starts a new path
                    // q, Q - operators save and restore the graphics state stack
                    "\nq %.2f %.2f %.2f %.2f re W n\nq\n", llx, lly, w, h);
 
            // The content, placed on a content stream before, will be rendered before the other content
            // and, therefore, could be understood as a background (bottom "layer")
            PdfPage pdfPage = pdfDoc.getPage(p);
            new PdfCanvas(pdfPage.newContentStreamBefore(), pdfPage.getResources(), pdfDoc)
                    .writeLiteral(command);
 
            // The content, placed on a content stream after, will be rendered after the other content
            // and, therefore, could be understood as a foreground (top "layer")
            new PdfCanvas(pdfPage.newContentStreamAfter(), pdfPage.getResources(), pdfDoc)
                    .writeLiteral("\nQ\nQ\n");
        }
 
        pdfDoc.close();
    }
	protected static void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());
 
        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();
 
        Cell cell = new Cell(1, 5).add(new Paragraph("Table XYZ (Continued)"));
        table.addHeaderCell(cell);
 
        cell = new Cell(1, 5).add(new Paragraph("Continue on next page"));
        table.addFooterCell(cell);
 
        table.setSkipFirstHeader(true);
        table.setSkipLastFooter(true);
 
        for (int i = 0; i < 350; i++) {
            table.addCell(String.valueOf(i + 1));
        }
 
        doc.add(table);
 
        doc.close();
    }
	protected static void manipulatePdf(String dest,String src) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(src));
        Rectangle pageSize = srcDoc.getFirstPage().getPageSize();
        PdfDocument resultPdfDoc = new PdfDocument(new PdfWriter(dest));
        resultPdfDoc.setDefaultPageSize(new PageSize(pageSize));
        resultPdfDoc.addNewPage();
 
        PdfFormXObject pageXObject = srcDoc.getFirstPage().copyAsFormXObject(resultPdfDoc);
        Rectangle toMove = new Rectangle(100, 500, 100, 100);
 
        // Create a formXObject of a page content, in which the area to move is cut.
        PdfFormXObject formXObject1 = new PdfFormXObject(pageSize);
        PdfCanvas canvas1 = new PdfCanvas(formXObject1, resultPdfDoc);
        canvas1.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas1.rectangle(toMove);
 
        // This method uses the even-odd rule to determine which regions lie inside the clipping path.
        canvas1.eoClip();
        canvas1.endPath();
        canvas1.addXObject(pageXObject, 0, 0);
 
        // Create a formXObject of the area to move.
        PdfFormXObject formXObject2 = new PdfFormXObject(pageSize);
        PdfCanvas canvas2 = new PdfCanvas(formXObject2, resultPdfDoc);
        canvas2.rectangle(toMove);
 
        // This method uses the nonzero winding rule to determine which regions lie inside the clipping path.
        canvas2.clip();
        canvas2.endPath();
        canvas2.addXObject(pageXObject, 0, 0);
 
        PdfCanvas canvas = new PdfCanvas(resultPdfDoc.getFirstPage());
        canvas.addXObject(formXObject1, 0, 0);
 
        // Add the area to move content, shifted 10 points to the left and 2 points to the bottom.
        canvas.addXObject(formXObject2, -20, -2);
 
        srcDoc.close();
        resultPdfDoc.close();
    }
	protected void manipulatePdf2(String dest,String src) throws Exception {
	    PdfWriter writer = new PdfWriter(dest);
	    writer.setSmartMode(true);
	    PdfDocument pdfDoc = new PdfDocument(writer);
	    pdfDoc.initializeOutlines();

	    ByteArrayOutputStream baos;
	    PdfReader reader;
	    PdfDocument pdfInnerDoc;
	    Map fields;
	    PdfAcroForm form;
	    StringTokenizer tokenizer;

	        // create a PDF in memory
	        baos = new ByteArrayOutputStream();
	        reader = new PdfReader(src);
	        pdfInnerDoc = new PdfDocument(reader, new PdfWriter(baos));
	        form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
	        //tokenizer = new StringTokenizer(line, ";");
	        fields = form.getFormFields();
//	        fields.get("name").setValue(tokenizer.nextToken());
//	        fields.get("abbr").setValue(tokenizer.nextToken());
//	        fields.get("capital").setValue(tokenizer.nextToken());
//	        fields.get("city").setValue(tokenizer.nextToken());
//	        fields.get("population").setValue(tokenizer.nextToken());
//	        fields.get("surface").setValue(tokenizer.nextToken());
//	        fields.get("timezone1").setValue(tokenizer.nextToken());
//	        fields.get("timezone2").setValue(tokenizer.nextToken());
//	        fields.get("dst").setValue(tokenizer.nextToken());
	        form.flattenFields();
	        pdfInnerDoc.close();

	        pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
	        pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
	        pdfInnerDoc.close();


	    pdfDoc.close();
	}
	protected static void manipulatePdf(String dest, PdfReader[] readers) throws FileNotFoundException {
	    PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
	    pdfDoc.initializeOutlines();
	    for (PdfReader reader : readers) {
	        PdfDocument readerDoc = new PdfDocument(reader);
	        readerDoc.copyPagesTo(1, readerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
	        readerDoc.close();
	    }
	    pdfDoc.close();
	}
	public static void image(String dest,String src) throws IOException {
		PdfDocument origPdf = new PdfDocument(new PdfReader(src));
		//PdfPage origPage = origPdf.getPage(1);
		
		PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
		//PdfFormXObject pageCopy = origPage.copyAsFormXObject(pdf);
		
//		Document document = new Document(pdf);
//		
//		Image image = new Image(pageCopy);
//		
//		document.add(image);
		
		PdfPage origPage = origPdf.getPage(1);
		Rectangle rect = origPage.getPageSize();
		Document document = new Document(pdf);

		PdfFormXObject pageCopy = origPage.copyAsFormXObject(pdf);
		Image image = new Image(pageCopy);
		image.setBorder(Border.NO_BORDER);
		image.setAutoScale(true);
		image.setHeight(rect.getHeight()-250);

		document.add(image);

		
		origPdf.close();
		pdf.close();
		document.close();
	}
	
//	public  static   void Pdf_Png(int pageNumber,String dest,String target )   {
//        int pagen= pageNumber;
//        File file = new File(dest);
//
//        PdfFile pdffile=null;
////      set up the PDF reading
//        try{
//            RandomAccessFile raf = new RandomAccessFile(file, "r");
//            FileChannel channel = raf.getChannel();
//            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
//            buf.clear();
//            byte[] b = new byte[buf.capacity()];
//            buf.get(b, 0, b.length);
//            // FileOutputStream outputStream =new FileOutputStream("F:\\公司相关资料\\pdf\\test2.pdf");
//            // outputStream.write(b);
//            // outputStream.close();
//            pdffile = new PdfFile(buf);
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//
//        if(pagen<pdffile.getNumPages())
//            return;
//        //print出该pdf文档的页数
//        System.out.println(pdffile.getNumPages());
//
//        //设置将第pagen也生成png图片
//        PdfPage page = pdffile.getPage(pagen);
////      create and configure a graphics object
//        int width = (int)page.getBBox().getWidth();
//        int height =(int)page.getBBox().getHeight();
//        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = img.createGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////      do the actual drawing
//        PDFRenderer renderer = new PDFRenderer(page, g2,
//                new Rectangle(0, 0, width, height), null, Color.RED);
//        try{
//            page.waitForFinish();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        renderer.run();
//        g2.dispose();
//
//        try{
//            ImageIO.write(img, "png", new File(target));
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
	public static void createPdf3() throws IOException {
		String dest = "/Users/robin/demo/hello.pdf";
		String FOX = "/Users/robin/demo/title.jpg";
		String DOG = "/Users/robin/demo/security.jpg";
		String SRC1 = "/Users/robin/demo/title.pdf";
		String SRC2 = "/Users/robin/demo/security.pdf";
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf);
        PdfDocument pdf1 = new PdfDocument(new PdfReader(SRC1));
        PdfDocument pdf2 = new PdfDocument(new PdfReader(SRC2));
        // Compose Paragraph
        Image fox = new Image(ImageDataFactory.create(FOX));
        Image dog = new Image(ImageDataFactory.create(DOG));
        //Paragraph p = new Paragraph()
                //.add(fox);
                //.add(" jumps over the lazy ");
                //.add(dog);
        // Add Paragraph to document
        document.add(fox).add(dog);

        //Close document
        document.close();
    }
	public static void createPdf2() throws IOException {
		String dest = "/Users/robin/demo/hello.pdf";
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf);

        // Create a PdfFont
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        // Add a Paragraph
        document.add(new Paragraph("iText is:").setFont(font));
        // Create a List
        List list = new List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022")
                .setFont(font);
        // Add ListItem objects
        list.add(new ListItem("Never gonna give you up"))
                .add(new ListItem("Never gonna let you down"))
                .add(new ListItem("Never gonna run around and desert you"))
                .add(new ListItem("Never gonna make you cry"))
                .add(new ListItem("Never gonna say goodbye"))
                .add(new ListItem("Never gonna tell a lie and hurt you"));
        // Add the list
        document.add(list);

        //Close document
        document.close();
    }
	public static void createPdf() throws IOException {
		String dest = "/Users/robin/demo/hello.pdf";
		File file = new File(dest);
        file.getParentFile().mkdirs();             //创建目录
        //生成文件
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf);

        //Add paragraph to the document
        document.add(new Paragraph("Hello World!"));

        //Close document
        document.close();
    }
	
	//合并
	public void merge() throws IOException {
		String dest = "/Users/robin/demo/all.pdf";
		String SRC1 = "/Users/robin/demo/security.pdf";
		String SRC2 = "/Users/robin/demo/title.pdf";
		PdfDocument destPdfDocument = new PdfDocument(new PdfWriter(dest));
		PdfDocument[] sources = new PdfDocument[] {
		        new PdfDocument(new PdfReader(SRC1)),
		        new PdfDocument(new PdfReader(SRC2))
		};
		for (PdfDocument sourcePdfDocument : sources) {
		    sourcePdfDocument.copyPagesTo(
		            1, sourcePdfDocument.getNumberOfPages(),
		            destPdfDocument, new PdfPageFormCopier());
		    sourcePdfDocument.close();
		}
		destPdfDocument.close();
	}
	/**
	 * 利用模板生成pdf
	 */
	public static void fillTemplate() {
		// 模板路径
		String templatePath = "/Users/robin/demo/security.pdf";

		ArrayList<ByteArrayOutputStream> baosList = new ArrayList<>();
		try {

			//根据路径生成 pdf
//			PdfWriter writer = new PdfWriter(newPDFPath);

			//内存中创建 PDF
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter writer = new PdfWriter(baos);
			PdfReader reader = new PdfReader(templatePath);
			PdfDocument pdf = new PdfDocument(reader, writer);
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
			Map<String, PdfFormField> fields = form.getFormFields();

			//处理中文问题
			PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);

			for (String name : fields.keySet()) {
				//获取文本域名称
//				if (StringUtils.equals(name, "name")) {
//					fields.get(name).setValue("李四").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "sex")) {
//					fields.get(name).setValue("男").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "id_number")) {
//					fields.get(name).setValue("320123199309223211").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "address")) {
//					fields.get(name).setValue("江苏省无锡市滨湖区第一人民医院").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "input_1")) {
//					//超过一行的文字使用 7 号字体
//					fields.get(name).setValue("盐城继续教育公需课我是一段很长很长很长超过一行的文字").setFont(font).setFontSize(TWO_ROWS_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "input_2")) {
//					fields.get(name).setValue("专业类别").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "input_3")) {
//					fields.get(name).setValue("学习形式").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "input_4")) {
//					fields.get(name).setValue("2").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "input_5")) {
//					fields.get(name).setValue("20180909").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
//
//				if (StringUtils.equals(name, "input_6")) {
//					fields.get(name).setValue("优秀").setFont(font).setFontSize(NORMAL_FONT_SIZE);
//				}
			}

			//生成的 pdf 不可以编辑
			form.flattenFields();

			//插入图片
			Document document = new Document(pdf);
//			Image image = new Image(ImageDataFactory.create("D:\\qr2.png"));
//			image.setWidth(80);
//			image.setHeight(80);

//			Paragraph paragraph = new Paragraph().add(image);
//			paragraph.setPaddings(140, 0, 0, 550);

//			document.add(paragraph);
			document.close();
			pdf.close();

			baosList.add(baos);


			mergerPDF(baosList);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void MergeFiles(String dest,ArrayList<String> src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfMerger merger = new PdfMerger(pdfDoc);            

        for (String file : src) {                
            PdfDocument newDoc = new PdfDocument(new PdfReader(file));
            merger.merge(newDoc, 1, newDoc.getNumberOfPages());

            newDoc.close();                                

        };
        pdfDoc.close();
    }
    /**
     * 合并 PDF
     */
    public static void mergerPDF(ArrayList<ByteArrayOutputStream> baosList) {
        try {
            PdfWriter writer = new PdfWriter("/Users/robin/demo/ok.pdf");
            PdfDocument pdf = new PdfDocument(writer);

            PdfMerger merger = new PdfMerger(pdf);

//			这里可以直接读取已存在的文件
//			PdfMerger merger = new PdfMerger(pdf);
			PdfDocument firstSourcePdf = new PdfDocument(new PdfReader("/Users/robin/demo/title.pdf"));
			merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());

            for (ByteArrayOutputStream byteArrayOutputStream : baosList) {
                PdfDocument SourcePdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
                merger.merge(SourcePdf, 1, firstSourcePdf.getNumberOfPages());
            }

            pdf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void pdf2Img(String dest,String src) throws Exception{

        try (final PDDocument document = PDDocument.load(new File(src))){
            PDFRenderer pdfRenderer = new PDFRenderer(document);
//            for (int page = 0; page < 1; ++page)
//            {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
               // String fileName = OUTPUT_DIR + "image-" + page + ".png";
                ImageIOUtil.writeImage(bim, dest, 300);
           // }
            document.close();
        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }
}