TinyLaF beta 1.3.02
====================


Please read the file license.txt for license information.

----------------------------------------------------------------------------------

Required JRE: 1.4.0 or later.

----------------------------------------------------------------------------------

To start the control panel, double-click tinylaf.jar or cd to the TinyLaF directory and execute:

java -jar tinylaf.jar

----------------------------------------------------------------------------------

To make TinyLaF the current L&F for your application, include this line:

UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");

at the beginning of your main method (you will have to catch any exceptions).
Since this version, YQ- (formerly known as XP-)themes can be run on all platforms.

(Additional ways to define the L&F can be found in the Sun API docs: api/javax/swing/UIManager.html)

TinyLaF looks for a default theme file 'Default.theme' (case-sensitive) and, if it finds one, this
file will be loaded at startup. (The 'Default.theme' file is an ordinary TinyLaF .theme file,
just with a special name, you can take any .theme file and rename it to 'Default.theme')

! The default theme search order changed with 1.3. TinyLaF will search the following URLs:
1) TinyLookAndFeel.class.getResource("/Default.theme");
2) Thread.currentThread().getContextClassLoader().getResource("Default.theme");
3) new File(System.getProperty("user.home"), "Default.theme").toURL();
4) new File(System.getProperty("user.dir"), "Default.theme").toURL();

IMPORTANT NOTE: The de.muntjak.tinylookandfeel classes have some public static methods and flags.
Do not call any of those methods and do not change any of those flags.

----------------------------------------------------------------------------------

To try the 'Decorated Frames' feature, include the following two lines (or maybe
just one) in your source code BEFORE THE FIRST FRAME IS MADE VISIBLE:

JFrame.setDefaultLookAndFeelDecorated(true);	// to decorate frames
JDialog.setDefaultLookAndFeelDecorated(true);	// to decorate dialogs

The flicker which appears when resizing a decorated frame is not supposed to
be a bug, but I'd be VERY happy, if someone could tell me, how to prevent it.

----------------------------------------------------------------------------------

Classpath issues:

The easy and recommended way to add tinylaf.jar to your class path is, to put it in
the lib/ext folder that comes with every JSDK and JRE.

For a JSDK the path is: .../j2sdkXXX/jre/lib/ext
For a JRE the path is: .../j2reXXX/lib/ext

(My experience is, that javac looks in the JSDK and that java looks in the JRE.)

----------------------------------------------------------------------------------

How to bundle tinylaf.jar with your packed application:

When JARing your application you should write a manifest file which is simply an ascii-file
which can have any name.

If you are new to manifest files:

The first line defines the main class of your application, like this:
Main-Class: package1/package2/YourMainClass

The second line should read like this:
Class-Path: tinylaf.jar

The third (last) line MUST be a blank line.

(More about JAR files and manifest files in the valuable SUN Java tutorial)

When JARing your application, you say:
jar cmf nameOfManifestFile nameOfJar contentsOfJar...

What you get is an executable JAR (you can run it with a double-click, at least on Windows)
which automatically finds tinylaf.jar if it is in the same directory.

----------------------------------------------------------------------------------

TinyLaF and Java Web Start

TinyLaF should work with Java Web Start without tinylaf.jar needing to be signed (but note:
if one of your JARs needs to be signed, you must also sign tinylaf.jar).

Because, in general, the "Default.theme" file will not be inside tinylaf.jar you may want
to give tinylaf.jar the attribute download="lazy" in your JNLP file (so the JAR including
the "Default.theme" file loads before tinylaf.jar).

----------------------------------------------------------------------------------

Changes since Beta 1.0:

	Fixed: JProgressBar.getFont() returning null. Aditionally one can set the font for JProgressBar now.
	Fixed: JProgressBar now displays strings even when in indeterminate mode.
	Fixed: Defining TinyLaF as the standard L&F in swing.properties threw exceptions (PanelUI not found).
	Fixed: ButtonUI now honours isBorderPainted and isContentAreaFilled properties.
	Fixed: Border of JToolBar now isn't painted if set to null or isBorderPainted resolves to false.
	Fixed: Border of JTextField and JComboBox now isn't painted if set to null.
	Fixed: Due to a bug in BasicProgressBarUI (unfortunately in a private method, caused by a private
		member) sometimes a NullPointerException was thrown soon after the ControlPanel started.
		Because this exception is harmless, I decided to catch it.

	Changed: Removed Popup Font.
	Changed: Added ProgressBar Font together with two additional text colors in Decoration/ProgressBar.
	Changed: File format changed slightly due to previous changes. 1.0-themes can still be loaded, but
		saved themes are not compatible with TinyLaF-1.0

