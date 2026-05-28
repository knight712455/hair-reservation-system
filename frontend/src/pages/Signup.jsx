import { useState } from "react";
import { signup } from "../api/authApi";
import { Link } from "react-router-dom";
function Signup() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phone, setPhone] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();

    const userData = {
      name: name,
      email: email,
      password: password,
      phone: phone,
    };

    try {
      const result = await signup(userData);

      console.log("회원가입 성공:", result);
      alert("회원가입 성공!");
    } catch (error) {
      console.error("회원가입 실패:", error);
      alert("회원가입 실패. 서버 주소나 입력값을 확인해주세요.");
    }
  };

  return (
    <div>
      <h1>회원가입</h1>

      <form onSubmit={handleSignup}>
        <div>
          <label>이름</label>
          <br />
          <input
            type="text"
            placeholder="이름을 입력하세요"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>

        <br />

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

        <div>
          <label>전화번호</label>
          <br />
          <input
            type="text"
            placeholder="01012345678"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
          />
        </div>

        <br />

        <button type="submit">회원가입</button>
        <p className="mt-4 text-sm">

  이미 계정이 있으신가요?

  <Link
    to="/"
    className="
      ml-2
      text-blue-500
    "
  >
    로그인
  </Link>

</p>
      </form>
    </div>
  );
}

export default Signup;