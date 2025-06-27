document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("로그인이 필요합니다.");
    window.location.href = "login.html";
    return;
  }

  const userIdField = document.getElementById("user-id");
  const passwordField = document.getElementById("user-password");
  const emailField = document.getElementById("user-email");
  const logoutBtn = document.getElementById("logout-btn");
  const mypageBtn = document.getElementById("mypage-btn");
  const welcomeMsg = document.getElementById("welcome-msg");

  // 정보 조회
  fetch("/api/users/me", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((res) => res.json())
    .then((data) => {
      userIdField.value = data.username;
      welcomeMsg.textContent = `환영합니다. ${data.username} 님`;
      passwordField.value = "**********";
      emailField.value = data.email;
    })
    .catch(() => {
      alert("회원정보를 불러오지 못했습니다.");
    });


  // 비밀번호 수정
  document.getElementById("update-password-btn").addEventListener("click", async () => {
    const newPassword = passwordField.value;
    if (!newPassword) return alert("새 비밀번호를 입력하세요.");

    try {
      const res = await fetch("/api/users/me", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ password: newPassword }),
      });

      if (!res.ok) throw new Error();
      alert("비밀번호가 수정되었습니다.");
    } catch {
      alert("비밀번호 수정 실패");
    }
  });

  // 이메일 수정
  document.getElementById("update-email-btn").addEventListener("click", async () => {
    const newEmail = emailField.value;
    if (!newEmail) return alert("이메일을 입력하세요.");

    try {
      const res = await fetch("/api/users/me", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ email: newEmail }),
      });

      if (!res.ok) throw new Error();
      alert("이메일이 수정되었습니다.");
    } catch {
      alert("이메일 수정 실패");
    }
  });

  // 회원 탈퇴
  document.getElementById("delete-account-btn").addEventListener("click", async () => {
    if (!confirm("정말 탈퇴하시겠습니까?")) return;

    try {
      const res = await fetch("/api/users/delete", {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error();
      alert("회원 탈퇴가 완료되었습니다.");
      localStorage.removeItem("token");
      window.location.href = "main.html";
    } catch {
      alert("회원 탈퇴 실패");
    }
  });

    // 로그아웃 처리
  logoutBtn.addEventListener("click", () => {
    localStorage.removeItem("token");
    alert("로그아웃되었습니다.");
    window.location.href = "main.html";
  });

  // 강제로 새로고침 포함 재이동
  mypageBtn.addEventListener("click", () => {
  window.location.href = "mypage.html"; 
});

});
