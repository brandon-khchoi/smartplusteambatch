package kr.co.lunasoft.batchadmin.config;

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
@MapperScan(value="kr.co.lunasoft.batchadmin.mapper.dbadmin.write", sqlSessionFactoryRef="dbAdminWriteSqlSessionFactory")
//@EnableTransactionManagement
public class DbAdminWriteConfig {
    @Autowired
    private Environment env;
    
    private static final String prefix = "spring.dbadminWrite.datasource.hikari.";
  
    @Bean(name = "dbAdminWriteDataSource", destroyMethod = "close")
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
    
    @Bean(name = "dbAdminWriteSqlSessionFactory")
    public SqlSessionFactory dbMallSqlSessionFactory(@Qualifier("dbAdminWriteDataSource") HikariDataSource dbAdminWriteDataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dbAdminWriteDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:dbadmin/write/*.xml"));        
        return sqlSessionFactoryBean.getObject();
    } 

    @Bean(name = "dbAdminWriteSqlSessionTemplate")   
    public SqlSessionTemplate dbAdminWriteSqlSessionTemplate(SqlSessionFactory dbAdminWriteSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(dbAdminWriteSqlSessionFactory);
    }


    
   
    @Bean(name="dbAdminWriteTransactionManager")    
    public PlatformTransactionManager dbAdminWriteTransactionManager(@Qualifier("dbAdminWriteDataSource") HikariDataSource dbAdminWriteDataSource) {
  
    	DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();      
    	transactionManager.setDataSource(dbAdminWriteDataSource);      
    	
        return transactionManager;
    }
 
    
}