package ActionPackage;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.intellij.openapi.wm.ToolWindow;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import java.io.FileReader;

class MethodCheck extends AnAction {
    public void actionPerformed(AnActionEvent e) {

        String text = "";
        Project project = getEventProject(e);
        ToolWindowManager twm = ToolWindowManager.getInstance(project);
        ToolWindow tw = twm.getToolWindow("Call Tree Visualization");
        tw.getContentManager().removeAllContents(true);
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        if (project == null || editor == null || psiFile == null || element == null) {
            JOptionPane.showMessageDialog(null, "No method call found", "GraalVM", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        text = element.getText();
        PsiElement uncleElement = element.getParent().getNextSibling();
        if (uncleElement == null) {
            JOptionPane.showMessageDialog(null, "No method call found", "GraalVM", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        else {
            //PsiElement cousinElement = uncleElement.getFirstChild();
            if (uncleElement.getText().trim().length() == 0) {
                uncleElement = uncleElement.getNextSibling();
            }
            if (uncleElement.getFirstChild() != null && uncleElement.getFirstChild().getText().equals("(")) {

//                JFrame newWindow = new JFrame ("Frame");
//                newWindow.setSize(300,300);
//                newWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                JLabel textLabel = new JLabel (text, JLabel.CENTER);
//                newWindow.getContentPane().add (textLabel, JLabel.CENTER);
//                newWindow.setVisible(true);
                //new code


                //code for json reader

                JSONParser jsonParser = new JSONParser();
                String filePath = "/Users/gusperson/call_tree_JSONhelloworld_20200310_021837.json";
                String filename = "JSON not found!";
                String linenum = "--";
                try {
                    FileReader reader = new FileReader(filePath);
                    Object obj = jsonParser.parse(reader);
                    JSONArray array = (JSONArray) obj;
                    JSONObject object = (JSONObject) array.get(0);
                    filename = (String) object.get("file");
                    int len = array.size();
                    JSONObject jsonOb = null;
                    for(int i = 0; i < len; i++){
                        jsonOb = (JSONObject) array.get(i);
                        String name = (String) jsonOb.get("name");
                        if(name.equals(text)){
                            break;
                        }
                    }

                    filename = (String) jsonOb.get("file");
                    long x = (long) jsonOb.get("line");
                    linenum = Long.toString(x);

//            System.out.println(jsonObject);
                }catch (Exception er) {
//            System.exit(0);
                    System.out.println(e);
                }

                //end code for json reader



                JLabel label = new JLabel ("<html> File: " + filename + "<br> Line Number: " + linenum + "</html>", JLabel.CENTER);
//                JLabel label2 = new JLabel ("fuck", JLabel.CENTER);
                ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
                Content content = contentFactory.createContent(label,"", false);
//                Content content2 = contentFactory.createContent(label2, "", false);

                tw.getContentManager().addContent(content);


                tw.show(null);


            }
            else {
                JOptionPane.showMessageDialog(null, "No method call found", "GraalVM", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

    }
}