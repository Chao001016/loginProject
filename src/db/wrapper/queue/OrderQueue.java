package db.wrapper.queue;

import common.Common;
import db.xml.XMLResultMapResolver;
import pojo.Question;
import pojo.Result;

import java.util.Map;

public class OrderQueue extends BaseQueue<SqlSort> {
    @Override
    public String increaseSQL(Class clazz) {
        StringBuilder sb = new StringBuilder();
        SqlSort sqlSort;
        if (this.size() > 0) sb.append(" order by ");
        String[] strArr = new String[this.size()];
        int count = 0;
        Map<String, Result> resultMap = new XMLResultMapResolver().getXMLObjMap(clazz);
        while ((sqlSort = this.poll()) != null) {
            String property = sqlSort.getProperty();
            SortType type = sqlSort.getType();
            Result result = resultMap.get(property);
            String column = result.getColumn();
            strArr[count++] = column + (type == SortType.ASC ? " asc" : " desc");
            System.out.println(column);
        }
        if (strArr.length > 0) sb.append(Common.join(strArr, ","));
        return sb.toString();
    }
}
