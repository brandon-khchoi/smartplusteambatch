package kr.co.lunasoft.batchadmin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = { "kr.co.lunasoft.batchadmin.vo.log.batch" }, mongoTemplateRef = "batchMongoTemplate")
@ConfigurationProperties(prefix = "spring.mongodb")
public class BatchMongoDBConfiguration extends AbstractMongoConfig {

	@Autowired
	private MongoMappingContext mongoMappingContext;

	public BatchMongoDBConfiguration() {
		setDatabase("db_batch");
	}
	
	@Override
	@Bean(name = "batchMongoTemplate")
	public MongoTemplate getMongoTemplate() {
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory()), mongoMappingContext);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // 해당라인이 있으면 _class 필드는 사라지게 된다.

		return new MongoTemplate(mongoDbFactory(), converter);
	}
}