Changes since Beta 1.1:

	Fixed: Icons for JMenuItems were painted disabled when menu item was selected.
	Fixed: The selection background and selection foreground for text components
		were both set to white.
	Fixed: Double-clicking the title bar of a frame maximized the frame without respect
		to the (Windows) task bar (while clicking the maximize button DID respect
		screen insets).
	
	Changed: Simplified the paint routine for progress bar border to be faster so it doesn't
		conflict with the animation speed in javax.swing.plaf.basic.BasicProgressBarUI.

	!!! IMPORTANT CHANGE !!!
	From beta 1.2 on you can apply themes derived from the Windows XP Style only on machines
	running Windows XP. TinyLaF checks the system property "os.name" and, if it doesn't
	resolve to "Windows XP" you will see a UnsupportedLookAndFeelException.

Changes since Beta 1.2:
	
	Fixed: Bug in Launcher calling wrong L&F class.
	Fixed: Now changing to TinyLookAndFeel from another L&F works -
		changing from TinyLookAndFeel to another L&F still causes problems
		(certain properties not updated, I guess this will never be fixed)

	Changed: Before searching the user.dir for a 'Default.theme', TinyLaF searches user.home,
		e.g. you can set a global default theme if you put it in user.home.

Changes since Beta 1.2.1:
	
	Fixed: Bug with scrollbar button arrows not changing direction when scrollbar
		orientation changes.
	Fixed: ArrayIndexOutOfBoundsException thrown in TinyTabbedPaneUI when trying to remove
		a tab by manipulating the tab.
	Fixed: Sub-menus painting partially above children.
	Fixed: JTabbedPane now honours tab background color set with setBackgroundAt(int, Color).
	Fixed: Menus and menu items now honour background and foreground properties set with
		setBackground(Color) resp. setForeground(Color).
		Additionally menus and menu items now are displayed with foreground colors
		defined in 'Menu Font' resp. 'Menu Item Font' by default.
	Fixed: Windows 98 style only: Arrowbuttons of JSpinner now draw themselves correctly.
	Fixed: Button margins were calculated without respect to the button border, so a margin
		of 0 made the button text obscure the border.
	Fixed: JToggleButton displayed the default icon instead of the selected icon if model
		was both selected and rollover.
	Fixed: JFormattedTextField of JSpinner editor did loose focus with each click on
		arrowbuttons. Therefore, with SpinnerDateModel, it was not possible to select
		distinct date fields.
	Fixed: If the JFrame.setDefaultLookAndFeelDecorated property is true and the frame is
		maximized, the frame border now is supressed. This is also true for internal frames.
	Fixed: JOptionPane.showInternalXXXDialog() bringing up dialogs which painted no
		decorations at all.
	Fixed: JComboBox now updates its display size as the model changes.
	Fixed: Usage in applets: the theme is now reloaded as the applet is reloaded.
	Fixed: Editable text fields in ColorChooser now work as expected.

	Changed: The Windows XP style mutated to YQ style, the Windows 98 style mutated to 99 style.
		The YQ style is now the default style and will run with every operating system.
		To get rid of copyright issues I decided to replace both Windows 98 and XP icons
		with a custom icon set. Therefore I improved the algorithm to colorize icons.
	Changed: Search order for a 'Default.theme' file is now as follows:
		1) TinyLookAndFeel.class.getResource("/Default.theme");
		2) Thread.currentThread().getContextClassLoader().getResource("Default.theme");
		3) new File(System.getProperty("user.home"), "Default.theme").toURL();
		4) new File(System.getProperty("user.dir"), "Default.theme").toURL();
		(as a consequence you can package the 'Default.theme' file inside your main
		class' JAR from now on)
	Changed: On startup TinyLaF now displays a message via System.out, reporting the version
		and the path to the 'Default.theme' file.
	Changed: In the past, radio buttons and check boxes painted rather slow. Though both paint
		more aesthetically now, painting speed was increased drastically (by painting icons
		only once and caching the result - this speeds up painting by a factor of 80 and more).
	Changed: Radio buttons and checkboxes now have an editable margin. Please note:
		A Metal checkbox (or radio button) has border insets of (2, 2, 2, 2) which adds to the
		margin of (2, 2, 2, 2), so, if you want a TinyLaF checkbox to behave as a Metal checkbox,
		you must set its margin to (4, 4, 4, 4).
	Changed: The thumb of a scrollbar now has a minimum height/width of 17 pixels.
	Changed: JComboBox now calculates its preferred size more realistic (and less space-consuming).
		The rollover border for JComboBox is now disabled by default.
	Changed: The rollover border for JSpinner is now disabled by default.
	Changed: If frames or dialogs are decorated, they now have a minimum width of 104 pixels.
		Internal frames now have a minimum width of 32 px. Internal frames defined as palettes
		now can additionally display an iconify and a maximize button. Palette buttons are
		smaller now than internal frame buttons.
	Changed: You can now define separate title bar fonts for (decorated) frames, internal frames
		and internal palettes.
	Changed: Buttons are now opaque by default (this was a trick of the XPLookAndFeel). This
		change doesn't affect rendering of buttons, just the value of the 'opaque' property.
		YQ-Buttons now shift their text if the button is pressed.
		There is an additional flag: "ENTER 'presses' focused button". If it is selected,
		the ENTER key triggers the buttonPressed action on JButton, JToggleButton, JCheckBox
		and JRadioButton (same as SPACE key).
		JButton, JToggleButton, JCheckBox nd JRadioButton now support a focused border. You can
		disable this by deselecting 'Paint Focus Border'.
	Changed: Toolbar buttons now have a definable margin and separate backgrounds and borders for
		their deselected-, rollover-, pressed- and selected state which makes them much more
		flexible to use.
	Changed: JProgressBar now looks like a WinXP progress bar.
	Changed: Added a SeparatorUI (see Decoration | Separator).
	Changed: Added a PasswordFieldUI (that just duplicates the settings from JTextFieldUI).
	Changed: Added the following UIs which support a default background color: EditorPaneUI,
		TextPaneUI and DesktopPaneUI (see Decoration | Miscellaneous).
	Changed: JSplitPane now has a divider size of 7 pixels (instead of 6 as before),
		oneTouchButtons have become smaller.
	Changed: JTree now supports arbitrary text colors. (Note: A JTree's text color is NOT
		its foreground color (and not a JTree property at all)).
		Additionally you can now adjust the line color (see Decoration | Tree).
	Changed: JComboBox now has its own background and foreground colors (see Decoration | ComboBox).
	Changed: Lists now support a background and a foreground color (see Decoration | List).
	Changed: Added support for floatable tool bars (see Decoration | ToolBar).
	Changed: You can now set the colors of toolbar separators (see Decoration | ToolBar).
	Changed: You can now set the colors of popup menu borders (see Decoration | Menu).
	Changed: Added borders for table and table headers (see Decoration | Table). The header
		border adds an inset of 1 pixel in every direction.
	Changed: Added some colors for disabled tabbed panes (see Decoration | TabbedPane).
		The 'Paint Focus' flag disappeared, instead there is a 'Ignore Selected Bg' flag.
		If this flag is on, a selected tab will not change background color.
		Tab insets and tab area insets now are editable.
		Added a 'Fixed Tab Positions' flag - it switches from Java-Metal behaviour (the selected
		tab is always in the lowest tab row) to Windows behaviour (tabs never change positions).
	Changed: Text components now additionally support a caret color.
	Changed: Now you can specify the border, background and foreground colors for disabled tool tips.
	Changed: Combobox button, spinner previous/next buttons and window buttons now are
		non-focusable. This means, they will not be included in a FocusTraversalPolicy.
	Changed: The JFileChooser dialog is now opened at a larger size so you see more files at a time.
	Changed: File format changed due to previous changes. Pre-1.3-themes can still be loaded,
		but saved themes are not compatible with TinyLaF versions prior to 1.3.

Changes since 1.3.0:
	
	Fixed: Fixed a memory leak introduced in 1.3.0.

Changes since 1.3.01:
	
	Fixed: The fix for reloading themes as an applet is reloaded didn't work in 1.3.0 and 1.3.01.

----------------------------------------------------------------------------------

Note: In the next major release the 99 style will have been disappeared.
My ideas for the future are:
Create a TinyLaF runtime version without the control panel.
Add undo/redo capabilities to the control panel.
Further optimize painting code (but don't expect too much).

----------------------------------------------------------------------------------

I hope, this files will be useful for you and have fun, creating your own themes.

2.9.2005   Hans Bickel

THANKS to everybody who helped me improve TinyLaF by sending bug reports!!

----------------------------------------------------------------------------------

The latest version of TinyLaF can be found at:

http://www.muntjak.de/hans/java/tinylaf/

Please send bug-reports, questions, suggestions, feedback to: hb@muntjak.de
