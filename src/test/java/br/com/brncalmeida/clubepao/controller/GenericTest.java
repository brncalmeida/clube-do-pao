package br.com.brncalmeida.clubepao.controller;

import java.text.MessageFormat;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;

import br.com.caelum.vraptor.util.test.MockLocalization;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;

@SuppressWarnings("deprecation")
public abstract class GenericTest {

	private Session session;

	public Session getSession() {
		if (session == null) {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure().setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:clubePaoDB");
			cfg.configure().setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
			cfg.configure().setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
			cfg.configure().setProperty("hibernate.connection.username", "sa");
			cfg.configure().setProperty("hibernate.connection.password", "");

			cfg.configure().setProperty("hibernate.show_sql", "true");
			cfg.configure().setProperty("hibernate.hbm2ddl.auto", "update");
			cfg.configure().setProperty("hibernate.cache.provider_class", "org.hibernate.dialect.HSQLDialect");

			session = cfg.buildSessionFactory().openSession();
			session.beginTransaction();
		}
		return session;
	}

	public String getMessage(String property) {
		return new MockLocalization().getBundle().getString(property);
	}

	public String getMessage(String property, String complement) {
		return MessageFormat.format(getMessage(property), complement);
	}

	public MockLocalization getLocalization() {
		return new MockLocalization();
	}
	
	public MockResult getMockResult(){
		return new MockResult();
	}

	public MockValidator getMockValidator(){
		return new MockValidator();
	}
}
