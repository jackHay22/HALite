package system_utils.io_tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import system_utils.CorrelationInfo;
import system_utils.DataStore;
import system_utils.Element;
import system_utils.ElementCorrelationInfo;
import system_utils.EquationPlot;
import ui_graphlib.BaseGraph;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.DrawablePanel;
import ui_graphlib.ModelGraph;
import ui_stdlib.SystemThemes;
import ui_stdlib.dialogwindows.ErrorDialog;

public class DataExporter {
	private DataStore ds;
	
	public DataExporter(DataStore ds) {
		this.ds = ds;
	}
	
	public boolean export_csv(String output, String file_path, int export_type) {
		try {
			PrintWriter pw = new PrintWriter(new File(file_path + ".csv"));
			pw.write(output);
			pw.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	public boolean export_calibration_graphs(String file_path, int export_type) {
		
		ModelGraph model_graph = new ModelGraph();
		model_graph.set_datastore(ds);
		
		// Create new PDF 
		int graphs_per_page = 1;
		PDFWriter<DataStore> pdf_doc = new PDFWriter<DataStore>("Calibration Model Graphs", graphs_per_page);
		
		write_graphs(pdf_doc, model_graph, export_type);
		
		return pdf_doc.write_to_disk(file_path + ".pdf");
	}
	
	public boolean export_response_graphs(String file_path, int export_type) {

		CorrelationGraph corr_graph = new CorrelationGraph();
		corr_graph.set_datastore(ds);
		
		// Create new PDF 
		int graphs_per_page = 3;
		PDFWriter<DataStore> pdf_doc = new PDFWriter<DataStore>("Response Graphs", graphs_per_page);
		
		write_graphs(pdf_doc, corr_graph, export_type);
		
		return pdf_doc.write_to_disk(file_path + ".pdf");
	}
	
	private void write_graphs(PDFWriter<DataStore> pdf_doc, BaseGraph<DataStore> graph, int export_type) {
		try {
			
			Map<Element, ElementCorrelationInfo> all_corrs = ds.get_correlation_map();
			for (Entry<Element, ElementCorrelationInfo> entry : all_corrs.entrySet()) {
				ElementCorrelationInfo elem_corrs = entry.getValue();
				ArrayList<CorrelationInfo> selected_elems = elem_corrs.get_selected();
				
				// Go to next element if no secondary elements selected
				if (selected_elems.isEmpty())
					continue;
				
				// Create at least one new page for every primary element
				String primary_elem = entry.getKey().name();
				
				if (export_type == SystemThemes.PDF_RESPONSE_GRAPHS) {
					pdf_doc.new_page(primary_elem + " Responses");
					
					for (CorrelationInfo corr_info : selected_elems) {
						
						String title = corr_info.get_secondary().name() + " vs. " + primary_elem;
						
						ds.set_correlation_graph_elements(entry.getKey(), corr_info.get_secondary());
						
						DrawablePanel<DataStore> gpanel = graph.get_points_panel();
					
						EquationPlot eq = corr_info.get_equation();
						
						String equation = "r^2: " + eq.get_r2() + "    ||    " + eq.get_str_rep();
						
						// Write secondary element name and corresponding graph to PDF file
						pdf_doc.init_img(gpanel);
						graph.refresh();
												
						// Write secondary element name and corresponding graph to PDF file
						pdf_doc.write(title, equation, gpanel);
						
					}
				}
				else {
					pdf_doc.new_page("Calibration Model " + primary_elem + " vs Given");
					
					EquationPlot eq = elem_corrs.get_equation();
					
					DecimalFormat df = new DecimalFormat("#.#####");
					
					String equation = "r^2: " + df.format(eq.get_r2()) + "    ||    " + eq.get_str_rep();
					
					ds.set_model_data_element(entry.getKey());
					
					DrawablePanel<DataStore> gpanel = graph.get_points_panel();
					
					pdf_doc.init_img(gpanel);
					graph.refresh();
					
					pdf_doc.write("", equation, gpanel);
				}
			}
			
		} catch (Exception e) {
			ErrorDialog<DataStore> err = new ErrorDialog<DataStore>("Export Error", "Unable to export project to PDF.");
			err.show_dialog();
		}
	}
}
