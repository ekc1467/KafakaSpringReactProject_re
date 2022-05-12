import axios from "axios";
import React,{useState} from "react";

function App() {
  const [num1, setNum1] = React.useState("");
  const [num2, setNum2] = React.useState("");
  const [idQ, setIdQ] = React.useState("");
  const [bscore, setBscore] = React.useState("");
  const [answer, setAnswer] = React.useState("");
  const [isDisable, setIsDisable] = useState(false);
  const getProblem = () => {
    axios.defaults.baseURL = "http://52.79.242.181:8080/Msa01";
    axios.get("/t1").then((res) => {
      console.log("/t1 then");
      setNum1(res.data.factorA);
      setNum2(res.data.factorB);
      setIdQ(res.data.idQ);
      setBscore(res.data.bscore);
    });
    setIsDisable(false);
  };

  const postAnswer = () => {
    let uId = document.getElementById("uId").value;
    let nickName = document.getElementById("nickName").value;
    let answer = document.getElementById("answer").value;
    axios.defaults.baseURL = "http://52.79.242.181:8080/Msa01/";
    console.log(uId);
    axios
      .post("/t2", {
        user: { alias: nickName, userid: uId },
        multiplication: { factorA: num1, factorB: num2 },
        resultAttempt: answer,
        idQ: idQ,
        bscore: bscore,
      })
      .then((res) => {
        let result = res.data ? "정답" : "오답";
        setAnswer(result);
      });
      setIsDisable(true);
  };

  return (
    <div className="App">
      <h2>문제</h2>
      <button onClick={getProblem}>문제요청</button>
      <p>
        <span>{num1}</span> * <span>{num2} = ?</span>
      </p>
      <form>
        <label htmlFor="uId">고유번호</label>
        <input id="uId" name="uId"></input>
        <label htmlFor="nickName">이름</label>
        <input id="nickName" name="nickName"></input>
        <label htmlFor="answer">정답</label>
        <input id="answer" name="answer"></input>
        <button type="button" onClick={postAnswer} disabled={isDisable}>
          제출
        </button>
      </form>
      <h3>결과</h3>
      <p>{answer}</p>
      <h4>수험번호 확인하세요🎁</h4>
      <table>
        <thead>
          <tr>
            <td>수험번호</td>
            <td>이름</td>
          </tr>
        </thead>
        <tbody>
          {user.map((list, index) => (
            <tr key={list.id}>
              <td>{list.id}</td>
              <td>{list.name}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const user = [
  {
    id: 0,
    name: "경승환",
  },
  {
    id: 1,
    name: "김미현",
  },
  {
    id: 2,
    name: "김아현",
  },
  {
    id: 3,
    name: "김익한",
  },
  {
    id: 4,
    name: "남궁윤호",
  },
  {
    id: 5,
    name: "방선호",
  },
  {
    id: 6,
    name: "변준혁",
  },
  {
    id: 7,
    name: "양원석",
  },
  {
    id: 8,
    name: "이권철",
  },
  {
    id: 9,
    name: "이동현",
  },
  {
    id: 10,
    name: "이윤성",
  },
  {
    id: 11,
    name: "이진선",
  },
  {
    id: 12,
    name: "임지연",
  },
  {
    id: 13,
    name: "장정우",
  },
  {
    id: 14,
    name: "정승엽",
  },
  {
    id: 15,
    name: "정안지",
  },
  {
    id: 16,
    name: "정연주",
  },
  {
    id: 17,
    name: "조영우",
  },
  {
    id: 18,
    name: "최호진",
  },
  {
    id: 19,
    name: "허예인",
  },
  {
    id: 20,
    name: "현지영",
  },
];

export default App;
