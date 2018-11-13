package system_utils.io_tools;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFWriter {
	private PDDocument document;
	private PDPage current_page;
	
	private PDFont font = PDType1Font.TIMES_ROMAN;
	private int font_size = 16;
	private int title_font_size = 24;
	private float margin_top = 150;
	private float margin_bottom = 150;
	
	private float graphing_offset;
	private int graphs_on_page;
	private String current_page_title;
	
	public PDFWriter(String title) {
		this.document = new PDDocument();
		this.graphs_on_page = 0;
		
		set_title_page(title);
	}
	
	private void set_title_page(String title) {
		PDPage title_page = new PDPage();
		document.addPage(title_page);

		try {
			// Beginning to add to document
			PDPageContentStream contentStream;
			contentStream = new PDPageContentStream(document, title_page);

			int marginTop = 250;
			float titleWidth = font.getStringWidth(title) / 1000 * title_font_size;
			float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * font_size;
			
			contentStream.beginText();
			contentStream.newLineAtOffset((title_page.getMediaBox().getWidth() - titleWidth) / 2, title_page.getMediaBox().getHeight() - marginTop - titleHeight);
			
			contentStream.setFont(font, title_font_size);
			contentStream.showText(title);
			contentStream.endText();
			contentStream.close();
		} catch (IOException e) {
		}
		
		// Set graphing offset for the rest of the document
		float page_height = title_page.getMediaBox().getHeight();
		graphing_offset = (page_height - (this.margin_top + this.margin_bottom)) / 4;
	}
	
	private float graph_position() {
		return current_page.getMediaBox().getHeight() - (margin_top + (graphing_offset * graphs_on_page));
	}
	
	public void new_page(String text) {
		PDPage page = new PDPage();
		document.addPage(page);
		
		String page_title = text + " Responses";

		try {
			// Beginning to add to document
			PDPageContentStream contentStream1;
			contentStream1 = new PDPageContentStream(document, page);

			contentStream1.beginText();
			
			float titleWidth = font.getStringWidth(page_title) / 1000 * title_font_size;
			contentStream1.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 100);
			
			contentStream1.setFont(font, title_font_size);
			contentStream1.showText(page_title);
			contentStream1.endText();
			contentStream1.close();
			
			current_page_title = text;
			current_page = page;
			graphs_on_page = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String text) {
		
		if (graphs_on_page == 4) 
			new_page(current_page_title);
		
		try {
			// Beginning to add to document
			PDPageContentStream contentStream;
			contentStream = new PDPageContentStream(document, current_page, PDPageContentStream.AppendMode.APPEND, true, true);

			float pos = graph_position();
			
			contentStream.beginText();
			contentStream.newLineAtOffset(100, pos);
			
			contentStream.setFont(font, font_size);
			contentStream.showText(text);
			contentStream.endText();
			contentStream.close();
			
			graphs_on_page++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean write_to_disk(String path) {

		try {
			// Save the newly created document
			document.save(path);

			// finally make sure that the document is properly closed.
			document.close();
			
			return true;
		} catch (IOException e1) {
			return false;
		}
	}
}
