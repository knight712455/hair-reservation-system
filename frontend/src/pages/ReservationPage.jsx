import { useState } from "react";

export default function ReservationPage() {

  const token = localStorage.getItem("token");

  console.log("토큰 확인:", token);

  if (!token) {

    window.location.href = "/";

    return null;
  }

  const [selectedDesigner, setSelectedDesigner] =
    useState(null);

  const [selectedDate, setSelectedDate] =
    useState("");

  const [selectedTime, setSelectedTime] =
    useState("");

  const [reservations, setReservations] =
    useState([]);

  const [timeSlots, setTimeSlots] =
    useState([]);

  const [loginUser] =
    useState({
      id: 1,
      name: "기사",
    });

const designers = [

  {
    id: 1,
    name: "민수",
    desc: "레이어드 / 남성펌 전문",

    startHour: 10,
    endHour: 18,

    holidays: ["SUN"],
  },

  {
    id: 2,
    name: "수아",
    desc: "단발 / 여성펌 전문",

    startHour: 12,
    endHour: 20,

    holidays: [],
  },

  {
    id: 3,
    name: "시온",
    desc: "다운펌 / 아이롱펌 전문",

    startHour: 11,
    endHour: 17,

    holidays: ["MON"],
  },

];
const generateDesignerTimeSlots = (
  designer
) => {

  if (!designer) {
    return [];
  }

  const slots = [];

  for (

    let hour = designer.startHour;

    hour < designer.endHour;

    hour++

  ) {

    slots.push(
      `${String(hour).padStart(2, "0")}:00`
    );

  }

  return slots;
};
const isHoliday = () => {

  if (
    !selectedDesigner ||
    !selectedDate
  ) {
    return false;
  }

  const date =
    new Date(selectedDate);

  const days = [
    "SUN",
    "MON",
    "TUE",
    "WED",
    "THU",
    "FRI",
    "SAT",
  ];

  const currentDay =
    days[date.getDay()];

  return selectedDesigner
    .holidays
    .includes(currentDay);
};
const formatDateTime = (
    
    
    
  dateTime
) => {

  return dateTime
    .replace("T", " ")
    .slice(0, 16);

};
const isReservedTime = (
  time
) => {

  return reservations.some((r) => {

    if (
      r.status === "CANCELED"
    ) {
      return false;
    }

    const reservationDate =
      r.slotStart.slice(0, 10);

    const reservationTime =
      r.slotStart.slice(11, 16);

    return (

      reservationDate ===
        selectedDate &&

      reservationTime ===
        time

    );

  });

};
const isPastTime = (
  time
) => {

  if (!selectedDate) {
    return false;
  }

  const today =
    new Date()
      .toISOString()
      .split("T")[0];

  if (
    selectedDate !== today
  ) {
    return false;
  }

  const now = new Date();

  const currentHour =
    now.getHours();

  const targetHour =
    parseInt(
      time.split(":")[0]
    );

  return targetHour <= currentHour;
};
  const loadTimeSlots = async (
  resourceId,
  date
) => {

  if (!resourceId || !date) {
    return;
  }

  try {

    const response = await fetch(
      `http://localhost:8080/reservations/timeslots?resourceId=${resourceId}&date=${date}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {

      console.log("타임슬롯 조회 실패");

      setTimeSlots([]);

      return;
    }

 const data =
  await response.json();

setTimeSlots(
  Array.isArray(data)
    ? data
    : []
);

  } catch (error) {

    console.log(error);

    setTimeSlots([]);

  }

};
  
const loadReservations = async (
  resourceId
) => {

  try {

    const response = await fetch(
      `http://localhost:8080/reservations?resourceId=${resourceId}&page=0&size=10`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {

      console.log("예약 조회 실패");

      setReservations([]);

      return;
    }

    const data =
      await response.json();

    setReservations(
  Array.isArray(data.content)
    ? data.content
    : []
);

  } catch (error) {

    console.log(error);

    setReservations([]);

  }

};
  const handleReservation = async () => {

    if (
      !selectedDesigner ||
      !selectedDate ||
      !selectedTime
    ) {
      alert("모든 항목을 선택해주세요.");
      return;
    }

    try {

      const start =
        `${selectedDate}T${selectedTime}:00`;

      const hour =
        parseInt(
          selectedTime.split(":")[0]
        );

      const end =
        `${selectedDate}T${
          String(hour + 1).padStart(2, "0")
        }:00:00`;

     const response = await fetch(
  "http://localhost:8080/reservations",
        {
          method: "POST",

     headers: {

  "Content-Type": "application/json",

  Authorization:
    `Bearer ${token}`,

},

          body: JSON.stringify({

           

            resourceId:
              selectedDesigner.id,

            start,
            end,

          }),
        }
      );

      let data = {};

try {

  data = await response.json();

} catch {

  data = {};

}

      if (!response.ok) {

        alert(
          data.message ||
          "예약 실패"
        );

        return;
      }

      alert("예약 완료");
await loadReservations(
  selectedDesigner.id
);

await loadTimeSlots(
  selectedDesigner.id,
  selectedDate
);
      console.log(data);

      setSelectedTime("");
      setSelectedDate("");

    } catch (error) {

  console.log(error);

  alert(error.message);

}

  };
const cancelReservation = async (
  reservationId
) => {

  try {

    const response = await fetch(

      `http://localhost:8080/reservations/${reservationId}/cancel`,

      {
  method: "PATCH",
  headers: {
    Authorization: `Bearer ${token}`,
  },
}

    );

    const text =
      await response.text();

    alert(text);

    loadReservations(
      selectedDesigner.id
    );

  } catch (error) {

    console.log(error);

    alert("취소 실패");

  }

};
const handleLogout = () => {

  localStorage.removeItem(
  "token",
);

  window.location.href = "/";
};
  return (

    <div className="min-h-screen bg-[#f5f1ee]">

      {/* 헤더 */}
      <header className="bg-white shadow-sm">

        <div className="flex items-center gap-4">

  <div className="text-sm text-gray-600">

    {loginUser?.name} 님

  </div>

<div className="flex gap-3">

  <button
    className="
      bg-black
      text-white
      px-4
      py-2
      rounded-xl
    "
  >

    예약내역

  </button>

  <button

    onClick={handleLogout}

    className="
      bg-red-500
      text-white
      px-4
      py-2
      rounded-xl
    "
  >

    로그아웃

  </button>

</div>

</div>

      </header>

      {/* 메인 */}
      <main className="max-w-6xl mx-auto px-6 py-10">

        {/* 소개 */}
        <section className="mb-12">

          <h2 className="text-5xl font-bold leading-tight mb-4">

            당신만의 스타일을
            <br />
            예약하세요

          </h2>

          <p className="text-gray-600 text-lg">

            원하는 디자이너를 선택하고
            편하게 예약하세요.

          </p>

        </section>

        {/* 전체 레이아웃 */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">

          {/* 디자이너 카드 */}
          <div className="lg:col-span-2">

            <section className="grid grid-cols-1 md:grid-cols-2 gap-6">

              {designers.map((designer) => (

                <div
                  key={designer.id}
                  onClick={() => {

  setSelectedDesigner(designer);

  loadReservations(designer.id);

}}
                  className={`
                    bg-white
                    rounded-2xl
                    p-6
                    shadow-sm
                    border-2
                    transition
                    cursor-pointer

                    ${
                      selectedDesigner?.id ===
                      designer.id
                        ? "border-black"
                        : "border-transparent"
                    }
                  `}
                >

                  <div className="h-56 bg-gray-200 rounded-xl mb-4"></div>

                  <h3 className="text-xl font-semibold mb-2">

                    디자이너 {designer.name}

                  </h3>

                  <p className="text-gray-500">

                    {designer.desc}

                  </p>

                </div>

              ))}

            </section>

          </div>

          {/* 예약 패널 */}
          <div>

            <div className="bg-white rounded-2xl p-6 shadow-sm sticky top-10">

              <h3 className="text-2xl font-bold mb-6">

                예약하기

              </h3>

              {!selectedDesigner ? (

                <p className="text-gray-500">

                  디자이너를 선택해주세요.

                </p>

              ) : (

                <>

                  {/* 선택 디자이너 */}
                  <div className="mb-6">

                    <p className="text-sm text-gray-400 mb-1">

                      선택 디자이너

                    </p>

                    <p className="text-lg font-semibold">

                      디자이너 {selectedDesigner.name}

                    </p>

                  </div>

                  {/* 날짜 선택 */}
                  <div className="mb-6">

                    <p className="text-sm text-gray-400 mb-2">

                      날짜 선택

                    </p>

                  <input
                    type="date"

                    min={
                    new Date()
                    .toISOString()
                    .split("T")[0]
  }

                    value={selectedDate}

onChange={(e) => {

  setSelectedDate(
    e.target.value
  );

  setSelectedTime("");

  loadTimeSlots(
    selectedDesigner?.id,
    e.target.value
  );

}}

  className="w-full border rounded-xl p-3"
/>

                  </div>
{isHoliday() && (

  <div className="mb-4 p-4 rounded-xl bg-red-100 text-red-700">

    해당 디자이너 휴무일입니다.

  </div>

)}
                  {/* 시간 선택 */}
                  <div>

                    <p className="text-sm text-gray-400 mb-3">

                      시간 선택

                    </p>

                    <div className="grid grid-cols-2 gap-3">

                      {Array.isArray(timeSlots) &&
  timeSlots
    .filter((slot) => {

    const hour =
      parseInt(
        slot.time.split(":")[0]
      );

    return (

      hour >=
        selectedDesigner.startHour

      &&

      hour <
        selectedDesigner.endHour

    );

  })

  .map((slot) => (

                        <button
                          key={slot.time}
                  disabled={

  isHoliday() ||

  !slot.available ||

  isPastTime(slot.time)

}

onClick={() =>
  setSelectedTime(slot.time)
}
                          className={`
                            py-3
                            rounded-xl
                            border
                            transition

                            ${
                              
                             !slot.available ||
isPastTime(slot.time)

  ? "bg-gray-300 text-gray-500 cursor-not-allowed"

  : selectedTime === slot.time

    ? "bg-black text-white"

    : "bg-white hover:bg-black hover:text-white"
                            }
                          `}
                        >

                          {slot.time}

                        </button>

                      ))}

                    </div>

                  </div>

                  {/* 예약 버튼 */}
                  <button
                    disabled={
                      !selectedDesigner ||
                      !selectedDate ||
                      !selectedTime
                    }
                    onClick={handleReservation}
                    className={`
                      w-full
                      py-4
                      rounded-xl
                      mt-8
                      font-semibold
                      transition

                      ${
                        !selectedDesigner ||
                        !selectedDate ||
                        !selectedTime
                          ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                          : "bg-black text-white hover:opacity-90"
                      }
                    `}
                  >

                    예약 확정

                  </button>

                </>

              )}

            </div>

          </div>

        </div>
{/* 예약 목록 */}
<section className="mt-16">

  <h2 className="text-3xl font-bold mb-6">

    예약 목록

  </h2>

  <div className="bg-white rounded-2xl shadow-sm overflow-hidden">

    <table className="w-full">

      <thead className="bg-gray-100">

        <tr>

          <th className="p-4 text-left">
            유저
          </th>

          <th className="p-4 text-left">
            디자이너
          </th>

          <th className="p-4 text-left">
            시작
          </th>

          <th className="p-4 text-left">
            종료
          </th>

          <th className="p-4 text-left">
            상태
            </th>
            <th className="p-4 text-left">
            관리
            </th>
        

        </tr>

      </thead>

      <tbody>
{Array.isArray(reservations) &&
  reservations.map((r) => (

    <tr
      key={r.id}

      className={`
        border-t

        ${
          r.status === "CANCELED"
            ? "opacity-50"
            : ""
        }
      `}
    >

      <td className="p-4">

        {r.userName}

      </td>

      <td className="p-4">

        {r.resourceName}

      </td>

      <td className="p-4">

        {formatDateTime(
          r.slotStart
        )}

      </td>

      <td className="p-4">

        {formatDateTime(
          r.slotEnd
        )}

      </td>

      <td className="p-4">

        <span
          className={`
            px-3
            py-1
            rounded-full
            text-sm
            font-semibold

            ${
              r.status === "CONFIRMED"

                ? "bg-green-100 text-green-700"

                : "bg-gray-200 text-gray-600"
            }
          `}
        >

          {
            r.status === "CONFIRMED"

              ? "예약완료"

              : "취소됨"
          }

        </span>

      </td>

      <td className="p-4">

        <button

          disabled={
            r.status === "CANCELED"
          }

          onClick={() =>
            cancelReservation(r.id)
          }

          className={`
            px-4
            py-2
            rounded-lg
            text-white

            ${
              r.status === "CANCELED"

                ? "bg-gray-400 cursor-not-allowed"

                : "bg-red-500 hover:opacity-90"
            }
          `}
        >

          취소

        </button>

      </td>

    </tr>

  ))}

</tbody>

    </table>

  </div>

</section>
      </main>

    </div>
  );
}