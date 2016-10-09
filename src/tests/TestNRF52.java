package tests;

import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 9/21/16.
 */
public class TestNRF52 {


    public static void main(String... args) throws IOException {
//        String mBuildPath = null;
//        mBuildPath = new File("").getAbsolutePath();
//        mBuildPath+="/src/tests/src/";
//        System.out.println("Build path " + mBuildPath);
//        Controller ctr = new Controller(null);
//        nrf52Platform api = new nrf52Platform(ctr, mBuildPath);
//        api.addTimer("random_ctr");
//        api.addTimer("config_ctr");
//        for(FileObject fo: api.getFiles()){
//            System.out.println("Writing file: " + fo.getFileName() + " to "  + fo.getPath());
//            fo.toFile();
//
//
//        }
//        List<Models> models = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            models.add(new Models(String.valueOf(Math.random())));
//        }
//        STGroup tmpl = new STGroupFile("tests/tmpl_test.stg");
//        ST t_obj = tmpl.getInstanceOf("model_c_obj");
//        t_obj.add("models", models);
        String s = "AwesomeModel";
        String[] r = s.split("(?=\\p{Upper})");
        System.out.println(toCName(s));
        System.out.println(toCVarName(s));
    }

    public static String toCName(String name){
        return StringUtils.join(name.split("(?=\\p{Upper})"),"_").toLowerCase();
    }
    public static String toCVarName(String name){
        return "m_" +StringUtils.join(name.split("(?=\\p{Upper})"),"_").toLowerCase();
    }
}
