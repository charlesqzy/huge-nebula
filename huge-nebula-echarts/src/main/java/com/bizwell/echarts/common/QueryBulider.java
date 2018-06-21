package com.bizwell.echarts.common;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.SheetMetaData;

public class QueryBulider {

    public static String getSql(String data, Integer userId) {

        System.out.println(data);

        String sql = getQueryString(data, userId);

        System.out.println("sql : \n" + sql);
        return sql;
    }
    
    /**
     * 获取查询sql
     *
     * @param jsonString
     * @return
     */
    public static String getQueryString(String jsonString, Integer userId) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        JSONArray dimension = jsonObject.getJSONArray("dimension");
        JSONArray measure1 = jsonObject.getJSONArray("measure1");
        JSONArray measure2 = jsonObject.getJSONArray("measure2");

        String[] dimAndGroupByStrings = getDimColString(jsonObject.getJSONArray("dimension"), userId);
        String dimString = dimAndGroupByStrings[0];
        String groupbyString = dimAndGroupByStrings[1];

        String tableName = "";
        if (dimension != null && !dimension.isEmpty())
            tableName = getTargetTable(dimension, userId);
        else if (measure1 != null && !measure1.isEmpty())
            tableName = getTargetTable(measure1, userId);
        else
            tableName = getTargetTable(measure2, userId);

        String measureString1 = getMeasureString(jsonObject.getJSONArray("measure1"), userId);
        String measureString2 = getMeasureString(jsonObject.getJSONArray("measure2"), userId);
        String measureString = "";
        if (measureString1.equals(""))
            measureString += measureString2;
        else if (measureString2.equals(""))
            measureString += measureString1;
        else
            measureString = measureString + measureString1 + ", " + measureString2;  // 此处必须为", "，后续处理需要

        String filterString = getFilterString(jsonObject.getJSONArray("filter"), userId);

        StringBuffer sqlStringBuffer = new StringBuffer();

