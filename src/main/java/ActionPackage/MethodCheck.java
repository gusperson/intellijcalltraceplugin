package ActionPackage;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
class MethodCheck extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        String text = "";
        Project project = getEventProject(e);
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
                JFrame newWindow = new JFrame ("Frame");
                newWindow.setSize(300,300);
                newWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel textLabel = new JLabel (text, JLabel.CENTER);
                newWindow.getContentPane().add (textLabel, JLabel.CENTER);
                newWindow.setVisible(true);
                ToolWindowManager.getInstance(project).getToolWindow("Call Tree Visualization").show(null);
            }
            else {
                JOptionPane.showMessageDialog(null, "No method call found", "GraalVM", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

    }
}