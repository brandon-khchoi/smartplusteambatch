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
@MapperScan(value="kr.co.lunasoft.batchadmin.mapper.dbbatch.write", sqlSessionFactoryRef="dbbatchWriteSqlSessionFactory")
public class DbBatchWriteConfig {
    @Autowired
    private Environment env;
 
    private static final String prefix = "spring.dbadminWrite.datasource.hikari.";
        
    @Bean(name = "dbbatchWriteSource", destroyMethod = "close")
    public HikariDataSource dbbatchWriteSource() {
    	HikariConfig config = new HikariConfig();
    	config.setUsername(env.getProperty(prefix+"username")); 
    	config.setPassword(env.getProperty(prefix+"password")); 
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
    
    @Bean(name = "dbbatchWriteSqlSessionFactory")
    public SqlSessionFactory dbbatchWriteSqlSessionFactory(@Qualifier("dbbatchWriteSource") DataSource dbbatchWriteSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dbbatchWriteSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:dbbatch/write/*.xml"));
        return sqlSessionFactoryBean.getObject();
    } 

    @Bean(name = "dbbatchWriteSqlSessionTemplate")
    public SqlSessionTemplate dbBatchSqlSessionTemplate(SqlSessionFactory dbbatchWriteSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(dbbatchWriteSqlSessionFactory);
    }


    
   
    @Bean(name="dbbatchWriteTransactionManager")
    public PlatformTransactionManager dbBatchWriteTransactionManager(@Qualifier("dbbatchWriteSource") DataSource dbbatchWriteSource) {
  
    	DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();      
    	transactionManager.setDataSource(dbbatchWriteSource);        
        return transactionManager;
    }


}
