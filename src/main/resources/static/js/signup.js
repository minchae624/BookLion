document.addEventListener("DOMContentLoaded", () => {
  const idInput = document.getElementById("signup-id");
  const checkBtn = document.getElementById("check-duplicate");
  const form = document.querySelector(".signup-form");

  let isIdAvailable = false;

  // ID 중복 확인
  checkBtn.addEventListener("click", async () => {
    const username = idInput.value.trim();
    if (!username) {
      alert("ID를 입력해주세요.");
      return;
    }

    try {
      const res = await fetch(`/api/users/check?username=${encodeURIComponent(username)}`);
      const data = await res.json();

      if (data.available) {
        alert("사용 가능한 ID입니다.");
        isIdAvailable = true;
      } else {
        alert("이미 사용 중인 ID입니다.");
        isIdAvailable = false;
      }
    } catch (err) {
      alert("중복 확인 중 오류가 발생했습니다.");
      isIdAvailable = false;
    }
  });

  // 회원가입
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = idInput.value.trim();
    const password = document.getElementById("signup-pw").value.trim();
    const email = document.getElementById("signup-email").value.trim();

    if (!isIdAvailable) {
      alert("ID 중복 확인을 해주세요.");
      return;
    }

    try {
      const res = await fetch("/api/users/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password, email })
      });

      const text = await res.text();

      if (res.ok && text === "success") {
        alert("회원가입이 완료되었습니다.");
        window.location.href = "/";
      } else if (text === "duplicate") {
        alert("이미 사용 중인 ID입니다.");
      } else {
        alert("회원가입에 실패했습니다.");
      }
    } catch (err) {
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  });
});
