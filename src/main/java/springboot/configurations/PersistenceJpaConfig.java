package springboot.configurations;

import java.sql.Connection;
import java.util.Properties;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.ValidationMode;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;


/* https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa */

@Configuration
@EnableJpaRepositories(basePackages = "springboot.repositories")
@EnableTransactionManagement
public class PersistenceJpaConfig
{
//	  @Bean
//	  public DataSource dataSource() {
//	    try {
//	    	EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
//	      return (DataSource) dbBuilder.setType(EmbeddedDatabaseType.H2)
//	          .addScripts("classpath:h2/schema.sql", "classpath:h2/test-data.sql")
//	          .build();
//	    } catch (Exception e) {
//	      return null;
//	    }
//	  }
	
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
	    LocalContainerEntityManagerFactoryBean emfb 
	        = new LocalContainerEntityManagerFactoryBean();
	    emfb.setDataSource(dataSource());
//	    String[] scanPackages = {"springboot.dto.response", "springboot.entities"};
	    String[] scanPackages = {"springboot.entities"};
	    emfb.setPackagesToScan(scanPackages);
	 
	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setShowSql(true);
	    vendorAdapter.setGenerateDdl(false);
	    vendorAdapter.setPrepareConnection(true); // hibernate 5.1 or 5.2
	    vendorAdapter.setDatabasePlatform("org.hibernate.community.dialect.SQLiteDialect");
	    
	    emfb.setJpaProperties(additionalProperties());
	    emfb.setJpaVendorAdapter(vendorAdapter);
	    emfb.setValidationMode(ValidationMode.NONE); // Needed for H2 to work in Docker
	    emfb.setPersistenceUnitName("VaAssessmentPU");

	    
	    return emfb;
	}	
	
	@Bean(name="pooledDataSource")
	public DataSource dataSource(){
//    	String appName = "VA-assessment";
    	
		DataSource aDataSource = buildTomcatPooledDataSource();
//		System.out.println("in PersistenceJpaConfig AppName: " + appName + " env is: " + env.getActiveProfiles()[0]);
    	
	    return aDataSource;
	}

	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf)
	{
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf);
	    
//	    System.out.println(" in transactionManager Object is: " + emf.toString());
	 
	    return transactionManager;
	}
	
	@Bean	
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
	{
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	private Properties additionalProperties()
	{
	    Properties properties = new Properties();
	    properties.setProperty("hibernate.ddl-auto", "none");
//	    properties.setProperty("hibernate.ddl-auto", "create-drop");
//	    properties.setProperty("hibernate.ddl-auto", "update");
	    properties.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
	    properties.setProperty("hibernate.current_session_context_class", "thread");
//	    <prop key="hibernate.current_session_context_class">org.hibernate.context.ThreadLocalSessionContext</prop>
	    properties.setProperty("hibernate.format_sql", "true");
	    properties.setProperty("hibernate.show_sql", "true");
	    properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
	    properties.setProperty("hibernate.connection.release_mode", "after_transaction");
	        
	    return properties;
	}
	
	private DataSource buildTomcatPooledDataSource()
	{
//		String envName =  anEnvironment.getActiveProfiles()[0].toUpperCase();
//		String connectString = getConfigSettingNoCache(appName, "DBCONN", anEnvironment);
//		String userName = getConfigSettingNoCache(appName, "DBCONN-UserName", anEnvironment);
//		String password = getConfigSettingNoCache(appName, "DBCONN-Password", anEnvironment);
//		String driverClass = getConfigSettingNoCache(appName, "DBCONN-DriverClass", anEnvironment);
		
		/* works outside of Docker */
		
		
		// data/SQLite is mounted from a DockerDesktop Volume joeFraserDataSQLite
		
		String connectString = "jdbc:sqlite:/data/SQLite/va.db";
		String userName = "sa"; // also try SA, with no password, initial h2
		String password = "sa"; // initial SQLite
		String driverClass = "org.sqlite.JDBC";
		
		// https://tomcat.apache.org/tomcat-9.0-doc/jdbc-pool.html#Plain_Ol'_Java

		PoolProperties p = new PoolProperties();
		
		p.setDefaultAutoCommit(false);
		p.setDefaultTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		
        p.setUrl(connectString);
        p.setUsername(userName);
        p.setPassword(password);
        p.setDriverClassName(driverClass);
        
        p.setTestWhileIdle(false);
        p.setValidationQuery("SELECT 1");
        
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(20);
        p.setMaxIdle(20);
        p.setInitialSize(5);
        p.setMaxWait(30000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(1);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setInitSQL("PRAGMA foreign_keys = ON;");
 		 
		DataSource aDataSource = null;
		
		try {
			aDataSource = new DataSource(p);
		}
		catch (Exception e)
		{
//			System.out.println("Create Pool Failed!!!");
			aDataSource = null;
		}
		
		return aDataSource;
		
	}
	
/*	
	private HikariDataSource buildHikariPooledDataSource() {
		
        HikariConfig config = new HikariConfig();
        
        config.setAutoCommit(false);
        config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"); // In-memory database
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        
        config.setConnectionTestQuery("SELECT 1");
        
        config.setValidationTimeout(30000);
        
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(1);
        
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);		
		
		HikariDataSource aDataSource = null;
		try {
			aDataSource = new HikariDataSource(config);
		}
		catch (Exception e)
		{
			System.out.println("Create Pool Failed!!!");
			aDataSource = null;
		}
		
		return aDataSource;
		
	}
*/	
	
	
}
