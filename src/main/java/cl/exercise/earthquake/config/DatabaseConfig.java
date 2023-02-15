package cl.exercise.earthquake.config;

import java.util.HashMap;
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

  @Bean
  public LocalContainerEntityManagerFactoryBean earthquakeEntityManager() {

    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    final HashMap<String, Object> properties = new HashMap<>();

    properties.put("hibernate.dialect", env.getProperty("db.earthquake.dialect"));

    em.setDataSource(earthquakeDataSource());
    em.setPackagesToScan("cl.exercise.earthquake.model");
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Bean
  @ConfigurationProperties(prefix = "db.earthquake")
  public DataSource earthquakeDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "transactionManagerEarthquake")
  public PlatformTransactionManager earthquakeTransactionManager() {
    final JpaTransactionManager transactionManagerEarthquake = new JpaTransactionManager();
    transactionManagerEarthquake.setEntityManagerFactory(earthquakeEntityManager().getObject());
    return transactionManagerEarthquake;
  }
}
