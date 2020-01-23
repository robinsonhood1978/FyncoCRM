package com.cms.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
 
 
 
/**
 * 
 * @author Admin
 *
 */
public class Itext2pdf {
    /**
     *生成一个简单的pdf文件
     * @throws IOException 
     * 
     */
	public static void hello(String dest) throws IOException {
//		//Initialize writer
//		PdfWriter writer=new PdfWriter(dest);
//
//		//Initialize document
//		PdfDocument pdfDoc = new PdfDocument(writer);
//		Document doc = new Document(pdfDoc);
//
//		//Add paragraph to the document
//		doc.add(new Paragraph("Hello World!"));
//
//		//Close document
//		doc.close();
		String fileName = "PDF";
		//inline 设置为可在浏览器内打开，下载文件名称为fileName
		//response.setHeader("Content-Disposition", "inline;fileName=" + new String(fileName.getBytes(), "ISO8859-1") + ".pdf");
		//response.setContentType("application/pdf;charset=UTF-8");

		//html文本地址
		String url = "http://127.0.0.1:82/application/view?l=p&id=31";
		//根据html获取网络上的html对象
		org.jsoup.nodes.Document html = Jsoup.connect(url).get();
		//替换html内容后将输出html文本
		String htmlStr = html.html();

		//导入字体
		//FontProvider font = new FontProvider();
		//font.addFont("/com/common/font/simsun.ttf");

		ConverterProperties c = new ConverterProperties();
		//c.setFontProvider(font);
		c.setCharset("utf-8");

		PdfDocument pd = new PdfDocument(new PdfWriter(dest));
		//设置文件标题为fileName，web上展示的标题为此标题
		pd.getDocumentInfo().setTitle(fileName);

		Document document = new Document(pd, PageSize.A3);
		document.add(new Paragraph("Hello World!"));
		try{
		    //设置页面边距 必须先设置边距，再添加内容，否则页边距无效
		    document.setMargins(20, 0, 20, 0);
		    java.util.List<IElement> list;
			try {
				list = HtmlConverter.convertToElements(htmlStr, c);
			
			    for (IElement ie : list) {
			        document.add((IBlockElement) ie);
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}finally {
		    document.close();
		}
	}
    public static void createPdf(String filePath) throws IOException{  
            //处理中文问题 
            PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);  
            
            PdfWriter writer = new PdfWriter(new FileOutputStream(new File(filePath)));
            PdfDocument pdf = new PdfDocument(writer);    
            Document document = new Document(pdf);
            Paragraph p =new Paragraph("hello word!我创建的第一个pdf文件");
            p.setFont(font);
            p.setFontSize(12);
           // p.setBorder(new SolidBorder(Color.makeColor(colorSpace),0.5f));//边框
            //p.setBackgroundColor(Color.GREEN);//绿色你懂的
            document.add(p);            
            document.close();
            writer.close();
            pdf.close();

    }
            /**
     *生成一个简单的模板pdf文件
     * 
     */
    public static void createTempPdf(String filePath){
        try {    
            PdfWriter writer = new PdfWriter(new FileOutputStream(new File(filePath)));
            PdfDocument pdf = new PdfDocument(writer);    
            Document document = new Document(pdf);
            addAcroForm(document);
            document.close();
            writer.close();
            pdf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 添加一个简单的模板
     * @param doc
     * @throws IOException 
     */
    public static void addAcroForm(Document doc) throws IOException {
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        Paragraph title = new Paragraph("社会主义核心价值观")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16);
        title.setFont(font);
        doc.add(title);
        doc.add(new Paragraph("名称:").setFont(font));
        doc.add(new Paragraph(" 一:").setFont(font));
        doc.add(new Paragraph(" 二:").setFont(font)); 
        doc.add(new Paragraph(" 三:").setFont(font));
        PdfAcroForm form = PdfAcroForm.getAcroForm(doc.getPdfDocument(), true);

        PdfTextFormField nameField = PdfTextFormField.createText(doc.getPdfDocument(),
                new Rectangle(99, 753, 425, 15), "名称:", "");//填充坐标
        PdfTextFormField nameField1 = PdfTextFormField.createText(doc.getPdfDocument(),
                new Rectangle(50, 725, 425, 15), "一:", "");
        PdfTextFormField nameField2 = PdfTextFormField.createText(doc.getPdfDocument(),
                new Rectangle(50, 695, 425, 15), "二:", "");
        PdfTextFormField nameField3 = PdfTextFormField.createText(doc.getPdfDocument(),
                new Rectangle(50, 667, 425, 15), "三:", "");
        form.addField(nameField);
        form.addField(nameField1);
        form.addField(nameField2);
        form.addField(nameField3);
      }
    
    /**
     * 使用pdf 模板生成 pdf 文件
     *      */
    public static void fillTemplate(String tempPdfPath,String targetPdfPath) {// 利用模板生成pdf
        try {
            //Initialize PDF document
            PdfDocument pdf = new PdfDocument(new PdfReader(tempPdfPath), new PdfWriter(targetPdfPath));
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();
            
            //处理中文问题  
            //PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);  
            String[] str = {
                    "111",
                    "asb",
                    "test",
                    "good"
                    };
            int i = 0;
            java.util.Iterator<String> it = fields.keySet().iterator();
            while (it.hasNext()) {
                //获取文本域名称
                String name = it.next().toString();
                System.out.println(name);
                //填充文本域
                fields.get(name).setValue(str[i]).setFontSize(12);
            }    
            form.flattenFields();//设置表单域不可编辑            
            pdf.close();
 
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        //createPdf("D:/firstPdf.pdf");
        //createTempPdf("D:/tempPdf.pdf");
        //fillTemplate("/Users/robin/demo/1.pdf","/Users/robin/demo/2.pdf");
//    	try {
//			hello("/Users/robin/demo/hello.pdf");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	HtmlConverter.convertToPdf(new File("/Users/robin/demo/good2/form.html"), new File("/Users/robin/demo/3.pdf"));

    }
}