package com.nowcoder.community.entity;

/**
 * @author LQZ
 * @date 2022-03-07 17:58
 * <p>
 * 封装分页相关的信息
 */

public class Page {

    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数（用于计算总页数）
    private int rows;
    // 查询路径（用于复用分页链接）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * 获取当前页的起始行
     * @return 起始位置
     */
    public int getOffset(){
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return 总页数
     */
    public int getTotal() {
        // 数据总数 / 每页个数, 除不尽要 +1
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     * @return 页码范围的起始值
     */
    public int getFrom() {
        // 默认页码范围起始在当前页码 -2
        int from = current - 2;
        // 不能小于 1
        return Math.max(from, 1);
    }

    /**
     * 获取结束页码
     * @return 页码范围的终止值
     */
    public int getTo() {
        // 默认页码范围停止在当前页码 +2
        int to = current + 2;
        // 不能超过总页数
        int total = getTotal();
        return Math.min(to, total);
    }
}
