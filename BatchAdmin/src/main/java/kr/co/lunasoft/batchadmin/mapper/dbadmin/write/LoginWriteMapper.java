package kr.co.lunasoft.batchadmin.mapper.dbadmin.write;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginWriteMapper {

	void updateLastLoinDate(String luna_id);

	void updatePassword(Map<String, Object> params) throws Exception;

}
