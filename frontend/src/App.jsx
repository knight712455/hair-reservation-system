import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ReservationPage from "./pages/ReservationPage";

function App() {

  const token =
    localStorage.getItem("token");

  return (

    <BrowserRouter>

      <Routes>

        {/* 로그인 */}
        <Route
          path="/"
          element={<Login />}
        />

        {/* 회원가입 */}
        <Route
          path="/signup"
          element={<Signup />}
        />

        {/* 예약 페이지 */}
        <Route
          path="/reservation"
          element={

            token

              ? <ReservationPage />

              : <Navigate to="/" />

          }
        />

      </Routes>

    </BrowserRouter>

  );
}

export default App;