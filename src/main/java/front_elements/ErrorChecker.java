package front_elements;
import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ErrorChecker {
    public static List<JTextField> tFields;

    public static List<String> checkForEmptyErrors(){
        List<String> rez = new LinkedList<>();
        rez.add("Field can't be empty!");
        for(int i=0;i<tFields.size();i++){
            if(tFields.get(i).getText().trim().isEmpty() && tFields.get(i).isEnabled() && tFields.get(i).isEditable())
                rez.add(String.valueOf(i+1));
        }
        if(rez.size()>1){
            return rez;
        }
        return null;
    }

    public static JTextField[] getErrorTextFields(List<String> fields) {
        if (fields != null) {
            JTextField[] textFields = new JTextField[fields.size() - 1];
            for (int i = 0; i < textFields.length; i++) {
                for (int j = 0; j < tFields.size(); j++) {
                    if (fields.get(i + 1).equals(String.valueOf(j + 1))) {
                        textFields[i] = tFields.get(j);
                        break;
                    }
                }
            }
            return List.of(textFields).toArray(new JTextField[0]);
        }
        return null;
    }
    public static boolean checkForLength(int length, int i){
        return tFields.get(i).getText().length() > length;
    }
    public static List<String> checkForLengthOne(int length, List<JTextField> fields){
        List<String> rez=new ArrayList<>();
        for (JTextField field : fields) {
            if (field.getText().length() > length)
                rez.add(String.valueOf(tFields.indexOf(field) + 1));
        }
       return rez;
    }
    public static List<String> checkForLength(int[] length, List<List<JTextField>> fields){
        List<String> rez=new ArrayList<>();
        rez.add("Field text is too long!");
        for(int i=0;i<fields.size();i++){
            rez.addAll(checkForLengthOne(length[i],fields.get(i)));
        }
        if(rez.size()>1){
            return rez;
        }
        return null;
    }

}
