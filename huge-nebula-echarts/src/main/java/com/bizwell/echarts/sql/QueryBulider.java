package com.bizwell.echarts.sql;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.SheetMetaData;

public class QueryBulider {

    private static final Map<Integer, SheetMetaData> metaDataMap = new HashMap<>();

    static {
        SheetMetaData metaData1 = new SheetMetaData();
        metaData1.setId(804);
        metaData1.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData1.setFieldType(3); //日期类型
        metaData1.setFieldColumn("A"); //数据库的字段名称
        metaDataMap.put(804, metaData1);

        SheetMetaData metaData2 = new SheetMetaData();
        metaData2.setId(805);
        metaData2.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData2.setFieldType(2); //text类型
        metaData2.setFieldColumn("B");
        metaDataMap.put(805, metaData2);

        SheetMetaData metaData3 = new SheetMetaData();
        metaData3.setId(806);
        metaData3.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData3.setFieldType(2); //text类型
        metaData3.setFieldColumn("C");
        metaDataMap.put(806, metaData3);

        SheetMetaData metaData4 = new SheetMetaData();
        metaData4.setId(810);
        metaData4.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData4.setFieldType(1); //数字类型
        metaData4.setFieldColumn("G");
        metaDataMap.put(810, metaData4);

        SheetMetaData metaData5 = new SheetMetaData();
        metaData5.setId(809);
        metaData5.setTableName("xls_571bebf42840428bb73393264dd4d793_sheet_1");
        metaData5.setFieldType(2); //text类型
        metaData5.setFieldColumn("F");
        metaDataMap.put(809, metaData5);

    }

    static String jsonString1 = "{\"echartType\":\"09\",\"moduleType\":\"04\",\"dimension\":[{\"metadataId\":1,\"name\":\"日期\",\"aggregate\":\"求和\",\"dateLevel\":\"按月\"}],\"measure1\":[{\"metadataId\":8,\"name\":\"人数\",\"aggregate\":\"求和\",\"dateLevel\":\"按日\"},{\"metadataId\":7,\"name\":\"账单数\",\"aggregate\":\"求和\",\"dateLevel\":\"按日\"}],\"measure2\":[],\"filter\":[],\"type\":\"bar\",\"stack\":\"\"}";

    static String jsonString2 = "{" +
            "\"echartType\": 2," +
            "\"dimension\": [{\"metadataId\": 804,\"dateLevel\": \"按日\"}]," +
            "\"measure1\": [{\"metadataId\": 810,\"aggregate\": \"求和\"}, {\"metadataId\": 810,\"aggregate\": \"计数\"}," +
            "{\"metadataId\": 809,\"aggregate\": \"计数\"}]," +
            "\"measure2\": [{\"metadataId\": 810,\"aggregate\": \"计数\"}]," +
            "\"filter\": [{\"metadataId\":804,\"name\":\"billdate\",\"type\":\"date\",\"selectIndex\":1,\"isshow\":true,\"condition\":{\"startTime\":\"2018-06-01 00:00:00\",\"endTime\":\"\"}}," +
            "{\"metadataId\": 810, \"name\": \"hotelid\", \"type\": \"number\", \"subType\": \"条件筛选\", \"isshow\": true, \"condition\": { \"type\": \"不为空\", \"value\": [ 9.98 ] } }," +
            "{\"metadataId\":806,\"type\":\"text\",\"subType\":\"精确筛选\",\"condition\":[\"Andriod\",\"IOS\"], \"invertSelection\":true }," +
            "{\"metadataId\": 804,\"name\": \"billdate\", \"type\": \"date\", \"selectIndex\": 7, \"isshow\": true, \"condition\": { \"startTime\": \"\", \"endTime\": \"\" } }" +
            "{\"metadataId\": 809, \"type\": \"text\", \"name\": \"hotelname\", \"subType\": \"精确筛选\", \"isshow\": true, \"invertSelection\": true, \"condition\": [ \"海腾名苑店\",\"汤臣豪园\" ] } ," +
            "{\"metadataId\": 805, \"type\": \"text\", \"name\": \"hotelname\", \"subType\": \"条件筛选\", \"isshow\": true, \"condition\": { \"logic\": \"OR\", \"fields\": [ { \"operator\": \"等于\", \"value\": \"11\" }, { \"operator\": \"结尾包含\", \"value\": \"11\" } ] } }  ]}";

