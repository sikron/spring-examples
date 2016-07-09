package com.skronawi.spring.examples.rest.communication;

public class MyPageable {

    private int offset;
    private int limit;

    //for bean creation
    public MyPageable() {
    }

    public MyPageable(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
