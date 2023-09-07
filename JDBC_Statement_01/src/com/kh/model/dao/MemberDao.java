package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;

// DAO(Data Access Object) : DB에 직접적으로 접근해서 사용자의 요청에 맞는 sql문 실행 후 결과 반환 (JDBC)
//							 결과를 Controller로 다시 리턴
public class MemberDao {
	

		
		/*
		 * * JDBC용 객체
		 * - Connection : DB의 연결정보를 담고있는 객체
		 * - [Prepared]Statement : 연결된 DB에 SQL문을 전달해서 실행하고, 결과를 받아내는 객체
		 * - ResultSet : SELECT문 실행 후	조회된 결과물들이 담겨있는 객체
		 * 
		 * *JDBC 과정 (순서중요)
		 * 1) jdbc driver 등록 : 해당 DBMS(오라클)가 제공하는 클래스 등록
		 * 2) Connection 생성 : 연결하고자 하는 DB정보를 입력해서 해당 DB와 연결하면서 생성
		 * 3) Statement 생성 : Connection 객체를 이용해서 생성(SQL문 실행 및 결과받는 객체)
		 * 4) SQL문 전달하면서 실행 : Statement 객체를 이용해서 SQL문 실행
		 * 5) 결과받기
		 * 		> SELECT문 실행 => ResultSet객체 (조회된 데이터들이 담겨있음) => 6_1
		 * 		> DML문 실핼    => int(처리된 행의 수)
		 * 6_1) ResultSet에 담겨있는 데이터들을 하나씩 하나씩 뽑아서 vo객체에 순서대로 옮겨담기[+ArrayList에 담아줌]
		 * 						(vo객체 : 불러온 테이블 양식에 맞게 만들어진 객체)
		 * 6_2) 트랜잭션 처리 (성공했다면 commit, 실패했다면 rollback 실행)
		 * 7) 다 사용한 JDBC용 객체들 반드시 자원 반납 (close) => 생성된 역순으로
		 * 
		 * */
		
	/**
	 * 사용자가 입력한 정보들을 db에 추가시켜ㅓ주는 메서드	
	 * @param m : 사용자가 입력한 값들이 담겨있는 member객체
	 * @return : insert문 실행 후 처리된 행수
	 */
	public int insertMember(Member m) {
		// insert문 => 처리된 행수(int) => 트랜잭션 처리 필요
		
		// 필요한 변수들 먼저 셋팅
		int result = 0;		// 처리된 결과(행 수)를 받아줄 변수
		Connection conn = null;		// 연결된 db의 연결정보를 담는 객체
		Statement stmt = null;		// 완성된 sql문 전달해서 실행한 후에 결과를 받는 객체
		
		// 실행할 spl문
		// INSERT INTO MEMBER
		// VALUES(SEQ_USERNO.NEXTVAL, 'user01', 'pass01', '홍길동', null, 23, 
		//		'user01@iei.or.kr', '01022222222', '부산', '등산, 영화보기', '2021-08-02');
		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, "
				+ "'" + m.getUserId()   + "',"
				+ "'" + m.getUserPwd()  + "',"
				+ "'" + m.getUserName() + "',"
				+ "'" + m.getGender()   + "',"
					  + m.getAge()	    + ","
				+ "'" + m.getEmail()    + "',"
				+ "'" + m.getPhone()    + "',"
				+ "'" + m.getAddress()  + "',"
				+ "'" + m.getHobby()    + "', SYSDATE)";
		
		
		try {
			// 1) jdbc driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성 => db연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) sql문 전달하고 실행 후 결과받기 (처리된 행수)
			result = stmt.executeUpdate(sql);
			
			// 6) 트랜잭션 처리
			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 다 쓴 jdbc객체를 반환
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	
	public ArrayList<Member> selectList() {
		// select문(여러행 조회) => ResultSet객체 => ArrayList<Member> 에 담기
		
		//필요한 변수들 셋팅
		ArrayList<Member> list = new ArrayList<>();  //비어있는 상태
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;		// select문으로 조회된 결과값들이 최초로 담기는 객체
		
		// 실행한 sql문
		String sql = "SELECT * FROM MEMBER";
		
		
		try {
			// 1) JDBC 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) Connection 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			// 3) Statement 생성
			stmt = conn.createStatement();
			// 4,5) sql 실행 및 결과받기
			rset = stmt.executeQuery(sql);		
			// 6) ResultSet 데이터를 하나씩 꺼내서 Member 객체화 시킨후 컬렉션에 넣기
			while (rset.next()) {
				// 현재 참조하고 있는 행에대한 모든 데이터들을 한 member 객체에 담기
				Member m = new Member();
				m.setUserNo(rset.getInt("userno"));
				m.setUserId(rset.getString("userid"));
				m.setUserPwd(rset.getString("userpwd"));
				m.setUserName(rset.getString("username"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enrolldate"));
				// 컬렉션 list에 담기
				list.add(m);				
			}
			
			// 조회된 데이터가 없을 경우 (리스트 null)
			// 조회된 데이터가 있을 경우 (리스트 1개 이상)
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	public Member selectByUserId(String userId) {
		// select(1개) => ResultSet 객체 => Member
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		Member m = null;			
		String sql = "SELECT * FROM MEMBER WHERE USERID = '" + userId + "'";	
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);	
			
			if (rset.next()) {
				m = new Member(rset.getInt("userno"),
						rset.getString("userid"),
						rset.getString("userpwd"),
						rset.getString("username"),
						rset.getString("gender"),
						rset.getInt("age"),
						rset.getString("email"),
						rset.getString("phone"),
						rset.getString("address"),
						rset.getString("hobby"),
						rset.getDate("enrolldate"));	
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return m;
	}
			
					
	public ArrayList<Member> selectByUserName(String keyword) {
		// select(여러행) => ResultSet 객체 => ArrayList<Member> 객체에 담기
		
		ArrayList<Member> list = new ArrayList<>(); //텅빈 상태
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
				
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%+keyword+%'
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%"+keyword+"%'";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			while (rset.next()) {
				// 현재 참조하고 있는 행에대한 모든 데이터들을 한 member 객체에 담기
				Member m = new Member();
				m.setUserNo(rset.getInt("userno"));
				m.setUserId(rset.getString("userid"));
				m.setUserPwd(rset.getString("userpwd"));
				m.setUserName(rset.getString("username"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enrolldate"));
				// 컬렉션 list에 담기
				list.add(m);				
			}
				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
		
		
	
	public int updateMember(Member m) {
		// update문 => 처리된 행 수 (int) => 트랜잭션
		int result = 0;

		Connection conn = null;
		Statement stmt = null;
				
		// update member set userpwd = 'xx', email = 'xx', phone = 'xx', address = 'xx' where userid = 'xx';
		String sql = "update member set userpwd = '" + m.getUserPwd() + "', "
										+ "email = '" + m.getEmail() + "', phone = '" + m.getPhone() + "', "
										+ "address = '" + m.getAddress() + "' where userid = '" + m.getUserId() + "'";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;

	}
	
	public int deleteMember(String userId) {
		// delete문 => 처리된 행 수 => 트랜잭션처리
		
		int result = 0;
		
		Connection conn = null;
		Statement stmt = null;
		
		String sql =  "DELETE FROM MEMBER WHERE USERID = '" + userId + "'";
		
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","JDBC","JDBC");
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			
			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
