/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月22日下午8:26:00
 * @fileName: IRowReader.java
 * @version:1.0
 * @describe : 类说明。。。
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.excel;

import java.util.ArrayList;
import java.util.List;
import me.shy.cmdtool.utils.Util;

public class DefaultRowHandler implements RowHandable {
    
    /**
     * 满足条件的row容器
     */
    private List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
    /**
     * 参考列Id，从0开始
     */
    private int referColumnId;
    /**
     * 参考值
     */
    private String[] referColumnValue;
    /**
     * 要获取的目标列
     */
    private int[] targetColumns;
    
    /**
     * 构造函数
     * 
     * @param referColumnId
     *            参考列Id，从0开始
     * @param referColumnValue
     *            参考值，如BSC名字
     * @param targetColumns
     *            要获取的目标列，从0开始
     */
    public DefaultRowHandler(int referColumnId, String[] referColumnValue, int[] targetColumns) {
        this.referColumnId = referColumnId;
        this.referColumnValue = referColumnValue;
        this.targetColumns = targetColumns;
    }
    
    /**
     * 业务逻辑实现方法，该方法将excel一行的cell值以List形式返回
     * 
     * @param sheetIndex
     *            当前处理的sheet的Index，从1开始
     * @param rowID
     *            当前的行号
     * @param cells
     *            包含当前行所有的cell的String值
     */
    @Override
    public void getRow(int sheetIndex, int rowId, List<String> cells) {
        if (referColumnId > cells.size() + 1) {
            throw new IndexOutOfBoundsException("ReferColumnId must be too large!");
        }
        for (int i = 0, j = referColumnValue.length; i < j; i++) {
            if (cells.get(referColumnId).equals(referColumnValue[i])) {
                // System.out.println(sheetIndex + " ----" + rowId + "-----" +
                // cells.size());
                if (Util.getMax(targetColumns) > cells.size()) {
                    throw new IndexOutOfBoundsException("TargetColumns must be too large!");
                }
                List<String> row = new ArrayList<String>();
                row.add(cells.get(referColumnId));
                for (int h = 0, k = targetColumns.length; h < k; h++) {
                    row.add(cells.get(targetColumns[h]));
                }
                rows.add((ArrayList<String>) row);
            }
        }
    }
    
    /**
     * 
     * @return 获取包含满足条件的row的容器
     */
    public List<ArrayList<String>> getRows() {
        return rows;
    }
}
