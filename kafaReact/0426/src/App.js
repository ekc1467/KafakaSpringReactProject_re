import axios from 'axios';
import React, {useEffect} from 'react';

const App = () => {
  const [resultTagList, setResultTagList] = React.useState([]);

  const testReq = () => {
    axios.defaults.baseURL = 'http://52.79.242.181:8080/MulRank/';
    axios.get("/")
    .then((res) => {
      console.log(res.data);
      setResultTagList(res.data);
      //test(res.data)

    })
  }

  useEffect(()=>{
    console.log(11111111);
    testReq();
  }, [])


  
  // async function test(recently){
  //   axios.defaults.baseURL = 'http://localhost:8081';
  //   let temp = recently.map(async (rec)=> {
  //     let res = await axios.post("/results/" + rec.idQ)
  //     let newtag = (<tr key={rec.id}>
  //       <td>{rec.name}</td>
  //       <td>{res.data.factorA} * {res.data.factorB}</td>
  //       <td>{rec.resultAttempt}</td>
  //       <td>{"정답"}</td>
  //     </tr>)
  //     return newtag
  //   })
  //   let acc = await Promise.all(temp)
    
  //   setResultTagList(acc)

  // }


  return (
    <div>
      

      <table>
        <thead>
          <tr>
            <th>순위</th>
            <th>이름</th>
            <th>획득 점수</th>
            <th>보너스 점수</th>
            <th>총 점수</th>
          </tr>
        </thead>
        <tbody>
          {/* //{resultTagList} */}
          {resultTagList.map((list, index)=>(
              <tr key={index}>
                <td>{index+1}</td>
                <td>{list.name}</td>
                <td>{list.dscore}</td>
                <td>{list.bscore}</td>
                <td>{list.tscore}</td>
              </tr>))}
        </tbody>
      </table>
    </div>
  );
};

export default App;