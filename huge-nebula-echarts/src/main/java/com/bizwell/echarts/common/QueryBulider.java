package com.bizwell.echarts.common;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.SheetMetaData;

public class QueryBulider {

    public static String getSql(String data) {

        System.out.println(data);

        String sql = getQueryString(data);

        System.out.println("sql : \n" + sql);
        return sql;
    }

    /**
     * 获取查询sql
     *
     * @param jsonString
     * @return
     */
    public static String getQueryString(String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        JSONArray dimension = jsonObject.getJSONArray("dimension");
        JSONArray measure1 = jsonObject.getJSONArray("measure1");
        JSONArray measure2 = jsonObject.getJSONArray("measure2");

        String[] dimAndGroupByStrings = getDimColString(jsonObject.getJSONArray("dimension"));
        String dimString = dimAndGroupByStrings[0];
        String groupbyString = dimAndGroupByStrings[1];

        String tableName = "";
        if (dimension != null && !dimension.isEmpty())
            tableName = getTargetTable(dimension);
        else if (measure1 != null && !measure1.isEmpty())
            tableName = getTargetTable(measure1);
        else
            tableName = getTargetTable(measure2);

        String measureString1 = getMeasureString(jsonObject.getJSONArray("measure1"));
        String measureString2 = getMeasureString(jsonObject.getJSONArray("measure2"));
        String measureString = "";
        if (measureString1.equals(""))
            measureString += measureString2;
        else if (measureString2.equals(""))
            measureString += measureString1;
        else
            measureString = measureString + measureString1 + "," + measureString2;

        String filterString = getFilterString(jsonObject.getJSONArray("filter"));

        StringBuffer sqlStringBuffer = new StringBuffer();

        if (!(dimString.equals("") && measureString.equals(""))) {
            sqlStringBuffer.append("SELECT ");
            sqlStringBuffer.append(dimString);
            if (!dimString.equals("") && !measureString.equals(""))
                sqlStringBuffer.append(",");
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
    private static String getTargetTable(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) return "";
        JSONObject dimJsonObj = jsonArray.getJSONObject(0);
        int metadataId = dimJsonObj.getIntValue("metadataId");
        SheetMetaData sheetMetaData = MetaDataMap.get(metadataId);
        return sheetMetaData.getTableName();
    }

    /**
     * 解析filter，用于构成sql的where表达式
     *
     * @param filter
     * @return
     */
    private static String getFilterString(JSONArray filter) {
        String result = "";
        for (int i = 0; i < filter.size(); i++) {
            if (i > 0) result += " AND ";
            JSONObject obj = filter.getJSONObject(i);
            int metadataId = obj.getIntValue("metadataId");
            SheetMetaData sheetMetaData = MetaDataMap.get(metadataId);
            String fieldName = sheetMetaData.getFieldColumn();

            String type = obj.getString("type");
            String subType = obj.getString("subType");

            if (type.equals("date")) {
                JSONObject condition = obj.getJSONObject("condition");
                String startTime = condition.getString("startTime");
                String endTime = condition.getString("endTime");
                result = result + "(" + fieldName + " BETWEEN \'" + startTime + "\' AND \'" + endTime + "\')";
            } else if (type.equals("text")) {
                if (subType.equals("精确筛选")) {
                    JSONArray condition = obj.getJSONArray("condition");
                    String colVal = "";
                    for (int j = 0; j < condition.size(); j++) {
                        if (j > 0 && j < condition.size()) colVal += ",";
                        colVal = colVal + "\'" + condition.getString(j) + "\'";
                    }
                    result = result + "(" + fieldName + " IN (" + colVal + ") )";
                } else if (subType.equals("条件筛选")) {
                    JSONObject condition = obj.getJSONObject("condition");
                    String logic = condition.getString("logic");
                    JSONArray fields = condition.getJSONArray("fields");
                    for (int j = 0; j < fields.size(); j++) {
                        if (j == 0) result += "( ";
                        JSONObject fieldObj = fields.getJSONObject(j);
                        String operator = fieldObj.getString("operator");
                        String value = fieldObj.getString("value");
                        switch (operator) {
                            case "等于":   // 等于
                                result = result + " (" + fieldName + " = \'" + value + "\') " + logic;
                                break;
                            case "不等于":  // 不等于
                                result = result + " (" + fieldName + " != \'" + value + "\') " + logic;
                                break;
                            case "包含": //包含
                                result = result + " (" + fieldName + " like \'%" + value + "%\') " + logic;
                                break;
                            case "不包含": // 不包含
                                result = result + " (" + fieldName + " not like \'%" + value + "%\') " + logic;
                                break;
                            case "开头包含": // 开头包含
                                result = result + " (" + fieldName + " like \'" + value + "%\') " + logic;
                                break;
                            case "结尾包含": // 结尾包含
                                result = result + " (" + fieldName + " like \'%" + value + "\') " + logic;
                                break;
                            case "为空": // 为空
                                result = result + " (" + fieldName + " is null ) " + logic;
                                break;
                            case "不为空": // 不为空
                                result = result + " (" + fieldName + " is not null ) " + logic;
                                break;
                            default:
                                break;
                        }
                    }
                    if (result.endsWith(logic))
                        result = result.substring(0, result.lastIndexOf(" ")) + " )";
                }
            } else if (type.equals("number")) {
                if (subType.equals("条件筛选")) {
                    JSONObject condition = obj.getJSONObject("condition");
                    JSONArray fields = condition.getJSONArray("fields");
                    for (int j = 0; j < fields.size(); j++) {
                        JSONObject fieldsObj = fields.getJSONObject(j);
                        String operator = fieldsObj.getString("operator");
                        Double value = fieldsObj.getDouble("value");
                        switch (operator) {
                            case "等于":
                                result = result + "(" + fieldName + " = " + value.intValue() + ")";
                                break;
                            case "不等于":
                                result = result + "(" + fieldName + " != " + value.intValue() + ")";
                                break;
                            case "大于":
                                result = result + "(" + fieldName + " > " + value + ")";
                                break;
                            case "小于":
                                result = result + "(" + fieldName + " < " + value + ")";
                                break;
                            case "大于等于":
                                result = result + "(" + fieldName + " >= " + value + ")";
                                break;
                            case "小于等于":
                                result = result + "(" + fieldName + " <= " + value + ")";
                                break;
                            case "区间":
                                Double value2 = fieldsObj.getDouble("value2");
                                result = result + "(" + fieldName + " BETWEEN " + value + " AND " + value2 + ")";
                                break;
                            case "不为空":
                                result = result + "(" + fieldName + " is not null )";
                                break;
                            case "为空":
                                result = result + "(" + fieldName + " is null )";
                                break;
                        }
                    }
                }

            }
        }
        return result;
    }

    /**
     * 解析度量字段
     *
     * @param measure
     * @return
     */
    private static String getMeasureString(JSONArray measure) {
        String result = "";
        if (measure == null || measure.isEmpty()) return result;
        for (int i = 0; i < measure.size(); i++) {
            JSONObject dataObj = measure.getJSONObject(i);
            int metadataId = dataObj.getIntValue("metadataId");
            SheetMetaData sheetMetaData = MetaDataMap.get(metadataId);
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
            result = result + " AS " + fieldName + ",";
        }
        if (result.endsWith(","))
            result = result.substring(0, result.length() - 1);
        return result;
    }

    /**
     * 解析维度字段
     *
     * @param dimension
     * @return
     */
    private static String[] getDimColString(JSONArray dimension) {
        String result[] = new String[2];
        String dimColumns = "";
        String groupbyString = "";

        if (dimension != null && !dimension.isEmpty()) {
            for (int i = 0; i < dimension.size(); i++) {
                JSONObject dimJsonObj = dimension.getJSONObject(i);
                int metadataId = dimJsonObj.getIntValue("metadataId");
                SheetMetaData sheetMetaData = MetaDataMap.get(metadataId);

                String fieldName = sheetMetaData.getFieldColumn();
                int fieldType = sheetMetaData.getFieldType();
                String dateLevel;
                if (fieldType == 3) { // 日期类型
                    dateLevel = dimJsonObj.getString("dateLevel");
                    switch (dateLevel) {
                        case "按年":
                            dimColumns = dimColumns + "YEAR(" + fieldName + ")";
                            break;
                        case "按季":
                            dimColumns = dimColumns + "CONCAT(YEAR(" + fieldName + "),\'年\'," + "QUARTER(" + fieldName + "),\'季度\')";
                            break;
                        case "按月":
                            dimColumns = dimColumns + "DATE_FORMAT(" + fieldName + ",'%Y-%m')";
                            break;
                        case "按周":
                            dimColumns = dimColumns + "CONCAT(YEAR(" + fieldName + "),\'年第\'," + "WEEKOFYEAR(" + fieldName + "),\'周\')";
                            break;
                        case "按日":
                            dimColumns = dimColumns + "DATE_FORMAT(" + fieldName + ",'%Y-%m-%d')";
                            break;
                        default:
                            break;
                    }
                    groupbyString = groupbyString + dimColumns + ",";
                    dimColumns = dimColumns + " AS " + fieldName + ",";
                } else {
                    groupbyString = groupbyString + fieldName + ",";
                    dimColumns = dimColumns + fieldName + ",";
                }

                if (groupbyString.endsWith(","))
                    groupbyString = groupbyString.substring(0, groupbyString.length() - 1);
                if (dimColumns.endsWith(","))
                    dimColumns = dimColumns.substring(0, dimColumns.length() - 1);
            }
        }
        result[0] = dimColumns;
        result[1] = groupbyString;
        return result;
    }
}
