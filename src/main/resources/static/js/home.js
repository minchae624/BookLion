document.addEventListener("DOMContentLoaded", async () => {
  const welcomeMsg = document.getElementById("welcome-msg");
  const logoutBtn = document.getElementById("logout-btn");
  const mypageBtn = document.getElementById("mypage-btn");

  const token = localStorage.getItem("token");

  // 사용자 정보 가져오기
  if (token) {
    try {
      const res = await fetch("/api/users/me", {
        method: "GET",
        headers: {
          "Authorization": "Bearer " + token
        }
      });

      if (res.ok) {
        const user = await res.json();
        welcomeMsg.textContent = `환영합니다. ${user.username}님`;
      } else {
        welcomeMsg.textContent = "환영합니다.";
      }
    } catch (err) {
      console.error("사용자 정보 조회 실패:", err);
      welcomeMsg.textContent = "환영합니다.";
    }
  } else {
    welcomeMsg.textContent = "환영합니다.";
  }

  // 로그아웃 처리
  logoutBtn.addEventListener("click", () => {
    localStorage.removeItem("token");
    alert("로그아웃되었습니다.");
    window.location.href = "/";
  });

  // 마이페이지 이동
  mypageBtn.addEventListener("click", () => {
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }
    window.location.href = "/mypage";
  });
});
