package com.eazytec.util;

import java.util.List;

/**
 * Created by min on 2016/6/16.
 */
public class PageResultRow {

    private Object id;//行id
    private List<Object> cell;//当前行的所有单元格

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public List<Object> getCell() {
        return cell;
    }

    public void setCell(List<Object> cell) {
        this.cell = cell;
    }

}
