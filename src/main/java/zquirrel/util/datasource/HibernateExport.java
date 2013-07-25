package zquirrel.util.datasource;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateExport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SchemaExport(new Configuration().configure()).create(true, true);
	}

}
