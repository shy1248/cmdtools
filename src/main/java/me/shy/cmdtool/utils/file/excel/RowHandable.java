/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月23日下午8:34:44
 * @fileName: RowHandlerInterface.java
 * @version:1.0
 * @describe : 类说明。。。
 * 
 *           ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.cmdtool.utils.file.excel;

import java.util.List;

public interface RowHandable {
    
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
    public void getRow(int sheetIndex, int rowId, List<String> cells);
}
