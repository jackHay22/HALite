package ui_stdlib.dialogwindows;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFileChooser;
import system_utils.DataStore;
import system_utils.DataTable;
import system_utils.Element;
import system_utils.ElementCorrelationInfo;
import system_utils.TableKey;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;
import ui_framework.SystemWindow;

@SuppressWarnings("serial")
public class OpenDialog extends SystemDialog implements ui_framework.ScheduledState {
	DataStore save_loader;
	FileDialog save_dialog;
	JFileChooser file_chooser;
	
	public OpenDialog(String title, SystemWindow main_window) {
		super(title);
		save_loader = new DataStore(main_window);
		save_dialog = new FileDialog(this, "Choose saved file");
		this.setLayout(new GridLayout(4,0));
	}

	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {
		
		file_chooser = new JFileChooser();
		String file = get_new_target();
		
		if (!file.isEmpty() && is_datastore_file(file)) {
			set_datastore(this.save_loader, file);
		}
		
	}

	private boolean load_from_save() {
		save_dialog.setMultipleMode(false);
		save_dialog.setVisible(true);
		java.io.File[] path = save_dialog.getFiles();
		
		try {
			return path.length > 0 && save_loader.load_from_save(path[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean isNumeric(String strNum) {
	    try {
	        @SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	private ArrayList<Double> getDoubleArray(ArrayList<String> arr) {
		ArrayList<Double> result = new ArrayList<Double>();
        for(String stringValue : arr) {
        	result.add(Double.parseDouble(stringValue)); 
        }       
        return result;
	}
	
	private void set_datastore(DataStore ds, String file) {
		BufferedReader reader = null;
		
		try {
		    reader = new BufferedReader(new FileReader(file));
		    
		    ArrayList<String> test = new ArrayList<String>();
		    test.add("hello");
		    test.add("boop");
		    test.add("wup");
		    
		    System.out.println(test.toString());
		    
		    String line; 
		    try {
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(":", 2);
					if (parts.length >= 2) {
			            String key = parts[0].replaceAll("\\s+","");
			            String value = parts[1].replaceAll("\\s+","");
			            
			            if (key.equals("Primary"))
			            	ds.set_primary(Element.valueOf(value));
			            else if (key.equals("Secondary"))
			            	ds.set_secondary(Element.valueOf(value));
			            else if (key.equals("Model_element"))
			            	ds.set_model_data_element(Element.valueOf(value));
			            else if (key.equals("xrf")) {
			            	DataTable xrf_table = new DataTable();
			            	
			            	value = value.substring(1, value.length()-1);
			            	String[] keyValuePairs = value.split(",(?=[A-z]+=\\[[A-z0-9,.]+\\])");
			            	
			            	for (String pair : keyValuePairs) {
			            		String[] entry = pair.split("=");
			            		TableKey data_key = new TableKey(entry[0]);
			            		
			            		String[] entries = entry[1].substring(1, entry[1].length()-1).split(",");
			            		
			            		// Set datatable string_data
			            		ArrayList<String> raw_entries = new ArrayList<String>(Arrays.asList(entries));
			            		xrf_table.put_info(data_key, raw_entries);
			            		
			            		if (isNumeric(raw_entries.get(0))) {
			            			xrf_table.put_data(data_key, getDoubleArray(raw_entries));
			            		}
			            	}
			            	
			            	ds.set_xrf_table(xrf_table);
			            }
			            else if (key.equals("standards")) {
			            	DataTable standards_table = new DataTable();
			            	
			            	value = value.substring(1, value.length()-1);
			            	String[] keyValuePairs = value.split(",(?=[A-z]+=\\[[A-z0-9,.]+\\])");
			            	
			            	for (String pair : keyValuePairs) {
			            		String[] entry = pair.split("=");
			            		TableKey data_key = new TableKey(entry[0]);
			            		
			            		String[] entries = entry[1].substring(1, entry[1].length()-1).split(",");
			            		
			            		// Set datatable string_data
			            		ArrayList<String> raw_entries = new ArrayList<String>(Arrays.asList(entries));
			            		standards_table.put_info(data_key, raw_entries);
			            		
			            		if (isNumeric(raw_entries.get(0))) {
			            			standards_table.put_data(data_key, getDoubleArray(raw_entries));
			            		}
			            	}
			            	
			            	ds.set_standards_table(standards_table);
			            }
			            else if (key.equals("means")) {
			            	DataTable means_table = new DataTable();
			            	
			            	value = value.substring(1, value.length()-1);
			            	String[] keyValuePairs = value.split(",(?=[A-z]+=\\[[A-z0-9,.]+\\])");
			            	
			            	for (String pair : keyValuePairs) {
			            		String[] entry = pair.split("=");
			            		TableKey data_key = new TableKey(entry[0]);
			            		
			            		String[] entries = entry[1].substring(1, entry[1].length()-1).split(",");
			            		
			            		// Set datatable string_data
			            		ArrayList<String> raw_entries = new ArrayList<String>(Arrays.asList(entries));
			            		means_table.put_info(data_key, raw_entries);
			            		
			            		if (isNumeric(raw_entries.get(0))) {
			            			means_table.put_data(data_key, getDoubleArray(raw_entries));
			            		}
			            	}
			            	
			            	ds.set_means_table(means_table);
			            }
			            else if (key.equals("correlations")) {
			            	HashMap<Element, ElementCorrelationInfo> correlations = new HashMap<Element, ElementCorrelationInfo>();
			            
			            	value = value.substring(1, value.length()-1);
			            	String[] keyValuePairs = value.split(",");
			            	
			            	for (String pair : keyValuePairs) {
			            		String[] entry = pair.split("=");
			            		//Element elem = Element.valueOf(entry[0]);
			            		
			            		//String[] entries = entry[1].substring(1, value.length()-1).split(",");
			            		
			            		//TO DO
			            		
			            		//correlations.put(elem, null);
			            	}
			            	
			            	ds.set_correlations(correlations);;
			            }
			            	
			        }
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
	}
	
	private boolean is_datastore_file(String file) {
		
		int pos = file.indexOf('.');
		if (pos < 0) {
			return false;
		}
		
		String extension = file.substring(pos + 1);
		if (extension.equals("ds")) {

			BufferedReader reader = null;
			
			// Check if .ds file conforms to standard, ie. not missing data
			try {
			    reader = new BufferedReader(new FileReader(file));
			    
			    String[] valid_keys = {"Primary", "Secondary", "Model_element", "xrf", "standards", "means", "correlations"};
			    String line; 
			    try {
			    	ArrayList<String> found_keys = new ArrayList<String>();
					while ((line = reader.readLine()) != null) {
						String[] parts = line.split(":", 2);
						if (parts.length >= 2) {
				            String key = parts[0].replaceAll("\\s+","");
				            
				            // Unknown key in .ds file
				            if (!Arrays.asList(valid_keys).contains(key)) {
				            	return false;
				            }
		
				            found_keys.add(key);
				        } 
						else {
							return false;
						}
					}
					for (String key : valid_keys) {
						// Missing keys in selected file
						if (!found_keys.contains(key)) {
							return false;
						}
					}
					// Selected file contains all necessary keys
					return true;
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			    
			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} finally {
			    try {
			        if (reader != null) {
			            reader.close();
			        }
			    } catch (IOException e) {
			    }
			}
		}
		return false;
	}
	
	private String get_new_target() {
		boolean approved = JFileChooser.APPROVE_OPTION == this.file_chooser.showOpenDialog(this);
		if (approved) {
			return file_chooser.getSelectedFile().getPath();
		}
		return "";
	}

	@Override
	public void init() {

	}
}
