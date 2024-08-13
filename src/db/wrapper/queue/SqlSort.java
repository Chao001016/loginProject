package db.wrapper.queue;

public class SqlSort {
    public SortType sortType;
    public String property;

    public SqlSort(SortType sortType, String property) {
        this.sortType = sortType;
        this.property = property;
    }

    public SortType getType() {
        return sortType;
    }

    public String getProperty() {
        return property;
    }
}
