import { useEffect, useState } from 'react';
import axios from 'axios';

const TestPage = function TestPageFunction() {
  const [testData, setTestData] = useState(null);

  useEffect(() => {
    const url = 'http://localhost:8080/api_test';
    async function getTestJson() {
      try {
        const response = await axios.get(url);
        const data = response.data;
        setTestData(data);
      } catch (e) {
        console.log(e);
        throw e;
      }
    }
    getTestJson();
  }, [setTestData]);

  return <>api 테스트 결과 : {testData ? testData : '실패'}</>;
};

export default TestPage;
