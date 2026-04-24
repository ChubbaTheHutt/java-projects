import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
/**
 *
 * @author Chubba
 */
public class SimpleTextEditor {
        private JFrame frame;
        private JTextArea textArea;
        private final JFileChooser jfc = new JFileChooser();
        private File currFile;
        private boolean changesUnsaved;
        private ActionMemory actmem; //change history


        public SimpleTextEditor() {
            initLookAndFeel();
            initUI();
            actmem = new ActionMemory();
        }

        private void initLookAndFeel() {
            try {
                UIManager.setLookAndFeel(new MetalLookAndFeel());
                MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void initUI(){
            frame = new JFrame("Hello Java Swing!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //todo: If unsaved changed, effect modal/popup for confirmation
            frame.setLayout(new BorderLayout());

            textArea = new JTextArea(10,10);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane textSP = new JScrollPane(textArea);

            frame.setJMenuBar(menuBar());
            frame.add(textSP, BorderLayout.CENTER);
            
            frame.setSize(500,600);
            frame.setVisible(true);
        }

    public JMenuBar menuBar(){
        JMenuBar jmb = new JMenuBar();

        jmb.add(fileMenu());
        jmb.add(editMenu());
        jmb.add(helpMenu());
        
        return jmb;
    }

    public JMenu fileMenu(){
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem saveAs = new JMenuItem("Save as");
        
        save.addActionListener(e -> {
            System.out.println(e);
            System.out.println("Saving file...");
            System.out.println(textArea.getText());

            //saveFile(boolean saveAs?)
            if(currFile == null){
                saveFile(true);
            } else {
                saveFile(false);
            }
            //todo
        });

        open.addActionListener(e -> {
            System.out.println("Opening file...");
            //if unsaved changes, confirmation page
            if(changesUnsaved == true){
                int result = JOptionPane.showConfirmDialog(frame, "You have unsaved changes, do you still want to open a new file",
                                                            "Unsaved Changes", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION){
                    openFile();
                } else {
                    System.out.println("Open file canceled");
                }
            } else {
                openFile();
            }
        });

        newFile.addActionListener(e ->   {
            if(changesUnsaved == true){
                int result = JOptionPane.showConfirmDialog(frame, "You have unsaved changes, do you still want to open a new file?",
                                                            "Unsaved Changes", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION){
                    textArea.setText("");
                    changesUnsaved = true;
                    currFile = null;
                } else {
                    System.out.println("Open new failed...");
                }
            } else {
                textArea.setText("");
                changesUnsaved = true;
                currFile = null;
            }
        });
       
        saveAs.addActionListener(e -> {
            System.out.println("Save as...");
            saveFile(true);
        });

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));       

        //TODO add accelerators
        file.add(save);
        file.add(open);
        file.add(newFile);
        file.add(saveAs);

        return file;
    }

    public JMenu helpMenu(){
        JMenu help = new JMenu("Help");
        JMenuItem shortcuts = new JMenuItem("Shortcuts");

        shortcuts.addActionListener(e -> {
            initHelpPage();
        });

        help.add(shortcuts);
        //todo: Modal/popup with shortcuts, could use JDialog

        return help;
    }

    public JMenu editMenu(){
        JMenu edit = new JMenu("Edit");
        JMenuItem font = new JMenuItem("Font");
        JMenuItem undo = new JMenuItem("Redo");
        JMenuItem redo = new JMenuItem("Undo");

        font.addActionListener(e -> {
            System.out.println("changing font...");
        });
        
        undo.addActionListener(e -> {
            System.out.println("Undoing...");
        });

        redo.addActionListener(e -> {
            System.out.println("Redoing...");
        });

        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        edit.add(font);
        edit.add(undo);
        edit.add(redo);

        return edit;
    }

    public void saveFile(boolean saveAs){
        System.out.println("saving file...");
        //save new instance of file 
        if(saveAs){   
            int result = jfc.showSaveDialog(frame);

            if(result == JFileChooser.APPROVE_OPTION){
                File file = jfc.getSelectedFile();
                if(!file.getName().endsWith(".txt")) {
                    file = new File(file.getAbsolutePath() + ".txt");
                }

                currFile = file; //set current working file

                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    textArea.write(bw);
                    changesUnsaved = false;
                    //todo: if 'file' exists in pwd, file(1).txt, file(2).txt, ...
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //quick save (ctrl+s)
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(currFile)); 
                //assumes currFile exists
                textArea.write(bw);

                changesUnsaved = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void openFile(){
        //open txt files or formatted text files from pc
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("TXT Files", "txt");
        jfc.setFileFilter(fnef);
        int result = jfc.showOpenDialog(frame);

        if(result == JFileChooser.APPROVE_OPTION){
            File file = jfc.getSelectedFile();
            String fp = file.getAbsolutePath();

            try {
                BufferedReader br = new BufferedReader(new FileReader(fp));
                textArea.setText("");

                String line;
                while((line = br.readLine()) != null){
                    textArea.append(line + '\n');
                }

                currFile = file; //set current file path for quick save
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void initHelpPage(){
        //todo create help page listing shortcuts and such
        JDialog helpPage = new JDialog(frame);
        helpPage.setTitle("Shortcuts & Help");

        JPanel panel = new JPanel((new GridLayout(2, 3)));
        helpPage.setContentPane(panel);

        String[] shortcuts = {
            "Ctrl + s - Save",
            "Ctrl + n - New File",
            "Ctrl + o - Open File",
            "Ctrl + shift + s - Save as",
        };

        for(String s : shortcuts){
            panel.add(shortcutGridPanel(s));
        }

        helpPage.setSize(300,300);
        helpPage.setVisible(true);
    };

    public JPanel shortcutGridPanel(String s){
        JPanel shortcutPanel = new JPanel(new BorderLayout());
        JLabel shortcutLabel = new JLabel(s);

        shortcutLabel.setHorizontalAlignment(SwingConstants.LEFT);
        shortcutLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        shortcutPanel.add(shortcutLabel, BorderLayout.CENTER);

        return shortcutPanel;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(SimpleTextEditor::new);
    }
}
