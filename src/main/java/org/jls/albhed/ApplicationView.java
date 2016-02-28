package org.jls.albhed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jls.toolbox.util.ImageUtils;
import org.jls.albhed.util.ResourceManager;

import net.miginfocom.swing.MigLayout;

/**
 * Main frame of the application.
 * 
 * @author Julien LE SAUCE
 * @date 28 févr. 2016
 */
public class ApplicationView extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 5624587162291736726L;

	public static ApplicationView APP_FRAME = null;

	private final ApplicationController controller;
	private final Logger logger;
	private final ResourceManager props;

	private final HashMap<String, JMenu> menus;
	private final HashMap<String, JMenuItem> menuItems;
	private JMenuBar menuBar;
	private JTextArea tfInput;
	private JTextArea tfOutput;

	/**
	 * Instanciates the main frame of the application.
	 * 
	 * @param model
	 *            Data model.
	 * @param controller
	 *            Application's controller.
	 */
	public ApplicationView (final ApplicationModel model, final ApplicationController controller) {
		super(model.getAppName());
		ApplicationView.APP_FRAME = this;
		this.logger = LogManager.getLogger();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.controller = controller;
		this.props = ResourceManager.getInstance();
		this.menus = new HashMap<>();
		this.menuItems = new HashMap<>();
		createComponents();
		createGui();
		addListeners();
	}

	/**
	 * Shows the Graphical User Interface.
	 */
	public void showGui () {
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Creates the components of the frame.
	 */
	private void createComponents () {
		createMenus();
		this.tfInput = new JTextArea();
		this.tfOutput = new JTextArea();

		this.tfInput.setLineWrap(true);
		this.tfInput.setWrapStyleWord(true);

		this.tfOutput.setLineWrap(true);
		this.tfOutput.setWrapStyleWord(true);
		this.tfOutput.setEditable(false);
	}

	/**
	 * Creates the menus.
	 */
	private void createMenus () {
		this.menuBar = new JMenuBar();
		// Create the menus
		this.menus.put("file", new JMenu(this.props.getString("mainView.menu.file.label")));
		this.menus.put("help", new JMenu(this.props.getString("mainView.menu.help.label")));

		// Create the items
		this.menuItems.put("file.exit", new JMenuItem(this.props.getString("mainView.menu.item.exit.label")));
		this.menuItems.put("help.about", new JMenuItem(this.props.getString("mainView.menu.item.about.label")));

		// Add the items
		this.menus.get("file").add(this.menuItems.get("file.exit"));
		this.menus.get("help").add(this.menuItems.get("help.about"));

		// Add the menus
		this.menuBar.add(this.menus.get("file"));
		this.menuBar.add(this.menus.get("help"));
		setJMenuBar(this.menuBar);
	}

	/**
	 * Adds the graphical elements to the frame.
	 */
	private void createGui () {
		ImageIcon logo = ImageUtils.scaleImage(this.props.getIcon("mainView.logo"), 0.65);

		setLayout(new MigLayout("fill"));

		add(new JLabel(logo), "wrap");
		add(new JLabel("Input :"), "wrap");
		add(new JScrollPane(this.tfInput), "grow, push, hmin 75lp, wrap");
		add(new JLabel("Output :"), "wrap");
		add(new JScrollPane(this.tfOutput), "grow, push, hmin 75lp, wrap");
	}

	/**
	 * Adds the listeners on the graphical elements of the frame.
	 */
	private void addListeners () {
		this.menuItems.get("file.exit").addActionListener(this);
		this.menuItems.get("help.about").addActionListener(this);

		this.tfInput.addKeyListener(this);
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		/*
		 * JMenuItem
		 */
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) e.getSource();

			// Exit application
			if (this.menuItems.get("file.exit").equals(item)) {
				this.logger.debug("Exit application");
				this.controller.exitApplication();
			}
			// About application
			else if (this.menuItems.get("help.about").equals(item)) {
				this.logger.debug("Show About panel");
			}
		}
	}

	@Override
	public void keyPressed (KeyEvent event) {
		// Nothing to do
	}

	@Override
	public void keyReleased (KeyEvent event) {
		String text = this.controller.translate(this.tfInput.getText());
		this.tfOutput.setText(text);
	}

	@Override
	public void keyTyped (KeyEvent event) {
		// Nothing to do
	}
}