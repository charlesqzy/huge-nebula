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
     * @param selected
     * @param fieldColumn
     * @param isNotIN
     * @return
     */
    private static StringBuffer getInString(JSONArray selected, String fieldColumn, boolean isNotIN) {
        StringBuffer sb = new StringBuffer();
        if (!(selected.size() == 1 && selected.getString(0).equals("全部"))) {
            if (selected.size() > 0) {
                if (isNotIN)
                    sb.append(fieldColumn + " NOT IN (");
                else
                    sb.append(fieldColumn + " IN (");
                for (int j = 0; j < selected.size(); j++) {
                    sb.append("\'" + selected.getString(j) + "\'");
                    if (j != selected.size() - 1)
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
                    String subType = obj.getString("subType");
                    if (subType.equals("精确筛选")) {
                        JSONArray selected = obj.getJSONArray("selected");
                        boolean isNotIn = obj.getBoolean("invertSelection");
                        tmpWhereBuffer = getInString(selected, fieldColumn, isNotIn);
                    } else if (subType.equals("条件筛选")) {
                        condition = obj.getJSONObject("selected");
                        tmpWhereBuffer = getStringCondition(condition, fieldColumn);
                    }
                    break;
                case 3:   // 日期
                    String dateLavel = obj.getString("dateLevel");
                    if (dateLavel.equals("常规")) {
                        condition = obj.getJSONObject("condition");
                        tmpWhereBuffer = getDataCondition(condition, fieldColumn);
                    } else {
                        JSONArray selected = obj.getJSONArray("selected");
                        String formatColumnName = getFormatDateColumn(dateLavel, fieldColumn);
                        tmpWhereBuffer = getInString(selected, formatColumnName, false);
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
     * 拼接字段格式是文本时的条件语句，如 colunName like '%信息'
     *
     * @param condition
     * @param fieldColumn
     * @return
     */
    private static StringBuffer getStringCondition(JSONObject condition, String fieldColumn) {
        StringBuffer sb = new StringBuffer();
        JSONArray fields = condition.getJSONArray("fields");
        if (fields != null && !fields.isEmpty()) {
            String logic = condition.getString("logic");
            for (int j = 0; j < fields.size(); j++) {
                JSONObject fieldObj = fields.getJSONObject(j);
                String operator = fieldObj.getString("operator");
                String value = fieldObj.getString("value");
                switch (operator) {
                    case "等于":   // 等于
                        sb.append(fieldColumn + " = \'" + value + "\' ");
                        break;
                    case "不等于":  // 不等于
                        sb.append(fieldColumn + " != \'" + value + "\' ");
                        break;
                    case "包含": //包含
                        sb.append(fieldColumn + " like \'%" + value + "%\' ");
                        break;
                    case "不包含": // 不包含
                        sb.append(fieldColumn + " not like \'%" + value + "%\' ");
                        break;
                    case "开头包含": // 开头包含
                        sb.append(fieldColumn + " like \'" + value + "%\' ");
                        break;
                    case "结尾包含": // 结尾包含
                        sb.append(fieldColumn + " like \'%" + value + "\' ");
                        break;
                    case "为空": // 为空
                        sb.append(fieldColumn + " is null ");
                        break;
                    case "不为空": // 不为空
                        sb.append(fieldColumn + " is not null ");
                        break;
                    default:
                        break;
                }
                if (j < fields.size() - 1) sb.append(logic + " ");
            }
        }
        return sb;
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
                    tmpResult = getStringCondition(condition, fieldColumn).toString();
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
        String jsonString2 = "{\n" +
                " \"filter\": [{\n" +
                "  \"fieldColumn\": \"B\",\n" +
                "  \"isshow\": true,\n" +
                "  \"metadataId\": 434,\n" +
                "  \"condition\": [\"中餐\", \"晚餐\"],\n" +
                "  \"name\": \"班次\",\n" +
                "  \"invertSelection\": false,\n" +
                "  \"subType\": \"精确筛选\",\n" +
                "  \"type\": \"text\",\n" +
                "  \"tableName\": \"xls_05f90cf21b60772d3015d50eaa0bafeb_u16_s01\"\n" +
                " }],\n" +
                " \"stack\": \"\",\n" +
                " \"measure2\": [],\n" +
                " \"moduleType\": \"01\",\n" +
                " \"measure1\": [{\n" +
                "  \"fieldColumn\": \"Q\",\n" +
                "  \"metadataId\": 449,\n" +
                "  \"name\": \"结算|折扣|公司折扣\",\n" +
                "  \"it\": 3,\n" +
                "  \"fieldType\": 1,\n" +
                "  \"dateLevel\": \"按日\",\n" +
                "  \"aggregate\": \"求和\",\n" +
                "  \"tableName\": \"xls_05f90cf21b60772d3015d50eaa0bafeb_u16_s01\"\n" +
                " }],\n" +
                " \"echartType\": \"00\",\n" +
                " \"inChartFilter\": [{\n" +
                "  \"fieldColumn\": \"C\",\n" +
                "  \"metadataId\": 435,\n" +
                "  \"level\": 1,\n" +
                "  \"name\": \"流水号\",\n" +
                "  \"it\": 100005,\n" +
                "  \"fieldType\": 2,\n" +
                "  \"tableName\": \"xls_05f90cf21b60772d3015d50eaa0bafeb_u16_s01\",\n" +
                "  \"selected\": [\"421115\", \"421116\"],\n" +
                "  \"subType\": \"精确筛选\",\n" +
                "  \"invertSelection\": false\n" +
                " }, {\n" +
                "  \"fieldColumn\": \"B\",\n" +
                "  \"metadataId\": 434,\n" +
                "  \"level\": 1,\n" +
                "  \"name\": \"班次\",\n" +
                "  \"it\": 100004,\n" +
                "  \"fieldType\": 2,\n" +
                "  \"tableName\": \"xls_05f90cf21b60772d3015d50eaa0bafeb_u16_s01\",\n" +
                "  \"selected\": {\n" +
                "   \"logic\": \"AND\",\n" +
                "   \"fields\": [{\n" +
                "    \"operator\": \"等于\",\n" +
                "    \"value\": \"555\"\n" +
                "   }, {\n" +
                "    \"operator\": \"包含\",\n" +
                "    \"value\": \"66\"\n" +
                "   }]\n" +
                "  },\n" +
                "  \"subType\": \"条件筛选\"\n" +
                " }],\n" +
                " \"type\": \"\",\n" +
                " \"dimension\": [{\n" +
                "  \"fieldColumn\": \"W\",\n" +
                "  \"metadataId\": 455,\n" +
                "  \"name\": \"结帐时间\",\n" +
                "  \"it\": 4,\n" +
                "  \"fieldType\": 3,\n" +
                "  \"dateLevel\": \"按日\",\n" +
                "  \"aggregate\": \"计数\",\n" +
                "  \"tableName\": \"xls_05f90cf21b60772d3015d50eaa0bafeb_u16_s01\"\n" +
                " }]\n" +
                "}";


        System.out.println(jsonString2);
        String sql = getQueryString(jsonString2);
        System.out.println("sql : \n" + sql);
    }

}