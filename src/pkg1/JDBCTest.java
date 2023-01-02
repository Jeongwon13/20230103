package pkg1;
/*
 * 1. JDBC 프로그래밍 예제 작성
 * 1) DBMS를 만든 회사에서 무료로 제공해주는 드라이버 프로그램을 자바 프로젝트로 불러오기
 *  - ojdbc6.jar
 *   - 경로: C:\oraclexe\app\oracle\product\11.2.0\server\jdbc\lib
 */
//2. java.sql 패키지를 사용하기: 이유는 JDBC Driver Interface 프로그램을 사용하기 위함
// - 목적: 자바 프로그램과 JDBC Driver 프로그램을 연결
import java.sql.*;
public class JDBCTest {
	
	/*
	 * 새로운 메소드를 정의하기
	 *  - 기능: INSERT SQL문 작성 후 실행하기
	 *  - 이름: insert
	 *  - 매개변수
	 *  1) main() 메소드 내부에서 선언한 Connection 참조 변수인 con을 입력 받는 변수 선언
	 *   - Connection pcon
	 *    - pcon에서 p는 parameter(매개체) 약어
	 *  2) main() 메소드 내부에서 선언한 Statement 참조 변수인 st를 입력 받는 변수 선언
	 *   - Statement pst
	 */
	public boolean insert(Connection pcon, Statement pst) {
		//결과 값인 true or false 보관할 변수를 선언
		boolean r = false;
		String insertsql = "INSERT INTO newbook(bookid, bookname, publisher, price) VALUES(14,'배구','진웅출판사',1300)";
		try {
			//createStatement() 메소드 호출
			pst = pcon.createStatement();
			//executeUpdate() 메소드 호출
			int row = pst.executeUpdate(insertsql);
			System.out.println("INSERT SQL문을 실행한 결과 값:"+row);
			if(row >= 1) {
				System.out.println("데이터베이스에 새로운 데이터 저장 성공");
				r = true;
			}else {
				System.out.println("데이터베이스에 새로운 데이터 저장 실패");
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				/*
				 * 1. 주의사항: Connection pcon 매개변수를 사용해서 close() 메서드 호출해서는 안됨. 왜냐! main() 메소드에 있는 다른 명령어에 영향을 끼침)
				 * 2. Statement pst 매개변수를 사용해 close() 메소드를 호출
				 */
				if(pst != null) pst.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return r;
	}
	
	/*
	 * 2. SELECT SQL문만을 작성하고 실행하는 메소드를 정의
	 * 1) 이름: select
	 * 2) 매개변수를 선언
	 * 가. main() 메소드내부에서 선언한 Connection con 참조 변수를 입력 받는 변수를 선언
	 * 		- Connection pcon
	 * 나. main() 메소드 내부에서 선안한 Statement st 참조변수 입력변수
	 * -Statement pst
	 * 다. main() 메소드의 ResultSet rs참조변수 입력변수
	 * -ResultSet prs
	 *
	 */
	public void select(Connection pcon, Statement pst, ResultSet prs) {
		String q = "SELECT bookname FROM newbook";
		try {
			pst = pcon.createStatement();
			prs = pst.executeQuery(q);
			while(prs.next()) {
				String value = prs.getString("bookname");
				System.out.println("데이터베이스로부터 가져온 책 이름은 " + value);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(prs != null) prs.close();
				if(pst != null) pst.close();
				System.out.println("다 사용한 객체들을 시스템에 반환");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * UPDATE SQL문 하나 만을 작성하고 실행하는 메소드 정의
	 * 1. 이름: update
	 * 2. 매개변수들을 선언
	 * - INSERT SQL문과 동일한 명령어를 작성
	 *  - executeUpdate() 메소드를 호출
	 * 3. 실행할 UPDATE SQL문 구상하기
	 *  -예) newbook 테이블이 갖고 있는 4개의 컬럼 이름 중 하나 선택하기
	 *   - 컬럼의 의미(이름) 이해하기
	 *   - bookid: 책을 구분해주는 번호
	 *   - bookname: 책 이름
	 *   - publisher: 출판사 이름
	 *   - price: 가격
	 *    - 기본키는 나중에 다른 테이블과 연결할 때 사용할 수 있으므로 현재 값을 바꾸면 연결이 되지 않음.
	 *    - sql developer에서 SELECT bookname FROM newbook;을 실행하기
	 */
	public boolean update(Connection pcon, Statement pst) {
		boolean r = false;
		String q = "UPDATE newbook SET bookname = '피구' WHERE bookid = 15";
		try {
			pst = pcon.createStatement();
			int row = pst.executeUpdate(q);
			if(row == 1) r= true;
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pst != null) pst.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	
	/*
	 * DELETE SQL문 하나 만을 작성하고 실행하는 메소드 정의
	 */
	
	public static void main(String[] args) {
		//1) 오라클 DBMS에 정상적으로 연결이 되었을 때 연결 정보를 보관할 객체 참조 변수 선언
		Connection con=null;
		//2) 실행할 SQL문장을 문자열 형식으로 보관할 객체의 주소를 저장할 변수 선언(다형성!)
		//-모든 종류의 DML(Data Manipulation Language: 데이터 조작어)
		// -DML의 종류: SELECT, INSERT, UPDATE, DELETE
		Statement st=null;
		
		//SELECT 문장을 실행할 executeQuery() 메소드의 반환 값을 보관할 참조 변수를 선언
		//-이유: 오라클 DBMS에서 SELECT 문장을 실행한 결과를 보관할 인터페이스
		//ResultSet: Result(결과) + Set(집합)의 합성어
		//  - SELECT문장을 실행한 결과로는 없거나 하나의 행, 2개 이상의 행들
		ResultSet rs=null;
		
		//3) 실행할 SQL문장을 문자열 형식으로 보관할 변수를 선언 + 초기화
		String select = "SELECT bookname FROM newbook";
		String select2 = "SELECT bookid FROM newbook";
		String select3 = "SELECT * FROM newbook";
		String select4 = "SELECT bookid, bookname FROM newbook";
		
		
		//3. 드라이버 객체를 생성하는 명령어를 작성
		try {
			java.lang.Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("forName() 메소드를 정상적으로 실행함");
			System.out.println("프로그램에서 오라클 데이터베이스로 연결할 때");
			System.out.println("사용할 드라이버 객체를 생성함");
			
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"user1","1234");
					
			System.out.println("오라클 DBMS에 연결되었습니다.");
			
			//createStatement() 메소드를 호출: SQL문장을 보관할 객체를 생성
			// - SELECT, INSERT, UPDATE, DELETE문 모두 공통
			st = con.createStatement();
			
			//위에서 정상적으로 객체가 생성된 경우, 화면에 SQL 문장 저장 객체 생성 성공 메세지 표시
			System.out.println("SELECT SQL문장 저장 객체 생성 성공");
			
			//SELECT문을 실행하고 실행 결과를 ResultSet 객체에 담아오기
			// - 실행할 SELECT문장: SELECT bookname FROM newbook
			//rs = st.executeQuery(select);
			//rs = st.executeQuery(select2);
			rs = st.executeQuery(select3); //모든 컬럼(순서:CREATE TABLE 명령어 작성)
			//순서: sql developer에서 desc newbook; 명령어를 실행한 결과와 동일(맞추기)
			// - bookid, bookname, publisher, price
			// - 순서를 다르게 하고 싶음 SELECT price, publisher, bookid, bookname FROM newbook 이렇게 써야함.
			
			
			//rs = st.executeQuery(select4);
			/*
			 * 실행할 SELECT 문장을 바꾼 후, 주의사항
			 *  - 컬럼 이름과 자료형을 맞춰야 함.
			 *   - 예) bookid: CREATE TABLE에서 number 자료형을 사용 -> Integer
			 *   - 예) price: CREATE TABLE에서 number 자료형을 사용 -> Integer
			 *  --> while(rs.next() == true) {
			 *  		int 지역변수이름 = rs.getInt("bookid");
			 *  		--> int 자료형과 "bookid" 조심
			 *  }
			 */
			
			//executeQuery() 메소드가 정상적으로 실행된 경우에는 모니터에 "SELECT 문장 실행 성공" 메세지 표시
			System.out.println("SELECT 문장 실행 성공");
			
			/*
			 * ResultSet 인터페이스가 가리키는 객체 내부에는 cursor 객체가 존재
			 * -cursor 객체를 사용할 때 next() 메소드를 호출
			 *  -ResultSet 객체가 갖고 있는 행의 개수가 0이상일 수 있기 때문에
			 *   반복문과 함께 사용해야 합니다.
			 *   - 주로 while 반복문을 많이 사용: 이유 조건식 하나를 사용해서
			 *     반복 여부를 결정 가능(간소화)
			 *     - 형식
			 * while(rs.next() == true){
			 * 	 //특정 컬럼(열 또는 속성)이 갖고 있는 값을 가져오기
			 * 	 	//아래 String은 bookname 타입이 varchar2 이기 때문
			 *  	String 지역변수이름 = rs.getString("컬럼이름 또는 열 이름");
			 *  	//화면에 표시하기
			 *  	sysout("오라클 데이터베이스에서 가져온 책 이름:" + 지역변수이름);
			 * }
			 */
		
			while(rs.next()) {
				int value = rs.getInt("bookid");
				String value2 = rs.getString("bookname");
				String value3 = rs.getString(3);
				int value4 = rs.getInt(4);
				
				System.out.println("오라클 데이터베이스에서 가져온 책 id:" + value);
				System.out.println("오라클 데이터베이스에서 가져온 책 이름:" + value2);
				System.out.println("오라클 데이터베이스에서 가져온 책 출판사:" + value3);
				System.out.println("오라클 데이터베이스에서 가져온 책 가격:" + value4);
				System.out.println();
			}
			
			
		}catch(java.lang.ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			/*
			 * 다 사용한 객체를 힙 메모리 영역에서 없애기
			 *  - 운영체제에게 돌려주기(반환하기)
			 *  - Connection 객체, Statement 객체, ResultSet 객체가 갖고 있는 close() 메소드 호출하기
			 *   - 기본 순서: 제일 마지막에 생성된 객체부터 호출
			 *    - ResultSet 객체가 제일 마지막에 생성 -> rs.close();
			 *    - Statement 객체는 두 번째로 생성 -> st.close();
			 *    - 제일 마지막으로는 첫 번째로 생성한 Connection 객체 -> con.colse();
			 *     - 주의사항: SQLException 예외가 발생할 가능성이 있으므로
			 *      try~catch{} 내부에 작성
			 */
			try {
				//NullPointerException 예외 방지하기 위해 if문 사용
				if(rs != null) rs.close();
				if(st != null) st.close();
				if(con != null) con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		

	}
} 

















