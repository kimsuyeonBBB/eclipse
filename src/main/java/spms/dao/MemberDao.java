package spms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import spms.vo.Member;

public class MemberDao {
	//MemberDao에서는 ServletContext에 접근할 수 없다.
	//때문에 외부로부터 Connection 객체를 주입받기 위한 셋터 메서드와 인스턴스 변수를 준비하였다.
	Connection connection;
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	//selectList()의 목적은 데이터베이스로부터 회원정보를 가져와서 값 객체 Member에 담고, 그 Member 객체의 목록을 컨트롤러에게 반환하는 것이다.
	//selectList()의 리턴 타입이 List 인터페이스인 것을 주목해야한다.
	//실제로 리턴하는 것은 ArrayList 객체이지만 리턴 타입은 List이다. (프로그래밍의 유연성 때문에 가능한 일)
	public List<Member> selectList() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT MNO,MNAME,EMAIL,CRE_DATE" + " FROM MEMBERS" + " ORDER BY MNO ASC");
			
			ArrayList<Member> members = new ArrayList<Member>();
			
			while(rs.next()) {
				members.add(new Member()
						.setNo(rs.getInt("MNO"))
						.setName(rs.getString("MNAME"))
						.setEmail(rs.getString("EMAIL"))
						.setCreatedDate(rs.getDate("CRE_DATE")));
			}
			return members;
			
		} catch(Exception e) {
			throw e;
		} finally {
			try {if(rs != null) rs.close();} catch(Exception e) {}
			try {if(stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	
	//회원 등록
	public int insert(Member member) throws Exception{
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement("INSERT INTO MEMBERS(EMAIL,PWD,MNAME,CRE_DATE,MOD_DATE)" + " VALUES (?,?,?,NOW(),NOW())");
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getPassword());
			stmt.setString(3, member.getName());
			return stmt.executeUpdate();
		} catch(Exception e) {
			throw e;
		} finally {
			try {if(stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	
	//회원 삭제
	public int delete(int no) throws Exception{
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			return stmt.executeUpdate("DELETE FROM MEMBERS WHERE MNO=" + no);
		
		} catch(Exception e) {
			throw e;
		
		} finally {
			try {if(stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	
	//회원 상세정보 조회
	public Member selectOne(int no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT MNO,EMAIL,MNAME,CRE_DATE FROM MEMBERS" + " WHERE MNO=" + no);
			if(rs.next()) {
				return new Member()
						.setNo(rs.getInt("MNO"))
						.setEmail(rs.getString("EMAIL"))
						.setName(rs.getString("MNAME"))
						.setCreatedDate(rs.getDate("CRE_DATE"));
			} else {
				throw new Exception("");
			}
		} catch(Exception e) {
			throw e;
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	
	//회원 정보 변경
	public int update(Member member) throws Exception{
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("UPDATE MEMBERS SET EMAIL=?,MNAME=?,MOD_DATE=now()" + " WHERE MNO=?");
			stmt.setString(1, member.getEmail());
			stmt.setString(2, member.getName());
			stmt.setInt(3, member.getNo());
			return stmt.executeUpdate();
		} catch(Exception e) {
			throw e;
		} finally {
			try {if(stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	
	//회원 정보가 있으면 Member 객체 리턴, 없으면 null 리턴
	public Member exist(String email, String password) throws Exception{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = connection.prepareStatement("SELECT MNAME,EMAIL FROM MEMBERS" + " WHERE EMAIL=? AND PWD=?");
			stmt.setString(1, email);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return new Member()
						.setName(rs.getString("MNAME"))
						.setEmail(rs.getString("EMAIL"));
			} else {
				return null;
			}
		} catch(Exception e) {
			throw e;
		} finally { 
			try {if(rs != null) rs.close();} catch(Exception e) {}
			try {if(stmt != null) stmt.close();} catch(Exception e) {} 
		}
	}

}
