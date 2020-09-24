package cl.exercise.earthquake.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@PropertySource({"classpath:persistence-${spring.profiles.active:default}.properties"})
@EnableJpaRepositories(
    basePackages = "cl.exercise.earthquake.repository",
    entityManagerFactoryRef = "earthquakeEntityManager",
    transactionManagerRef = "transactionManagerEarthquake")
public class DatabaseConfig {

  @Autowired private Environment env;

  public DatabaseConfig() {
    super();
  }

  private static String getPropertyAsString(Properties prop) {
    StringWriter writer = new StringWriter();
    prop.list(new PrintWriter(writer));
    return writer.getBuffer().toString();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean earthquakeEntityManager() throws SQLException {

    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    final HashMap<String, Object> properties = new HashMap<String, Object>();

    properties.put("hibernate.dialect", env.getProperty("db.earthquake.dialect"));

    em.setDataSource(earthquakeDataSource());
    em.setPackagesToScan("cl.exercise.earthquake.model");
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaPropertyMap(properties);

    log.info("earthquakeConfig...");
    log.info("--> env db {}", em.getDataSource().getConnection());
    log.info("--> jpa db {}", em.getJpaPropertyMap());
    log.info("--> vendor db {}", em.getJpaVendorAdapter().toString());

    return em;
  }

  @Bean
  @ConfigurationProperties(prefix = "db.earthquake")
  public DataSource earthquakeDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "transactionManagerEarthquake")
  public PlatformTransactionManager earthquakeTransactionManager() throws SQLException {
    final JpaTransactionManager transactionManagerEarthquake = new JpaTransactionManager();
    transactionManagerEarthquake.setEntityManagerFactory(earthquakeEntityManager().getObject());
    return transactionManagerEarthquake;
  }
}
