const BASE_URL =
  "http://localhost:8080";

export const login = async (
  loginData
) => {

  const response = await fetch(
    `${BASE_URL}/auth/login`,
    {
      method: "POST",

      headers: {
        "Content-Type":
          "application/json",
      },

      body: JSON.stringify(
        loginData
      ),
    }
  );

  if (!response.ok) {

    throw new Error(
      "로그인 실패"
    );

  }

  return response.text();
};

export const signup = async (
  signupData
) => {

  const response = await fetch(
    "http://localhost:8080/auth/signup",
    {
      method: "POST",

      headers: {
        "Content-Type":
          "application/json",
      },

      body: JSON.stringify(
        signupData
      ),
    }
  );

  if (!response.ok) {

    throw new Error(
      "회원가입 실패"
    );

  }

  return response.text();
};