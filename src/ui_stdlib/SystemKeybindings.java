package ui_stdlib;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

public class SystemKeybindings {
	private static int command_shortcut =  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
	//JMenu mnemonics 
	public static final KeyStroke SAVE = KeyStroke.getKeyStroke(KeyEvent.VK_S,command_shortcut);
	public static final KeyStroke EX_DATA = KeyStroke.getKeyStroke(KeyEvent.VK_E,command_shortcut);
	public static final KeyStroke NEW = KeyStroke.getKeyStroke(KeyEvent.VK_N,command_shortcut);
	public static final KeyStroke CLOSE_WINDOW = KeyStroke.getKeyStroke(KeyEvent.VK_W,command_shortcut);
	public static final KeyStroke DRIFT_CORRECTION = KeyStroke.getKeyStroke(KeyEvent.VK_D,command_shortcut);
}
