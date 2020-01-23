package com.cms.util;

import com.alibaba.druid.util.StringUtils;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 填充 pdf 并插入 二维码，如果有多个 pdf 进行合并操作
 *
 * @author Chengloong
 */
public class PdfTest {

	private static final int NORMAL_FONT_SIZE = 12;
	private static final int TWO_ROWS_FONT_SIZE = 7;

	public static void main(String[] args) throws Exception {

		//填充 pdf 模板
		//fillTemplate();
		
		PDFJoin.mergepdf(fileList,"d:/pdfjoin/all.pdf");

	}

	/**
	 * 利用模板生成pdf
	 */
	public static void fillTemplate() {
		// 模板路径
		String templatePath = "/Users/robin/demo/t.pdf";

		List<ByteArrayOutputStream> baosList = new ArrayList<>();
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
				if (StringUtils.equals(name, "name")) {
					fields.get(name).setValue("李四").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "sex")) {
					fields.get(name).setValue("男").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "id_number")) {
					fields.get(name).setValue("320123199309223211").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "address")) {
					fields.get(name).setValue("江苏省无锡市滨湖区第一人民医院").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "input_1")) {
					//超过一行的文字使用 7 号字体
					fields.get(name).setValue("盐城继续教育公需课我是一段很长很长很长超过一行的文字").setFont(font).setFontSize(TWO_ROWS_FONT_SIZE);
				}

				if (StringUtils.equals(name, "input_2")) {
					fields.get(name).setValue("专业类别").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "input_3")) {
					fields.get(name).setValue("学习形式").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "input_4")) {
					fields.get(name).setValue("2").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "input_5")) {
					fields.get(name).setValue("20180909").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}

				if (StringUtils.equals(name, "input_6")) {
					fields.get(name).setValue("优秀").setFont(font).setFontSize(NORMAL_FONT_SIZE);
				}
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

    /**
     * 合并 PDF
     */
    public static void mergerPDF(List<ByteArrayOutputStream> baosList) {
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
}