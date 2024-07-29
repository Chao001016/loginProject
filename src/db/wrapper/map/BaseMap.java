package db.wrapper.map;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseMap {
    private Map<String, Object> map = new HashMap();
    public void put (String key, String val) {
        this.map.put(key, val);
    };
    public void put (String key, Object val) {
        this.map.put(key, val);
    };

    public String get(String key) {
        return (String) map.get(key);
    }

    public Boolean has (String key) {
        return this.map.containsKey(key);
    }

    public int size () {
        return this.map.size();
    }

    public Set keySet() {
        return this.map.keySet();
    }



    public int setParameter (PreparedStatement pstt, int index) {
        for (Object key : this.map.keySet()) {
            Object val = this.map.get(key);
            String str = null;
            if (val != null) {
                str = this.map.get(key).toString();
            }

            try {
                pstt.setString(index++, str);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return index;
    }

    public abstract String increaseSQL(Class clazz) throws ParserConfigurationException, IOException, ClassNotFoundException, InvocationTargetException, SAXException, InstantiationException, IllegalAccessException, NoSuchMethodException;
}
