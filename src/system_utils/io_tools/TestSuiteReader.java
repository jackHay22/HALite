package system_utils.io_tools;

public class TestSuiteReader {
	public java.io.BufferedReader get_resources_input(String resource_path) {
		return new java.io.BufferedReader(new java.io.InputStreamReader(getClass().getResourceAsStream(resource_path)));
	}
}
