import { useState } from "react";
import { login } from "../api/authApi";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

const navigate = useNavigate();
 const handleLogin = async (e) => {

  e.preventDefault();

  try {

const result = await login({

  email,
  password,

});

console.log(result);

const data =
  typeof result === "string"
    ? JSON.parse(result)
    : result;

localStorage.setItem(
  "token",
  data.token
);

console.log(
  "저장된 토큰:",
  data.token
);

window.location.href =
  "/reservation";

alert("로그인 성공");

navigate("/reservation");
   

  } catch (error) {

    alert("로그인 실패");

  }

};

  return (
    <div>
      <h1>로그인</h1>

      <form onSubmit={handleLogin}>
        <div>
          <label>이메일</label>
          <br />
          <input
            type="email"
            placeholder="이메일을 입력하세요"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>

        <br />

        <div>
          <label>비밀번호</label>
          <br />
          <input
            type="password"
            placeholder="비밀번호를 입력하세요"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <br />

        <button type="submit">로그인</button>
        <p className="mt-4 text-sm">

  계정이 없으신가요?

  <Link
    to="/signup"
    className="
      ml-2
      text-blue-500
    "
  >
    회원가입
  </Link>

</p>
      </form>
    </div>
  );
}

export default Login;