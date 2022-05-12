package Pack01;

import java.io.IOException;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.*;


@RestController
@CrossOrigin(origins="*")
public class DC {
	@RequestMapping("/")
	public ArrayList<ResultDB>f0(HttpServletRequest req) {
        ResultDAO resultDAO = new ResultDAO();
		ArrayList<ResultDB> resultList = resultDAO.selectResult();
		req.setAttribute("resultList", resultList);
		System.out.println("t0 call");
		return resultList;
	}
	
	@RequestMapping("/t1")
	public String f1() {
		System.out.println("t1 call");
		//return "DCView";
		return "redirect:/";
	}
	
	
}

class ResultDAO {

	Connection con = null;
	PreparedStatement pstmt = null;
	Connection connectDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://react200.crmdy3g4qrsf.us-east-1.rds.amazonaws.com:3306/0425?&useSSL=false";
				con = DriverManager.getConnection(url, "admin", "1q2w3e4r");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
		
	}
	
	void close() {
		try {
			if(pstmt != null) {
				pstmt.close();				
			}
			if(con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	int insertResult(QuestionDTO mul) {
		connectDB();
		//sql���� �����ϱ�
		int n = 0;
		try {
			String sql = "update mulranking set Dscore= Dscore+10, "
					+ "Bscore = Bscore+?, "
					+ "Tscore=+Bscore+Dscore where Uid = ? and name=?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, mul.getBscore());
			pstmt.setInt(2, mul.getUser().getUserid() );
			pstmt.setString(3, mul.getUser().getAlias() );

			n = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return n;
	}
	
	ArrayList<ResultDB> selectResult() {
		connectDB();
		ArrayList<ResultDB> resultArray = new ArrayList<>();
		ResultSet rs = null;
		try {
			String sql = "select * from mulranking order by Tscore desc;";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {				
				String name = rs.getString(2);
				int Dscore = rs.getInt(3);
				int Bscore = rs.getInt(4);
				int Tscore = rs.getInt(5);
				resultArray.add(new ResultDB(name,Dscore, Bscore, Tscore));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		return resultArray;
	}
}


//
//
//@Component
//class RecvMulResult {
//
//	
//	@KafkaListener(
//            topics = "jinmin",
//            groupId = "foo"
//    )
//	public void listen(String msg) throws IOException {
//        System.out.println(String.format("Consumed message : %s", msg));
//    
//
//   	Gson gson = new Gson();
//   	MultiplicationResultAttempt mulRA = gson.fromJson(msg, MultiplicationResultAttempt.class);
//   	System.out.println(mulRA);
//   	
//    ResultDAO resultDAO = new ResultDAO();
//    resultDAO.insertResult(mulRA);
//   	
//   }
//
//	
//   
//}


//@Service
//class KafkaConsumer {
//
//    @KafkaListener(
//            topics = "kwon",
//            groupId = "foo"
//    )
//    public void listen(String msg) throws IOException {
//        System.out.println(String.format("Consumed message : %s", msg));
//    
//
//   	Gson gson = new Gson();
//   	MultiplicationResultAttempt mulRA = gson.fromJson(msg, MultiplicationResultAttempt.class);
//   	System.out.println(mulRA);
//   	
//    ResultDAO resultDAO = new ResultDAO();
//    resultDAO.insertResult(mulRA);
//   	
//   }
//}

@Service
class KafkaConsumer {

	 @KafkaListener(topics = "kwon", groupId = "foo", containerFactory = "stockChangeListener")
	 public void consume(QuestionDTO questionDTO) {
	        System.out.printf("Consumed message : %s%n", questionDTO);
	        //MultiplicationResultAttempt mulRA = gson.fromJson(msg, MultiplicationResultAttempt.class);
	        //QuestionDTO question = new QuestionDTO();
	        
	      ResultDAO resultDAO = new ResultDAO();
	      resultDAO.insertResult(questionDTO);
	    }
}




//@Component
//class RecvMulResult {
//   @RabbitListener(
//	         bindings = @QueueBinding(
//	               exchange = @Exchange(name = "multiple", type = ExchangeTypes.TOPIC),
//	               value = @Queue(name = "mulResult"), 	// �޴�Ű
//	               key = "mulResult")       			// �ִ�Ű
//	         )
//	   
//	    public void receiver(MultiplicationResultAttempt mulRA) {
//	    	System.out.println("������� ����");
//	        System.out.println(mulRA.toString());
//	        ResultDAO resultDAO = new ResultDAO();
//	        resultDAO.insertResult(mulRA);
//	        
//	   }
//}

@RequiredArgsConstructor
@Getter
@ToString
class Multiplication{
	final int factorA;
	final int factorB;
	Multiplication(){
		this(0,0);
	}
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
class User{
	String alias;
	int userid;
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
class MultiplicationResultAttempt{
	User user;						// ������
	String idQ;	// ����
	int resultAttempt;				// ���
	int bscore;
}

@AllArgsConstructor
@Getter
@Setter
class ResultDB {
	String name;
	int Dscore;
	int Bscore;
	int Tscore;
}