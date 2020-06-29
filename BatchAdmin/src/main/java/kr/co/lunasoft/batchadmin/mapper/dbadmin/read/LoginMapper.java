package kr.co.lunasoft.batchadmin.mapper.dbadmin.read;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

	Map<String, Object> loginUserInfo(Map<String, String> loginInfo) throws Exception;
	
	List<Map<String, Object>> loginUserMenuMain(String luna_auth_no) throws Exception;
	List<Map<String, Object>> loginUserMenuSub(String luna_auth_no) throws Exception;

	int checkOrgPw(Map<String, Object> params) throws Exception;

	int checkExistsId(Map<String, String> params) throws Exception;
	
	/*스마트플러스 공통 로그데이터
	 * 로그인 에서 구어드민DB users.id 와 일치하는 통합어드민DB 의 tb_member_info.member_no 값 조회*/
	Map<String, Object> getUsersMemberNo(Map<String, String> params) throws Exception;
	Map<String, Object> getUsersSubMemberNo(Map<String, String> params) throws Exception;
	Map<String, Object> selectLunaUserInfo(String dbOldLunaId);

	
}
