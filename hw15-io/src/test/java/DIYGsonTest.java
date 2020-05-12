
import com.google.gson.Gson;
import ru.otus.DIYGson;
import java.util.Arrays;

public class DIYGsonTest {
    public static void main(String[] args) {
        DIYGson gson = new DIYGson();
//        BagOfPrimitives obj = new BagOfPrimitives(22, "test", 10);
        Integer[] obj = new Integer[]{0, 5, 8};
        System.out.println(obj);

        String json = gson.toJson(obj);
        System.out.println(json);

//        BagOfPrimitives obj2 = new Gson().fromJson(json, BagOfPrimitives.class);
        Integer[] obj2 = new Gson().fromJson(json, Integer[].class);
        System.out.println(Arrays.equals(obj, obj2));
        System.out.println(obj2);
    }
}