    static String jsonString3 = "{\"echartType\":\"09\",\"moduleType\":\"04\",\"dimension\":[{\"metadataId\":804,\"name\":\"billdate\",\"aggregate\":\"求和\",\"dateLevel\":\"按日\"}],\"measure1\":[{\"metadataId\":810,\"name\":\"餐段数\",\"aggregate\":\"求和\"}],\"measure2\":[],\"filter\":[{\"metadataId\":810,\"name\":\"餐段数\",\"type\":\"number\",\"subType\":\"条件筛选\",\"isshow\":true,\"condition\":{\"type\":\"大于\",\"value\":[55]}},{\"metadataId\":809,\"type\":\"text\",\"name\":\"billdate\",\"subType\":\"精确筛选\",\"isshow\":true,\"invertSelection\":false,\"condition\":[\"海腾名苑店\"]}],\"type\":\"bar\",\"stack\":\"\"}";


    public static void main(String[] args) {

        System.out.println(jsonString2);

        String sql = getQueryString(jsonString2);

        System.out.println("sql : \n" + sql);
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
        JSONArray measure = combineJSONArray(measure1, measure2);

        String[] dimAndGroupByStrings = getDimColString(jsonObject.getJSONArray("dimension"));
        String dimString = dimAndGroupByStrings[0];
        String groupByString = dimAndGroupByStrings[1];

        String tableName = getTableName(dimension, measure);
        String measureString = getMeasureString(measure);

        String filterString = getFilterString(jsonObject.getJSONArray("filter"));

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
            if (!groupByString.equals("")) {
                sqlStringBuffer.append(" GROUP BY " + groupByString);
                sqlStringBuffer.append(" ORDER BY " + groupByString);
            }

        }
        return sqlStringBuffer.toString();
    }

    /**
     * 获取对应的表名
     *
     * @param dimension
     * @param measure
     * @return
     */
    private static String getTableName(JSONArray dimension, JSONArray measure) {
        String tableName = null;
        if (dimension != null && !dimension.isEmpty())
            tableName = getTargetTable(dimension);
        else if (measure != null && !measure.isEmpty())
            tableName = getTargetTable(measure);
        return tableName;
    }

    /**
     * 将两个JSONArray合并成一个JSONArray
     *
     * @param measure1
     * @param measure2
     * @return
     */
    private static JSONArray combineJSONArray(JSONArray measure1, JSONArray measure2) {
        if (measure1 == null && measure2 == null) return null;
        else if (measure1 == null) return measure2;
        else if (measure2 == null) return measure1;
        else {
            for (int i = 0; i < measure2.size(); i++)
                measure1.add(measure2.getJSONObject(i));
            return measure1;
        }
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
        SheetMetaData sheetMetaData = metaDataMap.get(metadataId);
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
            JSONObject obj = filter.getJSONObject(i);
            int metadataId = obj.getIntValue("metadataId");
            SheetMetaData sheetMetaData = metaDataMap.get(metadataId);
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
    private static String getMeasureString(JSONArray measure) {
        String result = "";
        if (measure == null || measure.isEmpty()) return result;
        for (int i = 0; i < measure.size(); i++) {
            JSONObject dataObj = measure.getJSONObject(i);
            int metadataId = dataObj.getIntValue("metadataId");
            SheetMetaData sheetMetaData = metaDataMap.get(metadataId);
            String fieldName = sheetMetaData.getFieldColumn();
            String aggregate = dataObj.getString("aggregate");
            switch (aggregate) {
                case "求和":
                    result = result + "SUM(" + fieldName + ") AS " + fieldName + "_SUM" + i;
                    break;
                case "计数":
                    result = result + "COUNT(" + fieldName + ") AS " + fieldName + "_COUNT" + i;
                    break;
                case "去重计数":
                    result = result + "COUNT(DISTINCT " + fieldName + ") AS " + fieldName + "_DISCOUNT" + i;
                    break;
                case "平均值":
                    result = result + "AVG(" + fieldName + ") AS " + fieldName + "_AVG" + i;
                    break;
                case "最大值":
                    result = result + "MAX(" + fieldName + ") AS " + fieldName + "_MAX" + i;
                    break;
                case "最小值":
                    result = result + "MIN(" + fieldName + ") AS " + fieldName + "_MIN" + i;
                    break;
                default:
                    break;
            }
            result = result + ", ";   //注意此处必须为", "，后续处理需要
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
    private static String[] getDimColString(JSONArray dimension) {
        String result[] = new String[2];
        String dimColumns = "";
        String groupbyString = "";

        if (dimension != null && !dimension.isEmpty()) {
            for (int i = 0; i < dimension.size(); i++) {
                JSONObject dimJsonObj = dimension.getJSONObject(i);
                int metadataId = dimJsonObj.getIntValue("metadataId");
                SheetMetaData sheetMetaData = metaDataMap.get(metadataId);

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
                    dimColumns = dimColumns + tmpDimString + " AS " + fieldName + "_" + i + ", "; //注意此处必须为", "，后续处理需要
                } else {
                    groupbyString = groupbyString + fieldName + ",";
                    dimColumns = dimColumns + fieldName + "_" + i + ", ";  //注意此处必须为", "，后续处理需要
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
