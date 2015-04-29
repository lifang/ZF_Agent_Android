package com.example.zf_android.entity;

import java.util.List;

/**
 *
 *
 *
 * @version
 *
 */
public class CommentList extends BaseEntity {

    public List<MarkEntity> list;
    public int total;

    public List<MarkEntity> getList() {
        return list;
    }
    public void setList(List<MarkEntity> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}
