package rahulshettyacademy.data;

import org.apache.commons.io.FileUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class DataReader {

    public  List<HashMap<String, String>> getJsonDataToMap() throws IOException {
      String jsonContent =  FileUtils.readFileToString(new File((System.getProperty("user.dir"))+"//src//test//java//rahulshettyacademy//data//PurchaseOrder.json"), StandardCharsets.UTF_8);

      //String to HashMap Jackson Binding

        ObjectMapper mapper=new ObjectMapper();
        List<HashMap<String,String>> data=mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
//            @Override
//            public Type getType() {
//                return super.getType();
//            }

        });
        return data;
    }
}
