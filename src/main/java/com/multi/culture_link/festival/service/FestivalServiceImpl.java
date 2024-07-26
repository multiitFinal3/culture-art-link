package com.multi.culture_link.festival.service;

import com.multi.culture_link.common.time.model.dto.TimeDTO;
import com.multi.culture_link.festival.model.dto.FestivalContentReviewNaverKeywordMapDTO;
import com.multi.culture_link.festival.model.dto.FestivalKeywordDTO;
import com.multi.culture_link.festival.model.dto.UserFestivalLoveHateMapDTO;
import com.multi.culture_link.festival.model.mapper.FestivalMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FestivalServiceImpl implements FestivalService {
	
	private final FestivalMapper festivalMapper;
	
	public FestivalServiceImpl(FestivalMapper festivalMapper) {
		this.festivalMapper = festivalMapper;
	}
	
	/**
	 * 페스티펄 아이디로 연관된 키워드 리스트 구하기
	 *
	 * @param festivalId
	 * @return 키워드 리스트
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalKeywordDTO> findFestivalKeywordByFestivalId(int festivalId) throws Exception {
		
		ArrayList<FestivalKeywordDTO> list = festivalMapper.findFestivalKeywordByFestivalId(festivalId);
		
		return list;
		
	}
	
	/**
	 * 회원 페스티발 찜 관심없음 매핑 테이블에 삽입
	 * @param map1
	 * @throws Exception
	 */
	@Override
	public void insertUserLoveFestival(UserFestivalLoveHateMapDTO map1) throws Exception {
		
		System.out.println("insertUserLoveFestival impl");
		festivalMapper.insertUserLoveFestival(map1);
		
	}
	
	/**
	 * 회원 찜 관심없음 키워드 매핑이 존재하는 지 확인
	 * @param userMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public UserFestivalLoveHateMapDTO findUserMapByUserMap(UserFestivalLoveHateMapDTO userMap) throws Exception {
		
		UserFestivalLoveHateMapDTO userMap2 = festivalMapper.findUserMapByUserMap(userMap);
		
		return userMap2;
		
		
	}
	
	/**
	 * 유저 찜 관심없음 키워드 매핑 테이블에 삽입
	 * @param userMap
	 * @throws Exception
	 */
	@Override
	public void insertUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception {
		
		festivalMapper.insertUserKeywordMap(userMap);
		
	}
	
	/**
	 * 이미 존재하는 유저 찜 관심없음 키워드 매핑의 카운트를 증가시킴
	 * @param userMap
	 * @throws Exception
	 */
	@Override
	public void updateUserKeywordMap(UserFestivalLoveHateMapDTO userMap) throws Exception {
		
		festivalMapper.updateUserKeywordMap(userMap);
		
	}
	
	/**
	 * 회원이 찜한 축제 리스트를 반환
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<Integer> findLoveList(int userId) throws Exception {
		ArrayList<Integer> list = festivalMapper.findLoveList(userId);
		
		return list;
	}
	
	/**
	 * 회원이 찜한 축제 매핑을 삭제
	 * @param userMap1
	 * @throws Exception
	 */
	@Override
	public void deleteUserLoveFestival(UserFestivalLoveHateMapDTO userMap1) throws Exception {
		
		festivalMapper.deleteUserLoveFestival(userMap1);
		
	}
	
	/**
	 * 회원과 찜 /관심없음 키워드 매핑을 삭제
	 * @param userMap2
	 * @throws Exception
	 */
	@Override
	public void deleteUserKeywordMap(UserFestivalLoveHateMapDTO userMap2) throws Exception {
		festivalMapper.deleteUserKeywordMap(userMap2);
	}
	
	/**
	 * 회원이 관심없음 버튼을 누른 페스티벌을 매핑 테이블에 삽입
	 * @param map1
	 * @throws Exception
	 */
	@Override
	public void insertUserHateFestival(UserFestivalLoveHateMapDTO map1) throws Exception {
		festivalMapper.insertUserHateFestival(map1);
	}
	
	/**
	 * 회원이 관심없음을 한 축제 목록을 반환
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<Integer> findHateList(int userId) throws Exception {
		ArrayList<Integer> list = festivalMapper.findHateList(userId);
		
		return list;
	}
	
	/**
	 * 해당 축제의 컨텐트 키워드를 반환
	 * @param festivalId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ArrayList<FestivalContentReviewNaverKeywordMapDTO> findContentKeywordListByFestivalId(int festivalId) throws Exception {
		
		ArrayList<FestivalContentReviewNaverKeywordMapDTO> list = festivalMapper.findContentKeywordListByFestivalId(festivalId);
		
		return list;
		
	}
	
	@Override
	public TimeDTO findTimeIdByFestivalId(int festivalId) throws Exception {
		
		TimeDTO timeDTO = festivalMapper.findTimeIdByFestivalId(festivalId);
		
		return timeDTO;
		
	}
}
