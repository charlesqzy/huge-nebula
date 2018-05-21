package com.bizwell.echarts.sql;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.bizwell.datasource.bean.SheetMetadata;


import java.util.HashMap;
import java.util.Map;

public class QueryBulider {

    private static final Map<Integer, SheetMetadata> metaDataMap = new HashMap<>();

    static {
        SheetMetadata metaData1 = new SheetMetadata();
        metaData1.setId(804);
        metaData1.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData1.setFieldType(3); //日期类型
        metaData1.setFieldNameNew("A"); //数据库的字段名称
        metaDataMap.put(804, metaData1);

        SheetMetadata metaData2 = new SheetMetadata();
        metaData2.setId(805);
        metaData2.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData2.setFieldType(2); //text类型
        metaData2.setFieldNameNew("B");
        metaDataMap.put(805, metaData2);

        SheetMetadata metaData3 = new SheetMetadata();
        metaData3.setId(806);
        metaData3.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData3.setFieldType(2); //text类型
        metaData3.setFieldNameNew("C");
        metaDataMap.put(806, metaData3);

        SheetMetadata metaData4 = new SheetMetadata();
        metaData4.setId(810);
        metaData4.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData4.setFieldType(1); //数字类型
        metaData4.setFieldNameNew("G");
        metaDataMap.put(810, metaData4);

        SheetMetadata metaData5 = new SheetMetadata();
        metaData5.setId(809);
        metaData5.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData5.setFieldType(2); //text类型
        metaData5.setFieldNameNew("F");
        metaDataMap.put(809, metaData5);

    }

    static String jsonString = "{" +
            "\"dimension\": [{\"metadataId\": 804,\"dateLevel\": \"week\"}]," +
            "\"measure1\": {\"data\": [{\"metadataId\": 810,\"aggregate\": \"sum\",\"color\": \"red\"}, " +
            "{\"metadataId\": 809,\"aggregate\": \"count\",\"color\": \"green\"}]," +
            "\"chartTypeId\": 2}," +
            "\"measure2\": {}," +
            "\"filter\": [{\"metadataId\": 804,\"type\": \"date\",\"subType\": \"\",\"condition\": {\"startTime\": \"2017-01-01 00:00:00\",\"endTime\": \"2018-01-05 15:30:00\"}}," +
            "{\"metadataId\": 805,\"type\": \"text\",\"subType\": \"精确筛选\",\"condition\": [\"中餐\"]}," +
            "{\"metadataId\": 806,\"type\": \"text\",\"subType\": \"条件筛选\",\"condition\": {\"logic\": \"OR\",\"fields\": [" +
            "{\"operator\": \"contains\",\"value\": \"115\"}]}}," +
            "{\"metadataId\": 810,\"type\": \"number\",\"subType\": \"条件筛选\",\"condition\": {\"logic\": \"\",\"fields\": [" +
            "{\"operator\": \"between\",\"value\": 1,\"value2\": 8}]" +
            "}}]}";

    public static void main(String[] args) {

        System.out.println(jsonString);

        String sql = getQueryString(jsonString);

        System.out.println("sql : \n" + sql);
    }

    /**
     * 获取查询sql
     *
     * @param jsonString
     * @return
     */
    private static String getQueryString(String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        JSONArray dimension = jsonObject.getJSONArray("dimension");
        String dimString = getDimColString(dimension);

        String tableName = getTargetTable(dimension);

        JSONObject measure1 = jsonObject.getJSONObject("measure1");
        String measureString1 = getMeasureString(measure1);

        JSONObject measure2 = jsonObject.getJSONObject("measure2");
        String measureString2 = getMeasureString(measure2);

        String measureString = measureString1 + measureString2;

        JSONArray filter = jsonObject.getJSONArray("filter");
        String filterString = getFilterString(filter);

        String sql = "SELECT " + dimString + measureString.substring(0, measureString.length() - 1) +
                " FROM " + tableName +
                " WHERE " + filterString +
                " GROUP BY " + dimString.substring(0, dimString.length() - 1) +
                " ORDER BY " + dimString.substring(0, dimString.length() - 1);

        return sql;
    }


