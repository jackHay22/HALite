package system_utils.io_tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestSuiteReader {
	public BufferedReader get_resources_input(String resource_path) {
		return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resource_path)));
	}
}
