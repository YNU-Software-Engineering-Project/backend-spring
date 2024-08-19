import React, { useState, FormEvent  } from "react";

type Message = string | boolean;

const SignUpTest = function SignUpTestFunction(){
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [nickname, setNickname] = useState<string>("");
  const [message, setMessage] = useState<Message>(""); //상태 체크

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => { // 회원 가입 처리 로직 구현.
    event.preventDefault(); //폼 제출 시 페이지 리로드 방지
    await new Promise((r) => setTimeout(r, 1000));
    try{
      const response = await fetch(
        'http://localhost:8080/api/users',
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email: email.trim(),
          nickname: nickname.trim(),
          password: password.trim(),

          }),
        }
      );
      const data = await response.json(); // JSON 응답을 처리.

      if (response.status === 200) {
        setMessage("회원가입 성공");
        console.log("회원가입 성공, 이메일주소:" + email);
      } else {
        if (response.status === 400) {
          setMessage(`Validation Failed: ${data.message}`);
        } else if (response.status === 401) {
          setMessage(`Unauthorized: ${data.message}`);
        } else if (response.status === 500) {
          setMessage(`Server Error: ${data.message}`);
        }
         else {
          setMessage(`Error: ${data.message}`);
        }
      }
    }catch (error) {
      console.error('Fetch error:', error);
    }
  };

  return (
    <div className="submit-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h1>회원가입</h1>
        <label htmlFor="username">이메일</label>
        <input
          type="text"
          id="username"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <label htmlFor="nickname">닉네임</label>
        <input
          type="text"
          id="nickname"
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
        />

        <label htmlFor="password">비밀번호</label>
        <input
          type="password"
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
         {message && (
        <label  style={{color: "red"}}>{message}</label>
        )}

        <button type="submit">회원 가입</button>
      </form>
    </div>
  );
};

export default SignUpTest;
