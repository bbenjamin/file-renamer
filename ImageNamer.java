package folderwatcher;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.JFileChooser;



public class ImageNamer extends JFrame {

    JButton applyButton = new JButton("Apply gaps");
    GridLayout appGridLayout = new GridLayout(0,1);
    private static String ENTER = "Enter";
    private static String BARCODE = "Barcode";
    private static String CHOOSER = "Chooser";
    private static String FILENAME = "Filename";
    static JButton enterButton;
    static JButton enterButton2;
    static JButton enterButton3;
    static JButton enterButton4;
    static JButton fileChooserButton;
    static Path folder;
    public static JTextArea output;
    public static JTextField input1;
    public static JTextField input2;
    public static JTextField input3;
    public static JTextField input4;
    public static JLabel label1;
    public static JLabel label2;
    public static JLabel label3;
    public static JLabel label4;
    public static JLabel pathLabel;
    public static JLabel lengthLabel;
    public static JLabel saveLabel;
    public static JLabel receivedLabel;
    public static String pathString;
    public static String lengthString;
    public static int lengthInt;
    public static String saveString;
    public static String receivedString;
    public static boolean active = true;
    public static Path newPath;
    public static Path fullPath;
    static JFrame frame;
    static JPanel panel;
    public static JFileChooser fileChooser;
    
    public ImageNamer(String name) {
        super(name);
        setResizable(true);
    }
    
    public void addComponentsToPane(final Container pane) {
    	ButtonListener buttonListener = new ButtonListener();
    	pathString = "No Path Selected";
    	lengthString = "No Length Selected";
    	receivedString = "none";
    	label1 = new JLabel("Select New Image Folder");
	 	label2 = new JLabel("Bar Code Length");
	 	label3 = new JLabel("New File Received: ");
	 	label4 = new JLabel("Enter Bar Code");
	 	
	 	Font font = label1.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		label1.setFont(boldFont);
		label2.setFont(boldFont);
		label3.setFont(boldFont);
		label4.setFont(boldFont);
	 
	 	
	 	pathLabel = new JLabel(pathString);
	 	lengthLabel = new JLabel(lengthString);
	 	receivedLabel = new JLabel(receivedString);
	 	saveLabel = new JLabel(".");
	 	
	 	pathLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	 	lengthLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	 	receivedLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	 	
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setOpaque(true);
        
       
        JPanel inputpanel = new JPanel();
        inputpanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel inputpanel2 = new JPanel();
        inputpanel2.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel inputpanel3 = new JPanel();
        inputpanel3.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel inputpanel4 = new JPanel();
        inputpanel4.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        input1 = new JTextField(30);
        input2 = new JTextField(6);      
        input3 = new JTextField(20);
        input4 = new JTextField(20);
       // input4.addActionListener(new MyTextActionListener());
        
        enterButton = new JButton("Enter");
        enterButton.setActionCommand(ENTER);
        enterButton.addActionListener(buttonListener);
        enterButton2 = new JButton("Enter");
        enterButton2.setActionCommand(BARCODE);
        enterButton2.addActionListener(buttonListener);
        enterButton3 = new JButton("Enter");
        enterButton4 = new JButton("Enter");
        
        fileChooserButton = new JButton("Select Image Folder");
        fileChooserButton.setActionCommand(CHOOSER);
        fileChooserButton.addActionListener(buttonListener);
        input1.setActionCommand(ENTER);
        input1.addActionListener(buttonListener);
        input4.addActionListener(buttonListener);
        input4.setActionCommand(FILENAME);
       
        //add listener to determine length of text in barcode input
        input4.getDocument().addDocumentListener(new MyDocumentListener());
        input4.getDocument().putProperty("name", "Text Field");
        
        inputpanel.add(fileChooserButton);
        inputpanel.add(pathLabel);
        inputpanel2.add(label2);
        inputpanel2.add(input2);
        inputpanel2.add(enterButton2);
        inputpanel2.add(lengthLabel);
        inputpanel3.add(label3);
        inputpanel3.add(receivedLabel);
        inputpanel4.add(label4);
        inputpanel4.add(input4);
        savePanel.add(saveLabel);
        

        
    	
        final JPanel appGUI = new JPanel();
        appGUI.setLayout(appGridLayout);

        JButton b = new JButton("Just fake button");
        Dimension buttonSize = b.getPreferredSize();
        appGUI.setPreferredSize(new Dimension((int)(buttonSize.getWidth() * 5.5),
                (int)(buttonSize.getHeight() * 6.5) * 2));
        

        appGUI.add(inputpanel);
        appGUI.add(inputpanel2);
        appGUI.add(inputpanel3);
        appGUI.add(inputpanel4);
        appGUI.add(savePanel);
        

        
        pane.add(appGUI, BorderLayout.NORTH);

    }
    public static void startLoop(){
    	Thread t = new Thread(new FileCheckerLoop());
		t.start();
    }
    
