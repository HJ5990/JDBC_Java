package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

// Controller : view를 통해서 사용자가 요청한 기능에 대해서 처리하는 담당
// 				해당 메서드로 전달된 데이터 [가공처리후] dao로 전달하며 호출
//				dao로부터 반환받은 결과에 따라서 성공, 실패 판단 후 응답화면 결정 (view 메서드 호출)
public class MemberController {
	
	
	/**
	 * 사용자의 회원추가요청을 처리해주는 메서드
	 * @param userId ~ hobby : 사용자가 입력했던 정보들이 담겨있는 매개변수
	 */
	public void insertMember(String userId, String userPwd, String userName, String gender, 
								String age, String email, String phone, String address, String hobby) {
		Member m = new Member(userId,userPwd,userName,gender,Integer.parseInt(age),email,phone,address,hobby);
		int result = new MemberService().insertMember(m);
		
		if (result > 0) {
			new MemberMenu().displaySuccess("성공적으로 회원을 추가하였습니다");
		} else {
			new MemberMenu().displayFail("회원 추가에 실패하였습니다");
		}
	}

	
	/**
	 * 회원 전제 조회
	 */
	public void selectList() {
		ArrayList<Member> list= new MemberService().selectList();
		
		if (list.isEmpty()) {
			new MemberMenu().displayNoData("전체 조회 결과가 없습니다");
		} else {
			new MemberMenu().displayMemberList(list);
		}
	
		
		
	}
	
	
	/**
	 * 사용자의 아이디로 회원 검색요청을 처리해주는 메서드
	 * @param userId : 사용자가 입력한 검색하고자 하는 회원아이디
	 */
	public void selectByUserId(String userId) {
		Member m = new MemberService().selectByUserId(userId);
		
		if (m != null) {
			new MemberMenu().displayMember(m);
		} else {
			new MemberMenu().displayNoData(userId + "에 해당하는 회원이 없습니다");
		}
	}
	
	
	/**
	 * 이름으로 키워드로 검색 요청시 처리해주는 메서드
	 * @param keyword : 사용자가 입력한 검색할 키워드
	 */
	public void selectByUserName(String keyword) {
		ArrayList<Member> list = new MemberService().selectByUserName(keyword);
		if (list.isEmpty()) {
			new MemberMenu().displayNoData(keyword + "에 해당하는 검색결과가 없습니다");
		} else {
			new MemberMenu().displayMemberList(list);
		}
	
	}
	
	
	/**
	 * 회원 정보 수정
	 * @param userId
	 * @param userPwd
	 * @param email
	 * @param phone
	 * @param address
	 */
	public void updateMember(String userId, String userPwd, 
							String email, String phone, String address) {
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		m.setEmail(email);
		m.setPhone(phone);
		m.setAddress(address);
		int result = new MemberService().updateMember(m);
		
		if (result > 0) {
			new MemberMenu().displaySuccess("수정이 완료되었습니다.");
		} else {
			new MemberMenu().displayFail("수정에 실패하였습니다.");
		}
		
	}
	
	
	/**
	 * 회원 삭제
	 * @param userId
	 */
	public void deleteMember(String userId) {
		int result = new MemberService().deleteMember(userId);
		if (result > 0) {
			new MemberMenu().displaySuccess(userId + " 회원이 정상적으로 삭제되었습니다");
		} else {
			new MemberMenu().displayFail(userId + " 회원 삭제 실패");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
