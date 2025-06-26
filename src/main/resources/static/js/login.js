document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".login-form");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("userId").value;
    const password = document.getElementById("password").value;

    try {
      const res = await fetch("/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) throw new Error("로그인 실패");

      const data = await res.json();
      localStorage.setItem("token", data.token);

      alert("로그인 성공!");
      window.location.href = "home.html";
    } catch (err) {
      alert("로그인에 실패했습니다. ID 또는 비밀번호를 확인해주세요.");
    }
  });
});
