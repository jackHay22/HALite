package system_utils.io_tools;

@SuppressWarnings("serial")
public class ValExpectedException extends Exception {
	private String msg;
	public ValExpectedException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void printStackTrace() {
		System.out.println("Rock Analysis => " + msg);
		super.printStackTrace();
	}
}
