package com.ensense.insense.data.common.entity;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
/**
 * Generic Id generator class for generating ids for entities
 * @author 434153
 *
 */
public class SequenceTable implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object entity)
			throws HibernateException {
		return this.getNextSequenceForTable(session, entity);
	}

	// Method returns the next valid id for the entity
	private synchronized int getNextSequenceForTable(
			SessionImplementor session, Object entity) {
		int currentValue = 1;
		String tableName = this.getTableName(entity.getClass());
		String primary = this.getPrimaryKey(entity.getClass());
		Connection con = session.connection();
		String query = "select " + primary + " from " + tableName
				+ " order by " + primary + " desc";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					if (rs.getInt(1) > 0) {
						currentValue += rs.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			throw new HibernateException("failed to get primary key");
		}
		return currentValue;
	}

	// Reads the table name from the entity
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getTableName(Class entityClass) {
		String value = null;
		Annotation annot = entityClass.getAnnotation(Table.class);
		value = ((Table) annot).name();
		return value;
	}

	// Reads the primary key column name from the entity
	@SuppressWarnings("rawtypes")
	private String getPrimaryKey(Class entityClass) {
		Field[] fieldList = entityClass.getDeclaredFields();
		String value = null;
		for (Field field : fieldList) {
			if (field.getAnnotation(Id.class) != null) {
				value = ((Column) field.getAnnotation(Column.class)).name();
			}
		}
		return value;
	}
}
