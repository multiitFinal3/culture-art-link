package com.multi.culture_link.festival.service;

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
	
	@Override
	public ArrayList<Integer> findLoveList(int userId) throws Exception {
		ArrayList<Integer> list = festivalMapper.findLoveList(userId);
		
		return list;
	}
}
