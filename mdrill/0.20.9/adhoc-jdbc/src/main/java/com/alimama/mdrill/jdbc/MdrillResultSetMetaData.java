package com.alimama.mdrill.jdbc;



import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;


public class MdrillResultSetMetaData implements java.sql.ResultSetMetaData{
	  private final List<String> columnNames;
	  private final List<String> columnTypes;

	  public MdrillResultSetMetaData(List<String> columnNames,
	      List<String> columnTypes) {
	    this.columnNames = columnNames;
	    this.columnTypes = columnTypes;
	  }

	  public String getCatalogName(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public String getColumnClassName(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public int getColumnCount() throws SQLException {
	    return columnNames.size();
	  }

	  public int getColumnDisplaySize(int column) throws SQLException {
	    int columnType = getColumnType(column);

	    return JdbcColumn.columnDisplaySize(columnType);
	  }

	  public String getColumnLabel(int column) throws SQLException {
	    return columnNames.get(column - 1);
	  }

	  public String getColumnName(int column) throws SQLException {
	    return columnNames.get(column - 1);
	  }

	  public int getColumnType(int column) throws SQLException {
	    if (columnTypes == null) {
	      throw new SQLException(
	          "Could not determine column type name for ResultSet");
	    }

	    if (column < 1 || column > columnTypes.size()) {
	      throw new SQLException("Invalid column value: " + column);
	    }

	    // we need to convert the thrift type to the SQL type
	    String type = columnTypes.get(column - 1);

	    // we need to convert the thrift type to the SQL type
	    return Utils.hiveTypeToSqlType(type);
	  }

	  public String getColumnTypeName(int column) throws SQLException {
	    if (columnTypes == null) {
	      throw new SQLException(
	          "Could not determine column type name for ResultSet");
	    }

	    if (column < 1 || column > columnTypes.size()) {
	      throw new SQLException("Invalid column value: " + column);
	    }

	    // we need to convert the Hive type to the SQL type name
	    // TODO: this would be better handled in an enum
	    String type = columnTypes.get(column - 1);
	    if ("string".equalsIgnoreCase(type)) {
	      return Constants.STRING_TYPE_NAME;
	    } else if ("float".equalsIgnoreCase(type)) {
	      return Constants.FLOAT_TYPE_NAME;
	    } else if ("double".equalsIgnoreCase(type)) {
	      return Constants.DOUBLE_TYPE_NAME;
	    } else if ("boolean".equalsIgnoreCase(type)) {
	      return Constants.BOOLEAN_TYPE_NAME;
	    } else if ("tinyint".equalsIgnoreCase(type)) {
	      return Constants.TINYINT_TYPE_NAME;
	    } else if ("smallint".equalsIgnoreCase(type)) {
	      return Constants.SMALLINT_TYPE_NAME;
	    } else if ("int".equalsIgnoreCase(type)) {
	      return Constants.INT_TYPE_NAME;
	    } else if ("bigint".equalsIgnoreCase(type)) {
	      return Constants.BIGINT_TYPE_NAME;
	    } else if (type.startsWith("map<")) {
	      return Constants.STRING_TYPE_NAME;
	    } else if (type.startsWith("array<")) {
	      return Constants.STRING_TYPE_NAME;
	    } else if (type.startsWith("struct<")) {
	      return Constants.STRING_TYPE_NAME;
	    }

	    throw new SQLException("Unrecognized column type: " + type);
	  }

	  public int getPrecision(int column) throws SQLException {
	    int columnType = getColumnType(column);

	    return JdbcColumn.columnPrecision(columnType);
	  }

	  public int getScale(int column) throws SQLException {
	    int columnType = getColumnType(column);

	    return JdbcColumn.columnScale(columnType);
	  }

	  public String getSchemaName(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public String getTableName(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public boolean isAutoIncrement(int column) throws SQLException {
	    // Hive doesn't have an auto-increment concept
	    return false;
	  }

	  public boolean isCaseSensitive(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public boolean isCurrency(int column) throws SQLException {
	    // Hive doesn't support a currency type
	    return false;
	  }

	  public boolean isDefinitelyWritable(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public int isNullable(int column) throws SQLException {
	    // Hive doesn't have the concept of not-null
	    return ResultSetMetaData.columnNullable;
	  }

	  public boolean isReadOnly(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public boolean isSearchable(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public boolean isSigned(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public boolean isWritable(int column) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public boolean isWrapperFor(Class<?> iface) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

	  public <T> T unwrap(Class<T> iface) throws SQLException {
	    throw new SQLException("Method not supported");
	  }

}