    /**
     * 用于确定查询目标表的表名
     *
     * @param dimension
     * @return
     */
    private static String getTargetTable(JSONArray dimension) {
        JSONObject dimJsonObj = dimension.getJSONObject(0);
        int metadataId = dimJsonObj.getIntValue("metadataId");
        SheetMetadata sheetMetadata = metaDataMap.get(metadataId);
        return sheetMetadata.getTableName();
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
            SheetMetadata sheetMetadata = metaDataMap.get(metadataId);
            String fieldName = sheetMetadata.getFieldNameNew();

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
                            case "eq":   // 等于
                                result = result + " (" + fieldName + " = \'" + value + "\') " + logic;
                                break;
                            case "ne":  // 不等于
                                result = result + " (" + fieldName + " != \'" + value + "\') " + logic;
                                break;
                            case "contains": //包含
                                result = result + " (" + fieldName + " like \'%" + value + "%\') " + logic;
                                break;
                            case "not_contains": // 不包含
                                result = result + " (" + fieldName + " not like \'%" + value + "%\') " + logic;
                                break;
                            case "begin_with": // 开头包含
                                result = result + " (" + fieldName + " like \'" + value + "%\') " + logic;
                                break;
                            case "end_with": // 结尾包含
                                result = result + " (" + fieldName + " like \'%" + value + "\') " + logic;
                                break;
                            case "is_null": // 为空
                                result = result + " (" + fieldName + " is null ) " + logic;
                                break;
                            case "not_null": // 不为空
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
                            case "eq":
                                result = result + "(" + fieldName + " = " + value.intValue() + ")";
                                break;
                            case "ne":
                                result = result + "(" + fieldName + " != " + value.intValue() + ")";
                                break;
                            case "gt":
                                result = result + "(" + fieldName + " > " + value + ")";
                                break;
                            case "lt":
                                result = result + "(" + fieldName + " < " + value + ")";
                                break;
                            case "ge":
                                result = result + "(" + fieldName + " >= " + value + ")";
                                break;
                            case "le":
                                result = result + "(" + fieldName + " <= " + value + ")";
                                break;
                            case "between":
                                Double value2 = fieldsObj.getDouble("value2");
                                result = result + "(" + fieldName + " BETWEEN " + value + " AND " + value2 + ")";
                                break;
                            case "not_null":
                                result = result + "(" + fieldName + " is not null )";
                                break;
                            case "is_null":
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
     * 解析度量字段，返回值以逗号结束
     *
     * @param measure
     * @return
     */
    private static String getMeasureString(JSONObject measure) {
        String result = "";
        if (measure == null || measure.isEmpty()) return result;
        JSONArray data = measure.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject dataObj = data.getJSONObject(i);
            int metadataId = dataObj.getIntValue("metadataId");
            SheetMetadata sheetMetadata = metaDataMap.get(metadataId);
            String fieldName = sheetMetadata.getFieldNameNew();
            String aggregate = dataObj.getString("aggregate");
            switch (aggregate) {
                case "sum":
                    result = result + "SUM(" + fieldName + "),";
                    break;
                case "count":
                    result = result + "COUNT(" + fieldName + "),";
                    break;
                case "count distinct":
                    result = result + "COUNT(DISTINCT " + fieldName + "),";
                    break;
                case "avg":
                    result = result + "AVG(" + fieldName + "),";
                    break;
                case "max":
                    result = result + "MAX(" + fieldName + "),";
                    break;
                case "min":
                    result = result + "MIN(" + fieldName + "),";
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    /**
     * 解析维度字段，返回值以逗号结尾
     *
     * @param dimension
     * @return
     */
    private static String getDimColString(JSONArray dimension) {
        String result = "";
        for (int i = 0; i < dimension.size(); i++) {

            JSONObject dimJsonObj = dimension.getJSONObject(i);
            int metadataId = dimJsonObj.getIntValue("metadataId");
            SheetMetadata sheetMetadata = metaDataMap.get(metadataId);

            String fieldName = sheetMetadata.getFieldNameNew();
            int fieldType = sheetMetadata.getFieldType();
            String dateLevel;
            if (fieldType == 3) { // 日期类型
                dateLevel = dimJsonObj.getString("dateLevel");
                switch (dateLevel) {
                    case "year":
                        result = result + "YEAR(" + fieldName + "),";
                        break;
                    case "quarter":
                        result = result + "CONCAT(YEAR(" + fieldName + "),\'年\'," + "QUARTER(" + fieldName + "),\'季度\'),";
                        break;
                    case "month":
                        result = result + "DATE_FORMAT(" + fieldName + ",'%Y-%m'),";
                        break;
                    case "week":
                        result = result + "CONCAT(YEAR(" + fieldName + "),\'年第\'," + "WEEKOFYEAR(" + fieldName + "),\'周\'),";
                        break;
                    case "day":
                        result = result + "DATE_FORMAT(" + fieldName + ",'%Y-%m-%d'),";
                        break;
                    default:
                        result = result + fieldName + ",";
                        break;
                }
            } else
                result = result + fieldName + ",";
        }
        return result;
    }


}
