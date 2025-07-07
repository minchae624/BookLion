document.addEventListener("DOMContentLoaded", async () => {
    const welcomeMsg = document.getElementById("welcome-msg");
    const logoutBtn = document.getElementById("logout-btn");
    const mypageBtn = document.getElementById("mypage-btn");
    const categoryMap = {
        1: "책 추천",
        2: "이벤트",
        3: "기타"
    }
	
	document.getElementById("search-form").addEventListener("submit", function (e) {
		e.preventDefault();

		const category = document.getElementById("category").value;
		const input = document.getElementById("keyword").value;
		const status = document.getElementById("status").value;

		let url = `/api/qna?page=0`;

		if (category) url += `&categoryId=` + category;
		if (input) url += `&input=` + encodeURIComponent(input);
		if (status) url += `&status=` + status;

		window.location.href = url;
	});

	
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
                username = `${user.username}`;
                userId = `${user.userId}`;
                welcomeMsg.textContent = `환영합니다. ${user.username}님`;
                const userIdInput = document.getElementById("user-id");
                const userNameInput = document.getElementById("user-name");
                if (userIdInput) userIdInput.value = user.userId;
                if (userNameInput) userNameInput.value = user.username;
                /* 글쓴이와 접속자가 일치한지 확인 */
                const postWriterInput = document.getElementById("post-writer");
                const editDeleteBtn = document.getElementById("edit-delete-btn");
                if (postWriterInput && editDeleteBtn) {
                    if (String(user.userId) !== String(postWriterInput.value)) {
                        editDeleteBtn.style.display = "none";
                    }
                }
            } else {
                window.location.href = "/"
                console.log(username)
                welcomeMsg.textContent = "환영합니다.";
            }
        } catch (err) {
            console.error("사용자 정보 조회 실패:", err);
            window.location.href = "/"
            welcomeMsg.textContent = "환영합니다.";
        }
    } else {
        window.location.href = "/"
        welcomeMsg.textContent = "환영합니다.";
    }
	
});