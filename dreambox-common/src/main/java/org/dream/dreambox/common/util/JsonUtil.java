package org.dream.dreambox.common.util;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * 
 * @author </a href="mailto:dingjun@llyuntong.com.cn">dingjun</a>
 * @version $Id: JsonUtil.java, v 0.1 2013-3-27 ����2:21:27 Administrator Exp $ 
 * @since 1.0
 */
public class JsonUtil {

    //private static JsonGenerator jsonGenerator;

    private static ObjectMapper  objectMapper;

    static {
        objectMapper = new ObjectMapper();
        /*try {
            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out,
                JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static <T> T parseJson2Object(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
