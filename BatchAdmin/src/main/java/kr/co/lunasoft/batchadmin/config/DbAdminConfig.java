package kr.co.lunasoft.batchadmin.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(value="kr.co.lunasoft.batchadmin.mapper.dbadmin.read", sqlSessionFactoryRef="dbAdminSqlSessionFactory")
//@EnableTransactionManagement
public class DbAdminConfig {
    @Autowired
    private Environment env;
    
    private static final String prefix = "spring.dbadmin.datasource.hikari.";
  
    @Bean(name = "dbAdminDataSource", destroyMethod = "close")
    public HikariDataSource dbAdminDataSource() {
    	HikariConfig config = new HikariConfig();
    	config.setUsername(env.getProperty(prefix + "username")); 
    	config.setPassword(env.getProperty(prefix +"password")); 
//    	config.setDriverClassName(env.getProperty(prefix+"driverClassName"));
    	config.setJdbcUrl( env.getProperty(prefix+"jdbc-url") );
    	config.setMaxLifetime( Long.parseLong(env.getProperty(prefix+"max-lifetime")) );
    	config.setConnectionTimeout(Long.parseLong( env.getProperty(prefix+"connection-timeout")));
    	config.setValidationTimeout(Long.parseLong( env.getProperty(prefix+"validation-timeout")));
    	
    	
    	config.addDataSourceProperty( "cachePrepStmts" ,  env.getProperty(prefix+"data-source-properties.cachePrepStmts"));
        config.addDataSourceProperty( "prepStmtCacheSize" , env.getProperty(prefix+"data-source-properties.prepStmtCacheSize"));
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , env.getProperty(prefix+"data-source-properties.prepStmtCacheSqlLimit") );	
        config.addDataSourceProperty( "useServerPrepStmts" , env.getProperty(prefix+"data-source-properties.useServerPrepStmts") );
        
    	HikariDataSource dataSource = new HikariDataSource( config );
        
        return dataSource;
    }
    
    @Bean(name = "dbAdminSqlSessionFactory")
    public SqlSessionFactory dbMallSqlSessionFactory(@Qualifier("dbAdminDataSource") DataSource dbAdminDataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dbAdminDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:dbadmin/read/*.xml"));        
        return sqlSessionFactoryBean.getObject();
    } 

    @Bean(name = "dbAdminSqlSessionTemplate")   
    public SqlSessionTemplate dbMallSqlSessionTemplate(SqlSessionFactory dbAdminSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(dbAdminSqlSessionFactory);
    }


    
   
    @Bean(name="dbAdminTransactionManager")    
    public PlatformTransactionManager dbAdminTransactionManager(@Qualifier("dbAdminDataSource") DataSource dbAdminDataSource) {
  
    	DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();      
    	transactionManager.setDataSource(dbAdminDataSource);      
    	
        return transactionManager;
    }
 
    
}