        if (!(dimString.equals("") && measureString.equals(""))) {
            sqlStringBuffer.append("SELECT ");
            sqlStringBuffer.append(dimString);
            if (!dimString.equals("") && !measureString.equals(""))
                sqlStringBuffer.append(", ");    // 此处必须为", "，后续处理需要
            sqlStringBuffer.append(measureString);
            sqlStringBuffer.append(" FROM ");
            sqlStringBuffer.append(tableName);
            if (!filterString.equals(""))
                sqlStringBuffer.append(" WHERE " + filterString);
            if (!groupbyString.equals("")) {
                sqlStringBuffer.append(" GROUP BY " + groupbyString);
                sqlStringBuffer.append(" ORDER BY " + groupbyString);
            }

        }
        return sqlStringBuffer.toString();
    }

    /**
     * 用于确定查询目标表的表名
     *
     * @param jsonArray
     * @return
     */
    private static String getTargetTable(JSONArray jsonArray, Integer userId) {
        if (jsonArray == null || jsonArray.isEmpty()) return "";
        JSONObject dimJsonObj = jsonArray.getJSONObject(0);
        int metadataId = dimJsonObj.getIntValue("metadataId");
        SheetMetaData sheetMetaData = MetaDataMap.get(userId, metadataId);
        return sheetMetaData.getTableName();
    }

    /**
     * 解析filter，用于构成sql的where表达式
     *
     * @param filter
     * @return
     */
    private static String getFilterString(JSONArray filter, Integer userId) {
        String result = "";
        for (int i = 0; i < filter.size(); i++) {
            JSONObject obj = filter.getJSONObject(i);
            int metadataId = obj.getIntValue("metadataId");
            SheetMetaData sheetMetaData = MetaDataMap.get(userId, metadataId);
            String fieldName = sheetMetaData.getFieldColumn();

            String type = obj.getString("type");
            String subType = obj.getString("subType");

            String tmpResult = "";

            if (type.equals("date")) {
                JSONObject condition = obj.getJSONObject("condition");
                String startTime = condition.getString("startTime");
                String endTime = condition.getString("endTime");
                if (!(startTime.equals("") && endTime.equals(""))) {
                    if (startTime.equals("")) {
                        tmpResult = fieldName + " <= \'" + endTime + "\'";
                    } else if (endTime.equals("")) {
                        tmpResult = fieldName + " >= \'" + startTime + "\'";
                    } else
                        tmpResult = "(" + fieldName + " BETWEEN \'" + startTime + "\' AND \'" + endTime + "\')";
                }
            } else if (type.equals("text")) {
                if (subType.equals("精确筛选")) {
                    JSONArray condition = obj.getJSONArray("condition");
                    String colVal = "";
                    for (int j = 0; j < condition.size(); j++) {
                        if (j > 0 && j < condition.size()) colVal += ",";
                        colVal = colVal + "\'" + condition.getString(j) + "\'";
                    }
                    boolean invertSelection = obj.getBoolean("invertSelection");
                    if (invertSelection)
                        tmpResult = "(" + fieldName + " NOT IN (" + colVal + ") )";
                    else
                        tmpResult = "(" + fieldName + " IN (" + colVal + ") )";
                } else if (subType.equals("条件筛选")) {
                    JSONObject condition = obj.getJSONObject("condition");
                    String logic = condition.getString("logic");
                    JSONArray fields = condition.getJSONArray("fields");
                    for (int j = 0; j < fields.size(); j++) {
                        if (j == 0) tmpResult += "( ";
                        JSONObject fieldObj = fields.getJSONObject(j);
                        String operator = fieldObj.getString("operator");
                        String value = fieldObj.getString("value");
                        switch (operator) {
                            case "等于":   // 等于
                                tmpResult = tmpResult + " (" + fieldName + " = \'" + value + "\') " + logic;
                                break;
                            case "不等于":  // 不等于
                                tmpResult = tmpResult + " (" + fieldName + " != \'" + value + "\') " + logic;
                                break;
                            case "包含": //包含
                                tmpResult = tmpResult + " (" + fieldName + " like \'%" + value + "%\') " + logic;
                                break;
                            case "不包含": // 不包含
                                tmpResult = tmpResult + " (" + fieldName + " not like \'%" + value + "%\') " + logic;
                                break;
                            case "开头包含": // 开头包含
                                tmpResult = tmpResult + " (" + fieldName + " like \'" + value + "%\') " + logic;
                                break;
                            case "结尾包含": // 结尾包含
                                tmpResult = tmpResult + " (" + fieldName + " like \'%" + value + "\') " + logic;
                                break;
                            case "为空": // 为空
                                tmpResult = tmpResult + " (" + fieldName + " is null ) " + logic;
                                break;
                            case "不为空": // 不为空
                                tmpResult = tmpResult + " (" + fieldName + " is not null ) " + logic;
                                break;
                            default:
                                break;
                        }
                    }
                    if (tmpResult.endsWith(logic))
                        tmpResult = tmpResult.substring(0, tmpResult.lastIndexOf(" ")) + " )";
                }
            } else if (type.equals("number")) {
                if (subType.equals("条件筛选")) {
                    JSONObject condition = obj.getJSONObject("condition");
                    String conditionType = condition.getString("type");
                    JSONArray conditionValues = condition.getJSONArray("value");

                    switch (conditionType) {
                        case "等于":
                            tmpResult = "(" + fieldName + " = " + conditionValues.getDouble(0) + ")";
                            break;
                        case "不等于":
                            tmpResult = "(" + fieldName + " != " + conditionValues.getDouble(0) + ")";
                            break;
                        case "大于":
                            tmpResult = "(" + fieldName + " > " + conditionValues.getDouble(0) + ")";
                            break;
                        case "小于":
                            tmpResult = "(" + fieldName + " < " + conditionValues.getDouble(0) + ")";
                            break;
                        case "大于等于":
                            tmpResult = "(" + fieldName + " >= " + conditionValues.getDouble(0) + ")";
                            break;
                        case "小于等于":
                            tmpResult = "(" + fieldName + " <= " + conditionValues.getDouble(0) + ")";
                            break;
                        case "区间":
                            tmpResult = "(" + fieldName + " BETWEEN " + conditionValues.getDouble(0) + " AND " + conditionValues.getDouble(1) + ")";
                            break;
                        case "不为空":
                            tmpResult = "(" + fieldName + " is not null )";
                            break;
                        case "为空":
                            tmpResult = "(" + fieldName + " is null )";
                            break;
                    }
                }

            }
            if (!tmpResult.equals("")) result = result + tmpResult + " AND ";
        }

        if (result.endsWith(" AND ")) result = result.substring(0, result.lastIndexOf(" AND "));
        return result;
    }

    /**
     * 解析度量字段
     *
     * @param measure
     * @return
     */
    private static String getMeasureString(JSONArray measure, Integer userId) {
        String result = "";
        if (measure == null || measure.isEmpty()) return result;
        for (int i = 0; i < measure.size(); i++) {
            JSONObject dataObj = measure.getJSONObject(i);
            int metadataId = dataObj.getIntValue("metadataId");
            SheetMetaData sheetMetaData = MetaDataMap.get(userId, metadataId);
            String fieldName = sheetMetaData.getFieldColumn();
            String aggregate = dataObj.getString("aggregate");
            switch (aggregate) {
                case "求和":
                    result = result + "SUM(" + fieldName + ")";
                    break;
                case "计数":
                    result = result + "COUNT(" + fieldName + ")";
                    break;
                case "去重计数":
                    result = result + "COUNT(DISTINCT " + fieldName + ")";
                    break;
                case "平均值":
                    result = result + "AVG(" + fieldName + ")";
                    break;
                case "最大值":
                    result = result + "MAX(" + fieldName + ")";
                    break;
                case "最小值":
                    result = result + "MIN(" + fieldName + ")";
                    break;
                default:
                    break;
            }
            result = result + " AS " + fieldName + ", ";   //注意此处必须为", "，后续处理需要
        }
        if (result.endsWith(", "))
            result = result.substring(0, result.length() - 2);
        return result;
    }

    /**
     * 解析维度字段
     *
     * @param dimension
     * @return
     */
    private static String[] getDimColString(JSONArray dimension, Integer userId) {
        String result[] = new String[2];
        String dimColumns = "";
        String groupbyString = "";

        if (dimension != null && !dimension.isEmpty()) {
            for (int i = 0; i < dimension.size(); i++) {
                JSONObject dimJsonObj = dimension.getJSONObject(i);
                int metadataId = dimJsonObj.getIntValue("metadataId");
                SheetMetaData sheetMetaData = MetaDataMap.get(userId, metadataId);

                String fieldName = sheetMetaData.getFieldColumn();
                int fieldType = sheetMetaData.getFieldType();
                String dateLevel;
                String tmpDimString = "";

                if (fieldType == 3) { // 日期类型
                    dateLevel = dimJsonObj.getString("dateLevel");
                    switch (dateLevel) {
                        case "按年":
                            tmpDimString = "YEAR(" + fieldName + ")";
                            break;
                        case "按季":
                            tmpDimString = "CONCAT(YEAR(" + fieldName + "),\'年\'," + "QUARTER(" + fieldName + "),\'季度\')";
                            break;
                        case "按月":
                            tmpDimString = "DATE_FORMAT(" + fieldName + ",'%Y-%m')";
                            break;
                        case "按周":
                            tmpDimString = "CONCAT(YEAR(" + fieldName + "),\'年第\'," + "WEEKOFYEAR(" + fieldName + "),\'周\')";
                            break;
                        case "按日":
                            tmpDimString = "DATE_FORMAT(" + fieldName + ",'%Y-%m-%d')";
                            break;
                        default:
                            break;
                    }
                    groupbyString = groupbyString + tmpDimString + ",";
                    dimColumns = dimColumns + tmpDimString + " AS " + fieldName + ", "; //注意此处必须为", "，后续处理需要
                } else {
                    groupbyString = groupbyString + fieldName + ",";
                    dimColumns = dimColumns + fieldName + ", ";  //注意此处必须为", "，后续处理需要
                }
            }
            if (groupbyString.endsWith(","))
                groupbyString = groupbyString.substring(0, groupbyString.length() - 1);
            if (dimColumns.endsWith(", "))
                dimColumns = dimColumns.substring(0, dimColumns.length() - 2);
        }
        result[0] = dimColumns;
        result[1] = groupbyString;
        return result;
    }
    
}
