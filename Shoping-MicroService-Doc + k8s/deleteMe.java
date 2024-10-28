import java.util.Arrays;
import java.util.List;

public class deleteMe {

    public static void main(String[] args) {
        String s = " -42";
        String trim = s.trim();
        if (s.charAt(1) == "-") {
            System.out.println(trim.substring(1));
        }else
        System.out.println(trim);
    }    
}