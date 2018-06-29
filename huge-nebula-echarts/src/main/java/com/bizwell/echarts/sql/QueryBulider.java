package com.bizwell.echarts.sql;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class QueryBulider {

    static String jsonString2 = "{ \"echartType\": \"00\", \"moduleType\": \"01\", \"dimension\": [ { \"metadataId\": 162, \"name\": \"billdate\", \"aggregate\": \"计数\", \"dateLevel\": \"按日\", \"fieldType\": 3, \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"C\", \"it\": 3 } ], \"measure1\": [ { \"metadataId\": 161, \"name\": \"hotelname\", \"aggregate\": \"去重计数\", \"dateLevel\": \"按日\", \"fieldType\": 2, \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"B\", \"it\": 1 } ], \"measure2\": [], \"filter\": [], \"type\": \"\", \"stack\": \"\", \"inChartFilter\": [ { \"name\": \"billdate\", \"metadataId\": 162, \"level\": 1, \"fieldType\": 3, \"dateLevel\": \"按日\", \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"C\", \"it\": 100001, \"selected\": [ \"2018-04-26\" ] }, { \"name\": \"hotelname\", \"metadataId\": 161, \"level\": 1, \"fieldType\": 2, \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"B\", \"it\": 100002, \"selected\": [ \"海腾名苑店\" ] }, { \"name\": \"hotelname\", \"metadataId\": 161, \"level\": 1, \"fieldType\": 1, \"aggregate\": \"去重计数\", \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"B\", \"it\": 1, \"condition\": { \"type\": \"等于\", \"value\": [ 9 ] } } ] }";




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
        String[] inChartFilterString = getInChartFilterString(jsonObject.getJSONArray("inChartFilter"));
        String allFilterString = combineFilterString(filterString, inChartFilterString[0]);
        String havingString = inChartFilterString[1];
        StringBuffer sqlStringBuffer = new StringBuffer();

        if (!(dimString.equals("") && measureString.equals(""))) {
            sqlStringBuffer.append("SELECT ");
            sqlStringBuffer.append(dimString);
            if (!dimString.equals("") && !measureString.equals(""))
                sqlStringBuffer.append(", ");    // 此处必须为", "，后续处理需要
            sqlStringBuffer.append(measureString);
            sqlStringBuffer.append(" FROM ");
            sqlStringBuffer.append(tableName);
            if (!allFilterString.equals(""))
                sqlStringBuffer.append(" WHERE " + allFilterString);
            if (!groupByString.equals("")) {
                sqlStringBuffer.append(" GROUP BY " + groupByString);
                if (!havingString.equals("")) sqlStringBuffer.append(" HAVING " + havingString);
                sqlStringBuffer.append(" ORDER BY " + groupByString);
            } else {
                if (!havingString.equals("")) sqlStringBuffer.append(" HAVING " + havingString);
            }


        }
        return sqlStringBuffer.toString();
    }

    /**
     * 将两个filterString合并
     *
     * @param filterString
     * @param inChartFilterString
     * @return
     */
    private static String combineFilterString(String filterString, String inChartFilterString) {
        StringBuffer result = new StringBuffer();
        if ((filterString == null || filterString.equals("")) &&
                (inChartFilterString == null || inChartFilterString.equals(""))) return result.toString();
        else if (filterString == null || filterString.equals(""))
            result.append(inChartFilterString);
        else if (inChartFilterString == null || inChartFilterString.equals(""))
            result.append(filterString);
        else {
            result.append(filterString);
            result.append(" AND ");
            result.append(inChartFilterString);
        }
        return result.toString();
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
        if (dimension != null && !dimension.isEmpty()) {
            JSONObject dm = dimension.getJSONObject(0);
            tableName = dm.getString("tableName");
        } else if (measure != null && !measure.isEmpty()) {
            JSONObject ms = measure.getJSONObject(0);
            tableName = ms.getString("tableName");
        }
        return tableName;
    }

    /**
     * 将两个JSONArray合并成一个JSONArray
     *
     * @param array1
     * @param array2
     * @return
     */
    private static JSONArray combineJSONArray(JSONArray array1, JSONArray array2) {
        if (array1 == null && array2 == null) return null;
        else if (array1 == null) return array2;
        else if (array2 == null) return array1;
        else {
            for (int i = 0; i < array2.size(); i++)
                array1.add(array2.getJSONObject(i));
            return array1;
        }
    }

    /**
     * 图内筛选器，解析为SQL
     *
     * @param inChartFilter
     * @return
     */
    private static String[] getInChartFilterString(JSONArray inChartFilter) {
        StringBuffer whereBuffer = new StringBuffer();
        StringBuffer havingBuffer = new StringBuffer();
        String[] result = new String[2];

        if (inChartFilter == null) {
            result[0] = whereBuffer.toString();
            result[1] = havingBuffer.toString();
            return result;
        }
        for (int i = 0; i < inChartFilter.size(); i++) {
            JSONObject obj = inChartFilter.getJSONObject(i);
            int fieldType = obj.getIntValue("fieldType");
            String tableName = obj.getString("tableName");
            String fieldColumn = obj.getString("fieldColumn");

            StringBuffer tmpWhereBuffer = new StringBuffer();
            StringBuffer tmpHavingBuffer = new StringBuffer();
            switch (fieldType) {
                case 1:  // 数字
                    String aggregate = obj.getString("aggregate");
                    String havingField = "";
                    switch (aggregate) {
                        case "求和":
                            havingField = "SUM(" + fieldColumn + ")";
                            break;
                        case "计数":
                            havingField = "COUNT(" + fieldColumn + ")";
                            break;
                        case "去重计数":
                            havingField = "COUNT(DISTINCT " + fieldColumn + ")";
                            break;
                        case "平均值":
                            havingField = "AVG(" + fieldColumn + ")";
                            break;
                        case "最大值":
                            havingField = "MAX(" + fieldColumn + ")";
                            break;
                        case "最小值":
                            havingField = "MIN(" + fieldColumn + ")";
                            break;
                        default:
                            break;
                    }
                    JSONObject condition = obj.getJSONObject("condition");
                    String type = condition.getString("type");
                    JSONArray value = condition.getJSONArray("value");
                    switch (type) {
                        case "全部":
                            break;
                        case "等于":
                            tmpHavingBuffer.append(havingField + " = " + value.getDouble(0));
                            break;
                        case "不等于":
                            tmpHavingBuffer.append(havingField + " != " + value.getDouble(0));
                            break;
                        case "大于":
                            tmpHavingBuffer.append(havingField + " > " + value.getDouble(0));
                            break;
                        case "小于":
                            tmpHavingBuffer.append(havingField + " < " + value.getDouble(0));
                            break;
                        case "大于等于":
                            tmpHavingBuffer.append(havingField + " >= " + value.getDouble(0));
                            break;
                        case "小于等于":
                            tmpHavingBuffer.append(havingField + " <= " + value.getDouble(0));
                            break;
                        case "区间":
                            tmpHavingBuffer.append(havingField + " BETWEEN " + value.getDouble(0) + " AND " + value.getDouble(1));
                            break;
                        case "不为空":
                            tmpHavingBuffer.append(havingField + " is not null");
                            break;
                        case "为空":
                            tmpHavingBuffer.append(havingField + " is null");
                            break;
                    }
                    break;
                case 2:  // 文本
                    JSONArray seleted = obj.getJSONArray("selected");
                    if (!(seleted.size() == 1 && seleted.getString(0).equals("全部"))) {
                        if (seleted.size() > 0) {
                            tmpWhereBuffer.append(fieldColumn + " IN (");
                            for (int j = 0; j < seleted.size(); j++) {
                                tmpWhereBuffer.append("\'" + seleted.getString(j) + "\'");
                                if (j != seleted.size() - 1)
                                    tmpWhereBuffer.append(",");
                            }
                            tmpWhereBuffer.append(")");
                        }
                    }
                    break;
                case 3:   // 日期
                    String dateLavel = obj.getString("dateLevel");
                    if (dateLavel.equals("常规")) {

                    } else {
                        seleted = obj.getJSONArray("selected");
                        String formatedColumnName = null;
                        switch (dateLavel) {
                            case "按年":
                                formatedColumnName = "YEAR(" + fieldColumn + ")";
                                break;
                            case "按季":
                                formatedColumnName = "CONCAT(YEAR(" + fieldColumn + "),\'年\'," + "QUARTER(" + fieldColumn + "),\'季度\')";
                                break;
                            case "按月":
                                formatedColumnName = "DATE_FORMAT(" + fieldColumn + ",'%Y-%m')";
                                break;
                            case "按周":
                                formatedColumnName = "CONCAT(YEAR(" + fieldColumn + "),\'年第\'," + "WEEKOFYEAR(" + fieldColumn + "),\'周\')";
                                break;
                            case "按日":
                                formatedColumnName = "DATE_FORMAT(" + fieldColumn + ",'%Y-%m-%d')";
                                break;
                        }
                        if (!(seleted.size() == 1 && seleted.getString(0).equals("全部"))) {
                            if (seleted.size() > 0) {
                                tmpWhereBuffer.append(formatedColumnName + " IN (");
                                for (int j = 0; j < seleted.size(); j++) {
                                    tmpWhereBuffer.append("\'" + seleted.getString(j) + "\'");
                                    if (j != seleted.size() - 1)
                                        tmpWhereBuffer.append(",");
                                }
                                tmpWhereBuffer.append(")");
                            }
                        }
                    }
                    break;
            }
            if (tmpWhereBuffer.length() > 0) whereBuffer.append(tmpWhereBuffer + " AND ");
            if (tmpHavingBuffer.length() > 0) havingBuffer.append(tmpHavingBuffer + " AND ");
        }
        String whereString = whereBuffer.toString();
        String havingString = havingBuffer.toString();
        if (whereString.endsWith(" AND ")) whereString = whereString.substring(0, whereString.lastIndexOf(" AND "));
        if (havingString.endsWith(" AND ")) havingString = havingString.substring(0, havingString.lastIndexOf(" AND "));
        result[0] = whereString;
        result[1] = havingString;
        return result;
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
            String fieldColumn = obj.getString("fieldColumn");
            String type = obj.getString("type");
            String subType = obj.getString("subType");
            String tmpResult = "";

            if (type.equals("date")) {
                JSONObject condition = obj.getJSONObject("condition");
                String startTime = condition.getString("startTime");
                String endTime = condition.getString("endTime");
                if (!(startTime.equals("") && endTime.equals(""))) {
                    if (startTime.equals("")) {
                        tmpResult = fieldColumn + " <= \'" + endTime + "\'";
                    } else if (endTime.equals("")) {
                        tmpResult = fieldColumn + " >= \'" + startTime + "\'";
                    } else
                        tmpResult = "(" + fieldColumn + " BETWEEN \'" + startTime + "\' AND \'" + endTime + "\')";
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
                        tmpResult = "(" + fieldColumn + " NOT IN (" + colVal + ") )";
                    else
                        tmpResult = "(" + fieldColumn + " IN (" + colVal + ") )";
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
                                tmpResult = tmpResult + " (" + fieldColumn + " = \'" + value + "\') " + logic;
                                break;
                            case "不等于":  // 不等于
                                tmpResult = tmpResult + " (" + fieldColumn + " != \'" + value + "\') " + logic;
                                break;
                            case "包含": //包含
                                tmpResult = tmpResult + " (" + fieldColumn + " like \'%" + value + "%\') " + logic;
                                break;
                            case "不包含": // 不包含
                                tmpResult = tmpResult + " (" + fieldColumn + " not like \'%" + value + "%\') " + logic;
                                break;
                            case "开头包含": // 开头包含
                                tmpResult = tmpResult + " (" + fieldColumn + " like \'" + value + "%\') " + logic;
                                break;
                            case "结尾包含": // 结尾包含
                                tmpResult = tmpResult + " (" + fieldColumn + " like \'%" + value + "\') " + logic;
                                break;
                            case "为空": // 为空
                                tmpResult = tmpResult + " (" + fieldColumn + " is null ) " + logic;
                                break;
                            case "不为空": // 不为空
                                tmpResult = tmpResult + " (" + fieldColumn + " is not null ) " + logic;
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
                            tmpResult = "(" + fieldColumn + " = " + conditionValues.getDouble(0) + ")";
                            break;
                        case "不等于":
                            tmpResult = "(" + fieldColumn + " != " + conditionValues.getDouble(0) + ")";
                            break;
                        case "大于":
                            tmpResult = "(" + fieldColumn + " > " + conditionValues.getDouble(0) + ")";
                            break;
                        case "小于":
                            tmpResult = "(" + fieldColumn + " < " + conditionValues.getDouble(0) + ")";
                            break;
                        case "大于等于":
                            tmpResult = "(" + fieldColumn + " >= " + conditionValues.getDouble(0) + ")";
                            break;
                        case "小于等于":
                            tmpResult = "(" + fieldColumn + " <= " + conditionValues.getDouble(0) + ")";
                            break;
                        case "区间":
                            tmpResult = "(" + fieldColumn + " BETWEEN " + conditionValues.getDouble(0) + " AND " + conditionValues.getDouble(1) + ")";
                            break;
                        case "不为空":
                            tmpResult = "(" + fieldColumn + " is not null )";
                            break;
                        case "为空":
                            tmpResult = "(" + fieldColumn + " is null )";
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
        StringBuffer result = new StringBuffer();
        if (measure == null || measure.isEmpty()) return result.toString();
        for (int i = 0; i < measure.size(); i++) {
            JSONObject dataObj = measure.getJSONObject(i);
            String fieldColumn = dataObj.getString("fieldColumn");
            String aggregate = dataObj.getString("aggregate");
            switch (aggregate) {
                case "求和":
                    result.append("SUM(" + fieldColumn + ") AS " + fieldColumn + "_SUM" + i);
                    break;
                case "计数":
                    result.append("COUNT(" + fieldColumn + ") AS " + fieldColumn + "_COUNT" + i);
                    break;
                case "去重计数":
                    result.append("COUNT(DISTINCT " + fieldColumn + ") AS " + fieldColumn + "_DISCOUNT" + i);
                    break;
                case "平均值":
                    result.append("AVG(" + fieldColumn + ") AS " + fieldColumn + "_AVG" + i);
                    break;
                case "最大值":
                    result.append("MAX(" + fieldColumn + ") AS " + fieldColumn + "_MAX" + i);
                    break;
                case "最小值":
                    result.append("MIN(" + fieldColumn + ") AS " + fieldColumn + "_MIN" + i);
                    break;
                default:
                    break;
            }
            result.append("_M");   //_M表示该字段为度量，后续处理需要
            result.append(", ");   //注意此处必须为", "，后续处理需要
        }
        String resultString = result.toString();
        if (resultString.endsWith(", "))
            resultString = resultString.substring(0, resultString.length() - 2);
        return resultString;
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
                String fieldColumn = dimJsonObj.getString("fieldColumn");
                int fieldType = dimJsonObj.getIntValue("fieldType");
                String dateLevel;
                String tmpDimString = "";

                if (fieldType == 3) { // 日期类型
                    dateLevel = dimJsonObj.getString("dateLevel");
                    switch (dateLevel) {
                        case "按年":
                            tmpDimString = "YEAR(" + fieldColumn + ")";
                            break;
                        case "按季":
                            tmpDimString = "CONCAT(YEAR(" + fieldColumn + "),\'年\'," + "QUARTER(" + fieldColumn + "),\'季度\')";
                            break;
                        case "按月":
                            tmpDimString = "DATE_FORMAT(" + fieldColumn + ",'%Y-%m')";
                            break;
                        case "按周":
                            tmpDimString = "CONCAT(YEAR(" + fieldColumn + "),\'年第\'," + "WEEKOFYEAR(" + fieldColumn + "),\'周\')";
                            break;
                        case "按日":
                            tmpDimString = "DATE_FORMAT(" + fieldColumn + ",'%Y-%m-%d')";
                            break;
                        default:
                            break;
                    }
                    groupbyString = groupbyString + tmpDimString + ",";
                    dimColumns = dimColumns + tmpDimString + " AS " + fieldColumn + "_" + i + "_D, "; //注意此处必须为", "，_D表示该字段是维度，后续处理需要
                } else {
                    dimColumns = dimColumns + fieldColumn + " AS " + fieldColumn + "_" + i + "_D, ";  //注意此处必须为", "，_D表示该字段是维度，后续处理需要
                    groupbyString = groupbyString + fieldColumn + ",";
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