package db.wrapper.map;

import common.Common;
import db.wrapper.map.BaseMap;
import db.xml.XMLResultMapResolver;
import org.xml.sax.SAXException;
import pojo.Question;
import pojo.Result;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

public class ModifyMap extends BaseMap {
    @Override
    public String increaseSQL(Class clazz) {
        String increase = "";
        String[] ModifyArr = new String[this.size()];
        int i = 0;
        Map<String, Result> resultMap = new XMLResultMapResolver().getXMLObjMap(clazz);
        for (Object key : this.keySet()) {
            Result result = resultMap.get(key);
            String property = result.getProperty();
            String column = result.getColumn();
            if (this.has(property)) {
                ModifyArr[i++] = column + "=?";
            }
        }
        if (ModifyArr.length > 0) {
            increase = " set " + Common.join(ModifyArr, " , ");
        }
        return increase;
    }
}
