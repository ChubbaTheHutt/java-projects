import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
        //instance vars for currFilePath? changesUnsaved? ...

        public SimpleTextEditor() {
            initLookAndFeel();
            initUI();
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
        jmb.add(styleMenu());
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
            openFile();
        });

        newFile.addActionListener(e -> {
            //Todo
            System.out.println("Are you sure you want to open a new file?");
            //modal yes/no;
            //textArea.clear()
            //changesUnsaved = true;
            //...
        });

        saveAs.addActionListener(e -> {
            System.out.println("Save as...");
            saveFile(true);
        });

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
            System.out.println("showing shortcuts page/popup");
        });

        help.add(shortcuts);
        //todo: Modal/popup with shortcuts, could use JDialog

        return help;
    }

    public JMenu styleMenu(){
        JMenu style = new JMenu("Style");
        JMenuItem font = new JMenuItem("Font");
        
        font.addActionListener(e -> {
            System.out.println("changing font...");
        });
        
        style.add(font);

        return style;
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

                currFile = file;

                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    textArea.write(bw);
                    //todo: if 'file' exists in pwd, file(1).txt, file(2).txt, ...
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //quick save (ctrl+s)
            System.out.println("file saved...");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(currFile)); 
                //assumes currFile exists
                textArea.write(bw);
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

        helpPage.show(true);
    };

    public void showHideHelpPage(){
        
    };

    public static void main(String[] args){
        SwingUtilities.invokeLater(SimpleTextEditor::new);
    }
}
