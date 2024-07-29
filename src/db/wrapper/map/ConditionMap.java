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

public class ConditionMap extends BaseMap {
    @Override
    public String increaseSQL(Class clazz) {
        String increase = "";
        String[] conditionArr = new String[this.size()];
        int i = 0;
        Map<String, Result> resultMap = new XMLResultMapResolver().getXMLObjMap(clazz);
        for (Object key : this.keySet()) {
            Result result = resultMap.get(key);
            String property = result.getProperty();
            String column = result.getColumn();
            if (this.has(property)) {
                conditionArr[i++] = column + "=?";
            }
        }

        if (conditionArr.length > 0) {
            increase = " where " + Common.join(conditionArr, " and ");
        }
        return increase;
    }
}
