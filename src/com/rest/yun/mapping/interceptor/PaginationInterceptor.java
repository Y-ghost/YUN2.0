package com.rest.yun.mapping.interceptor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.rest.yun.dto.Page;
import com.rest.yun.mapping.dialect.Dialect;
import com.rest.yun.mapping.dialect.TypeNames;
import com.rest.yun.util.Reflections;

@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = Connection.class))
public class PaginationInterceptor implements Interceptor, Serializable {

	private static final long serialVersionUID = -7454036093591325169L;

	private static Logger LOG = LoggerFactory.getLogger(PaginationInterceptor.class);

	private static String _sql_regex = "(.*)";

	protected Dialect dialect;

	public static final String PAGE_OBJECT = "page";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		if (target instanceof RoutingStatementHandler) {
			RoutingStatementHandler handler = (RoutingStatementHandler) target;

			// Get 'delegate' of the RoutingStatementHandler
			StatementHandler delegate = (StatementHandler) Reflections.getField(handler, "delegate");

			/*
			 * Get mappedStatement of the StatementHandler to gain mapping sql
			 * id in mybatis-config.xml
			 */
			MappedStatement ms = (MappedStatement) Reflections.getField(delegate, "mappedStatement");

			String sqlId = ms.getId();

			LOG.debug("mapped statement:" + sqlId);

			boolean intercept = sqlId.matches(_sql_regex);

			// No intercept the SQL
			if (!intercept) {
				return invocation.proceed();
			}
			// Database not support limit
			if (!dialect.supportsLimit()) {
				return invocation.proceed();
			}

			BoundSql boundSql = handler.getBoundSql();

			// Get parameters of calling sql method
			Object param = boundSql.getParameterObject();
			if (param instanceof Map) {

				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) param;
				Object object = map.get(PAGE_OBJECT);
				if (object==null) {
					return invocation.proceed();
				}
				Page<?> page = (Page<?>) object;
				// Get current sql
				String sql = boundSql.getSql();

				// build countSql
				String countSql = dialect.getCountSql(sql);

				Connection connection = (Connection) invocation.getArgs()[0];
				// get total rows
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				try {
					pstmt = connection.prepareStatement(countSql);

					// Get parameter mappings by BoundSql
					List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

					// Build BoundSql for count sql to set configuration and
					// parameters
					BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql, parameterMappings, param);

					// Build ParameterHandler to set parameters for count
					// sql
					ParameterHandler parameterHandler = new DefaultParameterHandler(ms, param, countBoundSql);

					parameterHandler.setParameters(pstmt);

					rs = pstmt.executeQuery();
					if (rs.next()) {
						int totalRows = rs.getInt(1);
						page.setTotal(totalRows);
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
				}

				// Build limit sql
				String limitSql = dialect.getLimitSql(sql, page.getStartRow(), page.getPageSize());

				if (LOG.isDebugEnabled()) {
					LOG.debug("Gen limit sql" + limitSql);
				}
				// override current sql by limit sql
				Reflections.setField(boundSql, "sql", limitSql);
			}
		}

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof RoutingStatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties p) {
		String dialectClass = p.getProperty("dialectClass");
		String dialectAliases = p.getProperty("dialect");
		if (StringUtils.isEmpty(dialectClass)) {
			dialect = TypeNames.getDialectByName(dialectAliases);
		} else {
			dialect = (Dialect) instance(dialectClass);
			TypeNames.putCustomeDialect(dialectClass, dialect);
		}

		String sqlRegex = p.getProperty("sqlRegex");
		if (!StringUtils.isEmpty(sqlRegex)) {
			_sql_regex = sqlRegex;
		}

	}

	private Object instance(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			Object obj = clazz.newInstance();
			if (!(obj instanceof Dialect)) {
				throw new RuntimeException("The class [" + className + "] must be sub class of Dialect");
			}
			dialect = (Dialect) obj;
			return obj;
		} catch (Exception e) {
			throw new RuntimeException("Instants [" + className + "] failed", e);
		}

	};

}
