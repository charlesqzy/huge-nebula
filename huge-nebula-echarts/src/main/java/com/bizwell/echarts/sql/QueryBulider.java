package com.bizwell.echarts.sql;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class QueryBulider {

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
     * 拼接出数值类型的where条件语句
     *
     * @param value
     * @param fieldColumn
     * @return
     */
    private static String getNumberCondition(String operator, JSONArray value, String fieldColumn) {
        String numberCondition = null;
        switch (operator) {
            case "等于":
                numberCondition = fieldColumn + " = " + value.getDouble(0);
                break;
            case "不等于":
                numberCondition = fieldColumn + " != " + value.getDouble(0);
                break;
            case "大于":
                numberCondition = fieldColumn + " > " + value.getDouble(0);
                break;
            case "小于":
                numberCondition = fieldColumn + " < " + value.getDouble(0);
                break;
            case "大于等于":
                numberCondition = fieldColumn + " >= " + value.getDouble(0);
                break;
            case "小于等于":
                numberCondition = fieldColumn + " <= " + value.getDouble(0);
                break;
            case "区间":
                numberCondition = fieldColumn + " BETWEEN " + value.getDouble(0) + " AND " + value.getDouble(1);
                break;
            case "不为空":
                numberCondition = fieldColumn + " IS NOT NULL";
                break;
            case "为空":
                numberCondition = fieldColumn + " IS NULL";
                break;
            default:
                break;
        }
        return numberCondition;
    }

    /**
     * 根据formatType，拼接出对应的日期函数字符串
     *
     * @param formatType
     * @param fieldColumn
     * @return
     */
    private static String getFormatDateColumn(String formatType, String fieldColumn) {
        String formatColumn = null;
        switch (formatType) {
            case "按年":
                formatColumn = "YEAR(" + fieldColumn + ")";
                break;
            case "按季":
                formatColumn = "CONCAT(YEAR(" + fieldColumn + "),\'年\'," + "QUARTER(" + fieldColumn + "),\'季度\')";
                break;
            case "按月":
                formatColumn = "DATE_FORMAT(" + fieldColumn + ",'%Y-%m')";
                break;
            case "按周":
                formatColumn = "CONCAT(YEAR(" + fieldColumn + "),\'年第\'," + "WEEKOFYEAR(" + fieldColumn + "),\'周\')";
                break;
            case "按日":
                formatColumn = "DATE_FORMAT(" + fieldColumn + ",'%Y-%m-%d')";
                break;
        }
        return formatColumn;
    }

    /**
     * 根据聚合类型，将字段封装，返回封装后的字段名
     *
     * @param aggregate
     * @param fieldColumn
     * @return
     */
    private static String getAggregateColumn(String aggregate, String fieldColumn) {
        String aggrateColumn = null;
        switch (aggregate) {
            case "求和":
                aggrateColumn = "SUM(" + fieldColumn + ")";
                break;
            case "计数":
                aggrateColumn = "COUNT(" + fieldColumn + ")";
                break;
            case "去重计数":
                aggrateColumn = "COUNT(DISTINCT " + fieldColumn + ")";
                break;
            case "平均值":
                aggrateColumn = "AVG(" + fieldColumn + ")";
                break;
            case "最大值":
                aggrateColumn = "MAX(" + fieldColumn + ")";
                break;
            case "最小值":
                aggrateColumn = "MIN(" + fieldColumn + ")";
                break;
            default:
                break;
        }
        return aggrateColumn;
    }

    /**
     * 拼接条件中的IN表达式，fieldColumn类型为字符型
     *
     * @param seleted
     * @param fieldColumn
     * @param isNotIN
     * @return
     */
    private static StringBuffer getInString(JSONArray seleted, String fieldColumn, boolean isNotIN) {
        StringBuffer sb = new StringBuffer();
        if (!(seleted.size() == 1 && seleted.getString(0).equals("全部"))) {
            if (seleted.size() > 0) {
                if (isNotIN)
                    sb.append(fieldColumn + " NOT IN (");
                else
                    sb.append(fieldColumn + " IN (");
                for (int j = 0; j < seleted.size(); j++) {
                    sb.append("\'" + seleted.getString(j) + "\'");
                    if (j != seleted.size() - 1)
                        sb.append(",");
                }
                sb.append(")");
            }
        }
        return sb;
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
            String fieldColumn = obj.getString("fieldColumn");

            StringBuffer tmpWhereBuffer = new StringBuffer();
            StringBuffer tmpHavingBuffer = new StringBuffer();
            switch (fieldType) {
                case 1:  // 数字
                    JSONObject condition = obj.getJSONObject("condition");
                    if (!condition.getString("type").equals("全部")) {
                        String havingField = getAggregateColumn(obj.getString("aggregate"), fieldColumn);
                        String numberCondition = getNumberCondition(condition.getString("type"), condition.getJSONArray("value"), havingField);
                        tmpHavingBuffer.append(numberCondition);
                    }
                    break;
                case 2:  // 文本
                    JSONArray seleted = obj.getJSONArray("selected");
                    boolean isNotIn = false;
                    tmpWhereBuffer = getInString(seleted, fieldColumn, isNotIn);
                    break;
                case 3:   // 日期
                    String dateLavel = obj.getString("dateLevel");
                    if (dateLavel.equals("常规")) {
                        condition = obj.getJSONObject("condition");
                        tmpWhereBuffer = getDataCondition(condition, fieldColumn);
                    } else {
                        seleted = obj.getJSONArray("selected");
                        String formatColumnName = getFormatDateColumn(dateLavel, fieldColumn);
                        isNotIn = false;
                        tmpWhereBuffer = getInString(seleted, formatColumnName, isNotIn);
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
     * 拼接日期范围条件语句
     *
     * @param condition
     * @param fieldColumn
     * @return
     */
    private static StringBuffer getDataCondition(JSONObject condition, String fieldColumn) {
        StringBuffer sb = new StringBuffer();
        String startTime = condition.getString("startTime");
        String endTime = condition.getString("endTime");
        if (!(startTime.equals("") && endTime.equals(""))) {
            if (startTime.equals("")) {
                sb.append(fieldColumn + " <= \'" + endTime + "\'");
            } else if (endTime.equals("")) {
                sb.append(fieldColumn + " >= \'" + startTime + "\'");
            } else
                sb.append(fieldColumn + " BETWEEN \'" + startTime + "\' AND \'" + endTime + "\'");
        }
        return sb;
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
                tmpResult = getDataCondition(condition, fieldColumn).toString();
            } else if (type.equals("text")) {
                if (subType.equals("精确筛选")) {
                    JSONArray condition = obj.getJSONArray("condition");
                    boolean invertSelection = obj.getBoolean("invertSelection");
                    tmpResult = getInString(condition, fieldColumn, invertSelection).toString();
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
                    JSONArray value = condition.getJSONArray("value");
                    tmpResult = getNumberCondition(conditionType, value, fieldColumn);
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
            String aggregateColumn = getAggregateColumn(aggregate, fieldColumn);

            String suffix;
            if (aggregateColumn.startsWith("COUNT(DISTINCT"))
                suffix = "DISCOUNT";
            else
                suffix = aggregateColumn.substring(0, aggregateColumn.indexOf("("));

            result.append(aggregateColumn + " AS " + "M" + String.format("%02d", i) + "_" + fieldColumn + "_" + suffix);
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
        String groupByString = "";

        if (dimension != null && !dimension.isEmpty()) {
            for (int i = 0; i < dimension.size(); i++) {
                JSONObject dimJsonObj = dimension.getJSONObject(i);
                String fieldColumn = dimJsonObj.getString("fieldColumn");
                int fieldType = dimJsonObj.getIntValue("fieldType");
                String tmpDimString;
                if (fieldType == 3)  // 日期类型
                    tmpDimString = getFormatDateColumn(dimJsonObj.getString("dateLevel"), fieldColumn);
                else
                    tmpDimString = fieldColumn;
                groupByString = groupByString + tmpDimString + ",";
                dimColumns = dimColumns + tmpDimString + " AS D" + String.format("%02d", i) + "_" + fieldColumn + ", ";  //注意此处必须为", "，_D表示该字段是维度，后续处理需要
            }
            if (groupByString.endsWith(","))
                groupByString = groupByString.substring(0, groupByString.length() - 1);
            if (dimColumns.endsWith(", "))
                dimColumns = dimColumns.substring(0, dimColumns.length() - 2);
        }
        result[0] = dimColumns;
        result[1] = groupByString;
        return result;
    }

    public static void main(String[] args) {
        String jsonString2 = "{ \"echartType\": \"00\", \"moduleType\": \"01\", \"dimension\": [ { \"metadataId\": 162, \"name\": \"billdate\", \"aggregate\": \"计数\", \"dateLevel\": \"按日\", \"fieldType\": 3, \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"C\", \"it\": 2 } ], \"measure1\": [ { \"metadataId\": 161, \"name\": \"hotelname\", \"aggregate\": \"计数\", \"dateLevel\": \"按日\", \"fieldType\": 2, \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"B\", \"it\": 1 } ], \"measure2\": [], \"filter\": [], \"type\": \"\", \"stack\": \"\", \"inChartFilter\": [ { \"name\": \"billdate\", \"metadataId\": 162, \"level\": 1, \"fieldType\": 3, \"dateLevel\": \"常规\", \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"C\", \"it\": 100001, \"type\": 7, \"condition\": { \"startTime\": \"2018-06-01 00:00:00\", \"endTime\": \"2018-06-30 00:00:00\" } }, { \"name\": \"hotelname\", \"metadataId\": 161, \"level\": 1, \"fieldType\": 2, \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"B\", \"it\": 100002, \"selected\": [ \"全部\" ] }, { \"name\": \"hotelname\", \"metadataId\": 161, \"level\": 1, \"fieldType\": 1, \"aggregate\": \"计数\", \"tableName\": \"xls_16d506e966a257c240adaed164fdbdcc_u16_s01\", \"fieldColumn\": \"B\", \"it\": 1, \"condition\": { \"type\": \"全部\", \"value\": [] } } ] }";


        System.out.println(jsonString2);
        String sql = getQueryString(jsonString2);
        System.out.println("sql : \n" + sql);
    }

}