package Pack01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
class Multiplication {
	final int factorA;
	final int factorB;

	Multiplication() {
		this(0, 0);
	}
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
class User {
	String alias;
	int userid;
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
class MultiplicationResultAttempt {
	User user; // ������
	Multiplication multiplication; // ����
	int resultAttempt; // ���
	String idQ;
	int bscore;
}

@Service
class MultiplicationServiceImpl {
	// ���� ��������
	public Multiplication createRandomMultiplication() {
		Random rnd = new Random();
		int factorA = rnd.nextInt(100);
		int factorB = rnd.nextInt(100);
		return new Multiplication(factorA, factorB);
	}

	// ä�� ����
	boolean checkAttempt(MultiplicationResultAttempt resultAttempt) {
		return resultAttempt.getResultAttempt() == resultAttempt.multiplication.getFactorA()
				* resultAttempt.multiplication.getFactorB();
	}
}

@Getter
@Setter
@AllArgsConstructor
class Question {
	String idQ;
	int factorA;
	int factorB;
	int result;
	int Bscore;
}

class QuestionDAO {
	Connection con = null;
	PreparedStatement pstmt = null;

	void connectDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://react200.crmdy3g4qrsf.us-east-1.rds.amazonaws.com:3306/0425?&useSSL=false";
			con = DriverManager.getConnection(url, "admin", "1q2w3e4r");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void close() {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	int insertQuestion(Question q) {
		connectDB();
		// sql���� �����ϱ�
		int n = 0;
		try {
			String sql = "insert into questions values(?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, q.getIdQ());
			pstmt.setInt(2, q.getFactorA());
			pstmt.setInt(3, q.getFactorB());
			pstmt.setInt(4, q.getResult());
			pstmt.setInt(5, q.getBscore());

			n = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return n;
	}

	Question selectQuestion(String idQ) {
		connectDB();
		Question question = null;
		ResultSet rs = null;
		try {
			String sql = "select * from Questions where id_q = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idQ);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				question = new Question(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return question;
	}
}

@CrossOrigin(origins = "*")
@RestController
//@RequiredArgsConstructor
@Service
public class Tiger {

	@Autowired
	MultiplicationServiceImpl ms;

	@RequestMapping("/t1")
	@CrossOrigin(origins = "*")
	Question f1() {
//		Multiplication m = ms.createRandomMultiplication();
//		System.out.println(m.getFactorA());
//		System.out.println(m.getFactorB());
		Multiplication multiplication = ms.createRandomMultiplication();
		String makedQID = new SimpleDateFormat("YYMMddHHmmssms").format(System.currentTimeMillis());
		QuestionDAO questionDAO = new QuestionDAO();
		int factorA = multiplication.getFactorA();
		int factorB = multiplication.getFactorB();
		int result = factorA * factorB;
		int Bscore = (factorA / 10) + (factorB / 10);
		
		Question question = new Question(makedQID, factorA, factorB, result, Bscore);
		questionDAO.insertQuestion(question);
		return question;
	}


	/*
	private final KafkaProducer kafkaProducer;
    
	@RequestMapping("/t2")
	@CrossOrigin(origins = "*")
	boolean f2(@RequestBody MultiplicationResultAttempt mul) {
		System.out.println(mul);
		System.out.println(mul.user.userid);
		System.out.println(mul.user.alias);
		System.out.println(mul.multiplication.factorA);
		System.out.println(mul.multiplication.factorB);
		System.out.println(mul.resultAttempt);
		System.out.println(mul.idQ);
		System.out.println(mul.bscore);

		System.out.println("f2: Successful Sending!!");

		boolean checkAttempt = ms.checkAttempt(mul);

		Gson gson = new Gson();
		String muljsonString = gson.toJson(mul);
		if (checkAttempt) {
		kafkaProducer.sendMessgae(muljsonString);
		}
		

		return checkAttempt;
	}
	*/
	private static final String TOPIC = "kwon";
    private final KafkaTemplate<String, QuestionDTO> kafkaTemplate;
    
    @Autowired
    public Tiger(KafkaTemplate<String, QuestionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
	
//    public void sendMessage(String message) {
//
//    	QuestionDTO questionDTO = QuestionDTO.builder()
//                .idQ("2021-01-01")
//                .factorA("10300000033")
//                .factorB("ipgoNo")
//                .diff(100)
//                .Bscore()
//                .build();
//
//        // Send a message
//        kafkaTemplate.send(TOPIC, stockChange);
//    }
    @RequestMapping("/t2")
	@CrossOrigin(origins = "*")
	boolean f2(@RequestBody MultiplicationResultAttempt mul) {
		System.out.println(mul);
		System.out.println(mul.user.userid);
		System.out.println(mul.user.alias);
		System.out.println(mul.multiplication.factorA);
		System.out.println(mul.multiplication.factorB);
		System.out.println(mul.resultAttempt);
		System.out.println(mul.idQ);
		System.out.println(mul.bscore);

		System.out.println("f2: Successful Sending!!");

		boolean checkAttempt = ms.checkAttempt(mul);
//
//		Gson gson = new Gson();
//		String muljsonString = gson.toJson(mul);
		
    	QuestionDTO questionDTO = QuestionDTO.builder()
    			.user(mul.user)
    			.idQ(mul.idQ)
		    	.resultAttempt(mul.resultAttempt)
		    	.bscore(mul.bscore)
			     .build();
		if (checkAttempt) {
			kafkaTemplate.send(TOPIC, questionDTO);
		}
		

		return checkAttempt;
	}
	
	
	

//	
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//	
//	@RequestMapping("/t2")
//	@CrossOrigin(origins="*")
//	boolean f2(@RequestBody MultiplicationResultAttempt mul) {
//		System.out.println(mul);
//		System.out.println(mul.user.userid);
//		System.out.println(mul.user.alias);
//		System.out.println(mul.multiplication.factorA);
//		System.out.println(mul.multiplication.factorB);
//		System.out.println(mul.resultAttempt);
//		System.out.println(mul.idQ);
//		System.out.println(mul.bscore);
//		
//		System.out.println("f2");
//		
//		boolean checkAttempt = ms.checkAttempt(mul);
//		if (checkAttempt) {
//			rabbitTemplate.convertAndSend("multiple", "mulResult", mul);
//		}
//		return checkAttempt;
//	}

//	@RequestMapping("/results/{id}")
//	@CrossOrigin(origins="*")
//	Question f3(@PathVariable String id) {
//		QuestionDAO questionDAO = new QuestionDAO();
//		Question question = questionDAO.selectQuestion(id);
//		return question;
//	}

}

//class KafkaRun_ {
//	MultiplicationResultAttempt mul;
//
//	KafkaRun_(MultiplicationResultAttempt mul1) {
//		this.mul = mul1;
//	}
//
//	Gson gson = new Gson();
//	String muljsonString = gson.toJson(mul);
//
//	@Bean
//	public NewTopic topic() {
//		return TopicBuilder.name("topic1").partitions(10).replicas(1).build();
//	}
//
//	@Bean
//	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
//		System.out.println("전송");
//		return args -> {
//			template.send("topic1", muljsonString);
//		};
//	}
//}




//@Service
//@RequiredArgsConstructor
//class KafkaProducer {
//    private static final String TOPIC = "kwon";
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendMessgae(String msg) {
//        System.out.println(String.format("Produce message : %s", msg));
//        kafkaTemplate.send(TOPIC, msg);
//    }
//}