    public static class ButtonListener implements ActionListener
    {
        public void actionPerformed(final ActionEvent ev)
        {
        	String cmd = ev.getActionCommand();

        	System.out.println(cmd);
        	if (CHOOSER.equals(cmd))
            {         		
        		JFileChooser fileChooser = new JFileChooser();
        		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        		fileChooser.setAcceptAllFileFilterUsed(false);
        		int result = fileChooser.showOpenDialog(null);
        		if (result == JFileChooser.APPROVE_OPTION) {
        		    File selectedFile = fileChooser.getSelectedFile();
        		    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        		    pathString = selectedFile.getAbsolutePath();
        		    pathLabel.setText("Image Folder = " + pathString);
        		    folder = Paths.get(pathString);
                	startLoop();
        		}       		
            }
            if (ENTER.equals(cmd))
            {              
                if (!input1.getText().trim().equals("") ) //
                {
                	System.out.println("clicked one");
                	pathString = input1.getText();
                	
                	if(new File(pathString).exists() && new File(pathString).isDirectory()){
                		
                    	pathLabel.setText("Path = " + pathString);
                    	folder = Paths.get(pathString);

                		//watchDirectoryPath(folder);
                    	startLoop();
                    	
                	}else{
                		pathLabel.setText("Path either doesn't exist or is not a directory");
                	}
                	System.out.println("exists " + new File(pathString).exists());
                	System.out.println("is directory " +new File(pathString).isDirectory());
                }
            }
            if (BARCODE.equals(cmd) ) 
            {   
                if (!input2.getText().trim().equals(""))
                {
                	System.out.println("clicked two");
                	lengthString = "Length = " + input2.getText();
                	lengthInt = Integer.parseInt(input2.getText());
                	lengthLabel.setText(lengthString);
                }
            }
            if (FILENAME.equals(cmd) ){
            	System.out.println("clorked it!");
            	String newName = input4.getText();
                active = false;
  				try {
  					String filename = newPath.toString();
  					String[] parts = filename.split("\\.");
  					String ext = parts[parts.length -1];
  					Files.move(fullPath, fullPath.resolveSibling(newName + "." + ext ));
  					receivedString = "waiting";
  					receivedLabel.setText(receivedString); 
  					input4.setText(""); 
  					active = true;
  					Thread t = new Thread(new FileCheckerLoop());
  					t.start();
  				} catch (IOException e1) {
  					e1.printStackTrace();
  				}
            }        
        }		
    }
    
    

    private static void createAndShowGUI() {
        //Create and set up the window.
        ImageNamer frame = new ImageNamer("Image Namer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    
    class MyDocumentListener implements DocumentListener {
        final String newline = "\n";

        public void insertUpdate(DocumentEvent e) {
        	Document doc = (Document)e.getDocument();
            int changeLength = doc.getLength();
            
            
        	if( lengthInt != 0){
        		System.out.println("changelength: " + changeLength);
        		if(lengthInt <= changeLength){
        			 String newName = input4.getText();
                     active = false;
       				try {
       					String filename = newPath.toString();
       					String[] parts = filename.split("\\.");
       					String ext = parts[parts.length -1];
       					Files.move(fullPath, fullPath.resolveSibling(newName + "." + ext ));
       					receivedString = "waiting";
       					receivedLabel.setText(receivedString);            					
       					active = true;
       					Thread t = new Thread(new FileCheckerLoop());
       					t.start();
       				} catch (IOException e1) {
       					e1.printStackTrace();
       				}
        		}
        	}
        }
        public void removeUpdate(DocumentEvent e) {

        }
        public void changedUpdate(DocumentEvent e) {
            
        }

    }

    public static  class FileCheckerLoop
    implements Runnable {

    	
	    public void run() {
	    	try {
				Boolean isFolder = (Boolean) Files.getAttribute(folder,
						"basic:isDirectory", NOFOLLOW_LINKS);
				if (!isFolder) {
					throw new IllegalArgumentException("Path: " + pathString + " is not a folder");
				}
			} catch (IOException ioe) {
				// Folder does not exists
				ioe.printStackTrace();
			}
			
			System.out.println("Watching path: " + pathString);
			
			// We obtain the file system of the Path
			FileSystem fs = folder.getFileSystem ();
			
			// We create the new WatchService using the new try() block
			input4.setText(""); 
			try(WatchService service = fs.newWatchService()) {
				
				// We register the path to the service
				// We watch for creation events
				folder.register(service, ENTRY_CREATE);
				
				// Start the infinite polling loop
				WatchKey key = null;
				int count = 0;
				
				while(active) {
					key = service.take();
					System.out.println(count);
					count++;
					// Dequeueing events
					Kind<?> kind = null;
					for(WatchEvent<?> watchEvent : key.pollEvents()) {
						// Get the type of the event
						kind = watchEvent.kind();
						if (OVERFLOW == kind) {
							continue; //loop
						} else if (ENTRY_CREATE == kind) {
							// A new Path was created 
							newPath = ((WatchEvent<Path>) watchEvent).context();
							// Output
							System.out.println("New path created: " + newPath);
							receivedLabel.setText(newPath.toString());
						
							fullPath = Paths.get(pathString + "/" + newPath);
							active = false;
							//Files.move(fullPath, fullPath.resolveSibling("hoobastank"));
						}
					}
					
					if(!key.reset()) {
						break; //loop
					}
				}
				
			} catch(IOException ioe) {
				ioe.printStackTrace();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
	    }
	}
    
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
